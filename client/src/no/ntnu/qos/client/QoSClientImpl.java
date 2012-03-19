package no.ntnu.qos.client;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Magnus Kirø
 *
 * The Implementation of QoSClient interface.
 */
public class QoSClientImpl implements QoSClient{

    private Sequencer sequencer;
    List<DataListener> dataListenerList = new ArrayList<DataListener>();

    public QoSClientImpl(Sequencer sequencer){
        setSequencer(sequencer);
    }

    @Override
    public void setCredentials(String username, String role, String password) {
        this.sequencer.setCredentials(username, role, password);
    }

    @Override
    public ReceiveObject sendData(String data, URI destination) {
        sequencer.sendData(data, destination);
        return null;
    }

    @Override
    public void addListener(DataListener listener) {
        this.dataListenerList.add(listener);
    }

    @Override
    public void removeListener(DataListener listener) {
        this.dataListenerList.remove(listener);
    }

    public void setSequencer(Sequencer sequencer) {
        this.sequencer = sequencer;
    }
}
