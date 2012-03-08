package no.ntnu.qos.server.store.test;

import static org.junit.Assert.*;

import no.ntnu.qos.server.mediators.MediatorConstants;
import no.ntnu.qos.server.store.PrioritizedMessageStore;

import org.apache.synapse.MessageContext;
import org.apache.synapse.config.SynapseConfiguration;
import org.apache.synapse.core.axis2.Axis2MessageContext;
import org.junit.Test;

public class PrioritizedMessageStoreTest {

	@Test
	public void testOffer() {
		MessageContext synCtx1 = new Axis2MessageContext(new org.apache.axis2.context.MessageContext(), new SynapseConfiguration(),null);
		MessageContext synCtx2 = new Axis2MessageContext(new org.apache.axis2.context.MessageContext(), new SynapseConfiguration(),null);
		MessageContext synCtx3 = new Axis2MessageContext(new org.apache.axis2.context.MessageContext(), new SynapseConfiguration(),null);
		synCtx1.setProperty(MediatorConstants.QOS_PRIORITY, 100);
		synCtx2.setProperty(MediatorConstants.QOS_PRIORITY, 10);
		synCtx3.setProperty(MediatorConstants.QOS_PRIORITY, 10);
		synCtx1.setProperty(MediatorConstants.QOS_TIME_ADDED, (long)1000);
		synCtx2.setProperty(MediatorConstants.QOS_TIME_ADDED, (long)10);
		synCtx3.setProperty(MediatorConstants.QOS_TIME_ADDED, (long)11);
		PrioritizedMessageStore pms = new PrioritizedMessageStore();
		pms.offer(synCtx1);
		pms.offer(synCtx2);
		pms.offer(synCtx3);
		assertEquals(synCtx1, pms.peek());
		assertEquals(synCtx1, pms.poll());
		assertEquals(synCtx2, pms.peek());
		assertEquals(synCtx2, pms.poll());
		assertEquals(synCtx3, pms.peek());
		assertEquals(synCtx3, pms.poll());
		assertNull(pms.peek());
		assertNull(pms.poll());
		
	}

}
