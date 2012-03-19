package no.ntnu.qos.server.mediators.test;

import static org.junit.Assert.*;

import javax.xml.namespace.QName;

import no.ntnu.qos.server.mediators.MediatorConstants;
import no.ntnu.qos.server.mediators.impl.SoapPriorityMediator;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.soap.SOAPProcessingException;
import org.apache.axis2.AxisFault;
import org.apache.synapse.MessageContext;
import org.apache.synapse.config.SynapseConfiguration;
import org.apache.synapse.core.axis2.Axis2MessageContext;
import org.junit.BeforeClass;
import org.junit.Test;

public class SoapPriorityMediatorTest {

	private static MessageContext synCtx;
	private static int PRI = 123;
	private static int DIF = 16;
	private static SoapPriorityMediator spm;
	
	
	@BeforeClass
	public static void init() throws AxisFault, SOAPProcessingException{
		synCtx = new Axis2MessageContext(new org.apache.axis2.context.MessageContext(), new SynapseConfiguration(), null);
		synCtx.setEnvelope(OMAbstractFactory.getSOAP12Factory().createSOAPEnvelope());
		spm = new SoapPriorityMediator();
		synCtx.setProperty(MediatorConstants.QOS_PRIORITY, PRI);
		synCtx.setProperty(MediatorConstants.QOS_DIFFSERV, DIF);
		
	}
	
	@Test
	public void testMediateImpl() {
		assertTrue("Test mediate",spm.mediate(synCtx));
		assertEquals("Test Priority",PRI+"", synCtx.getEnvelope().getHeader().getFirstChildWithName(
				new QName(MediatorConstants.QOS_PRIORITY)).getText());
		assertEquals("Test Diffserv",DIF+"", synCtx.getEnvelope().getHeader().getFirstChildWithName(
				new QName(MediatorConstants.QOS_DIFFSERV)).getText());
	}

}
