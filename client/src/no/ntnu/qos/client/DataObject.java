package no.ntnu.qos.client;

import java.net.URI;

import no.ntnu.qos.client.credentials.Token;
import no.ntnu.qos.ms.RoutingInfo;

/**
 * Object containing the data clients wish to send
 * @author Håvard
 *
 */
public class DataObject {
	
	private boolean		sane;
	private int			diffServ;
	private int			priority;
	private Sequencer	sequencer;
	private Token		samlTok;
	private RoutingInfo	routingInfo;
	private String		soapFromClient;
	private URI			destination;
	
	
	/**
	 * main constructor
	 * @param seq	- the sequencer creating the object
	 * @param sfc	- SOAP message from client
	 * @param dest	- destination of the message
	 */
	public DataObject(Sequencer seq, String sfc, URI dest){
		sequencer = seq;
		soapFromClient = sfc;
		destination = dest;
		
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
		samlTok		= t;
		diffServ	= samlTok.getDiffServ();
		priority	= samlTok.getPriority();
	}
		
	/**
	 * 
	 * @return	- a SOAP message
	 */
	public String getSoap(){
		//TODO
		return "";
	}
	
	/**
	 * gets the message destination
	 * @return
	 */
	public URI getDestination(){
		return destination;
	}
	
	/**
	 * gets the routingInfo this object is aware of
	 * @return
	 */
	public RoutingInfo getRoutingInfo(){
		return routingInfo;
	}

	/**
	 * gets the diffServ value this message will have
	 * @return
	 */
	public int getDiffServ(){
		return diffServ;
	}
	
	/**
	 * gets the priority value of the message
	 * @return
	 */
	public int getPriority(){
		return priority;
	}
	
}
