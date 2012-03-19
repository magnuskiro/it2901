package no.ntnu.qos.client.impl;

import no.ntnu.qos.client.DataObject;
import no.ntnu.qos.client.QoSClient;
import no.ntnu.qos.client.Sequencer;
import no.ntnu.qos.client.credentials.TokenManager;

import java.net.URI;

/**
 * @author Magnus Kirø
 */
public class SequencerImpl implements Sequencer {

    QoSClient qoSClient;
    TokenManager tokenManager;

    public SequencerImpl(QoSClient qoSClient) {
        this.qoSClient = qoSClient;
    }

    @Override
    public void setCredentials(String username, String role, String password) {
        tokenManager.setCredentials(username, role, password);
    }

    @Override
    public void sendData(String data, URI destination) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void sendData(DataObject dataObj) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void returnData(String data) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

	@Override
	public void setTokenManager(TokenManager tM) {
		// TODO Auto-generated method stub
		
	}
}
