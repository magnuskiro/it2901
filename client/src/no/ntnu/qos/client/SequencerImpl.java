package no.ntnu.qos.client;

import java.net.URI;

/**
 * @author Magnus Kirø
 */
public class SequencerImpl implements Sequencer{

    QoSClient qoSClient;

    public SequencerImpl(QoSClient qoSClient) {
        this.qoSClient = qoSClient;
    }

    @Override
    public void setCredentials(String username, String role, String password) {
        //To change body of implemented methods use File | Settings | File Templates.
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
}
