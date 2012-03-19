package no.ntnu.qos.ms;

/**
 * This interface for Routing Info, an implementation should
 * return the address of the last Tactical Router in a path
 * as well as the bandwidth of the limiting link on the path.
 * @author Ola Martin
 *
 */
public interface RoutingInfo {
	/**
	 * 
	 * @return {@link double} the limiting bandwidth on the path, -1 if no TR on path.
	 */
	public double getBandwidth();
	/**
	 * 
	 * @return {@link String} the GUID of the last Tactical Router on the path, "" if no TR.
	 */
	public String getLastTR();
}
