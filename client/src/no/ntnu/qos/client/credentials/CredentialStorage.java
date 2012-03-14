package no.ntnu.qos.client.credentials;

import java.net.URI;
import java.util.NoSuchElementException;

/**
 * The interface for the main storage for credentials and token objects
 * @author Stig Tore
 *
 */
public interface CredentialStorage {
	public final int USERNAME = 0;
	public final int ROLE = 1;
	public final int PASSWORD = 2;
	
	/**
	 * Takes a URI and checks if it has a valid token for it 
	 * @param destination the URI of the service
	 * @return Does a valid token exist
	 */
	public boolean hasToken(URI destination);
	
	/**
	 * Gets a valid token from storage
	 * @param destination the URI of the service to get a token for
	 * @return a valid token
	 * @throws NoSuchElementException If token does not exist in storage.
	 */
	public Token getToken(URI destination) throws NoSuchElementException;
	
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
