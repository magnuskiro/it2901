package no.ntnu.qos.client;

import no.ntnu.qos.client.credentials.Token;
import no.ntnu.qos.ms.RoutingInfo;

/**
 * Object containing the data clients wish to send
 * @author Håvard
 *
 */
public class DataObject {
	
	private boolean		sane;
	private Sequencer	sequencer;
	private Token		samlTok;
	private RoutingInfo	routingInfo;
	
	
	/* TODO: needs something to store the message in, as well as a
	 * way to add it, possibly by constructor*/ 
	
	
	
	
	
	
	public DataObject(){
		//TODO
	}
	
	public DataObject(Sequencer s){
		sequencer = s;
	}
	
	/**
	 * marks the data the object contains as sane
	 * @param s	- true if data is sane
	 */
	public void setSane(boolean s){
		sane = s;
	}
	
	/**
	 * sets the information on bandwidth and TR
	 * @param r	- routingINfo object obtained from an msCommunicator
	 */
	public void setRoutingInfo(RoutingInfo r){
		routingInfo = r;
	}
	
	/**
	 * sets the clients SAML-token
	 * @param t	- the Token
	 */
	public void setToken(Token t){
		samlTok = t;
	}
	
	/**
	 * 
	 * @return	- a SOAP message
	 */
	public String getSoap(){
		//TODO
		return "";
	}

}
