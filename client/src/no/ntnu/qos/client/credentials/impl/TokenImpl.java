package no.ntnu.qos.client.credentials.impl;

import java.net.URI;

import no.ntnu.qos.client.credentials.Token;

public class TokenImpl implements Token{
	//TODO: Change to accomodate an actual OpenSAML object?
	private String token;
	private long validUntil;
	private URI destination;
	private int diffServ;
	private int priority;
	
	
	public TokenImpl(String token, long validUntil, URI destination, int diffServ, int prio) {
		this.token = token;
		this.destination = destination;
		this.validUntil = validUntil;
		this.diffServ = diffServ;
		this.priority = prio;
	}
	@Override
	public String getXML() {
		return token;
	}

	@Override
	public boolean isValid() {
		if(validUntil > System.currentTimeMillis()) {
			return true;
		}
		return false;
	}

	@Override
	public URI getDestination() {
		return destination;
	}
	@Override
	public int getDiffServ() {
		return diffServ;
	}
	@Override
	public int getPriority() {
		return priority;
	}

}
