package net.net16.jeremiahlowe.webserver;

import java.io.*;
import java.net.Socket;
import java.util.StringTokenizer;

import javax.script.*;

import net.net16.jeremiahlowe.webserver.cfg.MimeType;
import net.net16.jeremiahlowe.webserver.utility.Enums.LogLevel;
import net.net16.jeremiahlowe.webserver.utility.Instance;

public class ClientHandlerThread extends Thread {
	private Socket socket;
	private String indexFile = "";
	private String indexDirectory = "";
	private static ScriptEngine php;
	
	static{
		ScriptEngineManager sem = new ScriptEngineManager();
		php = sem.getEngineByExtension("php");
	}
	
	public ClientHandlerThread(String indexFile){
		this.indexFile = indexFile;
		indexDirectory = new File(indexFile).getParent();
	}
	
	public void start(Socket socket){
		this.socket = socket;
		super.start();
	}
	
	public void restart(Socket socket){
		this.socket = socket;
		if(isAlive()) interrupt();
		super.start();
	}

	public void run() {
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			PrintStream out = new PrintStream(new BufferedOutputStream(socket.getOutputStream()));
			String s = in.readLine();
			String fileName = "";
			if(s != null){
				StringTokenizer st = new StringTokenizer(s);
				try {
					//Tokenize file
					if (st.hasMoreElements() && st.nextToken().equalsIgnoreCase("GET") && st.hasMoreElements()) fileName = st.nextToken();
					else throw new FileNotFoundException();
					fileName = formatFileName(fileName);
					if(indexDirectory != null && !fileName.startsWith(indexDirectory)) fileName = indexDirectory + File.separator.charAt(0) + fileName;
					if (fileName.indexOf("..") >= 0 || fileName.indexOf(':') >= 0 || fileName.indexOf('|') >= 0) throw new FileNotFoundException();
					if (new File(fileName).isDirectory()) {
						fileName = fileName.replace('\\', '/');
						out.print("HTTP/1.0 301 Moved Permanently\r\n" + "Location: /" + fileName + "/\r\n\r\n");
						out.close();
						return;
					}
					//Get mimetype
					String mimeType = "text/plain";
					boolean foundMimeType = false;
					System.out.println(fileName);
					for(MimeType m : MimeType.mimeTypes){
						if(foundMimeType) break;
						if(fileName.endsWith(m.fileName)){
							mimeType = m.mimeType;
							foundMimeType = true;
						}
					}
					if(!foundMimeType) Instance.globalInstance.logger.log(LogLevel.Error, "Unable to find mime type for " + fileName + " defaulting to " + mimeType);
					Instance.globalInstance.logger.log(LogLevel.Debug, "Found mime type for " + fileName + " (" + mimeType + ")");
					//Check for PHP if there is PHP run the PHP
					InputStream f = new FileInputStream(fileName);
					if(fileName.endsWith(".php")){
						StringWriter sw = new StringWriter();
						PrintWriter pw = new PrintWriter(sw);
						php.getContext().setWriter(pw);
						try {
							php.eval(new InputStreamReader(f));
							f = new ByteArrayInputStream((sw.getBuffer() + "").getBytes());
						} catch (ScriptException e) {
							e.printStackTrace();
						}
						mimeType = "text/html";
					}
					//Send file
					out.print("HTTP/1.0 200 OK\r\n" + "Content-type: " + mimeType + "\r\n\r\n");
					byte[] a = new byte[4096];
					int n = 0;
					while ((n = f.read(a)) > 0) out.write(a, 0, n);
					//Close streams
					out.close();
					f.close();
				} 
				catch (FileNotFoundException x) {
					Instance.globalInstance.logger.log(LogLevel.Detailed, getName() + ": Error 404-File not found (" + fileName + ")");
					out.println("HTTP/1.0 404 Not Found\r\n" + "Content-type: text/html\r\n\r\n" + Instance.globalInstance.config.get404Error() + "\n");
					out.close();
				}
			} 
		}
		catch (IOException x) {
			Instance.globalInstance.logger.log(LogLevel.Error, "Error in clientHandlerThread: " + x);
		}
	}
	public String formatFileName(String fileName){
		if (fileName.endsWith("/")){
			fileName += indexFile;
		}
		while (fileName.indexOf("/") == 0){
			fileName = fileName.substring(1);
		}
		fileName = fileName.replace('/', File.separator.charAt(0));
		int seperator = 0;
		for(seperator = 0; seperator < fileName.length(); seperator++) if(fileName.charAt(seperator) == '?') break;
		return fileName.substring(0, seperator);
	}
	public String runPHP(String code){
		return "";
	}
}
