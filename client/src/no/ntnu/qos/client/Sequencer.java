package no.ntnu.qos.client;

import java.net.URI;

/**
 * Interface for the main sequencer of the client library
 * @author Stig Tore
 *
 */
public interface Sequencer {
	
	/**
	 * Set the credentials of the client
	 * @param username username of the client
	 * @param role role of the client
	 * @param password password of the client
	 */
	public void setCredentials(String username, String role, String password);
	
	/**
	 * Send data to a service
	 * @param data SOAP message to send
	 * @param destination The URI of the service to send it to
	 */
	public ReceiveObject sendData(String data, URI destination);
	
	/**
	 * Send a completed DataObject
	 * @param dataObj the DataObject to send
	 */
	public void sendData(DataObject dataObj);
	
	/**
	 * Return data from the service to the client.
	 * passes it on to the QoSClient
	 * @param recObj	- the ReceiveObject that has received a reply
	 */
	public void returnData(ReceiveObject recObj);


}
