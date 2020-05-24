package filesynchronizer;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

public class InfoSaver {
	private int a; 
	public InfoSaver(int a) {
		// TODO Auto-generated constructor stub
		this.a = a;
		saveRunningInfo(a);
	}

	public void createDocument() {
		Document document = DocumentHelper.createDocument();
		Element root = document.addElement("root");
		Element author1 = root.addElement("author")
				.addAttribute("name","James")
				.addAttribute("location", "UK")
				.addText("James Strachan");
		
		Element author2 = root.addElement("author")
				.addAttribute("name", "Bob")
				.addAttribute("location", "bob")
				.addText("Bob McWhirter");
		
		try {
			System.out.println("[INFO]:saving xml to disk");
			FileWriter out = new FileWriter("D:\\foo.xml");
			document.write(out);
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	private void saveRunningInfo(int a) {
		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				try {
//					FileWriter fw = new FileWriter("D:\\t.log");
//					System.out.println("Im going to end");
//					fw.write("the application ended! " + (new Date()).toString()+ a);
//					fw.close();
//					Document document = DocumentHelper.createDocument();
//					document.addElement("filePosition")
//							.addText(String.valueOf(Test.filePosition));
//					document.addElement("i")
//							.addText(String.valueOf(Test.i));
//					document.addElement("uploadId").addText(Test.uploadId);
//					try {
//						System.out.println("[INFO]:saving xml to disk");
//						FileWriter out = new FileWriter("D:\\foo.xml");
//						document.write(out);
//						out.close();
//					} catch (IOException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
					
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		});
	}
	
}


 
