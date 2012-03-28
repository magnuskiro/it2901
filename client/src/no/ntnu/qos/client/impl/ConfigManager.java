package no.ntnu.qos.client.impl;

import java.io.IOException;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class for managing configuration options 
 * @author Stig Tore
 *
 */
public class ConfigManager {
	//Logger variables
	public static final Logger LOGGER = Logger.getLogger("no.ntnu.qos.client");
	private static FileHandler file;


	/**
	 * Whether to log anything more important than severe.
	 * @param on boolean
	 */
	public static void setLogging(boolean on) {
		if(on) {
			LOGGER.setLevel(Level.FINEST);
		} else {
			LOGGER.setLevel(Level.SEVERE);
		}
	}
	/**
	 * Whether to log to file
	 * @param on boolean
	 */
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
	/**
	 * Whether to log to console
	 * @param on boolean
	 */
	public static void setLogToConsole(boolean on) {
		LOGGER.setUseParentHandlers(on);
	}
	/**
	 * Initiate a log
	 * Defaults to logging off, log to console on and log to file to off.
	 */
	public static void initLog() {
		LOGGER.setLevel(Level.SEVERE);
	}
}
