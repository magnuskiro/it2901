package no.ntnu.qos.client.net;

/**
 * class used to contain link and bandwidth data acquired from the Monitoring Service
 * @author Håvard
 *
 */
public class RouteInfo {

	private int		bandwidth;
	private String	link;
	
	
	/**
	 * main constructor
	 * 
	 * @param b - int representing the bandwidth 
	 * @param l - string representing the link in use
	 */
	public RouteInfo(int b, String l){
		
	}
	
	/**
	 * returns the bandwith of the link
	 * @return
	 */
	public int getBandwidth(){
		return bandwidth;
	}
	
	/**
	 * return the link being used at the Tactical Router
	 * @return
	 */
	public String getLastLink(){
		return link;
	}
}
