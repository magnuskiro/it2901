package no.ntnu.qos.server.mediators.test;

import static org.junit.Assert.assertTrue;

import java.io.IOException;

import no.ntnu.qos.server.mediators.MediatorConstants;
import no.ntnu.qos.server.mediators.impl.MSMediator;
import no.ntnu.qos.server.mediators.impl.MetadataMediator;
import no.ntnu.qos.server.mediators.impl.ThrottleMediator;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.soap.SOAPProcessingException;
import org.apache.axis2.AxisFault;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.synapse.MessageContext;
import org.apache.synapse.config.SynapseConfiguration;
import org.apache.synapse.core.axis2.Axis2MessageContext;
import org.junit.BeforeClass;
import org.junit.Test;

public class ThrottleMediatorTest {

	private static MessageContext synCtx;
	private static final MSMediator msm = new MSMediator();
	private static final MetadataMediator mm = new MetadataMediator();
	private static final ThrottleMediator tm = new ThrottleMediator();
	private static final String FILENAME = "ppdtest.xml";
	private static final String ADDRESS = "http://125.50.50.73:8280/services/EchoService";
	
	@BeforeClass
	public static void setupMessageContext() throws AxisFault, SOAPProcessingException{
		synCtx = new Axis2MessageContext(new org.apache.axis2.context.MessageContext(), 
				new SynapseConfiguration(), null);
		synCtx.setTo(new EndpointReference(ADDRESS));

		mm.setPpdFilename(FILENAME);
		synCtx.setProperty(MediatorConstants.QOS_CLIENT_ROLE, "testRole");
		synCtx.setProperty(MediatorConstants.QOS_SERVICE, "testService");
		synCtx.setEnvelope(OMAbstractFactory.getSOAP12Factory().createSOAPEnvelope());
		synCtx.getEnvelope().addChild(OMAbstractFactory.getSOAP12Factory().createSOAPHeader());
		mm.mediate(synCtx);
		msm.mediate(synCtx);
	}
	
	@Test
	public void test() throws IOException {
		assertTrue(tm.mediate(synCtx));
	}

}
