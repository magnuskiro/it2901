package no.ntnu.qos.client.credentials.test;

import no.ntnu.qos.client.credentials.CredentialStorage;
import no.ntnu.qos.client.credentials.Token;
import no.ntnu.qos.client.credentials.impl.CredentialStorageImpl;
import no.ntnu.qos.client.credentials.impl.TokenImpl;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.NoSuchElementException;

import static org.junit.Assert.*;

/**
 * 
 * @author Stig Tore
 *	Testing the CredentialStorageImpl class.
 */
public class CredentialStorageImplTest {
	static CredentialStorage credentialStorage;
	static String token;
	static URI destination, destination2, destination3;
	static long validUntil1, validUntil2, validUntil3;
	static Token testToken1, testToken2, testToken3;
	static String user, role, pass;
	static String user2, role2, pass2;

	@Before
	public void setup() throws Exception {
		user = "testUser";
		role = "testRole";
		pass = "testPas";
		user2 = "testUser2";
		role2 = "testRole2";
		pass2 = "testPas2";

        token = "<saml2:Assertion IssueInstant=\"2012-03-12T13:50:20.021Z\" " +
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
                "</saml2:Assertion>"
        ;

		destination = new URI("http://127.0.0.25/services/myservice/service");
		destination2 = new URI("http://127.0.0.26/myservice/service");
        destination3 = new URI("http://127.0.0.26/myservice/service/myService");
		validUntil1 = System.currentTimeMillis()+3600000;
		validUntil2 = System.currentTimeMillis()-35000;
        validUntil3 = System.currentTimeMillis()+35000;
		testToken1 = new TokenImpl(token, validUntil1, destination);
		testToken2 = new TokenImpl(token, validUntil2, destination2);
        testToken3 = new TokenImpl(token, validUntil3, destination3);

		credentialStorage = new CredentialStorageImpl(user, role, pass);
		credentialStorage.storeToken(testToken1);
		credentialStorage.storeToken(testToken2);
        credentialStorage.storeToken(testToken3);
	}

    @Test
    public void getToken(){
    	assertNotNull(credentialStorage.getToken(destination));
        assertNotNull(credentialStorage.getToken(destination3));
    }

	@Test
	public void storageHasTokenTest() throws URISyntaxException {
		assertTrue(credentialStorage.hasToken(destination));
		assertFalse(credentialStorage.hasToken(destination2)); //Should be invalid because it's expired!
		assertFalse(credentialStorage.hasToken(new URI("http://127.0.0.1/")));
        assertTrue(credentialStorage.hasToken(destination3));
	}

	@Test
	public void storageGivesCorrectTokensTest() {
		assertEquals(credentialStorage.getToken(destination), testToken1);
		assertEquals(credentialStorage.getToken(destination3), testToken3);
	}

	@Test (expected=NoSuchElementException.class)
	public void testCorrectError() {
        credentialStorage.getToken(destination2);
	}

	@Test
	public void storedCredentialsTest() {
		String[] credentials = credentialStorage.getCredentials();
		assertTrue(credentials[CredentialStorage.USERNAME].equals(user));
		assertTrue(credentials[CredentialStorage.ROLE].equals(role));
		assertTrue(credentials[CredentialStorage.PASSWORD].equals(pass));
	}

	@Test
	public void setClientCredentialsInStorageTest() {
        credentialStorage.setCredentials(user2, role2, pass2);

        String[] credentials = credentialStorage.getCredentials();
//        System.out.print(credentials[2]);
        assertTrue(credentials[CredentialStorage.USERNAME].equals(user2));
		assertTrue(credentials[CredentialStorage.ROLE].equals(role2));
		assertTrue(credentials[CredentialStorage.PASSWORD].equals(pass2));
	}

}
