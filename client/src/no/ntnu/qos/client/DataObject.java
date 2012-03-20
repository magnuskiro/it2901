package no.ntnu.qos.client;

import java.net.URI;

import no.ntnu.qos.client.credentials.Token;
import no.ntnu.qos.client.net.RouteInfo;


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
    private Token samlToken;
    private RouteInfo routeInfo;
    private String		soapFromClient;
    private URI			destination;


    /**
     * main constructor
     * @param sequencer	- the sequencer creating the object
     * @param soapFromClient	- SOAP message from client
     * @param destination	- destination of the message
     */
    public DataObject(Sequencer sequencer, String soapFromClient, URI destination){
        this.sequencer = sequencer;
        this.soapFromClient = soapFromClient;
        this.destination = destination;

    }

    /**
     * marks the data the object contains as sane
     * @param sane	- true if data is sane
     */
    public void setSane(boolean sane){
        this.sane = sane;
    }

    /**
     * sets the information on bandwidth and TR
     * @param routeInfo	- routingINfo object obtained from an msCommunicator
     */
    public void setRoutingInfo(RouteInfo routeInfo){
        this.routeInfo = routeInfo;
    }

    /**
     * sets the clients SAML-token
     * @param token	- the Token
     */
    public void setToken(Token token){
        samlToken = token;
        diffServ	= samlToken.getDiffServ();
        priority	= samlToken.getPriority();
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
    public RouteInfo getRoutingInfo(){
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

    public Token getSamlToken() {
        return samlToken;
    }
}