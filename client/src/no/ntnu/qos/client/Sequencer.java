package no.ntnu.qos.client;

import java.net.URI;

import no.ntnu.qos.client.credentials.TokenManager;

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
	 * Return data from the service
	 * @param data The SOAP message the service returns
	 */
	public void returnData(String data);

	public void setTokenManager(TokenManager tM);
}
