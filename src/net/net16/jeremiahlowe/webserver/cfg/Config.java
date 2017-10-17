package net.net16.jeremiahlowe.webserver.cfg;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

import net.net16.jeremiahlowe.webserver.utility.Enums.LogLevel;
import net.net16.jeremiahlowe.webserver.utility.Instance;

public class Config{
	private boolean logToFile = false;
	private int port = 80;
	private String html = "index.html";
	private String html404 = "404.html";
	private boolean useHtml404 = false;
	private int threads = 2;
	private File configFile = new File("config/config.txt");
	private String error404Text = "";
	private Properties configProperties = new Properties();
	private boolean logHTTP = false;
	private boolean logDelegation = false;
	private boolean phpEnabled = false;
	private String loadingStatus = "Not loaded";
	
	public void load(){
		loadingStatus = "";
		try {
			configProperties.load(new FileInputStream(configFile));
			if(configProperties.containsKey("port")){
				port = Integer.parseInt(configProperties.getProperty("port"));
				loadingStatus += "Set port to " + port + "\n";
			}
			if(configProperties.containsKey("threads")){
				threads = Integer.parseInt(configProperties.getProperty("threads"));
				loadingStatus += "Set max threads to " + threads + "\n";
			}
			if(configProperties.containsKey("html")){
				html = configProperties.getProperty("html");
				loadingStatus += "Set HTML file to " + html + "\n";
			}
			if(configProperties.containsKey("logToFile")){
				logToFile = Boolean.parseBoolean(configProperties.getProperty("logToFile"));
				loadingStatus += (logToFile ? "Enabled" : "Disabled") + " logging to file\n";
			}
			if(configProperties.containsKey("logHTTP")){
				logHTTP = Boolean.parseBoolean(configProperties.getProperty("logHTTP"));
				loadingStatus += (logHTTP ? "Enabled" : "Disabled") + " logging of HTTP\n";
			}
			if(configProperties.containsKey("logDelegation")){
				logDelegation = Boolean.parseBoolean(configProperties.getProperty("logDelegation"));
				loadingStatus += (logDelegation ? "Enabled" : "Disabled") + " delegator logging\n";
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
					loadingStatus += "Set custom 404 error\n";
				}
				catch(Exception e){
					useHtml404 = false;
					loadingStatus += "Error setting custom 404 message: " + e + "\n";
				}
			}
			if(configProperties.containsKey("phpEnabled")){
				phpEnabled = Boolean.parseBoolean((String) configProperties.get("phpEnabled"));
				loadingStatus += (phpEnabled ? "Enabled" : "Disabled") + " PHP\n";
			}
			loadingStatus += "Successfully loaded config file!\n";
		} catch (Exception e) {
			loadingStatus += "Error loading config " + e + "\n";
			e.printStackTrace();
		}
	}
	public void save(){
		try{
			configProperties.put("html", html);
			configProperties.put("port", String.valueOf(port));
			configProperties.put("threads", String.valueOf(threads));
			configProperties.put("logToFile", String.valueOf(logToFile));
			configProperties.put("logDelegation", String.valueOf(logDelegation));
			configProperties.put("logHTTP", String.valueOf(logHTTP));
			configProperties.put("phpEnabled", String.valueOf(phpEnabled));
			if(!useHtml404) configProperties.put("html404", "default");
			configProperties.store(new FileOutputStream(configFile), "Main config file for java webserver");
		}
		catch(Exception e){Instance.globalInstance.logger.log(LogLevel.Severe, "Error saving config " + e);}
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
	public int getThreads(){return threads;}
	public boolean getLogToFile(){return logToFile;}
	public void setLogToFile(boolean logToFile){this.logToFile = logToFile;}
	public void setPort(int port){this.port = port;}
	public void setHTML(String html){this.html = html;}
	public void setThreads(int threads){this.threads = threads;}
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
	public String getLoadingStatus(){
		return loadingStatus;
	}
}
