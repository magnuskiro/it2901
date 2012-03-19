package no.ntnu.qos.client.credentials.impl;

import no.ntnu.qos.client.credentials.CredentialStorage;
import no.ntnu.qos.client.credentials.Token;

import java.net.URI;
import java.util.HashMap;

public class CredentialStorageImpl implements CredentialStorage {
	
	String[] credentials;
	HashMap<URI, Token> tokens;
	
	public CredentialStorageImpl() {
		credentials = new String[3];
		tokens = new HashMap<URI, Token>();
	}

	@Override
	public boolean hasToken(URI destination) {
		if(tokens.containsKey(destination)){
            return true;
        }
		return false;
	}

	@Override
	public Token getToken(URI destination) {
		return tokens.get(destination);
	}

	@Override
	public String[] getCredentials() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void storeToken(Token token) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setCredentials(String username, String role, String password) {
		// TODO Auto-generated method stub

	}

	@Override
	public void flushTokens() {
		// TODO Auto-generated method stub

	}

}
