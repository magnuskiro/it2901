package no.ntnu.qos.client.credentials.impl;

import no.ntnu.qos.client.DataObject;
import no.ntnu.qos.client.credentials.CredentialStorage;
import no.ntnu.qos.client.credentials.TokenManager;

/**
 * 
 * @author Stig Tore
 *
 */
public class TokenManagerImpl implements TokenManager{
	CredentialStorage cS;
	
	public TokenManagerImpl(String user, String role, String pass) {
		cS = new CredentialStorageImpl(user, role, pass);
	}
	
	public CredentialStorage getCredentialStorage() {
		return cS;
	}
	
	@Override
	public void getToken(DataObject dataObj) {
		// TODO Auto-generated method stub
		
	}

}
