package no.ntnu.qos.client.credentials;

import java.net.URI;

/**
 * The interface for the main storage for credentials and token objects
 * @author Stig Tore
 *
 */
public interface CredentialStorage {
	
	/**
	 * Takes a URI and checks if it has a valid token for it 
	 * @param destination the URI of the service
	 * @return Does a valid token exist
	 */
	public boolean hasToken(URI destination);
	
	/**
	 * Gets a token from storage
	 * @param destination the URI of the service
	 * @return a valid Token object
	 */
	public Token getToken(URI destination);
	
	/**
	 * Gets the client credentials stored
	 * @return string array of 0:username 1:role 2: password
	 */
	public String[] getCredentials();
	
	/**
	 * Stores a token in storage
	 * @param token the token to store
	 */
	public void storeToken(Token token);
	
	/**
	 * Changes the client credentials stored
	 * @param username client username
	 * @param role client role
	 * @param password client password
	 */
	public void setCredentials(String username, String role, String password);
	
	/**
	 * Flushes the token store
	 */
	public void flushTokens();
}
