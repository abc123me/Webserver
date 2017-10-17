package net.net16.jeremiahlowe.webserver.ui.gui;

import javax.swing.UIManager;

import org.apache.commons.cli.CommandLine;

import net.net16.jeremiahlowe.webserver.utility.Enums.LogLevel;
import net.net16.jeremiahlowe.webserver.utility.Instance;

public class MainGUI {
	public static void start(CommandLine cmd){
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			Instance.globalInstance.logger.log(LogLevel.Detailed, "Successfully set L+F!");
		}
		catch (Exception e) {
			Instance.globalInstance.logger.log(LogLevel.Warn, "Error setting L+F: " + e);
		}
		//Setup GUI
		Instance.globalInstance.gui = new GUI();
		Instance.globalInstance.gui.setGuiOptions();
		Instance.globalInstance.mimeTypeLoader.updateMimeTypesForGUI();
		Instance.globalInstance.gui.setVisible(true);
		Instance.globalInstance.logger.log(LogLevel.Detailed, "Setup GUI sucessfully");
		Instance.globalInstance.gui.setLogToFile(Instance.globalInstance.config.getLogToFile());
		Instance.globalInstance.config.save();
		Instance.globalInstance.gui.addListeners();
		Instance.globalInstance.guiReady = true;
	}
}
