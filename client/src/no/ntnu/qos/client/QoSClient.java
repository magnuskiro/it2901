package no.ntnu.qos.client;

import java.net.URI;

/**
 * 
 * @author Stig Tore
 * 
 * Main interface towards the client using the QoSClient library.
 */
public interface QoSClient {
	/**
	 * Set the credentials the client wishes to use towards services
	 * @param username client username
	 * @param role client role
	 * @param password client password
	 */
	public void setCredentials(String username, String role, String password);
	
	/**
	 * Send data to a service
	 * @param data the XML data to send (Valid SOAP only)
	 * @param destination the URI to the service
	 * @return Returns a RecieveObject where the specific reply will be made available.
	 */
	public RecieveObject sendData(String data, URI destination);
	
	/**
	 * Add a listener for any reply data the client library recieves
	 * @param listener a class implementing the DataListener interface
	 */
	public void addListener(DataListener listener);
	/**
	 * Remove a listener from the list of listeners
	 * @param listener a class implementing the DataListener interface
	 */
	public void removeListener(DataListener listener);
}