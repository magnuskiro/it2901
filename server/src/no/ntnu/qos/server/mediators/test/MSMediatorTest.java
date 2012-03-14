package no.ntnu.qos.server.mediators.test;

import static org.junit.Assert.*;

import no.ntnu.qos.server.mediators.MediatorConstants;
import no.ntnu.qos.server.mediators.impl.MSMediator;

import org.apache.axis2.addressing.EndpointReference;
import org.apache.synapse.MessageContext;
import org.apache.synapse.config.SynapseConfiguration;
import org.apache.synapse.core.axis2.Axis2MessageContext;
import org.junit.BeforeClass;
import org.junit.Test;

public class MSMediatorTest {

	private static MessageContext synCtx;
	private static MSMediator msm = new MSMediator();

	@BeforeClass
	public static void setupMessageContext(){
		synCtx = new Axis2MessageContext(new org.apache.axis2.context.MessageContext(), 
				new SynapseConfiguration(), null);
		synCtx.setTo(new EndpointReference("http://125.50.50.73:8280/services/EchoService"));
	}
	
	@Test
	public void testMediate() {
		assertNull(synCtx.getProperty(MediatorConstants.QOS_BANDWIDTH));
		assertNull(synCtx.getProperty(MediatorConstants.QOS_LAST_TR));
		assertTrue(msm.mediate(synCtx));
		assertEquals(123.5, (Double)synCtx.getProperty(MediatorConstants.QOS_BANDWIDTH), 0);
		assertEquals("bob", (String)synCtx.getProperty(MediatorConstants.QOS_LAST_TR));
		
		synCtx.setTo(new EndpointReference("https://127.0.0.1:8080/service/kake"));
		assertTrue(msm.mediate(synCtx));
		assertEquals(-1, (Double)synCtx.getProperty(MediatorConstants.QOS_BANDWIDTH),0);
		assertEquals("", (String)synCtx.getProperty(MediatorConstants.QOS_LAST_TR));

	}

}
