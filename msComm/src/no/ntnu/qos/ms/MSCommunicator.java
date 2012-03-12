package no.ntnu.qos.ms;

import java.net.URI;

public interface MSCommunicator {
	public RoutingInfo getRoutingInfo(URI destIP);
}
