package no.ntnu.qos.client.credentials.impl;

import no.ntnu.qos.client.credentials.Token;

import java.net.URI;

public class TokenImpl implements Token {
	//TODO: Change to use an actual OpenSAML object?
	String token;
	long validUntil;
    long expirationTimeBuffer = 30000; // 30000 is default as it resembles 30seconds when used.
	URI destination;
	int priority;
	int diffServ;
	
	public TokenImpl(String token, long validUntil, URI destination) {
        // todo - has to be expanded to set the diffserv and priority variables also.
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
        /*
            validUntil = 1.000.000 | To replace the token 30 seconds before it expires we do
            currentTimeMillis() + 30sec and compares it to ValidUntil.
            This means that when the token has less then 30 seconds left until it expires it becomes invalid.

            when (currentTimeMillis() + 30sec == validUntil) then we have 30 sec until the token is invalid.
            when (currentTimeMillis() + 30sec > validUntil) then the token has less then 30 sec until expiration, so we renew it.
            when (currentTimeMillis() + 30sec < validUntil) then the token is still valid, we do nothing.

        */
        return validUntil > (System.currentTimeMillis() + expirationTimeBuffer);
    }

    @Override
    public long getExpirationTimeBuffer() {
        return expirationTimeBuffer;
    }

    @Override
    public void setExpirationTimeBuffer(long expirationTimeBuffer) {
        this.expirationTimeBuffer = expirationTimeBuffer;
    }

    @Override
	public URI getDestination() {
		return this.destination;
	}

    @Override
    public int getDiffServ() {
        return diffServ;
    }

    @Override
    public int getPriority() {
        return priority;
    }

	@Override
	public void setPriority(int priority) {
		this.priority = priority;		
	}

	@Override
	public void setDiffServ(int diffServ) {
		this.diffServ = diffServ;
	}

}
