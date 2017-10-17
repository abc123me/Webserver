package net.net16.jeremiahlowe.webserver.utility;

import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import net.net16.jeremiahlowe.webserver.utility.Enums.LogLevel;

public class Logger implements Closeable{
	public static final String LOG_FILE_TYPE = ".log", LOG_FILE_DIR = "logs/";
	public boolean ansiEnabled;
	public int logLevelNormal = 0;
	public int logLevelFile = 0;
	
	private boolean logToFile = true;
	private File logFile;
	private FileOutputStream logStream;
	
	public Logger(boolean ansiEnabled, boolean logToFile) {
		this.ansiEnabled = ansiEnabled;
		this.logToFile = logToFile;
		setLogToFile(logToFile);
	}
	
	public void close() throws IOException{
		logStream.close();
		logStream = null;
		logFile = null;
	}
	
	public void log(LogLevel level, String text){
		if(level.getLevel() >= logLevelNormal)
			forceLogNorm(level, text);
		if(logToFile && level.getLevel() >= logLevelFile)
			forceLogFile(level, text);
	}
	public void logException(LogLevel level, String text, Exception e){
		int detail = 5;
		switch(level){
			case Fatal: detail = 1000; break;
			case Severe: detail = 100; break;
			case Error: detail = 20; break;
			case Warn: detail = 10; break;
			case Detailed: detail = 10; break;
			case Debug: detail = 30; break;
			default: detail = 5; break;
		}
		logException(level, text, e, detail);
	}
	public void logException(LogLevel level, String text, Exception e, int detail){
		StackTraceElement[] elems = e.getStackTrace();
		String stack = "";
		for(int i = 0; i < Math.min(elems.length, detail); i++){
			StackTraceElement elem = elems[i];
			stack += "\t" + elem.toString();
			stack += System.lineSeparator();
		}
		log(level, text + System.lineSeparator() + e + System.lineSeparator() + stack);
	}
	public void forceLogNorm(LogLevel level, String text) {
		System.out.println(level.getPrefix(ansiEnabled) + text);
		if(Instance.globalInstance.guiReady)
			Instance.globalInstance.gui.log(level.getPrefix(ansiEnabled) + text);
	}
	public void forceLogFile(LogLevel level, String text) {
		if(!logToFile) return;
		byte[] b = (level.getPrefix(false) + " " + text + System.lineSeparator()).getBytes();
		try{
			logStream.write(b);
		}
		catch(Exception e){
			setLogToFile(false);
			logException(LogLevel.Severe, "Unable to log to file: ", e);
		}
	}
	public void setLogToFile(boolean logToFile){
		if(logToFile){
			if(logStream != null || logFile != null) 
				return;
			try{
				String time = new SimpleDateFormat("yyyy.MM.dd-HH.mm-ss").format(new Date(System.currentTimeMillis()));
				String fileName = LOG_FILE_DIR + time + LOG_FILE_TYPE;
				logFile = new File(fileName);
				File logDir = new File(LOG_FILE_DIR);
				logDir.mkdir();
				logFile.createNewFile();
				logStream = new FileOutputStream(logFile);
			}
			catch(Exception e){
				this.logToFile = false;
				logException(LogLevel.Severe, "Unable to enable logging to file: ", e);
			}
		}
		else{
			try{
				if(logStream == null && logFile == null) 
					return;
				close();
			}
			catch(Exception e){
				this.logToFile = false;
				logException(LogLevel.Error, "Error closing file stream, but logging was still disabled: ", e);
			}
		}
		this.logToFile = logToFile;
	}
}
