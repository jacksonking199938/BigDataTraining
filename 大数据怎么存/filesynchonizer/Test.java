package filesynchonizer;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class Test {
	public static void main(String[] args) {
		SAXReader reader = new SAXReader();
		try {
			Document document = reader.read("D:\\foo.xml");
			Element root = document.getRootElement();
			Element author1 = root.element("author");
			System.out.println(author1.getText());
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
