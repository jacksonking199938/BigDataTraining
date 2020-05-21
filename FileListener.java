package listener;


import java.io.File;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.HiddenFileFilter;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;
//import org.apache.log4j.BasicConfigurator;
//import org.apache.log4j.Logger;


public class FileListener extends FileAlterationListenerAdaptor{
//	private Logger log = Logger.getLogger(FileListener.class);
	
//	�ļ�����
	public void onFileCreate(File file) {
//		log.info("[�½�]:"+file.getAbsolutePath());
		System.out.println("[�½�]:"+file.getAbsolutePath());
	}
	

//	   �ļ������޸�
	public void onFileChange(File file) {
		System.out.println("[�޸�]:" + file.getAbsolutePath());
	}

//	 �ļ�ɾ��
	public void onFileDelete(File file) {
		System.out.println("[ɾ��]:" + file.getAbsolutePath());
	}
	
//	Ŀ¼����
	public void onDirectoryCreate(File directory) {
		System.out.println("[�½�]:" + directory.getAbsolutePath());
	}
	
//	Ŀ¼�޸�
	public void onDirectoryChange(File directory) {
		System.out.println("[�޸�]:" + directory.getAbsolutePath());
	}

//	Ŀ¼ɾ��
	public void onDirectoryDelete(File directory) {
		System.out.println("[ɾ��]:" + directory.getAbsolutePath());
	}
	
	public void onStart(FileAlterationObserver observer) {
	    // TODO Auto-generated method stub
	    super.onStart(observer);
	}
	
	public void onStop(FileAlterationObserver observer) {
	    // TODO Auto-generated method stub
	    super.onStop(observer);
	}
	
	public static void main(String[] args) throws Exception{
		
	    // ���Ŀ¼
	    String rootDir = "E:\\cpp";
	    // ��ѯ��� 5 ��
	    long interval = TimeUnit.SECONDS.toMillis(1);
	    // ����������
	    IOFileFilter directories = FileFilterUtils.and(
	        FileFilterUtils.directoryFileFilter(),
	        HiddenFileFilter.VISIBLE);
	    IOFileFilter files    = FileFilterUtils.and(
	        FileFilterUtils.fileFileFilter(),
	        FileFilterUtils.suffixFileFilter(".txt"));
	    IOFileFilter filter = FileFilterUtils.or(directories, files);
	    // ʹ�ù�����
	    FileAlterationObserver observer = new FileAlterationObserver(new File(rootDir), filter);
	    //��ʹ�ù�����
	    //FileAlterationObserver observer = new FileAlterationObserver(new File(rootDir));
	    observer.addListener(new FileListener());
	    //�����ļ��仯������
	    FileAlterationMonitor monitor = new FileAlterationMonitor(interval, observer);
	    // ��ʼ���
	    monitor.start();
	}
}

