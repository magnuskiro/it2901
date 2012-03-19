package no.ntnu.qos.client.testing;

import static org.junit.Assert.*;

import java.net.URI;
import java.net.URISyntaxException;

import no.ntnu.qos.client.credentials.*;

import no.ntnu.qos.client.credentials.Impl.TokenImpl;
import no.ntnu.qos.client.credentials.Impl.TokenManagerImpl;
import org.junit.BeforeClass;
import org.junit.Test;

public class CredentialsTest {
	static TokenManager tM;
	static CredentialStorage cS;;
	static String token;
	static URI uri1, uri2;
	static long validUntil1, validUntil2;
	static Token testToken1, testToken2;


	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		token = "blah";
		uri1 = new URI("127.0.0.25");
		uri2 = new URI("127.0.0.26");
		validUntil1 = System.currentTimeMillis()+3600000;
		validUntil2 = System.currentTimeMillis()-1;
		testToken1 = new TokenImpl(token, validUntil1, uri1);
		testToken2 = new TokenImpl(token, validUntil2, uri2);

		tM = new TokenManagerImpl();
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
		assertFalse(cS.hasToken(new URI("127.0.0.1")));
	}
	
	@Test
	public void testStorageGivesCorrectTokens() {
		assertEquals(cS.getToken(uri1), testToken1);
		Token tester = cS.getToken(uri1);
		assertEquals(tester.getXML(), token);
	}

}
