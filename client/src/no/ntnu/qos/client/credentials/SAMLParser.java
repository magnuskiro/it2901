package no.ntnu.qos.client.credentials;

/**
 * The interface for the SAML parser
 * @author stigtore
 *
 */
public interface SAMLParser {
	/**
	 * Takes an XML SOAP token and converts it into a token object
	 * @param token The SOAP message
	 * @return a token object
	 */
	public Token tokenize(String token);
}
