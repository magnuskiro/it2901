package no.ntnu.qos.client.test;

import no.ntnu.qos.client.impl.ConfigManager;

public class ManualLoggingTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ConfigManager.initLog();
		ConfigManager.setLogToFile(true);
		ConfigManager.LOGGER.fine("THIS SHOULD NOT SHOW UP!");
		ConfigManager.LOGGER.warning("test Warning");
		ConfigManager.setLogging(true);
		ConfigManager.LOGGER.info("Test fine");
		ConfigManager.LOGGER.warning("test Warning2");
	}

}
