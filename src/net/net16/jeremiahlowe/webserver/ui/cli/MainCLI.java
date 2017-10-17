package net.net16.jeremiahlowe.webserver.ui.cli;

import java.io.File;
import java.util.Scanner;

import org.apache.commons.cli.CommandLine;

import net.net16.jeremiahlowe.webserver.utility.Enums.LogLevel;
import net.net16.jeremiahlowe.webserver.ui.TUI;
import net.net16.jeremiahlowe.webserver.utility.Instance;
import net.net16.jeremiahlowe.webserver.utility.Utility;

public class MainCLI {
	public static void start(CommandLine cmd){
		Scanner input = new Scanner(System.in);
		while(true){
			String command = input.nextLine().toLowerCase().trim();
			if(command.contentEquals("exit")) break;
			else processCommand(command);
		}
		Instance.globalInstance.delegator.setServerRunning(false);
		input.close();
	}
	public static void processCommand(String command){
		//===============================================================
		//start command
		//===============================================================
		if(command.indexOf("start") >= 0 && !Instance.globalInstance.delegatorThread.isAlive()) Instance.globalInstance.delegator.setServerRunning(true);
		else if(command.indexOf("start") >= 0) Instance.globalInstance.logger.log(LogLevel.Info, "Server is already running");
		//===============================================================
		//stop command
		//===============================================================
		else if(command.indexOf("stop") >= 0 && Instance.globalInstance.delegatorThread.isAlive()) Instance.globalInstance.delegator.setServerRunning(false);
		else if(command.indexOf("stop") >= 0) Instance.globalInstance.logger.log(LogLevel.Info, "Server is already stopped");
		//===============================================================
		//help conmmand
		//===============================================================
		else if(command.indexOf("help") >= 0) Instance.globalInstance.logger.log(LogLevel.Info, TUI.getHelp());
		//===============================================================
		//port command
		//===============================================================
		else if(command.indexOf("port") >= 0 && !Instance.globalInstance.delegatorThread.isAlive()){
			int newPort = 0;
			try{newPort = Integer.parseInt(command.substring(5));}
			catch(Exception e){Instance.globalInstance.logger.log(LogLevel.Info, "Invalid parameters, Syntax is port <port>");}
			if(newPort <= 0 || newPort > 65535) Instance.globalInstance.logger.log(LogLevel.Info, "Invalid port! (" + newPort + ")");
			else{
				Instance.globalInstance.config.setPort(newPort);
				Instance.globalInstance.logger.log(LogLevel.Info, "Successfully changed port to: " + Instance.globalInstance.config.getPort());
			}
		}
		else if(command.indexOf("port") >= 0) Instance.globalInstance.logger.log(LogLevel.Info, "Cannot change port while server is running");
		//===============================================================
		//logtofile command
		//===============================================================
		else if(command.indexOf("logtofile") >= 0){
			try{
				Instance.globalInstance.config.setLogToFile(Boolean.parseBoolean(command.substring(10)));
				Instance.globalInstance.logger.log(LogLevel.Info, (Instance.globalInstance.config.getLogToFile() ? "Enabled" : "Disabled") + " logging to file");
			}
			catch(Exception e){Instance.globalInstance.logger.log(LogLevel.Info, "Invalid parameters, Syntax is logtofile <true/false>");}
		}
		//===============================================================
		//html command
		//===============================================================
		else if(command.indexOf("html") >= 0 && !Instance.globalInstance.delegatorThread.isAlive()){
			String newHtml = "";
			try{newHtml = command.substring(5);}
			catch(Exception e){Instance.globalInstance.logger.log(LogLevel.Info, "Invalid parameters, Syntax is html <html>");}
			if(new File(newHtml).exists()){
				Instance.globalInstance.config.setHTML(newHtml);
				Instance.globalInstance.logger.log(LogLevel.Info, "Successfully changed html to: " + Instance.globalInstance.config.getHTMLFile());
			}
			else Instance.globalInstance.logger.log(LogLevel.Info, "Html file " + newHtml + " dont exist :(");
		}
		else if(command.indexOf("html") >= 0) Instance.globalInstance.logger.log(LogLevel.Info, "Cannot change html file while server is running");
		//===============================================================
		//threads command
		//===============================================================
		else if(command.indexOf("threads") >= 0 && !Instance.globalInstance.delegatorThread.isAlive()){
			int newThreads = 0;
			try{newThreads = Integer.parseInt(command.substring(8));}
			catch(Exception e){Instance.globalInstance.logger.log(LogLevel.Info, "Invalid parameters, Syntax is thread <threads>");}
			if(newThreads < 1 || newThreads > 100) Instance.globalInstance.logger.log(LogLevel.Info, "Invalid amount of max threads " + newThreads + " it must be between 1-100");
			else{
				Instance.globalInstance.config.setThreads(newThreads);
				Instance.globalInstance.logger.log(LogLevel.Info, "Successfully changed thread amount to " + Instance.globalInstance.config.getThreads());
			}
		}
		else if(command.indexOf("threads") >= 0) Instance.globalInstance.logger.log(LogLevel.Info, "Cannot change thread amount while running");
		//===============================================================
		//php command
		//===============================================================
		else if(command.indexOf("php") >= 0 && !Instance.globalInstance.delegatorThread.isAlive()){
			try{
				Instance.globalInstance.config.setPHPEnabled(Boolean.parseBoolean(command.substring(4)));
				Instance.globalInstance.logger.log(LogLevel.Info, (Instance.globalInstance.config.getPHPEnabled() ? "Enabled" : "Disabled") + " logging to file");
				Utility.verifyPHP();
			}
			catch(Exception e){Instance.globalInstance.logger.log(LogLevel.Info, "Invalid parameters, Syntax is php <true/false>");}
		}
		else if(command.indexOf("php") >= 0) Instance.globalInstance.logger.log(LogLevel.Info, "Cannot enable/disable PHP while running");
		else Instance.globalInstance.logger.log(LogLevel.Info, "Unknown command: " + command);
		//===============================================================
		//And finally *drumroll* SAVING!
		//===============================================================
		Instance.globalInstance.config.save();
	}
}
