package no.ntnu.qos.client;

/**
 * Interface for being able to listen to all reply messages from services recieved by the client library 
 * @author Stig Tore
 *
 */
public interface DataListener {
	/**
	 * Default recieve method
	 * @param data SOAP data
	 */
	public void fireNewData(String data);
}
