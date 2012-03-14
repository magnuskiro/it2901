package no.ntnu.qos.client.credentials;

import java.net.URI;

import no.ntnu.qos.client.credentials.Token;

public class TokenImpl implements Token{
	//TODO: Change to accomodate an actual OpenSAML object?
	String token;
	long validUntil;
	URI destination;
	
	public TokenImpl(String token, long validUntil, URI destination) {
		this.token = token;
		this.destination = destination;
		this.validUntil = validUntil;
	}
	@Override
	public String getXML() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isValid() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public URI getDestination() {
		// TODO Auto-generated method stub
		return null;
	}

}
