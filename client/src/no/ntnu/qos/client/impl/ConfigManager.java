package no.ntnu.qos.client.impl;

import java.io.IOException;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConfigManager {
	public static final Logger LOGGER = Logger.getLogger("no.ntnu.qos.client");
	private static FileHandler file;


	public static void setLogging(boolean on) {
		if(on) {
			LOGGER.setLevel(Level.FINEST);
		} else {
			LOGGER.setLevel(Level.SEVERE);
		}
	}
	public static void setLogToFile(boolean on) {
		if (on) {
			if (file==null) {
				try {
					String logname = "Client"+new Date().toGMTString().replaceAll(" ", "_")+".log";
					file = new FileHandler(logname, true);
				} catch (SecurityException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			LOGGER.addHandler(file);
		} else {
			if(file!=null) {
				LOGGER.removeHandler(file);
			}
		}
	}
	public static void setLogToConsole(boolean on) {
		LOGGER.setUseParentHandlers(on);
	}
	/**
	 * Initiate a log
	 */
	public static void initLog() {
		LOGGER.setLevel(Level.SEVERE);
	}
}
