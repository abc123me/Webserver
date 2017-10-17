package net.net16.jeremiahlowe.webserver.utility;

import net.net16.jeremiahlowe.webserver.Delegator;
import net.net16.jeremiahlowe.webserver.cfg.Config;
import net.net16.jeremiahlowe.webserver.cfg.MimeTypeLoader;
import net.net16.jeremiahlowe.webserver.ui.gui.GUI;

public class Instance {
	public static Instance globalInstance;
	
	public GUI gui;
	public Config config;
	public MimeTypeLoader mimeTypeLoader;
	public Thread delegatorThread; 
	public Delegator delegator; 
	public boolean consoleMode;
	public Logger logger;
	public boolean guiReady;
}
