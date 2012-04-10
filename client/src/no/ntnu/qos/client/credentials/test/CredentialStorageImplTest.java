package no.ntnu.qos.client.credentials.test;

import static org.junit.Assert.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.NoSuchElementException;

import no.ntnu.qos.client.credentials.*;
import no.ntnu.qos.client.credentials.impl.CredentialStorageImpl;
import no.ntnu.qos.client.credentials.impl.TokenImpl;

import org.junit.BeforeClass;
import org.junit.Test;

/**
 * 
 * @author Stig Tore
 *	Testing the CredentialStorageImpl class.
 */
public class CredentialStorageImplTest {
	static CredentialStorage credentialStorage;
	static String token;
	static URI destination, destination2;
	static long validUntil1, validUntil2;
	static Token testToken1, testToken2;
	static String user, role, pass;
	static String user2, role2, pass2;

	@BeforeClass
	public static void setup() throws Exception {
		user = "testUser";
		role = "testRole";
		pass = "testPas";
		user2 = "testUser2";
		role2 = "testRole2";
		pass2 = "testPas2";
		token = "blah";
		destination = new URI("http://127.0.0.25/services/myservice/service");
		destination2 = new URI("http://127.0.0.26/myservice/service");
		validUntil1 = System.currentTimeMillis()+3600000;
		validUntil2 = System.currentTimeMillis()-35000;
		testToken1 = new TokenImpl(token, validUntil1, destination);
		testToken2 = new TokenImpl(token, validUntil2, destination2);

		credentialStorage = new CredentialStorageImpl(user, role, pass);
		credentialStorage.storeToken(testToken1);
		credentialStorage.storeToken(testToken2);
	}

    @Test
    public void getToken(){
        assertNotNull(credentialStorage.getToken(destination));
        assertNotNull(credentialStorage.getToken(destination2));
    }

	@Test
	public void storageHasTokenTest() throws URISyntaxException {
		assertTrue(credentialStorage.hasToken(destination));
		assertFalse(credentialStorage.hasToken(destination2)); //Should be invalid because it's expired!
		assertFalse(credentialStorage.hasToken(new URI("http://127.0.0.1/")));
	}

	@Test
	public void storageGivesCorrectTokensTest() {
		assertEquals(credentialStorage.getToken(destination), testToken1);
		Token tester = credentialStorage.getToken(destination);
		assertEquals(tester.getXML(), token);
	}

	@Test (expected=NoSuchElementException.class)
	public void testCorrectError() {
        credentialStorage.getToken(destination2);
	}

	@SuppressWarnings("static-access")
	@Test
	public void storedCredentialsTest() {
		String[] credentials = credentialStorage.getCredentials();
		assertTrue(credentials[CredentialStorage.USERNAME].equals(user));
		assertTrue(credentials[CredentialStorage.ROLE].equals(role));
		assertTrue(credentials[CredentialStorage.PASSWORD].equals(pass));
	}

	@SuppressWarnings("static-access")
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
