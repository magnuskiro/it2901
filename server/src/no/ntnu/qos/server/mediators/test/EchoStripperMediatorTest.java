package no.ntnu.qos.server.mediators.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import no.ntnu.qos.server.mediators.impl.EchoStripperMediator;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMXMLBuilderFactory;
import org.apache.axiom.om.OMXMLParserWrapper;
import org.apache.axiom.soap.SOAPBody;
import org.apache.axis2.AxisFault;
import org.apache.synapse.MessageContext;
import org.apache.synapse.config.SynapseConfiguration;
import org.apache.synapse.core.axis2.Axis2MessageContext;
import org.junit.BeforeClass;
import org.junit.Test;

public class EchoStripperMediatorTest {

	private static MessageContext synCtx;
	private static final String SAML_BODY = "SAMLinEcho.xml";
	
	@BeforeClass
	public static void setupMessageContext() throws FileNotFoundException, AxisFault{
		synCtx = new Axis2MessageContext(new org.apache.axis2.context.MessageContext(), 
				new SynapseConfiguration(), null);
		//Configure the message context:
		//Add SOAP envelope
		synCtx.setEnvelope(OMAbstractFactory.getSOAP12Factory().createSOAPEnvelope());
		//Add SOAP Header to envelope
		synCtx.getEnvelope().addChild(OMAbstractFactory.getSOAP12Factory().createSOAPHeader());
		//Create SOAP body
		SOAPBody sb = OMAbstractFactory.getSOAP12Factory().createSOAPBody();
		//Add Echo to SOAP body
		createSAMLBody(sb);
		//Add SOAP body to envelope
		synCtx.getEnvelope().addChild(sb);
		//System.out.println(synCtx.getEnvelope());
	}
	
	private static void createSAMLBody(SOAPBody sb) throws FileNotFoundException{
		InputStream in = new FileInputStream(new File(SAML_BODY));
		OMXMLParserWrapper builder = OMXMLBuilderFactory.createOMBuilder(in);
		OMElement ele = builder.getDocumentElement();
		sb.addChild(ele);
	}
	
	@Test
	public void test() {
		assertTrue("Mediation Success?",new EchoStripperMediator().mediate(synCtx));
		assertEquals("SAML is element in Body:","Assertion",synCtx.getEnvelope().getBody().getFirstElement().getLocalName());
		//System.out.println(synCtx.getEnvelope().toString());
	}

}
