package no.ntnu.qos.client.credentials.impl;

import no.ntnu.qos.client.credentials.Token;

import java.net.URI;

public class TokenImpl implements Token {
	//TODO: Change to use an actual OpenSAML object?
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
		return this.token;
	}

	@Override
	public boolean isValid() {
		/* assumes that if the token became invalid less than 30 seconds ago, it is in practice OK to consider
		 * it still valid, as requesting a new token might take longer than that, depending on the network
		 * TODO: make it configurable*/
        return validUntil > (System.currentTimeMillis() - 30000);

    }

	@Override
	public URI getDestination() {
		return this.destination;
	}

    @Override
    public int getDiffServ() {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int getPriority() {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

}
