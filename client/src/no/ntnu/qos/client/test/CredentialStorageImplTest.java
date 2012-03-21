package no.ntnu.qos.client.test;

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
	static CredentialStorage cS;
	static String token;
	static URI uri1, uri2;
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
		uri1 = new URI("http://127.0.0.25/");
		uri2 = new URI("http://127.0.0.26/");
		validUntil1 = System.currentTimeMillis()+3600000;
		validUntil2 = System.currentTimeMillis()-35000;
		testToken1 = new TokenImpl(token, validUntil1, uri1);
		testToken2 = new TokenImpl(token, validUntil2, uri2);

		cS = new CredentialStorageImpl(user, role, pass);
		cS.storeToken(testToken1);
		cS.storeToken(testToken2);
	}

	@Test
	public void storageHasTokenTest() throws URISyntaxException {
		assertTrue(cS.hasToken(uri1));
		assertFalse(cS.hasToken(uri2)); //Should be invalid because it's expired!
		assertFalse(cS.hasToken(new URI("http://127.0.0.1/")));
	}

	@Test
	public void storageGivesCorrectTokensTest() {
		assertEquals(cS.getToken(uri1), testToken1);
		Token tester = cS.getToken(uri1);
		assertEquals(tester.getXML(), token);
	}

	@Test (expected=NoSuchElementException.class)
	public void testCorrectError() {
		cS.getToken(uri2);
	}

	@SuppressWarnings("static-access")
	@Test
	public void storedCredentialsTest() {
		String[] creds = cS.getCredentials();
		assertTrue(creds[cS.USERNAME].equals(user));
		assertTrue(creds[cS.ROLE].equals(role));
		assertTrue(creds[cS.PASSWORD].equals(pass));
	}

	@SuppressWarnings("static-access")
	@Test
	public void setClientCredentialsInStorageTest(){
		cS.setCredentials(user2, role2, pass2);

		String[] creds = cS.getCredentials();
        System.out.print(creds[2]);
		assertTrue(creds[cS.USERNAME].equals(user2));
		assertTrue(creds[cS.ROLE].equals(role2));
		assertTrue(creds[cS.PASSWORD].equals(pass2));

	}

}
