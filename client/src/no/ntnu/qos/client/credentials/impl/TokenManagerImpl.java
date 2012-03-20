package no.ntnu.qos.client.credentials.impl;

import no.ntnu.qos.client.DataObject;
import no.ntnu.qos.client.credentials.CredentialStorage;
import no.ntnu.qos.client.credentials.Token;
import no.ntnu.qos.client.credentials.TokenManager;

public class TokenManagerImpl implements TokenManager {
	CredentialStorage credentialStorage;
    SAMLCommunicatorImpl samlCommunicator;

    public TokenManagerImpl(String user, String role, String password) {
		credentialStorage = new CredentialStorageImpl(user, role, password);
        samlCommunicator = new SAMLCommunicatorImpl();
	}

    public CredentialStorage getCredentialStorage() {
        return credentialStorage;
    }

    @Override
	public void setTokenInDataObject(DataObject dataObject) {
        if(dataObject.getSamlToken()==null && credentialStorage.getToken(dataObject.getDestination())==null){
            String[] credentials = credentialStorage.getCredentials();
            Token token = samlCommunicator.getToken(dataObject.getDestination(), credentials[0], credentials[1], credentials[2]);
            dataObject.setToken(token);
            credentialStorage.storeToken(token);
        }
	}

    @Override
    public void setCredentials(String username, String role, String password) {
        credentialStorage.setCredentials(username,role,password);
    }

}
