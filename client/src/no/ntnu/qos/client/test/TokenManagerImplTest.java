package no.ntnu.qos.client.test;

import static org.junit.Assert.*;

import java.net.URI;
import no.ntnu.qos.client.DataObject;
import no.ntnu.qos.client.QoSClient;
import no.ntnu.qos.client.Sequencer;
import no.ntnu.qos.client.credentials.TokenManager;
import no.ntnu.qos.client.credentials.impl.TokenManagerImpl;
import no.ntnu.qos.client.impl.QoSClientImpl;
import no.ntnu.qos.client.impl.SequencerImpl;
import org.junit.BeforeClass;
import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URISyntaxException;


/**
 * @author Magnus Kirø - magnuskiro@ underdusken.no/gmail.com - 21/03/12
 */
public class TokenManagerImplTest {
    static String user, role, password, soapFromUser;
    static URI destination;
    static TokenManager tokenManager;
    static Sequencer sequencer;
    static QoSClient client;

    @BeforeClass
    public static void setup() throws URISyntaxException {
        user = "user";
        role = "role";
        password = "password";
        soapFromUser = "Soap test data";
        destination = new URI("http://127.0.0.25/");
        client = new QoSClientImpl(user, role, password, null);
        tokenManager = new TokenManagerImpl(user, role, password);
        sequencer = new SequencerImpl(client, user, role, password);
    }

    @Test
    public void constructorCredentialsTest(){
        String[] credentials = tokenManager.getCredentials();
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
    public void getTokenTest(){

    }

    @Test
    public void setTokenInDataObjectTest(){
        DataObject dataObject = new DataObject(sequencer, soapFromUser, destination);
        tokenManager.setTokenInDataObject(dataObject);

    }


}
