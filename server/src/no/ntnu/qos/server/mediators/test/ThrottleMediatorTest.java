package no.ntnu.qos.server.mediators.test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Scanner;


import no.ntnu.qos.server.mediators.impl.MSMediator;
import no.ntnu.qos.server.mediators.impl.ThrottleMediator;
import no.ntnu.qos.server.mediators.MediatorConstants;
import no.ntnu.qos.server.mediators.impl.MetadataMediator;

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
	private static MSMediator msm = new MSMediator();
	private static MetadataMediator mm = new MetadataMediator();
	private static ThrottleMediator tm = new ThrottleMediator();
	private static String FILENAME;
	private static String ADDRESS = "http://125.50.50.73:8280/services/EchoService";
	
	@BeforeClass
	public static void setupMessageContext() throws AxisFault, SOAPProcessingException{
		synCtx = new Axis2MessageContext(new org.apache.axis2.context.MessageContext(), 
				new SynapseConfiguration(), null);
		synCtx.setTo(new EndpointReference(ADDRESS));
		Scanner in = new Scanner(System.in);
		System.out.println("Path to ppdtest.xml:");
		FILENAME = in.nextLine();
//		MediatorProperty mp = new MediatorProperty();
//		mp.setName(MediatorConstants.PRIORITY_DATA_FILENAME);
//		mp.setValue(FILENAME);
//		mm.addProperty(mp);
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
