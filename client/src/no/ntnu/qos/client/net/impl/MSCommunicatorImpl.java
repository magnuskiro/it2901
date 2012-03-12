package no.ntnu.qos.client.net.impl;


import no.ntnu.qos.client.DataObject;
import no.ntnu.qos.client.net.MSCommunicator;


/**
 * Implementation of the no.ntnu.qos.client.net.MSCommunicator interface
 * @author Håvard
 *
 */
public class MSCommunicatorImpl implements MSCommunicator{

	/**
	 * main constructor
	 */
	public MSCommunicatorImpl(){
		
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
			
		}
		
	}
}
