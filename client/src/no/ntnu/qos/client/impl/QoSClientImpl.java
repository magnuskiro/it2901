package no.ntnu.qos.client.impl;

import no.ntnu.qos.client.*;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Magnus Kirø
 *
 * The Implementation of QoSClient interface.
 */
public class QoSClientImpl implements QoSClient {

    private Sequencer sequencer;
    List<DataListener> dataListenerList = new ArrayList<DataListener>();

    public QoSClientImpl(String userName, String role, String password, ExceptionHandler exceptionHandler){
    	ConfigManager.initLog();
        sequencer = new SequencerImpl(this, userName, role, password, exceptionHandler);
    }

    @Override
    public void setCredentials(String username, String role, String password) {
        this.sequencer.setCredentials(username, role, password);
    }

    @Override
    public synchronized ReceiveObject sendData(String data, URI destination) {
       return sequencer.sendData(data, destination);
    }

    @Override
    public void addListener(DataListener listener) {
        this.dataListenerList.add(listener);
    }

    @Override
    public void removeListener(DataListener listener) {
        this.dataListenerList.remove(listener);
    }

    public List<DataListener> getDataListenerList() {
        return dataListenerList;
    }

    @Override
    public Sequencer getSequencer() {
        return this.sequencer;
    }

	@Override
	public void receive(ReceiveObject recObj) {
		fireNewData(recObj);		
	}

	/**
	 * Informs the listeners of a new reply 
	 * @param recObj
	 */
	private void fireNewData(ReceiveObject recObj) {
		for(DataListener i : dataListenerList){
			i.newData(recObj);
		}
	}

	@Override
	public void setLogging(boolean on) {
		ConfigManager.setLogging(on);
	}

	@Override
	public void setLogToFile(boolean on) {
		ConfigManager.setLogToFile(on);
	}

	@Override
	public void setLogToConsole(boolean on) {
		ConfigManager.setLogToConsole(on);
	}
}
