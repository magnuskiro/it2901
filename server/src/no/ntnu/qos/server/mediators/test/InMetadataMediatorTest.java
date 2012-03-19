package no.ntnu.qos.server.mediators.test;

import static org.junit.Assert.*;

import javax.xml.namespace.QName;

import no.ntnu.qos.server.mediators.MediatorConstants;
import no.ntnu.qos.server.mediators.impl.InMetadataMediator;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.impl.llom.OMElementImpl;
import org.apache.axiom.soap.SOAPProcessingException;
import org.apache.axiom.soap.impl.llom.soap12.SOAP12Factory;
import org.apache.axis2.AxisFault;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.synapse.MessageContext;
import org.apache.synapse.config.SynapseConfiguration;
import org.apache.synapse.core.axis2.Axis2MessageContext;
import org.junit.BeforeClass;
import org.junit.Test;

public class InMetadataMediatorTest {

	private static MessageContext synCtx;
	private static InMetadataMediator imm;
	private static OMElement timeToLiveHeader;
	private static String from = "127.0.0.1";
	private static long ttl = 100;
	
	@BeforeClass
	public static void init() throws AxisFault, SOAPProcessingException{
		synCtx = new Axis2MessageContext(new org.apache.axis2.context.MessageContext(), 
				new SynapseConfiguration(),null);
		synCtx.setEnvelope(OMAbstractFactory.getSOAP12Factory().createSOAPEnvelope());
		synCtx.getEnvelope().addChild(OMAbstractFactory.getSOAP12Factory().createSOAPHeader());
		synCtx.setFrom(new EndpointReference(from));
		imm = new InMetadataMediator();
	}
	
	@Test
	public void mediate() {
		assertTrue("Test mediate 1",imm.mediate(synCtx));
		assertEquals("Test from 1",from, synCtx.getProperty(MediatorConstants.QOS_FROM_ADDR));
		assertFalse("Test useTTL 1",(Boolean)synCtx.getProperty(MediatorConstants.QOS_USE_TTL));
		timeToLiveHeader = new OMElementImpl(new QName(MediatorConstants.QOS_TTL), 
				synCtx.getEnvelope().getHeader(), new SOAP12Factory());
		timeToLiveHeader.setText(ttl+"");
		
		assertTrue("Test mediate 2",imm.mediate(synCtx));
		assertEquals("Test from 2",from, synCtx.getProperty(MediatorConstants.QOS_FROM_ADDR));
		assertTrue("Test useTTL 2",(Boolean)synCtx.getProperty(MediatorConstants.QOS_USE_TTL));
		assertEquals("Test TTL 2",ttl,synCtx.getProperty(MediatorConstants.QOS_TTL));		
	}

}
