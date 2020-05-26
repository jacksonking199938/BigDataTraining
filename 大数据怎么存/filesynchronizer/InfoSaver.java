package filesynchronizer;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import com.alibaba.fastjson.JSONObject;
import com.amazonaws.services.s3.model.PartETag;

public class InfoSaver {
	public static JSONObject uploadJson = new JSONObject();
	public static JSONObject downloadJson = new JSONObject();
	
	public static void saveInfo(String fileName, String uploadId,long filePosition,
			int partNum, ArrayList<PartETag> partETags) {
		try {
			System.out.println("[INFO]:saving info json to disk");
			JSONObject tempjson = new JSONObject();
			tempjson.put("filePath", fileName);
			tempjson.put("filePosition", filePosition);
			tempjson.put("partNum", partNum);
			tempjson.put("partETags", partETags);
			uploadJson.put(uploadId, tempjson);
			String filePath = System.getProperty("user.dir")+"\\upload.json";
			OutputStreamWriter writer = new OutputStreamWriter(new 
					FileOutputStream(filePath),"UTF-8");
			writer.write(uploadJson.toJSONString());
			writer.close();
		} catch (Exception e) {
			System.out.println(e.toString());
		}
	}
	
	public static void deleteInfo(String uploadId) {
		uploadJson.remove(uploadId);
		String filePath = System.getProperty("user.dir")+"\\upload.json";
		OutputStreamWriter writer;
		try {
			writer = new OutputStreamWriter(new 
					FileOutputStream(filePath),"UTF-8");
			writer.write(uploadJson.toJSONString());
			writer.close();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	public static void saveDownloadInfo(String fileName, long filePosition) {
		try {
			System.out.println("[INFO]:saving info json to disk");
			JSONObject tempjson = new JSONObject();
			tempjson.put("filePosition", filePosition);
			downloadJson.put(fileName, tempjson);
			String filePath = System.getProperty("user.dir")+"\\download.json";
			OutputStreamWriter writer = new OutputStreamWriter(new 
					FileOutputStream(filePath),"UTF-8");
			writer.write(downloadJson.toJSONString());
			writer.close();
		} catch (Exception e) {
			System.out.println(e.toString());
		}
	}
	
	public static void delDownLoadInfo(String fileName) {
		downloadJson.remove(fileName);
		String filePath = System.getProperty("user.dir")+"\\download.json";
		OutputStreamWriter writer;
		try {
			writer = new OutputStreamWriter(new 
					FileOutputStream(filePath),"UTF-8");
			writer.write(downloadJson.toJSONString());
			writer.close();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	//以下为历史版本，暂时保存
//	private void saveUploadInfo(String fileName, String uploadId,long filePosition,
//			int partNum, ArrayList<PartETag> partETags) {
//		// save necessary information when interruption occurs  
//		// and load them to continue when the program restart 
//		try {
//			System.out.println("[INFO]:saving xml to disk");
//			Element root;
//			if(this.document.hasContent())
//			{
//				root = this.document.getRootElement();
//			}
//			else {
//				root = this.document.addElement("root");
//			}
//			Element unfinishedTask = root.addElement("unfinishedTask");
//			unfinishedTask.addElement("uploadId")
//					.addText(uploadId);	
//			unfinishedTask.addElement("filePath")
//					.addText(fileName);
//			unfinishedTask.addElement("filePosition")
//					.addText(String.valueOf(filePosition));
//			unfinishedTask.addElement("partNum")
//					.addText(String.valueOf(partNum));
//			unfinishedTask.addElement("partETags").addText(JSON.toJSONString(partETags));
//			String filePath = System.getProperty("user.dir");
//			FileWriter out = new FileWriter(filePath+"\\foo.xml");
//			this.document.write(out);
//			out.close();
//
//		} catch (Exception e) {
//			System.out.println(e.toString());
//			// TODO: handle exception
//		}
//	}
//	
//	
//	private void deleteUploadInfo(String uploadId) {
//		try {
//			Element root = this.document.getRootElement();
//			for(Element element: root.elements()) {
//				if(element.elementText("uploadId")==uploadId)
//					root.remove(element);
//			}
//			String filePath = System.getProperty("user.dir");
//			FileWriter out = new FileWriter(filePath+"\\foo.xml");
//			this.document.write(out);
//			out.close();
//		} catch (Exception e) {
//			// TODO: handle exception
//			System.out.println(e.toString());
//		}
//
//	}
	
	
}


 
