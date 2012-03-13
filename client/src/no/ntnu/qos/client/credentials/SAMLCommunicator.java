package no.ntnu.qos.client.credentials;

import java.net.URI;

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
	 * @param pasword		- client password
	 * @param role			- client role
	 * @return a valid Token object
	 */
	public  Token getToken(URI destination, String userName, String pasword, String role);
}
