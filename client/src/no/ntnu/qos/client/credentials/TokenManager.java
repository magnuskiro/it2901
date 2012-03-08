package no.ntnu.qos.client.credentials;

import no.ntnu.qos.client.DataObject;


/**
 * The main interface to get tokens from the token storage system.
 * @author Stig Tore
 *
 */
public interface TokenManager {
	/**
	 * Should take a data object and give it the token it needs
	 * @param dataObj the data object that needs a token
	 */
	public void getToken(DataObject dataObj);
}
