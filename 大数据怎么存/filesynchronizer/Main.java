package filesynchronizer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.PartETag;

public class Main {
	private final static String bucketName = "jacksonkim";
	private final static String accessKey = "08C49C332E0DEBEE4151";
	private final static String secretKey = "W0Y0MTUwNjY0Q0I1RjM0MkQ2QzA1NTUyQUMyNzM2QzFFMEVGRjEyQTBd";
	private final static String serviceEndpoint = "http://scuts3.depts.bingosoft.net:29999";
	private final static String signingRegion = "";
	private static String rootDir = "E:\\cpp";

	public static void main(String[] args) {
//		System.out.print("please input the target rootDir:");
//		Scanner scanner = new Scanner(System.in);
//		// 指定同步目录
//		String rootDir = scanner.nextLine();
//		scanner.close();

		FileSynchronizer synchronizer = new FileSynchronizer(bucketName, 
				accessKey, secretKey, serviceEndpoint, signingRegion);
		FileListener listener = new FileListener(synchronizer);
		
		System.out.println("[INFO]:checking unfinished task...");
		checkUnfinishedUpload(synchronizer);
		checkUnfinishedDownload(synchronizer);
		System.out.println("[INFO]:checking Done！");
		
		System.out.println("[INFO]:checking consistency between local and server...");
		try {
			TimeUnit.SECONDS.sleep(1);
		} catch (InterruptedException e1) {
		}
		checkConsistency(synchronizer, listener);
		System.out.println("[INFO]:checking Done！");
		
		// 轮询间隔
		long interval = TimeUnit.SECONDS.toMillis(2);
		// 创建观察者，订阅listener主题
		FileAlterationObserver observer = new FileAlterationObserver(new File(rootDir));
		observer.addListener(listener);
		// 创建管理器
		FileAlterationMonitor monitor = new FileAlterationMonitor(interval, observer);
		try {
			monitor.start();
			System.out.println("file listener is working:");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static String getRootDir() {
		return rootDir;
	}

	private static void checkUnfinishedUpload(FileSynchronizer synchronizer) {
		// 检查是否有中断的上传任务，jsonPath是中断信息保存位置
		String jsonPath = System.getProperty("user.dir")+"\\upload.json";
		// 读取中断信息
		try {
			FileReader fis = new FileReader(new File(jsonPath));
			BufferedReader reader = new BufferedReader(fis);
			StringBuffer buffer = new StringBuffer();
			String s = null;
			while((s=reader.readLine())!=null) {
				buffer.append(s);
			}
			if(buffer.length()>1)
			{
				System.out.println("[DEBUG]: loading json file");
				InfoSaver.uploadJson = JSON.parseObject(buffer.toString());
			}
			fis.close();
		} catch (FileNotFoundException e) {
			System.out.println("[WARNING]:json file not found");
			return;
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		String uploadId = "";
		String filePath = "";
		long filePosition = 0;
		int partNum = 1;
		ArrayList<PartETag> partETags;
		// 恢复传输任务
		try {
		if(InfoSaver.uploadJson != null && (!InfoSaver.uploadJson.isEmpty())) {
			for(String key:InfoSaver.uploadJson.keySet()) {
				JSONObject unfinishedEntry = InfoSaver.uploadJson.getJSONObject(key);
				uploadId = key;
				filePath = unfinishedEntry.getString("filePath");
				filePosition = unfinishedEntry.getLong("filePosition");
				partNum = unfinishedEntry.getIntValue("partNum");
				partETags = (ArrayList<PartETag>) JSONArray.parseArray(
							unfinishedEntry.getString("partETags"), PartETag.class);
				synchronizer.multipartUpload(new File(filePath), 
						uploadId, filePosition, partNum, partETags);
			}
		}
		}catch (Exception e) {
			System.err.println(e.toString());
			e.printStackTrace();
			System.out.println("[ERROR]: something wrong! restart may help");
		}
		
		
	}
	
	public static void checkUnfinishedDownload(FileSynchronizer synchronizer) {
		// 检查中断的下载任务
		String jsonPath = System.getProperty("user.dir")+"\\download.json";
		// 读取中断信息
		try {
			FileReader fis = new FileReader(new File(jsonPath));
			BufferedReader reader = new BufferedReader(fis);
			StringBuffer buffer = new StringBuffer();
			String s = null;
			while((s=reader.readLine())!=null) {
				buffer.append(s);
			}
			if(buffer.length()>1)
			{
				System.out.println("[DEBUG]: loading json file");
				InfoSaver.downloadJson = JSON.parseObject(buffer.toString());
			}
			fis.close();
		} catch (FileNotFoundException e) {
			System.out.println("[WARNING]:json file not found");
			return;
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		// 恢复传输任务
		try {
		if(InfoSaver.downloadJson != null && (!InfoSaver.downloadJson.isEmpty())) {

			for(String key:InfoSaver.downloadJson.keySet()) {
				System.out.format("[INFO]: recover unfinished task: %s \n", key);
				JSONObject unfinishedEntry = InfoSaver.downloadJson.getJSONObject(key);
				long filePosition = unfinishedEntry.getLong("filePosition");
				synchronizer.multipartDownload(key, filePosition);
			}
		}
		}catch (Exception e) {
			// TODO: handle exception
			
		}
		
	}
	
	public static void checkConsistency(FileSynchronizer synchronizer,
			FileListener listener) {
		//检查本地目录和服务器目录是否一致
		List<String> filepaths = new ArrayList<String>();
		List<Long> fileSizes = new ArrayList<Long>();
		getFileList(new File(rootDir), filepaths, fileSizes);
		
		List<String> objectKeys = synchronizer.getObjectsKey();
		List<Long> objectSizes = synchronizer.getObjectsSize();
		
		for(int i=0;i<filepaths.size();i++) {
			if(!objectKeys.contains(filepaths.get(i))) {
				File file = new File(rootDir+"/"+filepaths.get(i));
				if(file.isDirectory())
					listener.onDirectoryCreate(file);
				else {
					listener.onFileCreate(file);
				}
			}
			else{
				// 该本地路径在objectKeys中的下标,
				int index = objectKeys.indexOf(filepaths.get(i));
				// 当文件同名，但大小不同时， 说明同步器里显示文件发生改变
				if (fileSizes.get(i).compareTo(objectSizes.get(index)) != 0) {
					File file = new File(rootDir+"/"+filepaths.get(i));
					if(file.isDirectory())
						listener.onDirectoryCreate(file);
					else {
						listener.onFileCreate(file);
					}
				}		
			}
		}
		//System.out.println("[DEBUG]: before chaji: size of objectkeys: " + String.valueOf(objectKeys.size()));
		objectKeys.removeAll(filepaths);
		objectKeys.sort(new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {
				// TODO Auto-generated method stub
				if(o1.length()<o2.length())
					return -1;
				else if(o1.length() == o2.length())
					return 0;
				else {
					return 1;
				}
			}
		});
		try {
		Scanner scanner = new Scanner(System.in);
		for(String keyName: objectKeys) {
			File object = new File(rootDir+"/"+keyName);
			System.out.println("[INFO]:obect "+keyName+" is found on server but not at local");
			String choice = "";
			while(!choice.equals("y") && !choice.equals("n")) {
				System.out.print("[INFO]:Would you like to download it (or else it will be deleted on server) [y/n]:");
				choice = scanner.next();
			}
			if (choice.equals("n")) {
				if(keyName.endsWith("/"))
				{
					synchronizer.deleteDirectory(object);
				}
				else {
					synchronizer.deleteFile(object);
				}
			}
			else {
				// 检查目录是否存在
				String [] paths = keyName.split("/");
				String path = rootDir;
				for(int i=0;i<paths.length-1;i++)
				{	
					path += "/"+paths[i];
					File file = new File(path);
					if(!file.exists())
					{
						file.mkdir();
					}
					
				}
				synchronizer.multipartDownload(keyName, 0);
			}
		}
		scanner.close();
		}catch (Exception e) {
			System.out.println("[ERROR]:"+ e.toString());
			return;
		}
	}
	
	private static void getFileList(File file, List<String> resultFileName, List<Long> filesSize){
        File[] files = file.listFiles();
        // 判断目录下是不是空的
        if(files==null) return;
        for (File f : files) {
        	// 判断是否文件夹
            if(f.isDirectory()){
                resultFileName.add(
                		f.getPath().replace(rootDir+"\\", "")
                		.replace("\\", "/")
                		+ "/");
                filesSize.add(f.length());
                // 调用自身,查找子目录
                getFileList(f,resultFileName, filesSize);
            }else
                resultFileName.add(
                		f.getPath().replace(rootDir+"\\", "")
                		.replace("\\", "/"));
            	filesSize.add(f.length());
        }
        return;
    }
}
