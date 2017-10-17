package net.net16.jeremiahlowe.webserver.ui;

import org.apache.commons.cli.*;

public class TUI {
	public static Options putOptions(Options options){
		options.addOption("cli", "Run as command line only");
		options.addOption("start", "Start the server instantly");
		options.addOption("php", "Enable PHP");
		options.addOption("no_log_file", "Disable logging to file");
		options.addOption("help", "Display help");
		options.addOption("no_ansi", "Disables ansi");
		options.addOption(Option.builder("port").hasArg().argName("port").desc("Sets the port to use").build());
		options.addOption(Option.builder("html").hasArg().argName("filename").desc("Sets the root page").build());
		options.addOption(Option.builder("error404").hasArg().argName("filename").desc("Sets the error 404 page").build());
		options.addOption(Option.builder("threads").hasArg().argName("threads").desc("Sets the amount of threads to use").build());
		return options;
	}
	public static final String getHelp(){
		String out = "Commands:" + System.lineSeparator();
		out += "\t-start: Starts the webserver" + System.lineSeparator();
		out += "\t-stop: Stops the webserver" + System.lineSeparator();
		out += "\t-exit: Exits the wenbserver" + System.lineSeparator();
		out += "\t-html <html>: Sets the main HTML file's location (default index.html)" + System.lineSeparator();
		out += "\t-port <port>: Sets the port (default 80)" + System.lineSeparator();
		out += "\t-threads <threads>: Sets the amount of threads to use (default 2)" + System.lineSeparator();
		out += "\t-logtofile <true/false>: Enables/Disables logging to file" + System.lineSeparator();
		out += "\t-help: Prints this out" + System.lineSeparator();
		out += "\t-php <true/false>: Enables/Disables PHP"+ System.lineSeparator();
		return out;
	}
}
