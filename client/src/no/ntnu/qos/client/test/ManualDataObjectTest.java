package no.ntnu.qos.client.test;

import java.net.URI;
import java.net.URISyntaxException;

import no.ntnu.qos.client.DataObject;
import no.ntnu.qos.client.credentials.Token;
import no.ntnu.qos.client.credentials.impl.TokenImpl;

public class ManualDataObjectTest {
	public static void main(String[] args) throws URISyntaxException {
		String xml = "<?xml version=\"1.0\" ?><S:Envelope xmlns:S=\"http://schemas.xmlsoap.org/soap/envelope/\"><S:Body><ns2:hello xmlns:ns2=\"http://me.test.org/\"><name>My text goes here</name></ns2:hello></S:Body></S:Envelope>";
		URI destination = new URI("http://122.22.33.44/destination");
		Token samlToken = new TokenImpl(
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
						"<saml2:AttributeValue>clientRole1</saml2:AttributeValue>" +
					"</saml2:Attribute>" +
				"</saml2:AttributeStatement>" +
				"</saml2:Assertion>", System.currentTimeMillis()+60000, destination);
		DataObject data = new DataObject(null, xml, destination, null);
		data.setToken(samlToken);
		System.out.println(data.getSoap());
	}
}
