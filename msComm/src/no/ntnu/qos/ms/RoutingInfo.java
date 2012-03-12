package no.ntnu.qos.ms;

/**
 * This Object holds the address of the last Tactical Router in a path
 * as well as the bandwidth of the limiting link on the path.
 * @author Ola Martin
 *
 */
public class RoutingInfo {
	final private String lastTR;
	final private double bandwidth;
	
	/**
	 * Builds the object
	 * @param lastTR the IP address of the last tactical router on the path, "" if no TR.
	 * @param bandwidth the limiting bandwidth on the path.
	 */
	public RoutingInfo(String lastTR, double bandwidth) {
		this.lastTR=lastTR;
		this.bandwidth=bandwidth;
	}
	/**
	 * 
	 * @return {@link double} the limiting bandwidth on the path.
	 */
	public double getBandwidth() {
		return bandwidth;
	}
	/**
	 * 
	 * @return {@link String} the IP address of the last Tactical Router on the path.
	 */
	public String getLastTR() {
		return lastTR;
	}
}
