package no.ntnu.qos.client.credentials.impl;

import no.ntnu.qos.client.DataObject;
import no.ntnu.qos.client.credentials.CredentialStorage;
import no.ntnu.qos.client.credentials.SAMLCommunicator;
import no.ntnu.qos.client.credentials.Token;
import no.ntnu.qos.client.credentials.TokenManager;

/**
 * @author Magnus Kirø - magnuskiro@ underdusken.no/gmail.com
 */
public class TokenManagerImpl implements TokenManager {
	private CredentialStorage credentialStorage;
	SAMLCommunicator samlCommunicator;

	public TokenManagerImpl(String user, String role, String password) {
		credentialStorage = new CredentialStorageImpl(user, role, password);
		samlCommunicator = new SAMLCommunicatorImpl();
	}

	@Override
	public void setTokenInDataObject(DataObject dataObject) {
		if(dataObject.getSamlToken()==null || !credentialStorage.hasToken(dataObject.getDestination())){
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

	public String[] getCredentials(){
		return this.credentialStorage.getCredentials();
	}

	@Override
	public Runnable getToken(DataObject dataObject) {
		return new RunningTokenFetcher(dataObject);
	}
	class RunningTokenFetcher implements Runnable {
		DataObject dataObj;
		public RunningTokenFetcher(DataObject data) {
			this.dataObj = data;
		}
		@Override
		public void run() {
			if(credentialStorage.hasToken(dataObj.getDestination())){
				dataObj.setToken(credentialStorage.getToken(dataObj.getDestination()));
			}else{
				String[] credentials = credentialStorage.getCredentials();
				Token newToken = samlCommunicator.getToken(dataObj.getDestination(),
						credentials[0], credentials[1], credentials[2]);
				credentialStorage.storeToken(newToken);
				dataObj.setToken(newToken); 
			}
		}
	}



}
