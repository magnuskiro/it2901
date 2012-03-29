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
    static long validUntil, validUntil2, validUntil3, validUntil4, validUntil5;
    static Token testToken, testToken2, testToken3, testToken4, testToken5;

    @BeforeClass
    public static void setup() throws URISyntaxException {

        token = "token - blah blah";
        token2 = "This is the 2. token";
        destination = new URI("http://127.0.0.25/");
        destination2 = new URI("http://127.0.0.26/");
        validUntil = System.currentTimeMillis()+3600000;
        validUntil2 = System.currentTimeMillis()-35000;
        validUntil3 = System.currentTimeMillis()+30000;
        validUntil4 = System.currentTimeMillis()+29000;
        validUntil5 = System.currentTimeMillis()+31000;
        testToken = new TokenImpl(token, validUntil, destination);
        testToken2 = new TokenImpl(token2, validUntil2, destination2);
        testToken3 = new TokenImpl(token, validUntil3, destination);
        testToken4 = new TokenImpl(token, validUntil4, destination);
        testToken5 = new TokenImpl(token, validUntil5, destination);
    }

    @Test
    public void isValidTokenTest() {
        assertTrue(testToken.isValid());
        assertFalse(testToken2.isValid());
        assertFalse(testToken3.isValid());
        assertFalse(testToken4.isValid());
        assertTrue(testToken5.isValid());
    }

    @Test
    public void setExpirationTest(){
        // tests if a token becomes invalid after the change of the expirationTimeBuffer.
        Token testExpirationToken = new TokenImpl(token, System.currentTimeMillis()+31000, destination);
        assertTrue(testExpirationToken.isValid());
        testExpirationToken.setExpirationTimeBuffer(32000L);
        assertEquals(testExpirationToken.getExpirationTimeBuffer(), 32000L);
        assertFalse(testExpirationToken.isValid());

        // tests if a token becomes valid after changing the expirationTimeBuffer.
        Token testExpirationToken2 = new TokenImpl(token, System.currentTimeMillis()+29000, destination);
        assertFalse(testExpirationToken2.isValid());
        testExpirationToken2.setExpirationTimeBuffer(28000L);
        assertEquals(testExpirationToken2.getExpirationTimeBuffer(), 28000L);
        assertTrue(testExpirationToken2.isValid());
    }

    @Test
    public void getDestinationTest(){
        assertEquals("Destinations Match", destination, testToken.getDestination());
        assertEquals("Destinations Match", destination2, testToken2.getDestination());
    }

    @Test
    public void getXMLTest(){
        assertEquals("Token comparison", token, testToken.getXML());
        assertEquals("Token2 comparison", token2, testToken2.getXML());
    }

    @Test
    public void getDiffServTest(){
        // Todo write the test
    }

    @Test
    public void getPriorityTest(){
        // Todo write the test
    }

}
