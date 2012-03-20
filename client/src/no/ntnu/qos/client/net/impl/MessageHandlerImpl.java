package no.ntnu.qos.client.net.impl;

import no.ntnu.qos.client.DataObject;
import no.ntnu.qos.client.net.MessageHandler;

public class MessageHandlerImpl implements MessageHandler{

	//need an http-thingamajig and possibly other stuff
	
	public MessageHandlerImpl(){
		//TODO: build me
	}
	
	@Override
	public void sendData(DataObject data) {
		/* TODO: fetch socket from httpComponents, set diffServ
		 * and send message
		 */
		
	}
	
	//how should this receive replies from httpComponents?

}
