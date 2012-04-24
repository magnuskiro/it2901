package no.ntnu.qos.client.credentials;

import java.net.URI;

import no.ntnu.qos.client.DataObject;

/**
 * Interface for handling the communication with an identity server
 * @author Håvard
 *
 */
public interface SAMLCommunicator {
	
	/**
	 * fetches a SAML token from the given identity server
	 * @param destination	- destination of the server
	 * @param userName		- client username
	 * @param role			- client role
	 * @param password		- client password
	 * @param dataObj		- the data object
	 * @return a valid Token object
	 * @throws Exception if a token could not be received from the IS.
	 */
	public  Token getToken(URI destination, String userName, String role, String password, DataObject dataObj) throws Exception;
}
