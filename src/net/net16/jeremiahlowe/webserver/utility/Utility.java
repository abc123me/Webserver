package net.net16.jeremiahlowe.webserver.utility;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.Socket;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.swing.JOptionPane;

import net.net16.jeremiahlowe.webserver.utility.Enums.LogLevel;

public class Utility {
	public static String getOS(){
		String os = System.getProperty("os.name").toLowerCase();
		System.out.println("Using: " + os);
		return os;
	}
	public static String getIP(Socket in){return in.getRemoteSocketAddress().toString();}
	public static String removeLeadingChar(String from, char toRemove){
		int si = 0;
		for(int i = 0; i < from.length(); i++){
			if(from.charAt(i) == toRemove) si = i;
			else break;
		}
		return from.substring(si);
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
	public static void verifyPHP(){
		Instance.globalInstance.logger.log(LogLevel.Info, "Verifying PHP");
		try{
			ScriptEngineManager sem = new ScriptEngineManager();
			ScriptEngine php = sem.getEngineByExtension("php");
			StringWriter sw = new StringWriter();
		    PrintWriter pw = new PrintWriter(sw);
			php.getContext().setWriter(pw);
			php.eval("<?php echo \"PHP is working\";");
			Instance.globalInstance.logger.log(LogLevel.Info, sw.getBuffer() + "");
		}
		catch(Exception e){
			Instance.globalInstance.config.setPHPEnabled(false);
			if(!Instance.globalInstance.consoleMode){
				JOptionPane.showMessageDialog(Instance.globalInstance.gui, "PHP cannot be enabled because it is not installed" 
						+ System.lineSeparator() + "Please install php and php-cgi");
				Instance.globalInstance.gui.setPHPEnabled(false);
			}
			Instance.globalInstance.logger.log(LogLevel.Info, "PHP was not installed so it was disabled");
		}
	}
}
