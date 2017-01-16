package net.net16.jeremiahlowe.webserver;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utility {
	private static PrintStream logFileOut;
	private static File logDirectory = new File("logs");
	private static File logFile;
	private static boolean firstTimeLog = true;
	private static String logFileName = "";
	
	public static String getIP(Socket in){return in.getRemoteSocketAddress().toString();}
	public static void init(){
		String time = new SimpleDateFormat("yyyy.MM.dd-HH.mm-ss").format(new Date());
		logFileName = "logs/" + time + ".txt";
		logFile = new File(logFileName);
	}
	public static String removeChars(String in, char[] toRemove){
		String out = "";
		for(int i = 0; i < in.length(); i++){
			boolean add = true;
			char c = in.charAt(i);
			for(int j = 0; j < toRemove.length; j++){
				if(c == toRemove[j]){
					add = false;
					break;
				}
			}
			if(add) out += c;
		}
		return out;
	}
	public static void logHelp(){
		log("Commands:", false);
		log("\t-start: Starts HTTP server", false);
		log("\t-stop: Stops HTTP server", false);
		log("\t-exit: Exits this program", false);
		log("\t-html <html>: Sets the main HTML file's location (default index.html)", false);
		log("\t-port <port>: Sets the port (default 80)", false);
		log("\t-clients <max clients>: Sets the maximum clients (default 100)", false);
		log("\t-logtofile <true/false>: Enables/Disables logging to file", false);
		log("\t-help: Prints this out", false);
	}
	public static void log(String message){log(message, true);}
	public static void log(String message, boolean toFile){
		if(!Startup.isConsoleMode()) Instance.gui.log(message);
		if(toFile && (Instance.gui == null ? Instance.config.getLogToFile() : Instance.gui.shouldLogToFile())){
			try {
				if(!logDirectory.exists()) logDirectory.mkdir();
				if(!logFile.exists()) logFile.createNewFile();
			}catch (Exception e) {e.printStackTrace(); System.exit(1);}
			
			try{
				if(firstTimeLog){
					firstTimeLog = false;
					logFileOut = new PrintStream(new FileOutputStream(logFile));
				}
				logFileOut.println(message);
			}catch(Exception e){e.printStackTrace(); System.exit(-1);}
		}
		System.out.println(message);
	}
}
