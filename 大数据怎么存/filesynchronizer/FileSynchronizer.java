package filesynchronizer;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;
import org.apache.commons.io.monitor.FileAlterationObserver;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.alibaba.fastjson.JSON;
import com.amazonaws.AmazonClientException;
import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.AbortMultipartUploadRequest;
import com.amazonaws.services.s3.model.BucketLifecycleConfiguration;
import com.amazonaws.services.s3.model.CompleteMultipartUploadRequest;
import com.amazonaws.services.s3.model.InitiateMultipartUploadRequest;
import com.amazonaws.services.s3.model.PartETag;
import com.amazonaws.services.s3.model.UploadPartRequest;
import com.amazonaws.services.s3.model.BucketLifecycleConfiguration.Rule;


public class FileSynchronizer  extends FileAlterationListenerAdaptor{
//	private final static String bucketName = "jacksonkim";
//	private final static String accessKey = "08C49C332E0DEBEE4151";
//	private final static String secretKey = "W0Y0MTUwNjY0Q0I1RjM0MkQ2QzA1NTUyQUMyNzM2QzFFMEVGRjEyQTBd";
//	private final static String serviceEndpoint = "http://scuts3.depts.bingosoft.net:29999";
//	private final static String signingRegion = "";
	private static long partSize = 5<<20;
	private Document document;
	private String bucketName;
	private BasicAWSCredentials credentials;
	private ClientConfiguration ccfg;
	private EndpointConfiguration endpoint;
	private AmazonS3 s3;
	
	public FileSynchronizer(Document document, String bucketName, String accessKey, String secretKey,
			String serviceEndpoint, String signingRegion) {
		this.document = document;
		this.bucketName = bucketName;
		this.credentials = new BasicAWSCredentials(accessKey, secretKey);
		this.ccfg = new ClientConfiguration().withUseExpectContinue(false);

		this.endpoint = new EndpointConfiguration(serviceEndpoint, signingRegion);

		this.s3 = AmazonS3ClientBuilder.standard()
				.withCredentials(new AWSStaticCredentialsProvider(credentials)).withClientConfiguration(ccfg)
				.withEndpointConfiguration(endpoint).withPathStyleAccessEnabled(true).build();
	}
	
	public void multipartUpload(File file,String uploadId,long filePosition, 
			int partNum, ArrayList<PartETag> partETags) {
		String keyName = Paths.get(file.getAbsolutePath()).getFileName().toString();
		// Create a list of UploadPartResponse objects. You get one of these
        // for each part upload.
//		ArrayList<PartETag> partETags = new ArrayList<PartETag>();
		if((partNum-1)*partSize < filePosition) {
			partNum ++ ;
		}
		long contentLength = file.length();
//		String uploadId = null;
		try {
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
//			long filePosition = 0;
			for (int i = partNum; filePosition < contentLength;i++) {
				// Last part can be less than 5 MB. Adjust part size.
				partSize = Math.min(partSize, contentLength - filePosition);

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
				saveUploadInfo(file.getAbsolutePath(),uploadId, filePosition, partNum, partETags);
			}

			// Step 3: Complete.
			System.out.println("Completing upload");
			CompleteMultipartUploadRequest compRequest = 
					new CompleteMultipartUploadRequest(bucketName, keyName, uploadId, partETags);
			s3.completeMultipartUpload(compRequest);
			deleteUploadInfo(uploadId);
		} catch (Exception e) {
			System.err.println(e.toString());
			if (uploadId != null && !uploadId.isEmpty()) {
				// Cancel when error occurred
				System.out.println("Aborting upload");
				s3.abortMultipartUpload(new AbortMultipartUploadRequest(bucketName, keyName, uploadId));
			}
			System.exit(1);
		}
		System.out.println("Done!");
	}
	
	
	private void saveUploadInfo(String fileName, String uploadId,long filePosition,
			int partNum, ArrayList<PartETag> partETags) {
		// save necessary information when interruption occurs  
		// and load them to continue when the program restart 
		try {
			System.out.println("[INFO]:saving xml to disk");
			Element root;
			if(this.document.hasContent())
			{
				root = this.document.getRootElement();
			}
			else {
				root = this.document.addElement("root");
			}
			
			Element unfinishedTask = root.addElement("unfinishedTask");
			unfinishedTask.addElement("uploadId")
					.addText(uploadId);	
			unfinishedTask.addElement("filePath")
					.addText(fileName);
			unfinishedTask.addElement("filePosition")
					.addText(String.valueOf(filePosition));
			unfinishedTask.addElement("partNum")
					.addText(String.valueOf(partNum));
			unfinishedTask.addElement("partETags").addText(JSON.toJSONString(partETags));
			String filePath = System.getProperty("user.dir");
			System.out.println(filePath);
			FileWriter out = new FileWriter(filePath+"\\foo.xml");
			this.document.write(out);
			out.close();
			
		} catch (Exception e) {
			System.out.println(e.toString());
			// TODO: handle exception
		}
	}
	
	
	private void deleteUploadInfo(String uploadId) {
		try {
			Element root = this.document.getRootElement();
			for(Element element: root.elements()) {
				if(element.elementText("uploadId")==uploadId)
					root.remove(element);
			}
			String filePath = System.getProperty("user.dir");
			System.out.println(filePath);
			FileWriter out = new FileWriter(filePath+"\\foo.xml");
			this.document.write(out);
			out.close();
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.toString());
		}
		
	}
	
	

}
