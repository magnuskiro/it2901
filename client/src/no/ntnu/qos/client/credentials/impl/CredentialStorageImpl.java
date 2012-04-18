package no.ntnu.qos.client.credentials.impl;

import no.ntnu.qos.client.credentials.CredentialStorage;
import no.ntnu.qos.client.credentials.Token;
import no.ntnu.qos.client.impl.ConfigManager;

import java.net.URI;
import java.util.HashMap;
import java.util.NoSuchElementException;
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
		ConfigManager.LOGGER.info("Checked token:"+destination);
		if(tokens.containsKey(getKeystring(destination)) ) {
			ConfigManager.LOGGER.info("Checked token exists:"+destination);
			if (tokens.get(getKeystring(destination)).isValid()) {
				ConfigManager.LOGGER.info("Checked token is valid:"+destination);
				return true;
			}
			ConfigManager.LOGGER.info("Checked token not valid:"+destination);
			tokens.remove(getKeystring(destination));
		}
		ConfigManager.LOGGER.info("Checked token not exists:"+destination);
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
        if(hasToken(destination)){
            Token token = tokens.get(getKeystring(destination));
            if (token != null) {
                return token;
            }
        }
        throw new NoSuchElementException();
	}

	@Override
	public String[] getCredentials() {
		return credentials;
	}

	@Override
	public void storeToken(Token token) {
		ConfigManager.LOGGER.info("Stored token:"+token.getDestination());
		tokens.put(getKeystring(token.getDestination()), token);
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

	/**
	 * Will return a key for the given URI, the key will be in the form of host/path except the last link in the path
	 * EXAMPLE: "http://someplace.com/services/myservice/page" will return "someplace.com/services/myservice"
	 * @param destination an URI for the given destination
	 * @return the key to use for storing it
	 */
	private String getKeystring(URI destination) {
		String init = destination.getPath();
		if (!init.equals("")) {
			String[] split = init.split("/");
			if(split.length>1) {
				StringBuilder sb = new StringBuilder();
				int range = split.length-1;
				for(int i=0; i<range; i++) {
					sb.append("/").append(split[i]);
				}
                return destination.getPath()+sb;
			}
		}
		return destination.getHost();
	}

}