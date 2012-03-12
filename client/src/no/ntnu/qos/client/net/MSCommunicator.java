package no.ntnu.qos.client.net;


import no.ntnu.qos.client.DataObject;

/**
 * Interface for communicating with the Monitoring Service on a tactical router
 * in order to fetch link and bandwidth-data for the client.
 * @author Håvard
 *
 */
public interface MSCommunicator {

		
	/**
	 * method for acquiring route information (link and bandwidth) from the MS given a DataObject containing
	 * all required information. The route information will be inserted to the DataObject.
	 *  
	 * @param dataObj - DataObject containing the required information
	 */
	public Runnable getRouteInfo(DataObject dataObj);
}
