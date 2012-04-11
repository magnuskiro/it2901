package no.ntnu.qos.client.credentials.test;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;

import no.ntnu.qos.client.credentials.SAMLParser;
import no.ntnu.qos.client.credentials.Token;
import no.ntnu.qos.client.credentials.impl.SAMLParserImpl;

public class ManualSamlParserImplTest {
	public static void main(String[] args) {
		URI destination = null;
		try {
			destination = new URI("http://127.0.0.1/hei");
		} catch (URISyntaxException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String pre = "<soap11:Envelope xmlns:soap11=\"http://schemas.xmlsoap.org/soap/envelope/\">" +
						"<soapenv:Header xmlns:soapenv=\"http://www.w3.org/2003/05/soap-envelope\">" +
							"<qosPriority>123</qosPriority><qosDiffserv>16</qosDiffserv>" +
						"</soapenv:Header>" +
					"<soap11:Body>";
		String post = "</soap11:Body></soap11:Envelope>";
		String xml = "<saml2:Assertion IssueInstant=\"2012-03-12T13:50:20.021Z\" " +
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
							"SessionIndex=\"abcdef123456\" SessionNotOnOrAfter=\"2012-04-12T13:50:20.316Z\">" +
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
					"</saml2:Assertion>";
		SAMLParser parser = new SAMLParserImpl();
		try {
			Token testToken = parser.tokenize(pre+xml+post, destination);
			System.out.println("Success!");
			System.out.println("diffserv: "+testToken.getDiffServ());
			System.out.println("prio: "+testToken.getPriority());
			System.out.println("Valid? "+testToken.isValid());
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
