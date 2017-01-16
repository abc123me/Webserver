package net.net16.jeremiahlowe.webserver;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Enumeration;
import java.util.Properties;

public class MimeTypeLoader {
	private File mimeTypeFile = new File("config/mimeTypes.txt");
	private Properties mimeTypeList = new Properties();
	
	public void loadToGUI(){
		Instance.gui.updateMimeTypes();
	}
	public void addMimeType(MimeType m){
		try{
			mimeTypeList.put(m.fileName, m.mimeType);
			mimeTypeList.store(new FileOutputStream(mimeTypeFile), "Webserver mime type list");
			Instance.gui.updateMimeTypes();
		}
		catch(Exception e){Utility.log("Error adding mimetype " + e);}
	}
	public String loadMimes(){
		String out = "";
		try{
			mimeTypeList.load(new FileInputStream(mimeTypeFile));
			Enumeration<Object> keys = mimeTypeList.keys();
			while(keys.hasMoreElements()){
				Object key = keys.nextElement();
				MimeType mime = new MimeType(key.toString(), mimeTypeList.get(key).toString());
				MimeType.mimeTypes.add(mime);
			}
		}
		catch(Exception e){out += "Error loading mimetypes: " + e + "\n";}
		return out;
	}
	public String createMimeFile() throws Exception{
		if(!mimeTypeFile.exists()){
			mimeTypeFile.createNewFile();
			for(MimeType m : MimeType.defaultMimeTypes) mimeTypeList.put(m.fileName, m.mimeType);
			mimeTypeList.store(new FileOutputStream(mimeTypeFile), "Webserver mime type list");
			return "Made mime types file\n";
		}
		return "";
	}
}
