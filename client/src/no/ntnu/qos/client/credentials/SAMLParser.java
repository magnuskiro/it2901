package no.ntnu.qos.client.credentials;

import java.io.UnsupportedEncodingException;
import java.net.URI;

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
	 * @throws UnsupportedEncodingException 
	 */
	public Token tokenize(String token, URI destination) throws UnsupportedEncodingException;
}
