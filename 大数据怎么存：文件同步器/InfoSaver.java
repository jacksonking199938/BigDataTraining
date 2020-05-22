package listener;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

public class InfoSaver {
	public InfoSaver() {
		// TODO Auto-generated constructor stub
		saveRunningInfo();
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
	
	public static void main(String[] args) {
		 
	    InfoSaver saver = new InfoSaver();  
	    long s = System.currentTimeMillis(); 
	    saver.createDocument();
	    for (int i = 0; i < 100000; i++) {  
	    //在这里增添您需要处理代码
	    }
	    long se = System.currentTimeMillis();
	    System.out.println(se - s);
	}
	
	private void saveRunningInfo() {
		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				try {
					FileWriter fw = new FileWriter("D:\\t.log");
					System.out.println("Im going to end");
					fw.write("the application ended! " + (new Date()).toString());
					fw.close();
				} catch (IOException e) {
					// TODO: handle exception
				}
			}
		});
	}
	
	
	
	
	
}


 
