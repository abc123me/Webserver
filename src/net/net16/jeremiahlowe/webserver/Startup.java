package net.net16.jeremiahlowe.webserver;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Scanner;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

public class Startup {
	private static boolean consoleMode = false;
	private static File configDirectory = new File("config");

	public static void main(String[] args) {
		Utility.init();
		String startingLog = "";
		startingLog += preInit();
		startingLog += parseArgs(args);
		Instance.delegator = new Delegator();
		if(!consoleMode) postInitGUI(startingLog);
		else postInitConsole(startingLog); 
		Runtime.getRuntime().addShutdownHook(new Thread(){@Override public void run(){Instance.config.save();}});
		verifyPHP();
	}
	public static void postInitConsole(String startingLog){
		Scanner input = new Scanner(System.in);
		Utility.log(startingLog);
		Utility.logHelp();
		Instance.delegatorThread = new Thread(Instance.delegator);
		Instance.delegatorThread.setName("Delegator");
		Instance.config.save();
		while(true){
			String command = input.nextLine().toLowerCase();
			if(command.indexOf("exit") > 0) break;
			else processCommand(command);
		}
		onServerActivate(false);
		input.close();
	}
	public static void postInitGUI(String startingLog){
		try {UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());}
		catch (Exception e) {startingLog += "Error setting LF\n" + e + "\n";}
		Instance.gui = new GUI();
		Instance.gui.setGuiOptions();
		Instance.mimeTypeLoader.loadToGUI();
		Instance.gui.setVisible(true);
		startingLog += "GUI initated succesfully!";
		Utility.log(startingLog);
		Instance.delegatorThread = new Thread(Instance.delegator);
		Instance.delegatorThread.setName("Delegator");
		Instance.gui.setLogToFile(Instance.config.getLogToFile());
		Instance.config.save();
		Instance.gui.addListeners();
	}
	public static String preInit(){
		String startingLog = "";
		Instance.config = new Config();
		Instance.mimeTypeLoader = new MimeTypeLoader();
		startingLog += init();
		startingLog += "Loading config\n";
		startingLog += Instance.config.loadConfig();
		startingLog += "Config successfully loaded!\n";
		startingLog += "Loading mime types\n";
		startingLog += Instance.mimeTypeLoader.loadMimes();
		startingLog += "Mime types successfully loaded!\n";
		return startingLog;
	}
	public static String parseArgs(String[] args){
		String startingLog = "";
		try{if(args.length >= 1) consoleMode = Boolean.parseBoolean(args[0]);}
		catch(Exception e){startingLog += "Error parsing args[0]: " + e + "\n";}
		return startingLog;
	}
	public static String init(){
		String out = "";
		if(!configDirectory.exists()){
			configDirectory.mkdir();
			out += "Made config directory\n";
		}
		try{
			out += Instance.config.makeFile();
			Instance.mimeTypeLoader.createMimeFile();
		}catch(Exception e){e.printStackTrace(); System.exit(1);}
		return out;
	}
	public static void processCommand(String command){
		if(command.indexOf("start") >= 0 && !Instance.delegatorThread.isAlive()) onServerActivate(true);
		else if(command.indexOf("start") >= 0) Utility.log("Server is already running");
		
		else if(command.indexOf("stop") >= 0 && Instance.delegatorThread.isAlive()) onServerActivate(false);
		else if(command.indexOf("stop") >= 0) Utility.log("Server is already stopped");
		
		else if(command.indexOf("help") >= 0) Utility.logHelp();
		
		else if(command.indexOf("port") >= 0 && !Instance.delegatorThread.isAlive()){
			int newPort = 0;
			try{newPort = Integer.parseInt(command.substring(5));}
			catch(Exception e){Utility.log("Invalid parameters, Syntax is port <port>");}
			if(newPort <= 0 || newPort > 65535) Utility.log("Invalid port! (" + newPort + ")");
			else{
				Instance.config.setPort(newPort);
				Utility.log("Successfully changed port to: " + Instance.config.getPort());
			}
		}
		else if(command.indexOf("port") >= 0) Utility.log("Cannot change port while server is running");
		
		else if(command.indexOf("logtofile") >= 0){
			try{
				Instance.config.setLogToFile(Boolean.parseBoolean(command.substring(10)));
				Utility.log((Instance.config.getLogToFile() ? "Enabled" : "Disabled") + " logging to file");
			}
			catch(Exception e){Utility.log("Invalid parameters, Syntax is logtofile <true/false>");}
		}
		
		else if(command.indexOf("html") >= 0 && !Instance.delegatorThread.isAlive()){
			String newHtml = "";
			try{newHtml = command.substring(5);}
			catch(Exception e){Utility.log("Invalid parameters, Syntax is html <html>");}
			if(new File(newHtml).exists()){
				Instance.config.setHTML(newHtml);
				Utility.log("Successfully changed html to: " + Instance.config.getHTMLFile());
			}
			else Utility.log("Html file " + newHtml + " dont exist :(");
		}
		else if(command.indexOf("html") >= 0) Utility.log("Cannot change html file while server is running");
		
		else if(command.indexOf("clients") >= 0 && !Instance.delegatorThread.isAlive()){
			int newClients = 0;
			try{newClients = Integer.parseInt(command.substring(8));}
			catch(Exception e){Utility.log("Invalid parameters, Syntax is clients <max clients>");}
			if(newClients <= 0 || newClients > 1500) Utility.log("Invalid amount of max clients " + newClients + " must be between 0-1500");
			else{
				Instance.config.setClients(newClients);
				Utility.log("Successfully changed client amount to " + Instance.config.getClients());
			}
		}
		else if(command.indexOf("clients") >= 0) Utility.log("Cannot change client amount while running");
		else if(command.indexOf("php") >= 0 && !Instance.delegatorThread.isAlive()){
			try{
				Instance.config.setPHPEnabled(Boolean.parseBoolean(command.substring(4)));
				Utility.log((Instance.config.getPHPEnabled() ? "Enabled" : "Disabled") + " logging to file");
				verifyPHP();
			}
			catch(Exception e){Utility.log("Invalid parameters, Syntax is php <true/false>");}
		}
		else if(command.indexOf("php") >= 0) Utility.log("Cannot enable/disable PHP while running");
		else Utility.log("Unknown command: " + command);
		Instance.config.save();
	}
	public static void onServerActivate(boolean isOn){
		if(isOn){
			Utility.log("Starting server on port " + (consoleMode ? Instance.config.getPort() : Instance.gui.getPort()));
			Instance.delegatorThread = new Thread(Instance.delegator);
			Instance.delegatorThread.start();			
		}
		else{
			Utility.log("Stopping server and killing any rouge threads");
			Instance.delegator.stopServer();
			try{Instance.delegator.closeServerSocket();}
			catch(Exception e){Utility.log("Error closing serversocket: " + e);}
		}
	}
	public static boolean isConsoleMode(){return consoleMode;}
	public static void verifyPHP(){
		if(Instance.config.getPHPEnabled()){
			Utility.log("Verifying PHP");
			ScriptEngineManager sem = new ScriptEngineManager();
			ScriptEngine php = sem.getEngineByExtension("php");
			try{
				StringWriter sw = new StringWriter();
			    PrintWriter pw = new PrintWriter(sw);
				php.getContext().setWriter(pw);
				php.eval("<?php echo \"PHP is working\n\";");
				Utility.log(sw.getBuffer() + "");
			}
			catch(Exception e){
				Instance.config.setPHPEnabled(false);
				if(!consoleMode){
					JOptionPane.showMessageDialog(Instance.gui, "PHP cannot be enabled because it is not installed\nPlease install php and php-cgi");
					Instance.gui.setPHPEnabled(false);
				}
				System.out.println(e);
				Utility.log("PHP was not installed so it was disabled");
			}
		}
	}
}


