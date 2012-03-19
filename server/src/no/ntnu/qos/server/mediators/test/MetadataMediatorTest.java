package no.ntnu.qos.server.mediators.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import no.ntnu.qos.server.mediators.MediatorConstants;
import no.ntnu.qos.server.mediators.impl.OutMetadataMediator;

import org.apache.axiom.soap.SOAPProcessingException;
import org.apache.axis2.AxisFault;
import org.apache.synapse.MessageContext;
import org.apache.synapse.config.SynapseConfiguration;
import org.apache.synapse.core.axis2.Axis2MessageContext;
import org.junit.Test;

public class MetadataMediatorTest {
	
	private static final String FILENAME = "ppdtest.xml";
	
	@Test
	public void testEmptyFileNameMediate(){
		MessageContext synCtx = new Axis2MessageContext(new org.apache.axis2.context.MessageContext(), new SynapseConfiguration(),null);
		OutMetadataMediator mm = new OutMetadataMediator();
		mm.setPpdFilename("");
		assertFalse(mm.mediate(synCtx));
	}

	@Test
	public void testMediate() throws AxisFault, SOAPProcessingException {
		//meh.
		MessageContext synCtx = new Axis2MessageContext(new org.apache.axis2.context.MessageContext(), 
				new SynapseConfiguration(),null);
		OutMetadataMediator mm = new OutMetadataMediator();
		synCtx.setProperty(MediatorConstants.QOS_CLIENT_ROLE, "testRole");
		synCtx.setProperty(MediatorConstants.QOS_SERVICE, "testService");
		assertEquals(synCtx.getProperty(MediatorConstants.QOS_PRIORITY), null);
		assertEquals(synCtx.getProperty(MediatorConstants.QOS_DIFFSERV), null);

		mm.setPpdFilename(FILENAME);
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
