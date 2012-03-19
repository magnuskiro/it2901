package no.ntnu.qos.client.testing;

import static org.junit.Assert.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.NoSuchElementException;

import no.ntnu.qos.client.credentials.*;
import no.ntnu.qos.client.credentials.impl.TokenImpl;
import no.ntnu.qos.client.credentials.impl.TokenManagerImpl;

import no.ntnu.qos.client.credentials.impl.TokenImpl;
import no.ntnu.qos.client.credentials.impl.TokenManagerImpl;
import org.junit.BeforeClass;
import org.junit.Test;
/**
 * 
 * @author Stig Tore
 *	Testing classes for the credentials system
 */
public class CredentialsTest {
	static TokenManager tM;
	static CredentialStorage cS;;
	static String token;
	static URI uri1, uri2;
	static long validUntil1, validUntil2;
	static Token testToken1, testToken2;
	static String user, role, pass;


	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		user = "testUser";
		role = "testRole";
		pass = "testPas";
		token = "blah";
		uri1 = new URI("http://127.0.0.25/");
		uri2 = new URI("http://127.0.0.26/");
		validUntil1 = System.currentTimeMillis()+3600000;
		validUntil2 = System.currentTimeMillis()-35000;
		testToken1 = new TokenImpl(token, validUntil1, uri1, 1, 2);
		testToken2 = new TokenImpl(token, validUntil2, uri2, 3, 4);

		tM = new TokenManagerImpl(user, role, pass);
		cS = ((TokenManagerImpl)tM).getCredentialStorage();
		cS.storeToken(testToken1);
		cS.storeToken(testToken2);
	}

	@Test
	public void testTokensForValidity() {
		assertTrue(testToken1.isValid());
		assertFalse(testToken2.isValid());
	}

	@Test
	public void testStorageHasTokens() throws URISyntaxException {
		assertTrue(cS.hasToken(uri1));
		assertFalse(cS.hasToken(uri2)); //Should be invalid because it's expired!
		assertFalse(cS.hasToken(new URI("http://127.0.0.1/")));
	}
	
	@Test
	public void testStorageGivesCorrectTokens() {
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
	public void testCredentials() {
		String[] creds = cS.getCredentials();
		assertTrue(creds[cS.USERNAME].equals(user));
		assertTrue(creds[cS.ROLE].equals(role));
		assertTrue(creds[cS.PASSWORD].equals(pass));
	}

}
