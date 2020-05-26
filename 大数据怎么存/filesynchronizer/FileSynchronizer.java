package filesynchronizer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.ClientConfiguration;
import com.amazonaws.SdkClientException;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.AbortMultipartUploadRequest;
import com.amazonaws.services.s3.model.CompleteMultipartUploadRequest;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.GetObjectAclRequest;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.InitiateMultipartUploadRequest;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.PartETag;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.amazonaws.services.s3.model.UploadPartRequest;


public class FileSynchronizer extends FileAlterationListenerAdaptor{
	private final long maxPartSize = 20<<20;
	private final int maxLoop = 3;
	private String bucketName;
	private BasicAWSCredentials credentials;
	private ClientConfiguration ccfg;
	private EndpointConfiguration endpoint;
	private AmazonS3 s3;
	
	public FileSynchronizer(String bucketName, String accessKey, String secretKey,
			String serviceEndpoint, String signingRegion) {
		this.bucketName = bucketName;
		this.credentials = new BasicAWSCredentials(accessKey, secretKey);
		this.ccfg = new ClientConfiguration().withUseExpectContinue(false);
		this.endpoint = new EndpointConfiguration(serviceEndpoint, signingRegion);
		this.s3 = AmazonS3ClientBuilder.standard()
				.withCredentials(new AWSStaticCredentialsProvider(credentials)).withClientConfiguration(ccfg)
				.withEndpointConfiguration(endpoint).withPathStyleAccessEnabled(true).build();
	}
	
	
	public long getMaxSize() {
		return this.maxPartSize;
	}
	
	
	public void multipartUpload(File file,String uploadId,long filePosition, 
			int partNum, ArrayList<PartETag> partETags) {
		// 分块传输函数
		long partSize = this.maxPartSize;
		// Step0: get the file name
		String keyName = file.getAbsolutePath()
				.replace(Main.getRootDir()+"\\", "")
				.replace("\\", "/");
		for(int k=0;k<this.maxLoop;k++) {
			try {
				if((partNum-1)*this.maxPartSize < filePosition) {
					partNum ++ ;
				}
				long contentLength = file.length();
			
				// Step 1: Initialize.
				if(uploadId == "") {
					InitiateMultipartUploadRequest initRequest = 
							new InitiateMultipartUploadRequest(bucketName, keyName);
					uploadId = s3.initiateMultipartUpload(initRequest).getUploadId();
				}
				System.out.format("Created upload ID was %s\n", uploadId);
				if(partETags == null) {
					partETags = new ArrayList<PartETag>();
				}
	
				// Step 2: Upload parts.
				for (int i = partNum; filePosition < contentLength;i++) {
					System.out.println(String.format("%s\t%s",filePosition, contentLength));
					// Last part can be less than 5 MB. Adjust part size.
					partSize = Math.min(this.maxPartSize, contentLength - filePosition);
	
					// Create request to upload a part.
					UploadPartRequest uploadRequest = new UploadPartRequest()
							.withBucketName(bucketName)
							.withKey(keyName)
							.withUploadId(uploadId)
							.withPartNumber(i)
							.withFileOffset(filePosition)
							.withFile(file)
							.withPartSize(partSize);
	
					// Upload part and add response to our list.
					System.out.format("Uploading part %d\n", i);
					partETags.add(s3.uploadPart(uploadRequest).getPartETag());
					filePosition += partSize;
					partNum++;
//					saveUploadInfo(file.getAbsolutePath(),uploadId, filePosition, partNum, partETags);
					InfoSaver.saveInfo(file.getAbsolutePath(), uploadId, filePosition, partNum, partETags);
				}

				// Step 3: Complete.
				System.out.println("Completing upload");
				CompleteMultipartUploadRequest compRequest = 
						new CompleteMultipartUploadRequest(bucketName, keyName, uploadId, partETags);
				s3.completeMultipartUpload(compRequest);
//				deleteUploadInfo(uploadId);
				InfoSaver.deleteInfo(uploadId);
				break;
			}catch (IllegalArgumentException e) {
				System.err.println(e.toString());
				if(e.toString().contains("Failed to open file")) {
					try {
						TimeUnit.SECONDS.sleep(1);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
				}
			}catch (Exception e) {
				System.err.println(e.toString());
				if (uploadId != null && !uploadId.isEmpty()) {
					// Cancel when error occurred
					System.out.println("Aborting upload");
					s3.abortMultipartUpload(new AbortMultipartUploadRequest(bucketName, keyName, uploadId));
				}
//				System.exit(1);
			}
		}
		System.out.println("Uploading Done!");
	}
	
	
	public void simpleUpload(File file) {
		// 简单传输，不用分块传输
		String keyName = file.getAbsolutePath()
				.replace(Main.getRootDir()+"\\", "")
				.replace("\\", "/");
		for(int k=0;k<this.maxLoop;k++) {
			try {
				s3.putObject(bucketName,keyName,file);
				System.out.println("Uploading Done!");
				return;
			}catch (Exception e) {
				System.err.println(e.toString());
				System.out.println("[INFO]:retreive again");
			}
		}
		System.out.println("[INFO]:network error, restart may help!");
	}
	
	
	
	public void multipartDownload(String keyName, long filePosition) {
		// 分块下载函数
		try {
	        long length = s3.getObjectMetadata(bucketName, keyName).getContentLength();
	        RandomAccessFile raf = new RandomAccessFile(
	        		new File(Main.getRootDir() + "\\"+ keyName), "rw");
	        raf.seek(filePosition);
	        System.out.format("[INFO]: Downloading %s from position %s \n", keyName, filePosition);
	        // 判断是否大于最大分块大小
	        if(length > this.maxPartSize) {
	        	// 分块下载
		        for(long i=filePosition;i<length; i+=this.maxPartSize) {
		        	System.out.println(String.format("%s\t%s",filePosition, length));
		        	// 发起请求
		        	GetObjectRequest request = new GetObjectRequest(bucketName, keyName);
		        	long newPosition = Math.min(length, i+this.maxPartSize);
		        	request.setRange(i, newPosition);
		        	InputStream inputStream = s3.getObject(request).getObjectContent();
	            	byte [] read_buf = new byte[64*1024];
	            	int read_len=0;
	            	// 保存文件
	            	while((read_len=inputStream.read(read_buf))>0){
	            		raf.write(read_buf,0,read_len);
	            	}
	            	filePosition = newPosition;
	            	InfoSaver.saveDownloadInfo(keyName, filePosition);
		        }
		        InfoSaver.delDownLoadInfo(keyName);
	        }
	        // 一次下载完成
	        else {
	        	InputStream inputStream = s3.getObject(bucketName, keyName).getObjectContent();
	        	byte [] read_buf = new byte[64*1024];
            	int read_len=0;
            	while((read_len=inputStream.read(read_buf))>0){
            		raf.write(read_buf,0,read_len);
            	}
	        }
	        raf.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Downloading Done!");
	}
	
	
	public void deleteFile(File file) {
		String keyName = file.getAbsolutePath()
				.replace(Main.getRootDir()+"\\", "")
				.replace("\\", "/");
		for(int k=0;k<this.maxLoop;k++) {
			try {
		            s3.deleteObject(new DeleteObjectRequest(bucketName, keyName));
		            System.out.println("Deleting Done");
		            return;
		    } catch (AmazonServiceException e) {
	        // The call was transmitted successfully, but Amazon S3 couldn't process 
	        // it, so it returned an error response.
	            e.printStackTrace();
	        } catch (SdkClientException e) {
	            // Amazon S3 couldn't be contacted for a response, or the client
	            // couldn't parse the response from Amazon S3.
	            e.printStackTrace();
	        }
		}
		System.out.println("[INFO]:network error, restart may help!");
	}
	
	

	public void createDirectory(File directory) {
		// 创建一个空目录
		String keyName = directory.getAbsolutePath()
				.replace(Main.getRootDir()+"\\", "")
				.replace("\\", "/");
		for(int k=0; k<this.maxLoop; k++) {
			try {
					s3.putObject(bucketName, keyName+"/", "");
		            System.out.println("Creating Done!");
		            return;
		    } catch (AmazonServiceException e) {
	        // The call was transmitted successfully, but Amazon S3 couldn't process 
	        // it, so it returned an error response.
	            e.printStackTrace();
	        } catch (SdkClientException e) {
	            // Amazon S3 couldn't be contacted for a response, or the client
	            // couldn't parse the response from Amazon S3.
	            e.printStackTrace();
	        }
		}
		System.out.println("[INFO]:network error, restart may help!");
	}
	
	public void deleteDirectory(File directory) {
		// 删除一个空目录
		String keyName = directory.getAbsolutePath()
				.replace(Main.getRootDir()+"\\", "")
				.replace("\\", "/");
		for(int k=0; k<this.maxLoop; k++) {
			try {
		            s3.deleteObject(new DeleteObjectRequest(bucketName, keyName+"/"));
		            System.out.println("Deleting Done!");
		            return;
		    } catch (AmazonServiceException e) {
	        // The call was transmitted successfully, but Amazon S3 couldn't process 
	        // it, so it returned an error response.
	            e.printStackTrace();
	        } catch (SdkClientException e) {
	            // Amazon S3 couldn't be contacted for a response, or the client
	            // couldn't parse the response from Amazon S3.
	            e.printStackTrace();
	        }
		}
		System.out.println("[INFO]:network error, restart may help!");
	}
	
	public List<String> getObjectsKey(){
		// 获取文件对象们的KeyName
		ObjectListing objectList = s3.listObjects(bucketName);
		List<S3ObjectSummary> summaries = objectList.getObjectSummaries();
		List<String> objectsKey = new ArrayList<String>(summaries.size());
		for(S3ObjectSummary summary:summaries) {
			objectsKey.add(summary.getKey());
		}
		return objectsKey;
	}
	
	public List<Long> getObjectsSize(){
		// 获取文件对象们的大小
		ObjectListing objectList = s3.listObjects(bucketName);
		List<S3ObjectSummary> summaries = objectList.getObjectSummaries();
		List<Long> objectsSize = new ArrayList<Long>(summaries.size());
		for(S3ObjectSummary summary:summaries) {
			objectsSize.add(summary.getSize());
			
		}
		return objectsSize;
	}
		
}

