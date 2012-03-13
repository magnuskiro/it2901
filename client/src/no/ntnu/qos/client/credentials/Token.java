package no.ntnu.qos.client.credentials;

import java.net.URI;

/**
 * The interface for a token object, should contain all the information needed to validate and output a valid token
 * @author Stig Tore
 *
 */
public interface Token {
	/**
	 * Method to get the XML representation of the token
	 * @return XML data
	 */
	public String getXML();
	/**
	 * Should check to see if a token is valid
	 * @return true if valid, false if not.
	 */
	public boolean isValid();
	/**
	 * Returns the server this token is valid for
	 * @return URI of the server/service
	 */
	public URI getDestination();
}
