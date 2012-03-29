package no.ntnu.qos.client.test;

import no.ntnu.qos.client.ExceptionHandler;
import no.ntnu.qos.client.ReceiveObject;
import no.ntnu.qos.client.Sequencer;
import no.ntnu.qos.client.credentials.TokenManager;
import no.ntnu.qos.client.impl.QoSClientImpl;
import no.ntnu.qos.client.impl.SequencerImpl;
import org.junit.BeforeClass;
import org.junit.Test;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * @author Magnus Kirø - magnuskiro@ underdusken.no/gmail.com - 21/03/12
 */
public class SequencerTest {
    static Sequencer sequencer;
    static ExceptionHandler exceptionHandler;

    @BeforeClass
    public static void setup(){
    	//TODO: use a proper ExceptionHandler, test will probably break until it is done
    	
        sequencer = new SequencerImpl(new QoSClientImpl("", "", "", null), "user", "role", "password", exceptionHandler);
    }

    @Test
    public void setCredentials() {
        // String username, String role, String password
        //todo - test this method.
    }

    @Test
    public void sendDataTest() throws URISyntaxException {
        // input
        String data = "";
        URI destination = new URI("http//127.0.0.23/");

        // returned
        ReceiveObject receiveObject;

        //todo - test this method.
    }

    @Test
    public void sendData() {
        // DataObject dataObj
        //todo - test this method.
    }

    @Test
    public void returnData(String data) {
        //todo - test this method.
    }

    @Test
    public void setTokenManager(TokenManager tM) {
        //todo - test this method.
    }
}
