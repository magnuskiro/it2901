package no.ntnu.qos.client.impl;

import no.ntnu.qos.client.DataObject;
import no.ntnu.qos.client.QoSClient;
import no.ntnu.qos.client.SanityChecker;
import no.ntnu.qos.client.Sequencer;
import no.ntnu.qos.client.credentials.TokenManager;
import no.ntnu.qos.client.credentials.impl.TokenManagerImpl;
import no.ntnu.qos.client.net.ClientMSCommunicator;
import no.ntnu.qos.client.net.MessageHandler;
import no.ntnu.qos.client.net.impl.ClientMSCommunicatorImpl;
import no.ntnu.qos.client.net.impl.MessageHandlerImpl;

import java.net.URI;

/**
 * @author Magnus Kirø
 */
public class SequencerImpl implements Sequencer {

    QoSClient		qoSClient;
    TokenManager	tokenManager;
    MessageHandler	msgHandler;
    SanityChecker	sanityChecker;
    ClientMSCommunicator msComm;
    

    public SequencerImpl(QoSClient qoSClient, String username, String role, String password) {
        this.qoSClient = qoSClient;
        
        tokenManager	= new TokenManagerImpl(username, role, password);
        msgHandler		= new MessageHandlerImpl();
        sanityChecker	= new SanitycheckerImpl();
        
        /* TODO: need a way to do this properly, how do we know where the MS is?
         * this implementation only reads an xml-file, but still... */
        msComm			= new ClientMSCommunicatorImpl(null);
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
    	msgHandler.sendData(dataObj);
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
