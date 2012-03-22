package no.ntnu.qos.client.net.impl;


import no.ntnu.qos.client.DataObject;
import no.ntnu.qos.client.net.ClientMSCommunicator;
import no.ntnu.qos.ms.MSCommunicator;
import no.ntnu.qos.ms.impl.MSCommunicatorImpl;

/**
 * Implementation of the no.ntnu.qos.client.net.MSCommunicator interface
 * This implementation uses a dummy xml file, as we have no access to an actual ms
 * @author Håvard
 *
 */
public class ClientMSCommunicatorImpl implements ClientMSCommunicator{

	
	private String routingXMLInfoPath;
	
	/**
	 * main constructor
	 * @param routingXMLInfoPath - path of the xml file containing the routing info
	 */
	public ClientMSCommunicatorImpl(String routingXMLInfoPath){
		this.routingXMLInfoPath = routingXMLInfoPath;
	}
	
	@Override
	public Runnable getRouteInfo(DataObject dataObj) {		
		return new RouteFetcher(dataObj);
	}

	/**
	 * private runnable that will be returned by the getRouteInfo method
	 */
	private class RouteFetcher implements Runnable{

		private DataObject dataObj;
		
		public RouteFetcher(DataObject data){
			dataObj = data;
		}
		
		
		@Override
		public void run() {
			MSCommunicator msComm = new MSCommunicatorImpl(routingXMLInfoPath);
			dataObj.setRoutingInfo(msComm.getRoutingInfo(dataObj.getDestination()));
		}
		
	}
}
