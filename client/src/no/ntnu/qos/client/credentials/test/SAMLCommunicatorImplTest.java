package no.ntnu.qos.client.credentials.test;

import static org.junit.Assert.*;

import java.net.URI;
import java.net.URISyntaxException;

import no.ntnu.qos.client.credentials.Token;
import no.ntnu.qos.client.credentials.impl.SAMLCommunicatorImpl;

import org.junit.Test;

public class SAMLCommunicatorImplTest {

	@Test
	public void test() throws URISyntaxException {
		SAMLCommunicatorImpl SAMLCommunicator = new SAMLCommunicatorImpl();
		URI destination = new URI("https://78.91.9.62:8243/services/EchoService");
		String role = "clientRole1";
		String username = "kalle";
		String password = "klovn";
		Token token = SAMLCommunicator.getToken(destination, username, role, password);
		assertNotNull(token);
		assertEquals(10, token.getDiffServ());
		assertEquals(100, token.getPriority());
		System.out.println(token.getXML());
		fail("Not yet implemented");
	}

}
