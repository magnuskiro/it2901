package no.ntnu.qos.client.credentials.impl;

import no.ntnu.qos.client.DataObject;
import no.ntnu.qos.client.credentials.CredentialStorage;
import no.ntnu.qos.client.credentials.TokenManager;

public class TokenManagerImpl implements TokenManager {
	CredentialStorage cS;
	
	public TokenManagerImpl() {
		cS = new CredentialStorageImpl();
	}
	
	public CredentialStorage getCredentialStorage() {
		return cS;
	}
	
	@Override
	public void getToken(DataObject dataObj) {
		// TODO Auto-generated method stub
		
	}

    @Override
    public void setCredentials(String username, String role, String password) {
        //To change body of implemented methods use File | Settings | File Templates.
        // getToken() in the SamlCommunicator usig all the parameters.
    }

}
