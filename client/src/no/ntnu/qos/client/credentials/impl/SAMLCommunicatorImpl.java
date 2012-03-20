package no.ntnu.qos.client.credentials.impl;

import java.net.URI;

import no.ntnu.qos.client.credentials.SAMLCommunicator;
import no.ntnu.qos.client.credentials.Token;

public class SAMLCommunicatorImpl implements SAMLCommunicator {

	@Override
	public Token getToken(URI destination, String userName, String password,
			String role) {
		// TODO Auto-generated method stub
        Token token = new TokenImpl();
		return token;
	}

}
