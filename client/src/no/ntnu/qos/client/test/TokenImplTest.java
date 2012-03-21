package no.ntnu.qos.client.test;

import no.ntnu.qos.client.credentials.Token;
import no.ntnu.qos.client.credentials.impl.TokenImpl;
import org.junit.BeforeClass;
import org.junit.Test;

import java.net.URI;
import java.net.URISyntaxException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Magnus Kirø - magnuskiro@ underdusken.no/gmail.com - 21/03/12
 */
public class TokenImplTest {
    static String token, token2;
    static URI destination, destination2;
    static long validUntil, validUntil2;
    static Token testToken, testToken2;

    @BeforeClass
    public static void setup() throws URISyntaxException {

        token = "token - blah blah";
        token2 = "This is the 2. token";
        destination = new URI("http://127.0.0.25/");
        destination2 = new URI("http://127.0.0.26/");
        validUntil = System.currentTimeMillis()+3600000;
        validUntil2 = System.currentTimeMillis()-35000;
        testToken = new TokenImpl(token, validUntil, destination);
        testToken2 = new TokenImpl(token, validUntil2, destination2);

    }

    @Test
    public void isValidTokenTest() {
        assertTrue(testToken.isValid());
        assertFalse(testToken2.isValid());
    }

    @Test
    public void getDestinationTest(){
        assertEquals("Destinations Match", destination, testToken.getDestination());
        assertEquals("Destinations Match", destination2, testToken2.getDestination());
    }

    @Test
    public void getXMLTest(){
        assertEquals("Tokens comparison", token, testToken.getXML());
        assertEquals("Tokens comparison", token2, testToken2.getXML());
    }

    @Test
    public void getDiffservTest(){
        // Todo write the test
    }

    @Test
    public void getPriorityTest(){
        // Todo write the test
    }

}
