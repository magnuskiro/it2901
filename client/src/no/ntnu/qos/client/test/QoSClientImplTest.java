package no.ntnu.qos.client.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.net.URI;
import java.net.URISyntaxException;

import no.ntnu.qos.client.DataListener;
import no.ntnu.qos.client.ReceiveObject;
import no.ntnu.qos.client.credentials.TokenManager;
import no.ntnu.qos.client.impl.QoSClientImpl;
import no.ntnu.qos.client.impl.SequencerImpl;

import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Magnus Kirø - magnuskiro@ underdusken.no/gmail.com - 21/03/12
 *
 * Tests the QoSClientImpl class.
 */
public class QoSClientImplTest{
    static SequencerImpl sequencer;
    static QoSClientImpl qoSClient;

    @BeforeClass
    public static void setup(){
        qoSClient = new QoSClientImpl("user", "role", "passwd", null);
        sequencer = (SequencerImpl) qoSClient.getSequencer();
    }

    @Test
    public void setCredentialsTest() {
        // input 
        String username, role, password;
        username = "newuser";
        role = "newrole";
        password = "newpassword";
        TokenManager TM = sequencer.getTokenManager();
        
        qoSClient.setCredentials(username, role, password);
        
        String[] creds = TM.getCredentials();
        
        assertEquals(username, creds[0]);
        assertEquals(role, creds[1]);
        assertEquals(password, creds[2]);
        
    }

    @Test
    public void sendDataTest() throws URISyntaxException {
        // input
        String data = "";
        URI destination = new URI("http//127.0.0.23/");
        
        // returned
        ReceiveObject receiveObject;

        receiveObject = qoSClient.sendData(data, destination);
        
        assertNotNull(receiveObject);
        

    }

    @Test
    public void addListenerTest() {
        DataListener dataListener = new DataListenerImpl();

        qoSClient.addListener(dataListener);
        
        assertTrue(qoSClient.getDataListenerList().contains(dataListener));
        

    }

    @Test
    public void removeListenerTest() {
        DataListener dataListener= new DataListenerImpl();

        qoSClient.addListener(dataListener);

        qoSClient.removeListener(dataListener);
        
        assertFalse(qoSClient.getDataListenerList().contains(dataListener));

    }

    @Test
    public void getSequencerTest() {
       assertNotNull(qoSClient.getSequencer());
    }
    
    
    private class DataListenerImpl implements DataListener{

		@Override
		public void newData(ReceiveObject recObj) {
			// TODO Auto-generated method stub
			
		}
    	
    }
}
