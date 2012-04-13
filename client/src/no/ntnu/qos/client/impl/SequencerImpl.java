package no.ntnu.qos.client.impl;

import no.ntnu.qos.client.DataObject;
import no.ntnu.qos.client.ExceptionHandler;
import no.ntnu.qos.client.QoSClient;
import no.ntnu.qos.client.ReceiveObject;
import no.ntnu.qos.client.SanityChecker;
import no.ntnu.qos.client.Sequencer;
import no.ntnu.qos.client.credentials.TokenManager;
import no.ntnu.qos.client.credentials.impl.TokenManagerImpl;
import no.ntnu.qos.client.net.MessageHandler;
import no.ntnu.qos.client.net.impl.MessageHandlerImpl;

import java.io.UnsupportedEncodingException;
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
	ExceptionHandler	exceptionHandler;
	ExecutorService		threadPool;


	public SequencerImpl(QoSClient qoSClient, String username, String role, String password,
			ExceptionHandler exceptionHandler) {
		this.qoSClient = qoSClient;

		tokenManager	= new TokenManagerImpl(username, role, password);
		messageHandler	= new MessageHandlerImpl(this);
		sanityChecker	= new SanityCheckerImpl();
		this.exceptionHandler = exceptionHandler;
		threadPool		= Executors.newFixedThreadPool(20); //add number of threads to config-thingy

	}

	@Override
	public void setCredentials(String username, String role, String password) {
		ConfigManager.LOGGER.info("Setting Credentials");
		tokenManager.setCredentials(username, role, password);
	}

	@Override
	public synchronized ReceiveObject sendData(String data, URI destination) {
		ConfigManager.LOGGER.info("Received request to send data to: "+destination.getHost());
		DataObject dataObj = new DataObject(this, data, destination, exceptionHandler);
		ConfigManager.LOGGER.info("DataObject created");

		ReceiveObject receiveObj = new ReceiveObjectImpl();
		ConfigManager.LOGGER.info("ReceiveObject created");
		dataObj.setReceiveObject(receiveObj);

		//fetches various data the DataObject needs
		ConfigManager.LOGGER.info("Getting runnables to get token and check sanity");
		Runnable getToken = tokenManager.getToken(dataObj);
//		Runnable getSane = sanityChecker.isSane(dataObj);  	//Sanity check integrated into dataobject when generating soap
		//Executes the runnables
		ConfigManager.LOGGER.info("Runnables received, executing");
		threadPool.execute(getToken);
//		threadPool.execute(getSane); 						//Sanity check integrated into dataobject when generating soap
		ConfigManager.LOGGER.info("Returning receiveObject to Qosclient");
		return receiveObj;
	}

	@Override
	public void sendData(DataObject dataObj) {
		ConfigManager.LOGGER.info("Sending data to message handler");
		try {
			threadPool.execute(messageHandler.sendData(dataObj));
		} catch (UnsupportedEncodingException e) {
			ConfigManager.LOGGER.warning("Client Attempted to send invalid SOAP message");
		}
	}

	@Override
	public void returnData(ReceiveObject recObj) {
		ConfigManager.LOGGER.info("Returning reply to QosClient");
		qoSClient.receive(recObj);
	}

	public TokenManager getTokenManager() {
		return tokenManager;
	}

}
