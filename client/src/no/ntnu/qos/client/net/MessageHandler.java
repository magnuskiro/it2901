package no.ntnu.qos.client.net;

import no.ntnu.qos.client.DataObject;
import no.ntnu.qos.client.ReceiveObject;

/**
 * Interface for classes that handle messages and sends them over the network.
 * @author Håvard
 *
 */
public interface MessageHandler {

	/**
	 * sends the given DataObject
	 * @param data - DataObject containing recipient info and other data
	 */
	public ReceiveObject sendData(DataObject data);
}
