package no.ntnu.qos.client;

import java.io.ByteArrayInputStream;
import java.net.URI;

import javax.xml.stream.XMLStreamException;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.impl.builder.StAXOMBuilder;

import no.ntnu.qos.client.credentials.Token;
import no.ntnu.qos.client.credentials.TokenAxiom;


/**
 * Object containing the data clients wish to send
 * It will send itself when it detects that the criteria are met
 * (is sane, has a token)
 * @author Håvard
 *
 */
public class DataObject {

	private boolean		sane;
	private int			diffServ;
	private int			priority;
	private Sequencer	sequencer;
	private Token		samlToken;
	private String		soapFromClient;
	private String		soapToSend;
	private URI			destination;
	private ReceiveObject receiveObj;//added here to let the messageHandler get access to it 
	private ExceptionHandler exceptionHandler;


	/**
	 * main constructor
	 * @param sequencer	- the sequencer creating the object
	 * @param soapFromClient	- SOAP message from client
	 * @param destination	- destination of the message
	 * @param exceptionHandler
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
		if(samlToken != null) {
			if(soapToSend == null || soapToSend.equals("")) {
				buildSoap();
			}
			return soapToSend; 
		}
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
		if(sane && samlToken != null && priority!=0 && diffServ!=0 && destination!=null){
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

	/**
	 * Build the SOAP message to be sent.
	 */
	private void buildSoap() {
		ByteArrayInputStream stream = new ByteArrayInputStream(soapFromClient.getBytes());
		OMFactory factory = OMAbstractFactory.getOMFactory();
		StAXOMBuilder builder = null;
		try {
			builder = new StAXOMBuilder(stream);
		} catch (XMLStreamException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		OMElement root = builder.getDocumentElement();
		OMElement parsedToken = null;
		if(samlToken instanceof TokenAxiom) {
			parsedToken = ((TokenAxiom) samlToken).getOMElement();
		} else {
			ByteArrayInputStream tokenStream = new ByteArrayInputStream(samlToken.getXML().getBytes());
			StAXOMBuilder tokenBuilder = null;
			try {
				tokenBuilder = new StAXOMBuilder(tokenStream);
			} catch (XMLStreamException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			parsedToken = tokenBuilder.getDocumentElement();
		}
		OMElement body = (OMElement) root.getChildrenWithLocalName("Body").next();
		body.addChild(parsedToken);
		soapToSend = root.toString();
	}
}