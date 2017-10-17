package net.net16.jeremiahlowe.webserver.cfg;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Enumeration;
import java.util.Properties;

import net.net16.jeremiahlowe.webserver.utility.Enums.LogLevel;
import net.net16.jeremiahlowe.webserver.utility.Instance;

public class MimeTypeLoader{
	private File mimeTypeFile = new File("config/mimeTypes.txt");
	private Properties mimeTypeList = new Properties();
	
	public void updateMimeTypesForGUI(){
		Instance.globalInstance.gui.updateMimeTypes();
	}
	public void addMimeType(MimeType m){
		try{
			mimeTypeList.put(m.fileName, m.mimeType);
			save();
		}
		catch(Exception e){
			Instance.globalInstance.logger.log(LogLevel.Error, "Error adding mimetype " + e);
		}
	}
	public void save(){
		try{
			mimeTypeList.store(new FileOutputStream(mimeTypeFile), "Webserver mime type list");
		}
		catch(Exception e){
			Instance.globalInstance.logger.log(LogLevel.Error, "Error adding mimetype " + e);
		}
	}
	public void load(){
		try{
			mimeTypeList.load(new FileInputStream(mimeTypeFile));
			Enumeration<Object> keys = mimeTypeList.keys();
			while(keys.hasMoreElements()){
				Object key = keys.nextElement();
				MimeType mime = new MimeType(key.toString(), mimeTypeList.get(key).toString());
				MimeType.mimeTypes.add(mime);
			}
		}
		catch(Exception e){
			Instance.globalInstance.logger.log(LogLevel.Error, "Error loading mimetypes: " + e + "\n");
		}
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
