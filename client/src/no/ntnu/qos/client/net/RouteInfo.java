package no.ntnu.qos.client.net;

/**
 * class used to contain information acquired from the Monitoring Service
 * @author Håvard
 *
 */
public class RouteInfo {

	/**
	 * returns the bandwith of the link
	 * @return
	 */
	public int getBandwidth(){
//		TODO: return something real
		return 0;
	}
	
	/**
	 * return the link being used at the Tactical Router
	 * @return
	 */
	public String getLastLink(){
		//TODO: return a real value
		return null;
	}
}
