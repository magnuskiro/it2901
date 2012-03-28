package no.ntnu.qos.client;

import java.net.URI;

import no.ntnu.qos.client.credentials.Token;
import no.ntnu.qos.ms.RoutingInfo;


/**
 * Object containing the data clients wish to send
 * It will send itself when it detects that the criteria are met
 * (is sane, has a token, has routing info)
 * @author Håvard
 *
 */
public class DataObject {

	private boolean		sane;
	private int			diffServ;
	private int			priority;
	private Sequencer	sequencer;
	private Token		samlToken;
	private RoutingInfo routeInfo;
	private String		soapFromClient;
	private URI			destination;
	private ReceiveObject receiveObj;//added here to let the messageHandler get access to it 
	private ExceptionHandler exceptionHandler;


	/**
	 * main constructor
	 * @param sequencer	- the sequencer creating the object
	 * @param soapFromClient	- SOAP message from client
	 * @param destination	- destination of the message
	 */
	public DataObject(Sequencer sequencer, String soapFromClient, URI destination, ExceptionHandler exceptionHandler){
		this.sequencer = sequencer;
		this.soapFromClient = soapFromClient;
		this.destination = destination;
		this.exceptionHandler = exceptionHandler;

	}

	/**
	 * marks the data the object contains as sane, sends itself if other
	 * criteria are met
	 * @param sane	- true if data is sane
	 */
	public synchronized void setSane(boolean sane){
		this.sane = sane;

		if (isReadyToSend()){
			sequencer.sendData(this);
		}
	}

	/**
	 * sets the information on bandwidth and TR, send itself if other
	 * criteria are met
	 * @param routeInfo	- routingINfo object obtained from an msCommunicator
	 */
	public synchronized void setRoutingInfo(RoutingInfo routeInfo){
		this.routeInfo = routeInfo;

		if (isReadyToSend()){
			sequencer.sendData(this);
		}
	}

	/**
	 * sets the clients SAML-token, sends itself if other criteria are met
	 * @param token	- the Token
	 */
	public synchronized void setToken(Token token){
		samlToken = token;
		diffServ	= samlToken.getDiffServ();
		priority	= samlToken.getPriority();

		if (isReadyToSend()){
			sequencer.sendData(this);
		}
	}

	/**
	 *
	 * @return	- a SOAP message
	 */
	public String getSoap(){
		return this.soapFromClient;
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
		return routeInfo;
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

	/**
	 * checks if all necessary data/criteria for sending are present/met
	 * @return - true if ready to send, false if not 
	 */
	private synchronized boolean isReadyToSend(){
		if(sane && routeInfo != null && samlToken != null && samlToken.isValid()
                && priority!=0 && diffServ!=0 && destination!=null
                ){
			/* token may become invalid before other criteria are met
			 * TODO: trigger a token refresh if token is invalid?
			 */
            return true;
		}
		return false;
	}

	/**
	 * returns the SAML token in this object
	 * @return
	 */
    public Token getSamlToken() {
        return samlToken;
    }

    /**
     * sets the receiveObject that the reply to this message will be returned in
     * @param receiveObj
     */
	public void setReceiveObject(ReceiveObject receiveObj) {
		this.receiveObj = receiveObj;		
	}
	
	/**
	 * gets the receiveObject to return the reply in
	 * @return
	 */
	public ReceiveObject getReceiveObject(){
		return receiveObj;
	}
	/**
	 * gets the exception handler defined by the client
	 * @return ExceptionHandler
	 */
	public ExceptionHandler getExceptionHandler() {
		return exceptionHandler;
	}
}