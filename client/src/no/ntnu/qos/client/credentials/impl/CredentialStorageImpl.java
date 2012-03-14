package no.ntnu.qos.client.credentials.impl;

import java.net.URI;
import java.util.HashMap;
import java.util.NoSuchElementException;

import no.ntnu.qos.client.credentials.CredentialStorage;
import no.ntnu.qos.client.credentials.Token;
/**
 * 
 * @author Stig Tore
 *
 */
public class CredentialStorageImpl implements CredentialStorage {
	private String[] credentials;
	private HashMap<String, Token> tokens;
	
	public CredentialStorageImpl(String user, String role, String pass) {
		credentials = new String[3];
		credentials[USERNAME] = user;
		credentials[ROLE] = role;
		credentials[PASSWORD] = pass;
		tokens = new HashMap<String, Token>();
	}

	@Override
	public boolean hasToken(URI destination) {
		if(tokens.containsKey(destination.getHost()) ) {
			if (tokens.get(destination.getHost()).isValid()) {
				return true;
			}
			tokens.remove(destination.getHost());
		}
		return false;
	}

	/**
	 * Gets a token from the token storage. NOTE: Check to see if element exists with hasToken() before fetching it!
	 * @param destination The URI of the service to get a token for.
	 * @return a valid token for the service.
	 * @throws NoSuchElementException Thrown if token does not exist in storage.
	 */
	@Override
	public Token getToken(URI destination) throws NoSuchElementException {
		Token token = tokens.get(destination.getHost());
		if (token != null) {
			return token;
		}
		NoSuchElementException exception = new NoSuchElementException();
		throw exception;
	}

	@Override
	public String[] getCredentials() {
		return credentials;
	}

	@Override
	public void storeToken(Token token) {
		tokens.put(token.getDestination().getHost(), token);
	}

	@Override
	public void setCredentials(String username, String role, String password) {
		credentials[USERNAME] = username;
		credentials[ROLE] = role;
		credentials[PASSWORD] = password;
		flushTokens();
	}

	@Override
	public void flushTokens() {
		tokens.clear();
	}

}
