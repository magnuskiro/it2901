package no.ntnu.qos.client.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import no.ntnu.qos.client.ExceptionHandler;
import no.ntnu.qos.client.ReceiveObject;
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
public class SequencerImplTest {
    static SequencerImpl sequencer;
    static ExceptionHandler exceptionHandler;

    @BeforeClass
    public static void setup(){
    	//TODO: use a proper ExceptionHandler, test will probably break until it is done
    	
        sequencer = new SequencerImpl(new QoSClientImpl("", "", "", null), "user", "role", "password", exceptionHandler);
    }

    @Test
    public void setCredentials() {
        // String username, String role, String password
    	TokenManager TM = sequencer.getTokenManager();
    	sequencer.setCredentials("newuser", "newrole", "newpassword");
    	
    	String[] creds = TM.getCredentials();
    	
    	assertEquals("newuser", creds[0]);
    	assertEquals("newrole", creds[1]);
    	assertEquals("newpassword", creds[2]);

    }

    @Test
    public void sendDataTest() throws URISyntaxException {
        // input
        String data = "";
        URI destination = new URI("http//127.0.0.23/");

        // returned
        ReceiveObject receiveObject;

        receiveObject = sequencer.sendData(data, destination);
        
        assertNotNull(receiveObject);

    }

    @Test
    public void sendData() {
        // DataObject dataObj
        //TODO - test this method.
        assertFalse(true); // fails to remind us that the test is not complete. 

    }

    @Test
    public void returnData() {
        //TODO - test this method.
        assertFalse(true); // fails to remind us that the test is not complete. 

    }

    
}
