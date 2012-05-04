package no.ntnu.qos.client.credentials.test;

import no.ntnu.qos.client.credentials.Token;
import no.ntnu.qos.client.credentials.impl.SAMLParserImpl;

import org.junit.Before;
import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Magnus Kirø - magnuskiro@ underdusken.no/gmail.com - 21/03/12
 */
public class TokenImplTest {
    static String token1a, token1b, token2a, token2b, role, time1, time2, time3, time4, time5, pre, post;
    static URI destination1, destination2;
    static long validUntil1, validUntil2, validUntil3, validUntil4, validUntil5, TimeZoneOffset;
    static Token testToken1, testToken2, testToken3, testToken4, testToken5;
    static SAMLParserImpl SAMLParser;
    static DateFormat dateFormatter;
    static Calendar calendar;
    static TimeZone timeZone;
    

    @Before
    public void setup() throws URISyntaxException {
        role = "role";

        token1a = "<saml2:Assertion xmlns:saml2=\"urn:oasis:names:tc:SAML:2.0:assertion\" " +
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
        
        token1b = "\">" +
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
        
        token2a = "<saml2:Assertion xmlns:saml2=\"urn:oasis:names:tc:SAML:2.0:assertion\" " +
				"Version=\"2.0\" IssueInstant=\"2012-03-12T13:50:20.021Z\">" +
				"<saml2:Issuer>http://allbowtotheawesomenessofjan.com</saml2:Issuer>" +
				"<saml2:Subject>" +
					"<saml2:NameID " +
						"Format=\"urn:oasis:names:tc:SAML:1.1:nameid-format:unspecified\" NameQualifier=\"My website\">Alles Menschen</saml2:NameID>" +
					"<saml2:SubjectConfirmation>" +
						"<saml2:SubjectConfirmationData>" +
							"NotBefore=\"2012-03-12T13:50:20.021Z\" NotOnOrAfter=\"2012-03-12T13:52:20.021Z\">" +
						"</saml2:SubjectConfirmationData>" +
					"</saml2:SubjectConfirmation>" +
				"</saml2:Subject>" +
				"<saml2:Conditions>" +
					"<saml2:OneTimeUse>" +
					"</saml2:OneTimeUse>" +
				"</saml2:Conditions>" +
				"<saml2:AuthnStatement " +
					"AuthnInstant=\"2012-03-12T13:50:20.301Z\" " +
					"SessionIndex=\"abcdef123456\" SessionNotOnOrAfter=\"";
        
        token2b ="\">" +
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
        
        
        dateFormatter	= new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
        calendar		= Calendar.getInstance();
        timeZone		= TimeZone.getDefault();
        TimeZoneOffset	= timeZone.getRawOffset() + timeZone.getDSTSavings();
        
        SAMLParser		= new SAMLParserImpl();
        
        destination1	= new URI("http://127.0.0.25/");
        destination2	= new URI("http://127.0.0.26/");
        validUntil1	= System.currentTimeMillis()+3600000;
        validUntil2 = System.currentTimeMillis()-35000;
        validUntil3 = System.currentTimeMillis()+30000;
        validUntil4 = System.currentTimeMillis()+29000;
        validUntil5 = System.currentTimeMillis()+31000;

        
        calendar.setTimeInMillis(validUntil1 - TimeZoneOffset);
        time1 = dateFormatter.format(calendar.getTime()) + "Z";
        calendar.setTimeInMillis(validUntil2 - TimeZoneOffset);
        time2 = dateFormatter.format(calendar.getTime()) + "Z";
        calendar.setTimeInMillis(validUntil3 - TimeZoneOffset);
        time3 = dateFormatter.format(calendar.getTime()) + "Z";
        calendar.setTimeInMillis(validUntil4 - TimeZoneOffset);
        time4 = dateFormatter.format(calendar.getTime()) + "Z";
        calendar.setTimeInMillis(validUntil5 - TimeZoneOffset);
        time5 = dateFormatter.format(calendar.getTime()) + "Z";
        
        try {
			testToken1 = SAMLParser.tokenize(pre + token1a + time1 + token1b + post, destination1);
			testToken2 = SAMLParser.tokenize(pre + token2a + time2 + token2b + post, destination2);
			testToken3 = SAMLParser.tokenize(pre + token1a + time3 + token1b + post, destination1);
			testToken4 = SAMLParser.tokenize(pre + token1a + time4 + token1b + post, destination1);
			testToken5 = SAMLParser.tokenize(pre + token1a + time5 + token1b + post, destination1);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
    }

    @Test
    public void isValidTokenTest() {
        assertTrue(testToken1.isValid());
        assertFalse(testToken2.isValid());
        assertFalse(testToken3.isValid());
        assertFalse(testToken4.isValid());
        assertTrue(testToken5.isValid());
    }

    @Test
    public void setExpirationTest() throws UnsupportedEncodingException{
        // tests if a token becomes invalid after the change of the expirationTimeBuffer.
    	calendar.setTimeInMillis(System.currentTimeMillis()+31000 - TimeZoneOffset);
        time1 = dateFormatter.format(calendar.getTime()) + "Z";
        Token testExpirationToken = SAMLParser.tokenize(pre + token1a + time1 + token1b + post, destination1);
        assertTrue(testExpirationToken.isValid());
        testExpirationToken.setExpirationTimeBuffer(32000L);
        assertEquals(testExpirationToken.getExpirationTimeBuffer(), 32000L);
        assertFalse(testExpirationToken.isValid());

        // tests if a token becomes valid after changing the expirationTimeBuffer.
        calendar.setTimeInMillis(System.currentTimeMillis()+29000 - TimeZoneOffset);
        time1 = dateFormatter.format(calendar.getTime()) + "Z";
        Token testExpirationToken2 = SAMLParser.tokenize(pre + token1a + time1 + token1b + post, destination1);
        assertFalse(testExpirationToken2.isValid());
        testExpirationToken2.setExpirationTimeBuffer(28000L);
        assertEquals(testExpirationToken2.getExpirationTimeBuffer(), 28000L);
        assertTrue(testExpirationToken2.isValid());
    }

    @Test
    public void getDestinationTest(){
        assertEquals("Destinations Match", destination1, testToken1.getDestination());
        assertEquals("Destinations Match", destination2, testToken2.getDestination());
    }

    @Test
    public void getXMLTest(){

        // what is the returned xml supposed to look like?

        assertEquals("Token1 comparison", token1a + time1 + token1b, testToken1.getXML());
        assertEquals("Token2 comparison", token2a + time2 + token2b, testToken2.getXML());
    }

    @Test
    public void getDiffServTest(){
    	assertEquals(16, testToken1.getDiffServ());
    	
    }

    @Test
    public void getPriorityTest(){
        assertEquals(123, testToken1.getPriority());
    }

}
