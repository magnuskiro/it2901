package no.ntnu.qos.ms.impl;

import no.ntnu.qos.ms.RoutingInfo;

/**
 * This Object holds the address of the last Tactical Router in a path
 * as well as the bandwidth of the limiting link on the path.
 * @author Ola Martin
 *
 */
public class RoutingInfoImpl implements RoutingInfo {
	final private String lastTR;
	final private double bandwidth;
	
	/**
	 * Builds the object
	 * @param lastTR the GUID of the last Tactical Router on the path, "" if no TR.
	 * @param bandwidth the limiting bandwidth on the path, -1 if no TR.
	 */
	public RoutingInfoImpl(String lastTR, double bandwidth) {
		this.lastTR=lastTR;
		this.bandwidth=bandwidth;
	}

	public double getBandwidth() {
		return bandwidth;
	}

	public String getLastTR() {
		return lastTR;
	}
}
