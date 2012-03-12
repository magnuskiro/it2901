package no.ntnu.qos.server.mediators.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import no.ntnu.qos.server.mediators.MediatorConstants;
import no.ntnu.qos.server.mediators.SAMLMediator;

import org.apache.axis2.addressing.EndpointReference;
import org.apache.synapse.MessageContext;
import org.apache.synapse.config.SynapseConfiguration;
import org.apache.synapse.core.axis2.Axis2MessageContext;
import org.junit.BeforeClass;
import org.junit.Test;

public class SAMLMediatorTest {

	private static MessageContext synCtx;
	private static final String ADDRESS = "127.0.0.1";
	private static final String SERVICE = "serviceName1";

	@BeforeClass
	public static void setupMessageContext(){
		synCtx = new Axis2MessageContext(new org.apache.axis2.context.MessageContext(), 
				new SynapseConfiguration(), null);
		//Configure the message context
		synCtx.setTo(new EndpointReference(ADDRESS));
	}

	@Test
	public void testMediate() {
		SAMLMediator sm = new SAMLMediator();
		assertTrue("Mediate synapse context", sm.mediate(synCtx));
		assertEquals("Client role", ADDRESS, synCtx.getProperty(MediatorConstants.QOS_CLIENT_ROLE));
		assertEquals("Service", SERVICE, synCtx.getProperty(MediatorConstants.QOS_SERVICE));
	}

}
