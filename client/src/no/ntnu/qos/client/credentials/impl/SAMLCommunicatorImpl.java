package no.ntnu.qos.client.credentials.impl;

import no.ntnu.qos.client.credentials.SAMLCommunicator;
import no.ntnu.qos.client.credentials.SAMLParser;
import no.ntnu.qos.client.credentials.Token;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;


public class SAMLCommunicatorImpl implements SAMLCommunicator {

	private SAMLParser samlParser;
	
	public SAMLCommunicatorImpl(){
		samlParser = new SAMLParserImpl();
	}
	
	
	@Override
	public Token getToken(URI destination, String userName, String role,
			String password) {
        // TODO write the rest of the method. which probably includes the network communication with the identity server.

        
		
		//placeholderstuff until proper communication is in place
		String tokena, tokenb, pre, post, time;
		
		tokena = "<saml2:Assertion xmlns:saml2=\"urn:oasis:names:tc:SAML:2.0:assertion\" " +
				"IssueInstant=\"2012-03-12T13:50:20.021Z\" Version=\"2.0\">" +
				"<saml2:Issuer>http://allbowtotheawesomenessofjan.com</saml2:Issuer>" +
				"<saml2:Subject>" +
					"<saml2:NameID " +
						"Format=\"urn:oasis:names:tc:SAML:1.1:nameid-format:unspecified\" NameQualifier=\"My website\">Alles Menschen</saml2:NameID>" +
					"<saml2:SubjectConfirmation>" +
						"<saml2:SubjectConfirmationData " +
							"NotBefore=\"2012-03-12T13:50:20.021Z\" NotOnOrAfter=\"2012-03-12T13:52:20.021Z\">" +
					"</saml2:SubjectConfirmationData></saml2:SubjectConfirmation>" +
				"</saml2:Subject>" +
				"<saml2:Conditions>" +
					"<saml2:OneTimeUse>" +
					"</saml2:OneTimeUse>" +
				"</saml2:Conditions>" +
				"<saml2:AuthnStatement " +
					"AuthnInstant=\"2012-03-12T13:50:20.301Z\" " +
					"SessionIndex=\"abcdef123456\" SessionNotOnOrAfter=\"";
        
        tokenb = "\">" +
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
        
        pre = "<soap11:Envelope xmlns:soap11=\"http://schemas.xmlsoap.org/soap/envelope/\">" +
				"<soapenv:Header xmlns:soapenv=\"http://www.w3.org/2003/05/soap-envelope\">" +
					"<qosPriority>123</qosPriority><qosDiffserv>16</qosDiffserv>" +
				"</soapenv:Header>" +
			"<soap11:Body>";
        
        post = "</soap11:Body></soap11:Envelope>";

        DateFormat	dateFormatter	= new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
        Calendar	calendar		= Calendar.getInstance();
        TimeZone	timeZone		= TimeZone.getDefault();
        long		TimeZoneOffset	= timeZone.getRawOffset() + timeZone.getDSTSavings();
        
        long 		validUntil	= System.currentTimeMillis()+3600000;
        calendar.setTimeInMillis(validUntil - TimeZoneOffset);
        time = dateFormatter.format(calendar.getTime()) + "Z";
		
		//end placeholderstuff
		
		Token token;
		try {
			token = samlParser.tokenize(pre + tokena + time + tokenb + post, destination);
			return token;
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//not quite sure about this and the try-catch-block
		return new TokenImpl("", validUntil, destination);
	}

}
