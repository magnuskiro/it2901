package no.ntnu.qos.client;

/**
 * Interface for the recieveobject returned when a request is sent to a service through the client interface.
 * @author stigtore
 *
 */
public interface RecieveObject {
	/**
	 * Recieve method for a specific request.
	 * NOTE: Should be implemented as a blocking method.
	 * @return XML message from the service
	 */
	public String recieve();
}
