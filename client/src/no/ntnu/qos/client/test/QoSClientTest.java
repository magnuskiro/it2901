package no.ntnu.qos.client.test;

import static org.junit.Assert.assertFalse;
import no.ntnu.qos.client.DataListener;
import no.ntnu.qos.client.QoSClient;
import no.ntnu.qos.client.ReceiveObject;
import no.ntnu.qos.client.Sequencer;
import no.ntnu.qos.client.impl.QoSClientImpl;
import org.junit.BeforeClass;
import org.junit.Test;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * @author Magnus Kirø - magnuskiro@ underdusken.no/gmail.com - 21/03/12
 *
 * Tests the QoSClientImpl class.
 */
public class QoSClientTest{
    static Sequencer sequencer;
    static QoSClient qoSClient;

    @BeforeClass
    public static void setup(){
        qoSClient = new QoSClientImpl("user", "role", "passwd", null);
        sequencer = qoSClient.getSequencer();
    }

    @Test
    public void setCredentialsTest() {
        // input 
        String username, role, password;
        username = "user";
        role = "role";
        password = "password";
        //TODO - test this method.
        assertFalse(true); // fails to remind us that the test is not complete. 

    }

    @Test
    public void sendDataTest() throws URISyntaxException {
        // input
        String data = "";
        URI destination = new URI("http//127.0.0.23/");
        
        // returned
        ReceiveObject receiveObject;

        //TODO - test this method.
        assertFalse(true); // fails to remind us that the test is not complete. 

    }

    @Test
    public void addListenerTest() {
        DataListener dataListener;

        //TODO - test this method.
        assertFalse(true); // fails to remind us that the test is not complete. 

    }

    @Test
    public void removeListenerTest() {
        DataListener dataListener;

        //TODO - test this method.
        assertFalse(true); // fails to remind us that the test is not complete. 

    }

    @Test
    public void getSequencerTest() {
        //TODO - test this method.
        assertFalse(true); // fails to remind us that the test is not complete. 

    }
}
