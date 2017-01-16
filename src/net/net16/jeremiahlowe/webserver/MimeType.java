package net.net16.jeremiahlowe.webserver;

import java.util.ArrayList;
import java.util.List;

public class MimeType {
	public static final MimeType[] defaultMimeTypes = {
			new MimeType(".txt", "text/plain"),
			new MimeType(".html", "text/html"),
			new MimeType(".htm", "text/html"),
			new MimeType(".js", "text/javascript"),
			new MimeType(".css", "text/css"),
			new MimeType(".gif", "image/gif"),
			new MimeType(".jpg", "image/jpeg"),
			new MimeType(".jpeg", "image/jpeg"),
			new MimeType(".bmp", "image/bmp"),
			new MimeType(".mp3", "audio/mpeg"),
			new MimeType(".mpeg", "video/mpeg"),
			new MimeType(".pdf", "application/pdf"),
			new MimeType(".png", "image/png"),
			new MimeType(".wav", "audio/wav"),
			new MimeType(".xml", "application/xml")
	};
	public static List<MimeType> mimeTypes = new ArrayList<MimeType>();
	public String fileName = ".txt";
	public String mimeType = "text/plain";
	public MimeType(String fileName, String mimeType){
		this.fileName = fileName;
		this.mimeType = mimeType;
	}
}
