package no.ntnu.qos.client.credentials.impl;

import no.ntnu.qos.client.DataObject;
import no.ntnu.qos.client.credentials.CredentialStorage;
import no.ntnu.qos.client.credentials.TokenManager;

public class TokenManagerImpl implements TokenManager {
	CredentialStorage credentialStorage;
	
	public TokenManagerImpl() {
		credentialStorage = new CredentialStorageImpl();
	}

    public CredentialStorage getCredentialStorage() {
        return credentialStorage;
    }

    @Override
	public void getToken(DataObject dataObject) {

        // the variables have to be changed and used to create a token.
        // dataObject.setToken(new TokenImpl(tokenString, LongValidUntil, URI-destination));
	}

    @Override
    public void setCredentials(String username, String role, String password) {
        credentialStorage.setCredentials(username,role,password);
    }

}
