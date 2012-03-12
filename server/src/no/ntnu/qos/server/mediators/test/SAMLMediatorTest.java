package no.ntnu.qos.server.mediators.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;
import no.ntnu.qos.server.mediators.SAMLMediator;

import org.apache.axiom.soap.SOAPEnvelope;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.synapse.MessageContext;
import org.apache.synapse.config.SynapseConfiguration;
import org.apache.synapse.core.axis2.Axis2MessageContext;
import org.junit.Test;

public class SAMLMediatorTest {

	@Test
	public void testMediate() {
		MessageContext synCtx = new Axis2MessageContext(new org.apache.axis2.context.MessageContext(), 
				new SynapseConfiguration(), null);
		SAMLMediator sm = new SAMLMediator();
		assertFalse(sm.mediate(synCtx));
		synCtx.setTo(new EndpointReference("127.0.0.1"));
		SOAPEnvelope sh = synCtx.getEnvelope();
		fail("Not yet implemented");
	}

}
