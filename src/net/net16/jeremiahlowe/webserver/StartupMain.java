package net.net16.jeremiahlowe.webserver;

import java.io.IOException;

import org.apache.commons.cli.*;

import net.net16.jeremiahlowe.webserver.cfg.Config;
import net.net16.jeremiahlowe.webserver.cfg.MimeTypeLoader;
import net.net16.jeremiahlowe.webserver.ui.TUI;
import net.net16.jeremiahlowe.webserver.ui.cli.MainCLI;
import net.net16.jeremiahlowe.webserver.ui.gui.MainGUI;
import net.net16.jeremiahlowe.webserver.utility.Enums.LogLevel;
import net.net16.jeremiahlowe.webserver.utility.Instance;
import net.net16.jeremiahlowe.webserver.utility.Logger;

public class StartupMain {
	public static void main(String[] args){
		Options opt = TUI.putOptions(new Options());
		CommandLineParser parser = new DefaultParser();
		CommandLine cmd = null;
		try {cmd = parser.parse(opt, args);
		} catch (ParseException e) {
			System.out.println("Cannot parse arguments, Use -help for a list of arguments!");
			return; //Quit
		}
		if(cmd.hasOption("help")){
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp(" ", opt);
			return; //Quit
		}
		init(cmd);
		postInit(cmd);
	}
	public static void init(CommandLine cmd){
		//Setup key components
		Instance.globalInstance = new Instance();
		boolean ansiN = cmd.hasOption("no_ansi"), logToFileN = cmd.hasOption("no_log_file");
		if(ansiN) System.out.println("ANSI disabled");
		if(logToFileN) System.out.println("Log to file disabled");
		Instance.globalInstance.logger = new Logger(!ansiN, !logToFileN);
		//Load up mime types and configuration
		Instance.globalInstance.mimeTypeLoader = new MimeTypeLoader();
		Instance.globalInstance.logger.log(LogLevel.Detailed, "Loading mime types!");
		Instance.globalInstance.mimeTypeLoader.load();
		Instance.globalInstance.config = new Config();
		Instance.globalInstance.logger.log(LogLevel.Detailed, "Loading config!");
		Instance.globalInstance.config.load();
		//Start creating server stuff
		Instance.globalInstance.delegator = new Delegator();
		Instance.globalInstance.delegatorThread = new Thread(Instance.globalInstance.delegator);
		Instance.globalInstance.delegatorThread.setName("Delegator");
	}
	public static void postInit(CommandLine cmd){
		//Shutdown hook
		Runtime.getRuntime().addShutdownHook(new Thread(){
			@Override public void run(){
				Instance.globalInstance.config.save();
				try {Instance.globalInstance.logger.close();} 
				catch (IOException e) {}
			}
		});
		//Startup
		if(cmd.hasOption("cli")) MainCLI.start(cmd);
		else MainGUI.start(cmd);
		
		Instance.globalInstance.logger.log(LogLevel.Fatal, "test");
		Instance.globalInstance.logger.log(LogLevel.Severe, "test");
		Instance.globalInstance.logger.log(LogLevel.Error, "test");
		Instance.globalInstance.logger.log(LogLevel.Warn, "test");
		Instance.globalInstance.logger.log(LogLevel.Info, "test");
	}
}
