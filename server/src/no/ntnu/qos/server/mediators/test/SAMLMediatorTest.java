package no.ntnu.qos.server.mediators.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import no.ntnu.qos.server.mediators.MediatorConstants;
import no.ntnu.qos.server.mediators.impl.SAMLMediator;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMXMLBuilderFactory;
import org.apache.axiom.om.OMXMLParserWrapper;
import org.apache.axiom.soap.SOAPBody;
import org.apache.axiom.soap.SOAPProcessingException;
import org.apache.axis2.AxisFault;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.synapse.MessageContext;
import org.apache.synapse.config.SynapseConfiguration;
import org.apache.synapse.core.axis2.Axis2MessageContext;
import org.junit.BeforeClass;
import org.junit.Test;

public class SAMLMediatorTest {

	private static MessageContext synCtx;
	private static final String CLIENT_ROLE = "clientRole1";
	private static final String SERVICE = "127.0.0.1";
	private static final String SAML_BODY = "SAMLinSOAP.xml";
	
	@BeforeClass
	public static void setupMessageContext() throws FileNotFoundException, AxisFault{
		synCtx = new Axis2MessageContext(new org.apache.axis2.context.MessageContext(), 
				new SynapseConfiguration(), null);
		//Configure the message context:
		
		//Add Endpoint
		synCtx.setTo(new EndpointReference(SERVICE));
		//Add SOAP envelope
		synCtx.setEnvelope(OMAbstractFactory.getSOAP12Factory().createSOAPEnvelope());
		//Add SOAP Header to envelope
		synCtx.getEnvelope().addChild(OMAbstractFactory.getSOAP12Factory().createSOAPHeader());
		//Create SOAP body
		SOAPBody sb = OMAbstractFactory.getSOAP12Factory().createSOAPBody();
		//Add SAML to SOAP body
		createSAMLBody(sb);
		//Add SOAP body to envelope
		synCtx.getEnvelope().addChild(sb);
		//System.out.println(synCtx.getEnvelope());
	}
	
	private static void createSAMLBody(SOAPBody sb) throws FileNotFoundException{
		InputStream in = new FileInputStream(new File(SAML_BODY));
		OMXMLParserWrapper builder = OMXMLBuilderFactory.createOMBuilder(in);
		OMElement ele = builder.getDocumentElement();
		//System.out.println(ele);
		sb.addChild(ele);
	}

	@Test
	public void testMediate() {
		SAMLMediator sm = new SAMLMediator();
		assertTrue("Mediate synapse context", sm.mediate(synCtx));
		assertEquals("Service", SERVICE, synCtx.getProperty(MediatorConstants.QOS_SERVICE));
		assertEquals("Client role", CLIENT_ROLE, synCtx.getProperty(MediatorConstants.QOS_CLIENT_ROLE));
	}
	
	@Test
	public void testEmptySAMLMediate() throws AxisFault, SOAPProcessingException{
		//Configuration:
		MessageContext synCtx1 = new Axis2MessageContext(new org.apache.axis2.context.MessageContext(), 
				new SynapseConfiguration(), null);
		synCtx1.setEnvelope(OMAbstractFactory.getSOAP12Factory().getDefaultEnvelope());
		synCtx1.setTo(new EndpointReference(SERVICE));
		
		//Testing:
		SAMLMediator sm = new SAMLMediator();
		assertFalse("Mediate without SAML in SOAP body", sm.mediate(synCtx1));
		assertNull("Client is not set in property", synCtx1.getProperty(MediatorConstants.QOS_CLIENT_ROLE));
		assertNull("Service is not set in property", synCtx1.getProperty(MediatorConstants.QOS_SERVICE));
	}

}
