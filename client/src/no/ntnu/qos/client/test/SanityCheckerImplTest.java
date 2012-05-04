package no.ntnu.qos.client.test;

import no.ntnu.qos.client.impl.SanityCheckerImpl;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Magnus Kirø - magnuskiro@ gmail.com/underdusken.no - 29/03/12
 */
public class SanityCheckerImplTest {
    private static String[] validUserNames;
    private static String[] validPasswords;
    private static String[] validRoles;
    private static String[] invalidUserNames;
    private static String[] invalidPasswords;
    private static String[] invalidRoles;

    private static SanityCheckerImpl sanityChecker;

    @Before
    public void setUp(){
        validUserNames = new String[] {"username", "mkyong34", "mkyong_2002", "mkyong-2002" ,"mk3-4_yong"};
        invalidUserNames = new String[] {"mk", "mk@yong", "mkyong123456789_-"};
        validPasswords = new String[] {"password", "password1", "abcdefg_-","kake_moms" ,"-keke_ke-"};
        invalidPasswords = new String[] {"mk", "mk@yong", "passwordHasIllegalLengthNow"};
        validRoles = new String[] {"Role", "mkyong34", "mkyong_2002", "mkyong2002" ,"mk34_yong"};
        invalidRoles = new String[] {"mk", "mk@yong", "mkyong123456789_-", "mkyong123456789822"};

        sanityChecker = new SanityCheckerImpl();
    }

    @After
    public void tearDown() {

    }

    @Test
    public void dataObjectIsSaneTest() {

    }

    @Test
    public void credentialsIsSaneTest() {
        // test true
        assertTrue(sanityChecker.isSane("userName", "password", "Role"));
        // test false
        assertFalse(sanityChecker.isSane("", "", ""));
    }

    @Test
    public void validUserTest(){
        for(String s:validUserNames){
            assertTrue(sanityChecker.validUser(s));
        }
        for(String s:invalidUserNames){
            assertFalse(sanityChecker.validUser(s));
        }
    }

    @Test
    public void validPasswordTest(){
        for(String s:validPasswords){
            assertTrue(sanityChecker.validPassword(s));
        }
        for(String s:invalidPasswords){
            assertFalse(sanityChecker.validPassword(s));
        }
    }

    @Test
    public void validRoleTest(){
        for(String s:validRoles){
            assertTrue(sanityChecker.validRole(s));
        }
        for(String s:invalidRoles){
            assertFalse(sanityChecker.validRole(s));
        }
    }
    
}
