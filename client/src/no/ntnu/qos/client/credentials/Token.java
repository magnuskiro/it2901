package no.ntnu.qos.client.credentials;

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
}
