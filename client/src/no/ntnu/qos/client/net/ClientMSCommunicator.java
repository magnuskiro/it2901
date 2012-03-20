package no.ntnu.qos.client.net;


import no.ntnu.qos.client.DataObject;

/**
 * Interface for communicating with the Monitoring Service on a tactical router
 * in order to fetch link and bandwidth-data for the client.
 * @author Håvard
 *
 */
public interface ClientMSCommunicator {

		
	/**
	 * method for acquiring route information (link and bandwidth) from the MS given a DataObject containing
	 * all required information. The route information will be inserted to the DataObject.
	 *  
	 * @param dataObj - DataObject containing the required information
	 * @return Runnable - class that implements runnable and does the actual work when run
	 */
	public Runnable getRouteInfo(DataObject dataObj);
}
