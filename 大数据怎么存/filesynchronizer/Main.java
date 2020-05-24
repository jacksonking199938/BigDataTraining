package filesynchronizer;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.HiddenFileFilter;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.alibaba.fastjson.JSONArray;
import com.amazonaws.services.s3.model.PartETag;

public class Main {
	private final static String bucketName = "jacksonkim";
	private final static String savePath   = "D:\\ACoder\\AllProject\\JavaProject";
	private final static String accessKey = "08C49C332E0DEBEE4151";
	private final static String secretKey = "W0Y0MTUwNjY0Q0I1RjM0MkQ2QzA1NTUyQUMyNzM2QzFFMEVGRjEyQTBd";
	private final static String serviceEndpoint = "http://scuts3.depts.bingosoft.net:29999";
	private final static String signingRegion = "";
	private static Document document;

	public static void main(String[] args) {
		System.out.print("please input the target rootDir:");
//		Scanner scanner = new Scanner(System.in);
//		// 同步目录
//		String rootDir = scanner.nextLine();
//		scanner.close();
		String rootDir = "E:\\cpp";
		System.out.print("file listener is working:");
		checkUnfinished();
		// 轮询间隔
		long interval = TimeUnit.SECONDS.toMillis(1);
		FileAlterationObserver observer = new FileAlterationObserver(new File(rootDir));
		FileSynchronizer synchronizer = new FileSynchronizer(document, bucketName, 
				accessKey, secretKey, serviceEndpoint, signingRegion);
		FileListener listener = new FileListener(synchronizer);
		observer.addListener(listener);
		FileAlterationMonitor monitor = new FileAlterationMonitor(interval, observer);
		try {
			monitor.start();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println(e.toString());
		}
		
	}
	
	private static void checkUnfinished() {
		String xmlPath = System.getProperty("user.dir")+"\\foo.xml";
		System.out.println(xmlPath);
		File file = new File(xmlPath);
		try {
			if(!file.exists() || file.length() < 1) {
				file.createNewFile();
				document = DocumentHelper.createDocument();
				document.addElement("root");
			}
			else {
				SAXReader reader = new SAXReader();
				document = reader.read(file);
			}	
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		FileSynchronizer synchronizer = new FileSynchronizer(document, bucketName,
				accessKey, secretKey, serviceEndpoint, signingRegion);
		Element root = document.getRootElement();
		if(root.hasContent()) {
			long filePosition = 0;
			int partNum = 1;
			ArrayList<PartETag> partETags = null;
			String uploadId = "";
			String filePath = "";
			for(Element task:root.elements())
			{
				uploadId = task.elementText("uploadId");
				filePath = task.elementText("filePath");
				filePosition = Long.valueOf(task.elementText("filePosition").toString());
				partNum  = Integer.valueOf(task.elementText("partNum").toString());
				if(task.elementText("partETags") != "null" && uploadId != "")
					partETags = (ArrayList<PartETag>) JSONArray.parseArray(task.elementText("partETags"), PartETag.class);
				else {
					partETags = new ArrayList<PartETag>();
				}
				synchronizer.multipartUpload(new File(filePath), 
						uploadId, filePosition, partNum, partETags);
			}
		}
	}
	
}
