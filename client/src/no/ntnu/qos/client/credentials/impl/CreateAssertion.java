package no.ntnu.qos.client.credentials.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;


/**
 * helper class  that builds SAML assertions
 * @author Håvard
 *
 */
public class CreateAssertion{
	/**
	 * 
	 * @param destination
	 * @param role
	 * @return 
	 */
	public String createSAML(String destination, String role){
		String token, time;
		
		DateFormat	dateFormatter	= new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
        Calendar	calendar		= Calendar.getInstance();
        TimeZone	timeZone		= TimeZone.getDefault();
        long		TimeZoneOffset	= timeZone.getRawOffset() + timeZone.getDSTSavings();
        long		validUntil		= System.currentTimeMillis() + 3600000 -TimeZoneOffset;
        
        calendar.setTimeInMillis(validUntil);
        time = dateFormatter.format(calendar.getTime()) + "Z";
        
        
        token = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
        		"<S:Envelope xmlns:S=\"http://schemas.xmlsoap.org/soap/envelope/\">" +
        			"<S:Header/>" +
        			"<S:Body>" +
        				"<saml2:Assertion xmlns:saml2=\"urn:oasis:names:tc:SAML:2.0:assertion\" " +
        				"ID=\"abcd1234\" " +
        				"IssueInstant=\"2012-04-18T10:07:27.304Z\" " +
        				"Version=\"2.0\"> " +
        					"<saml2:Issuer>" +
        						"http://example.org" +
        					"</saml2:Issuer> " +
        					"<saml2:Subject> " +
        						"<saml2:NameID Format=\"urn:oasis:names:tc:SAML:1.1:nameid-format:unspecified\" " +
        						"NameQualifier=\"Example Qualifier\">" +
        							"General Curly" +
        						"</saml2:NameID> " +
        						"<saml2:SubjectConfirmation> " +
        							"<saml2:SubjectConfirmationData NotBefore=\"2012-04-18T10:07:27.304Z\" " +
        							"NotOnOrAfter=\"2012-04-18T10:09:27.304Z\" " +
        							"Recipient=\"" + destination +"\" /> " +
        						"</saml2:SubjectConfirmation> " +
        					"</saml2:Subject> " +
        					"<saml2:Conditions> " +
        						"<saml2:OneTimeUse /> " +
        					"</saml2:Conditions> " +
        					"<saml2:AuthnStatement AuthnInstant=\"2012-04-18T10:07:27.422Z\" " +
        					"SessionIndex=\"abcd1234\" " +
        					"SessionNotOnOrAfter=\"" + time + "\"> " +
        						"<saml2:AuthnContext>" +
        							"<saml2:AuthnContextClassRef>" +
        								"urn:oasis:names:tc:SAML:2.0:ac:classes:Password" +
        							"</saml2:AuthnContextClassRef> " +
        						"</saml2:AuthnContext> " +
        					"</saml2:AuthnStatement> " +
        					"<saml2:AttributeStatement> " +
        						"<saml2:Attribute FriendlyName=\"qosClientRole\" Name=\"urn:oid:1.3.6.1.4.1.5923.1.1.1.1\"> " +
        							"<saml2:AttributeValue xmlns:xs=\"http://www.w3.org/2001/XMLSchema\" " +
        							"xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" " +
        							"xsi:type=\"xs:string\">" + role + "</saml2:AttributeValue> " +
        						"</saml2:Attribute> " +
        					"</saml2:AttributeStatement> " +
        				"</saml2:Assertion>" +
        			"</S:Body>" +
        		"</S:Envelope>";
        
        
        
		return token;		
	}
}