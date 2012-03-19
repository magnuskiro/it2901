package no.ntnu.qos.ms.test;

import static org.junit.Assert.assertEquals;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Scanner;

import no.ntnu.qos.ms.MSCommunicator;
import no.ntnu.qos.ms.RoutingInfo;
import no.ntnu.qos.ms.impl.MSCommunicatorImpl;

import org.junit.Test;

public class MSCommunicatorImplTest {

	private static String FILENAME;

	public MSCommunicatorImplTest() {
		if(FILENAME == null){
			Scanner in = new Scanner(System.in);
			System.out.println("Where is ppdtest.xml:");
			FILENAME = in.nextLine();
		}
	}
	@Test
	public void test() throws URISyntaxException {
		MSCommunicator mscomm = new MSCommunicatorImpl(FILENAME);
		RoutingInfo ri = mscomm.getRoutingInfo(new URI("125.50.50.73"));
		assertEquals(123.5, ri.getBandwidth(),0);
		assertEquals("bob", ri.getLastTR());
		ri = mscomm.getRoutingInfo(new URI("127.0.0.1"));
		assertEquals(-1.0, ri.getBandwidth(),0);
		assertEquals("", ri.getLastTR());
	}

}
