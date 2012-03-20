package no.ntnu.qos.client.impl;

import no.ntnu.qos.client.DataObject;
import no.ntnu.qos.client.QoSClient;
import no.ntnu.qos.client.SanityChecker;
import no.ntnu.qos.client.Sequencer;
import no.ntnu.qos.client.credentials.TokenManager;
import no.ntnu.qos.client.net.ClientMSCommunicator;
import no.ntnu.qos.client.net.MessageHandler;

import java.net.URI;

/**
 * @author Magnus Kirø
 */
public class SequencerImpl implements Sequencer {

    QoSClient		qoSClient;
    TokenManager	tokenManager;
    MessageHandler	msgHandle;
    SanityChecker	sanityChecker;
    ClientMSCommunicator msComm;
    

    public SequencerImpl(QoSClient qoSClient) {
        this.qoSClient = qoSClient;
        
    }

    @Override
    public void setCredentials(String username, String role, String password) {
        tokenManager.setCredentials(username, role, password);
    }

    @Override
    public void sendData(String data, URI destination) {
    	DataObject dataObj = new DataObject(this, data, destination);

    	//fetches various data the DataObject needs 
    	tokenManager.getToken(dataObj);
    	msComm.getRouteInfo(dataObj);
    	sanityChecker.isSane(dataObj);
    	
    }

    @Override
    public void sendData(DataObject dataObj) {
    	msgHandle.sendData(dataObj);
    }

    @Override
    public void returnData(String data) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public TokenManager getTokenManager() {
        return tokenManager;
    }

    public void setTokenManager(TokenManager tokenManager) {
        this.tokenManager = tokenManager;
    }
}
