package no.ntnu.qos.client;

/**
 * Interface for the ReceiveObject returned when a request is sent to a service through the client interface.
 * @author stigtore
 *
 */
public interface ReceiveObject {
	/**
	 * Receive method for a specific request.
	 * NOTE: Should be implemented as a blocking method.
	 * @return XML message from the service
	 * @throws InterruptedException 
	 */
	public String receive() throws InterruptedException;
}
