package no.ntnu.qos.client.credentials.impl;

import no.ntnu.qos.client.DataObject;
import no.ntnu.qos.client.credentials.CredentialStorage;
import no.ntnu.qos.client.credentials.SAMLCommunicator;
import no.ntnu.qos.client.credentials.Token;
import no.ntnu.qos.client.credentials.TokenManager;
import no.ntnu.qos.client.impl.ConfigManager;

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
	public void setCredentials(String username, String role, String password) {
		credentialStorage.setCredentials(username,role,password);
	}

	public String[] getCredentials(){
		return this.credentialStorage.getCredentials();
	}

	@Override
	public Runnable getToken(DataObject dataObject) {
		ConfigManager.LOGGER.info("Received request for Token, creating runnable");
		return new RunningTokenFetcher(dataObject);
	}
	class RunningTokenFetcher implements Runnable {
		DataObject dataObj;
		public RunningTokenFetcher(DataObject data) {
			this.dataObj = data;
		}
		@Override
		public void run() {
			ConfigManager.LOGGER.info("Getting token");
			if(credentialStorage.hasToken(dataObj.getDestination())){
				ConfigManager.LOGGER.info("Token found in credential storage, putting it into data object");
				dataObj.setToken(credentialStorage.getToken(dataObj.getDestination()));
			}else{
				ConfigManager.LOGGER.info("Token not found in credential storage, fetching from identity server");
				String[] credentials = credentialStorage.getCredentials();
				Token newToken;
				try {
					newToken = samlCommunicator.getToken(dataObj.getDestination(),
							credentials[0], credentials[1], credentials[2], dataObj);
				} catch (Exception e) {
					ConfigManager.LOGGER.warning("Could not get token from Identity Server");
					return;
				}
				credentialStorage.storeToken(newToken);
				ConfigManager.LOGGER.info("Setting token in dataobject");
				dataObj.setToken(newToken); 
			}
		}
	}



}
