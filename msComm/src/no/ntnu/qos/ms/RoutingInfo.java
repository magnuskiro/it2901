package no.ntnu.qos.ms;

import java.net.URI;

public class RoutingInfo {
	URI lastTR;
	double bandwidth;
	
	public RoutingInfo() {
	}
	
	public RoutingInfo(URI lastTR, double bandwidth) {
		this.lastTR=lastTR;
		this.bandwidth=bandwidth;
	}
	
	public double getBandwidth() {
		return bandwidth;
	}
	
	public URI getLastTR() {
		return lastTR;
	}
	
	public void setBandwidth(double bandwidth) {
		this.bandwidth = bandwidth;
	}
	
	public void setLastTR(URI lastTR) {
		this.lastTR = lastTR;
	}
}
