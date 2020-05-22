package filesynchronizer;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;

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


public class FileSynchronizer {
//	private final static String bucketName = "jacksonkim";
//	private final static String accessKey = "08C49C332E0DEBEE4151";
//	private final static String secretKey = "W0Y0MTUwNjY0Q0I1RjM0MkQ2QzA1NTUyQUMyNzM2QzFFMEVGRjEyQTBd";
//	private final static String serviceEndpoint = "http://scuts3.depts.bingosoft.net:29999";
//	private final static String signingRegion = "";
	private static long partSize = 20<<20;
	private static String bucketName;
	private static BasicAWSCredentials credentials;
	private static ClientConfiguration ccfg;
	private static EndpointConfiguration endpoint;
	private static AmazonS3 s3;
	
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
	
	public void createFile(File file) {
		String keyName = Paths.get(file.getAbsolutePath()).getFileName().toString();
		// Create a list of UploadPartResponse objects. You get one of these
        // for each part upload.
		ArrayList<PartETag> partETags = new ArrayList<PartETag>();
		long contentLength = file.length();
		String uploadId = null;
		try {
			// Step 1: Initialize.
			InitiateMultipartUploadRequest initRequest = 
					new InitiateMultipartUploadRequest(bucketName, keyName);
			uploadId = s3.initiateMultipartUpload(initRequest).getUploadId();
			System.out.format("Created upload ID was %s\n", uploadId);
			
			// Step 2: Upload parts.
			long filePosition = 0;
			for (int i = 1; filePosition < contentLength; i++) {
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
			}

			// Step 3: Complete.
			System.out.println("Completing upload");
			CompleteMultipartUploadRequest compRequest = 
					new CompleteMultipartUploadRequest(bucketName, keyName, uploadId, partETags);

			s3.completeMultipartUpload(compRequest);
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
	
	

}
