package no.ntnu.qos.ms;

import java.net.URI;

/**
 * This interface is used for communication with the Monitoring Service.
 * @author Ola Martin
 *
 */
public interface MSCommunicator {
	/**
	 * Should contact the Monitoring Service to retrieve routing info.
	 * @param destIP, the IP address of the message destination.
	 * @return {@link RoutingInfo} the routing info for the path to destIP.
	 */
	public RoutingInfo getRoutingInfo(URI destIP);
}
