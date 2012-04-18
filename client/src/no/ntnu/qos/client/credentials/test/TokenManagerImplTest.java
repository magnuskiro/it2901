package no.ntnu.qos.client.credentials.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

import no.ntnu.qos.client.DataObject;
import no.ntnu.qos.client.ExceptionHandler;
import no.ntnu.qos.client.QoSClient;
import no.ntnu.qos.client.Sequencer;
import no.ntnu.qos.client.credentials.SAMLParser;
import no.ntnu.qos.client.credentials.Token;
import no.ntnu.qos.client.credentials.TokenManager;
import no.ntnu.qos.client.credentials.impl.SAMLParserImpl;
import no.ntnu.qos.client.credentials.impl.TokenManagerImpl;
import no.ntnu.qos.client.impl.QoSClientImpl;

import org.junit.Before;
import org.junit.Test;


/**
 * @author Magnus Kirø - magnuskiro@ underdusken.no/gmail.com - 21/03/12
 */
public class TokenManagerImplTest {
    static String user, role, password, soapFromUser, tokena, tokenb, pre, post, time;
    static URI destination, destination2;
    static TokenManager tokenManager;
    static Sequencer sequencer;
    static QoSClient client;
    static DataObject dataObject;
    static DataObject dataObject2;
    static ExceptionHandler exceptionHandler;
    static Token token;

    @Before
    public void setup() throws URISyntaxException {
        user = "user";
        role = "role";
        password = "password";
        soapFromUser = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><S:Envelope " +
        		"xmlns:S=\"http://schemas.xmlsoap.org/soap/envelope/\">" +
        		"<S:Header/><S:Body><ns2:hello xmlns:ns2=\"http://me.test.org\">" +
        		"<name>{REQID}</name></ns2:hello></S:Body></S:Envelope>";
        
        destination = new URI("http://127.0.0.25/services/myservice/service");
        destination2 = new URI("http://127.0.0.24/myservice/service");

        client = new QoSClientImpl(user, role, password, null);
        sequencer = client.getSequencer();
        tokenManager = new TokenManagerImpl(user, role, password);
    	
        dataObject = new DataObject(sequencer, soapFromUser, destination, exceptionHandler);
        dataObject2 = new DataObject(sequencer, soapFromUser, destination2, exceptionHandler);

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
						"<saml2:AttributeValue>" + role + "</saml2:AttributeValue>" +
					"</saml2:Attribute>" +
				"</saml2:AttributeStatement>" +
			"</saml2:Assertion>";

        pre = "<soap11:Envelope xmlns:soap11=\"http://schemas.xmlsoap.org/soap/envelope/\">" +
				"<soapenv:Header xmlns:soapenv=\"http://www.w3.org/2003/05/soap-envelope\">" +
					"<qosPriority>100</qosPriority><qosDiffserv>10</qosDiffserv>" +
				"</soapenv:Header>" +
			"<soap11:Body>";
        post = "</soap11:Body></soap11:Envelope>";
        
        DateFormat	dateFormatter	= new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
        Calendar	calendar		= Calendar.getInstance();
        TimeZone	timeZone		= TimeZone.getDefault();
        long		TimeZoneOffset	= timeZone.getRawOffset() + timeZone.getDSTSavings();
        
        SAMLParser samlParser		= new SAMLParserImpl();
        
        calendar.setTimeInMillis(System.currentTimeMillis()+3600000 - TimeZoneOffset);
        
        time = dateFormatter.format(calendar.getTime()) + "Z";

        try {
			token = samlParser.tokenize(pre + tokena + time + tokenb + post, destination);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
                        
    }

    @Test
    public void getTokenTest(){
        tokenManager.setTokenInDataObject(dataObject);
        tokenManager.setTokenInDataObject(dataObject2);
        assertNotNull(tokenManager.getToken(dataObject));
        assertNotNull(tokenManager.getToken(dataObject2));
        assertNotSame(tokenManager.getToken(dataObject), tokenManager.getToken(dataObject2));

    }

    @Test
    public void constructorCredentialsTest(){
        TokenManager tokenManager1 = new TokenManagerImpl(user, role, password);
        String[] credentials = tokenManager1.getCredentials();
        assertEquals("Username is correct?", "user", credentials[0]); // [0] is the username
        assertEquals("Role is correct?", "role", credentials[1]); // [1] is the role
        assertEquals("Password is correct?", "password", credentials[2]); // [2] is the password
    }

    @Test
    public void setCredentialsTest(){
        tokenManager.setCredentials("newUser", "newRole", "newPassword");
        String[] credentials = tokenManager.getCredentials();
        assertEquals("Changed the username", "newUser", credentials[0]); // [0] is the username
        assertEquals("Changed the role", "newRole", credentials[1]); // [1] is the role
        assertEquals("changed the password", "newPassword", credentials[2]); // [2] is the password
    }



}
