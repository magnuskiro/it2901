package no.ntnu.qos.server.mediators.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import no.ntnu.qos.server.mediators.MediatorConstants;
import no.ntnu.qos.server.mediators.MetadataMediator;

import org.apache.synapse.MessageContext;
import org.apache.synapse.config.SynapseConfiguration;
import org.apache.synapse.core.axis2.Axis2MessageContext;
import org.apache.synapse.mediators.MediatorProperty;
import org.junit.Test;

public class MetadataMediatorTest {
	
	private static final String FILENAME = "/home/mahou/Documents/it2901/server/src/no/ntnu/qos/server/mediators/test/ppdtest.xml";
	@Test
	public void testEmptyFileNameMediat(){
		MessageContext synCtx = new Axis2MessageContext(new org.apache.axis2.context.MessageContext(), new SynapseConfiguration(),null);
		MetadataMediator mm = new MetadataMediator();
		MediatorProperty mp = new MediatorProperty();
		mp.setName(MediatorConstants.PRIORITY_DATA_FILENAME);
		mp.setValue("");
		mm.addProperty(mp);
		assertFalse(mm.mediate(synCtx));
	}

	@Test
	public void testMediate() {
		//meh.
		MessageContext synCtx = new Axis2MessageContext(new org.apache.axis2.context.MessageContext(), new SynapseConfiguration(),null);
		MetadataMediator mm = new MetadataMediator();
		synCtx.setProperty(MediatorConstants.QOS_CLIENT_ROLE, "testRole");
		synCtx.setProperty(MediatorConstants.QOS_SERVICE, "testService");
		assertEquals(synCtx.getProperty(MediatorConstants.QOS_PRIORITY), null);
		assertEquals(synCtx.getProperty(MediatorConstants.QOS_DIFFSERV), null);
		MediatorProperty mp = new MediatorProperty();
		mp.setName(MediatorConstants.PRIORITY_DATA_FILENAME);
		mp.setValue(FILENAME);
		mm.addProperty(mp);
		assertTrue(mm.mediate(synCtx));
		assertEquals(synCtx.getProperty(MediatorConstants.QOS_PRIORITY), 123);
		assertEquals(synCtx.getProperty(MediatorConstants.QOS_DIFFSERV), 16);
		
		//checking default values.
		synCtx.setProperty(MediatorConstants.QOS_CLIENT_ROLE, "FAIL");
		assertTrue(mm.mediate(synCtx));
		assertEquals(synCtx.getProperty(MediatorConstants.QOS_PRIORITY), 321);
		assertEquals(synCtx.getProperty(MediatorConstants.QOS_DIFFSERV), 8);
		
		
	}
	

}
