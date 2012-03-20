package no.ntnu.qos.client.net.impl;


import no.ntnu.qos.client.DataObject;
import no.ntnu.qos.client.net.ClientMSCommunicator;
import no.ntnu.qos.client.net.RouteInfo;

/**
 * Implementation of the no.ntnu.qos.client.net.MSCommunicator interface
 * This implementation uses a dummy xml file, as we have no access to an actual ms
 * @author Håvard
 *
 */
public class ClientMSCommunicatorImpl implements ClientMSCommunicator{

	
	private String msXML;
	
	/**
	 * main constructor
	 * @param newMS - path of the xml file containing the routing info
	 */
	public ClientMSCommunicatorImpl(String newMS){
		msXML	= newMS;
	}
	
	@Override
	public Runnable getRouteInfo(DataObject dataObj) {		
		return new RouteFetcher(dataObj);
	}

	/**
	 * private runnable that will be returned by the getRouteInfo method
	 * @author Håvard
	 *
	 */
	private class RouteFetcher implements Runnable{

		private DataObject dataObj;
		
		public RouteFetcher(DataObject data){
			dataObj = data;
		}
		
		
		@Override
		public void run() {
            ClientMSCommunicator clientMSCommunicator = new ClientMSCommunicatorImpl(msXML);
            dataObj.setRoutingInfo((RouteInfo) clientMSCommunicator.getRouteInfo(dataObj));
			//MSCommunicator msComm = new MSCommunicatorImpl(msXML);
			//dataObj.setRoutingInfo(msComm.getRoutingInfo(dataObj.getDestination()));
		}
		
	}
}
