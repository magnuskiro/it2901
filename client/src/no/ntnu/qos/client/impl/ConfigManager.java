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
			LOGGER.config("Set Logging to on");
		} else {
			LOGGER.config("Set Logging to off");
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
					@SuppressWarnings("deprecation")
					String logname = "Client"+new Date().toGMTString().replaceAll(" ", "_")+".log";
					file = new FileHandler(logname, true);
				} catch (SecurityException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			LOGGER.addHandler(file);
			LOGGER.config("Setting Logging to file on");
		} else {
			if(file!=null) {
				LOGGER.config("Setting Logging to file off");
				LOGGER.removeHandler(file);
			}
		}
	}
	/**
	 * Whether to log to console
	 * @param on boolean
	 */
	public static void setLogToConsole(boolean on) {
		if(on) {
			LOGGER.config("Setting Logging to console on");
		} else {
			LOGGER.config("Setting Logging to console off");
		}
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
