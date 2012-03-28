package no.ntnu.qos.client;

/**
 * Interface for being able to listen to all reply messages from services received by the client library
 * @author Stig Tore
 *
 */
public interface DataListener {
	/**
	 * Default receive method
	 * @param data SOAP data
	 */
	public void newData(ReceiveObject recObj);
}
