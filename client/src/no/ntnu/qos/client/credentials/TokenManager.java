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

    /**
     * Set the credentials of the client
     * @param username username of the client
     * @param role role of the client
     * @param password password of the client
     */
    public void setCredentials(String username, String role, String password);

}
