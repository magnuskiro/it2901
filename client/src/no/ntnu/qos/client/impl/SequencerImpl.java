package no.ntnu.qos.client.impl;

import no.ntnu.qos.client.DataObject;
import no.ntnu.qos.client.ExceptionHandler;
import no.ntnu.qos.client.QoSClient;
import no.ntnu.qos.client.ReceiveObject;
import no.ntnu.qos.client.SanityChecker;
import no.ntnu.qos.client.Sequencer;
import no.ntnu.qos.client.credentials.TokenManager;
import no.ntnu.qos.client.credentials.impl.TokenManagerImpl;
import no.ntnu.qos.client.net.ClientMSCommunicator;
import no.ntnu.qos.client.net.MessageHandler;
import no.ntnu.qos.client.net.impl.ClientMSCommunicatorImpl;
import no.ntnu.qos.client.net.impl.MessageHandlerImpl;

import java.net.URI;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Magnus Kirø
 */
public class SequencerImpl implements Sequencer {

	QoSClient			qoSClient;
	TokenManager		tokenManager;
	MessageHandler		messageHandler;
	SanityChecker		sanityChecker;
	ClientMSCommunicator msCommunicator;
	ExceptionHandler	exceptionHandler;
	ExecutorService		threadPool;


	public SequencerImpl(QoSClient qoSClient, String username, String role, String password,
			ExceptionHandler exceptionHandler) {
		this.qoSClient = qoSClient;

		tokenManager	= new TokenManagerImpl(username, role, password);
		messageHandler	= new MessageHandlerImpl(this);
		sanityChecker	= new SanitycheckerImpl();
		this.exceptionHandler = exceptionHandler;
		threadPool		= Executors.newFixedThreadPool(20); //add number of threads to config-thingy

		/* TODO: need a way to do this properly, how do we know where the MS is?
		 * this implementation only reads an xml-file, but still... */
		// takes the path to the XML file containing the routing info as the argument.
		msCommunicator = new ClientMSCommunicatorImpl("routingXMLInfoPath");
	}

	@Override
	public void setCredentials(String username, String role, String password) {
		tokenManager.setCredentials(username, role, password);
	}

	@Override
	public ReceiveObject sendData(String data, URI destination) {
		DataObject dataObj = new DataObject(this, data, destination, exceptionHandler);

		ReceiveObject receiveObj = new ReceiveObjectImpl();
		dataObj.setReceiveObject(receiveObj);

		//fetches various data the DataObject needs
		tokenManager.getToken(dataObj);
		msCommunicator.getRouteInfo(dataObj);
		sanityChecker.isSane(dataObj);


		return receiveObj;
	}

	@Override
	public void sendData(DataObject dataObj) {
		threadPool.execute(messageHandler.sendData(dataObj));
	}

	@Override
	public void returnData(ReceiveObject recObj) {
		qoSClient.Receive(recObj);
	}

	public TokenManager getTokenManager() {
		return tokenManager;
	}

}
