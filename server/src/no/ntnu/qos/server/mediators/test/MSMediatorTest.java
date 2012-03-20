package no.ntnu.qos.server.mediators.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import no.ntnu.qos.server.mediators.MediatorConstants;
import no.ntnu.qos.server.mediators.impl.MSMediator;

import org.apache.synapse.MessageContext;
import org.apache.synapse.config.SynapseConfiguration;
import org.apache.synapse.core.axis2.Axis2MessageContext;
import org.junit.BeforeClass;
import org.junit.Test;

public class MSMediatorTest {

	private static MessageContext synCtx;
	private static final MSMediator msm = new MSMediator();
	private static final String ADDRESS = "125.50.50.73";
	private static final String ADDRESS_LOCAL = "127.0.0.1";

	@BeforeClass
	public static void setupMessageContext(){
		synCtx = new Axis2MessageContext(new org.apache.axis2.context.MessageContext(), 
				new SynapseConfiguration(), null);
		synCtx.setProperty(MediatorConstants.QOS_FROM_ADDR, ADDRESS);
	}
	
	@Test
	public void testMediate() {
		assertNull(synCtx.getProperty(MediatorConstants.QOS_BANDWIDTH));
		assertNull(synCtx.getProperty(MediatorConstants.QOS_LAST_TR));
		assertTrue(msm.mediate(synCtx));
		assertEquals(123.5, (Double)synCtx.getProperty(MediatorConstants.QOS_BANDWIDTH), 0);
		assertEquals("bob", (String)synCtx.getProperty(MediatorConstants.QOS_LAST_TR));
		
		synCtx.setProperty(MediatorConstants.QOS_FROM_ADDR, ADDRESS_LOCAL);
		assertTrue(msm.mediate(synCtx));
		assertEquals(-1, (Double)synCtx.getProperty(MediatorConstants.QOS_BANDWIDTH),0);
		assertEquals("", (String)synCtx.getProperty(MediatorConstants.QOS_LAST_TR));

	}

}