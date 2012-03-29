package no.ntnu.qos.client.credentials.test;

import static org.junit.Assert.*;

import java.net.URI;
import no.ntnu.qos.client.DataObject;
import no.ntnu.qos.client.ExceptionHandler;
import no.ntnu.qos.client.QoSClient;
import no.ntnu.qos.client.Sequencer;
import no.ntnu.qos.client.credentials.TokenManager;
import no.ntnu.qos.client.credentials.impl.TokenManagerImpl;
import no.ntnu.qos.client.impl.QoSClientImpl;
import org.junit.BeforeClass;
import org.junit.Test;

import java.net.URISyntaxException;


/**
 * @author Magnus Kirø - magnuskiro@ underdusken.no/gmail.com - 21/03/12
 */
public class TokenManagerImplTest {
    static String user, role, password, soapFromUser;
    static URI destination, destination2;
    static TokenManager tokenManager;
    static Sequencer sequencer;
    static QoSClient client;
    static DataObject dataObject;
    static DataObject dataObject2;
    static ExceptionHandler exceptionHandler;


    @BeforeClass
    public static void setup() throws URISyntaxException {
        user = "user";
        role = "role";
        password = "password";
        soapFromUser = "Soap test data";
        destination = new URI("http://127.0.0.25/");
        destination2 = new URI("http://127.0.0.24/");

        client = new QoSClientImpl(user, role, password, null);
        sequencer = client.getSequencer();
        tokenManager = new TokenManagerImpl(user, role, password);
    	//TODO: use a proper ExceptionHandler, test will probably break until it is done
        dataObject = new DataObject(sequencer, soapFromUser, destination, exceptionHandler);
        dataObject2 = new DataObject(sequencer, soapFromUser, destination2, exceptionHandler);
        tokenManager.setTokenInDataObject(dataObject);
        tokenManager.setTokenInDataObject(dataObject2);
    }

    @Test
    public void getTokenTest(){
        tokenManager.setTokenInDataObject(dataObject);
        tokenManager.setTokenInDataObject(dataObject2);
        assertNotNull(tokenManager.getToken(dataObject));
        assertNotNull(tokenManager.getToken(dataObject2));
        assertNotSame(tokenManager.getToken(dataObject), tokenManager.getToken(dataObject2));

    }

    @Test
    public void constructorCredentialsTest(){
        TokenManager tokenManager1 = new TokenManagerImpl(user, role, password);
        String[] credentials = tokenManager1.getCredentials();
        assertEquals("Username is correct?", "user", credentials[0]); // [0] is the username
        assertEquals("Role is correct?", "role", credentials[1]); // [1] is the role
        assertEquals("Password is correct?", "password", credentials[2]); // [2] is the password
    }

    @Test
    public void setCredentialsTest(){
        tokenManager.setCredentials("newUser", "newRole", "newPassword");
        String[] credentials = tokenManager.getCredentials();
        assertEquals("Changed the username", "newUser", credentials[0]); // [0] is the username
        assertEquals("Changed the role", "newRole", credentials[1]); // [1] is the role
        assertEquals("changed the password", "newPassword", credentials[2]); // [2] is the password
    }

    @Test
    public void setTokenInDataObjectTest(){
        // Todo change the checked object when the samlCommunicatorImpl is complete so we know that the Tokens are created correctly.
        assertEquals("Set token in DataObject", "token", dataObject.getSamlToken().getXML());
        assertEquals("Set token in DataObject", destination, dataObject.getSamlToken().getDestination());
    }


}
