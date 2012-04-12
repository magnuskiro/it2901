package no.ntnu.qos.client.credentials.impl;

import no.ntnu.qos.client.credentials.SAMLCommunicator;
import no.ntnu.qos.client.credentials.Token;

import java.net.URI;

public class SAMLCommunicatorImpl implements SAMLCommunicator {

	@Override
	public Token getToken(URI destination, String userName, String role,
			String password) {
        // TODO write the rest of the method. which probably includes the network communication with the identity server.

        // TODO -  this method has to set the diffserv and priority in the token before it is returned.
        // you get the diffserv and priority values from the default value set in the config.

        // TODO: has to be changed to the correct variables
        // takes "token" - valid Long, destination URI
		
        Token token = new TokenImpl(
        		"<saml2:Assertion IssueInstant=\"2012-03-12T13:50:20.021Z\" " +
				"Version=\"2.0\" xmlns:saml2=\"urn:oasis:names:tc:SAML:2.0:assertion\">" +
				"<saml2:Issuer>http://allbowtotheawesomenessofjan.com</saml2:Issuer>" +
				"<saml2:Subject>" +
				"<saml2:NameID " +
				"Format=\"urn:oasis:names:tc:SAML:1.1:nameid-format:unspecified\" NameQualifier=\"My website\">Alles Menschen</saml2:NameID>" +
				"<saml2:SubjectConfirmation>" +
				"<saml2:SubjectConfirmationData " +
				"NotBefore=\"2012-03-12T13:50:20.021Z\" NotOnOrAfter=\"2012-03-12T13:52:20.021Z\"/>" +
				"</saml2:SubjectConfirmation>" +
				"</saml2:Subject>" +
				"<saml2:Conditions>" +
				"<saml2:OneTimeUse/>" +
				"</saml2:Conditions>" +
				"<saml2:AuthnStatement " +
				"AuthnInstant=\"2012-03-12T13:50:20.301Z\" " +
				"SessionIndex=\"abcdef123456\" SessionNotOnOrAfter=\"2012-03-12T13:50:20.316Z\">" +
				"<saml2:AuthnContext>" +
				"<saml2:AuthnContextClassRef>urn:oasis:names:tc:SAML:2.0:ac:classes:Password</saml2:AuthnContextClassRef>" +
				"</saml2:AuthnContext>" +
				"</saml2:AuthnStatement>" +
				"<saml2:AttributeStatement>" +
					"<saml2:Attribute " +
						"xmlns:x500=\"urn:oasis:names:tc:SAML:2.0:profiles:attribute:X500\" " +
						"x500:Encoding=\"LDAP\" " +
						"NameFormat=\"urn:oasis:names:tc:SAML:2.0:attrname-format:uri\" " +
						"Name=\"urn:oid:1.3.6.1.4.1.5923.1.1.1.1\" " +
						"FriendlyName=\"qosClientRole\">" +
						"<saml2:AttributeValue>"+role+"</saml2:AttributeValue>" +
					"</saml2:Attribute>" +
				"</saml2:AttributeStatement>" +
				"</saml2:Assertion>", System.currentTimeMillis()+3600000, destination);
        token.setDiffServ(10);
        token.setPriority(100);
        return token;
	}

}
