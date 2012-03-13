package no.ntnu.qos.client.net.impl;


import no.ntnu.qos.client.DataObject;
import no.ntnu.qos.client.net.ClientMSCommunicator;
import no.ntnu.qos.ms.MSCommunicator;
import no.ntnu.qos.ms.impl.MSCommunicatorImpl;


/**
 * Implementation of the no.ntnu.qos.client.net.MSCommunicator interface
 * @author Håvard
 *
 */
public class ClientMSCommunicatorImpl implements ClientMSCommunicator{

	private String ms;
	
	/**
	 * main constructor
	 */
	public ClientMSCommunicatorImpl(String newMS){
		ms	= newMS;
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
			// TODO fetch the routing info and insert it into the DataObject
//			MSCommunicator msComm = new MSCommunicatorImpl(ms);
//			dataObj.setRouteInfo(msComm.getRoutingInfo(destIP));
		}
		
	}
}
