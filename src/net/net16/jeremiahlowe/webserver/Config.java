package net.net16.jeremiahlowe.webserver;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

public class Config {
	private boolean logToFile = false;
	private int port = 80;
	private String html = "index.html";
	private String html404 = "404.html";
	private boolean useHtml404 = false;
	private int clients = 100;
	private File configFile = new File("config/config.txt");
	private String error404Text = "";
	private Properties configProperties = new Properties();
	private boolean logHTTP = false;
	private boolean logDelegation = false;
	private boolean phpEnabled = false;
	
	public String loadConfig(){
		String status = "";
		try {
			configProperties.load(new FileInputStream(configFile));
			if(configProperties.containsKey("port")){
				port = Integer.parseInt(configProperties.getProperty("port"));
				status += "Set port to " + port + "\n";
			}
			if(configProperties.containsKey("clients")){
				clients = Integer.parseInt(configProperties.getProperty("clients"));
				status += "Set max clients to " + clients + "\n";
			}
			if(configProperties.containsKey("html")){
				html = configProperties.getProperty("html");
				status += "Set HTML file to " + html + "\n";
			}
			if(configProperties.containsKey("logToFile")){
				logToFile = Boolean.parseBoolean(configProperties.getProperty("logToFile"));
				status += (logToFile ? "Enabled" : "Disabled") + " logging to file\n";
			}
			if(configProperties.containsKey("logHTTP")){
				logHTTP = Boolean.parseBoolean(configProperties.getProperty("logHTTP"));
				status += (logHTTP ? "Enabled" : "Disabled") + " logging of HTTP\n";
			}
			if(configProperties.containsKey("logDelegation")){
				logDelegation = Boolean.parseBoolean(configProperties.getProperty("logDelegation"));
				status += (logDelegation ? "Enabled" : "Disabled") + " delegator logging\n";
			}
			if(configProperties.containsKey("html404") && configProperties.getProperty("html404") != "default"){
				html404 = configProperties.getProperty("html404");
				try{
					File error = new File(html404);
					if(error != null && error.exists() && !error.isDirectory() && error.canRead()){
						error404Text = new String(Files.readAllBytes(Paths.get(error.getPath())));
						useHtml404 = true;
					}
					else useHtml404 = false;
					status += "Set custom 404 error\n";
				}
				catch(Exception e){
					useHtml404 = false;
					status += "Error setting custom 404 message: " + e + "\n";
				}
			}
			if(configProperties.containsKey("phpEnabled")){
				phpEnabled = Boolean.parseBoolean((String) configProperties.get("phpEnabled"));
				status += (phpEnabled ? "Enabled" : "Disabled") + " PHP\n";
			}
			status += "Successfully loaded config file!\n";
		} catch (Exception e) {
			status += "Error loading config " + e + "\n";
			e.printStackTrace();
		}
		return status;
	}
	public void save(){
		try{
			configProperties.put("html", html);
			configProperties.put("port", String.valueOf(port));
			configProperties.put("clients", String.valueOf(clients));
			configProperties.put("logToFile", String.valueOf(logToFile));
			configProperties.put("logDelegation", String.valueOf(logDelegation));
			configProperties.put("logHTTP", String.valueOf(logHTTP));
			configProperties.put("phpEnabled", String.valueOf(phpEnabled));
			if(!useHtml404) configProperties.put("html404", "default");
			configProperties.store(new FileOutputStream(configFile), "Main config file for java webserver");
		}
		catch(Exception e){Utility.log("Error saving config " + e);}
	}
	public String makeFile() throws Exception{
		String out = "";
		if(!configFile.exists()){
			configFile.createNewFile();
			out += "Made config file\n";
		}
		return out;
	}
	
	public boolean getLogHTTP(){return logHTTP;}
	public boolean getLogDelegator(){return logDelegation;}
	public void setLogHTTP(boolean logHTTP){this.logHTTP = logHTTP;}
	public void setLogDelegator(boolean logDelegation){this.logDelegation = logDelegation;}
	public int getPort(){return port;}
	public String getHTMLFile(){return html;}
	public int getClients(){return clients;}
	public boolean getLogToFile(){return logToFile;}
	public void setLogToFile(boolean logToFile){this.logToFile = logToFile;}
	public void setPort(int port){this.port = port;}
	public void setHTML(String html){this.html = html;}
	public void setClients(int clients){this.clients = clients;}
	public void setPHPEnabled(boolean enabled){phpEnabled = enabled;}
	public boolean getPHPEnabled(){return phpEnabled;}
	public String get404Error(){
		if(useHtml404) return error404Text;
		else{
			return "<!DOCTYPE html><html><head><title>Error 404: Page not found</title><style>"
					+ "#ohno{color:red;display:inline;}</style></head><body>"
					+ "<h1><b><center><p><div id=\"ohno\">Oh noes!</div> The internet puppy has a migrain :(</p></b></center></h1>"
					+ "<h2><center><p>Error 404: Page not found</h2></center></p></body></html>";
		}
	}
}
