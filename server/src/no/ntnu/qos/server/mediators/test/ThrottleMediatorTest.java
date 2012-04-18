package no.ntnu.qos.server.mediators.test;

import static org.junit.Assert.assertTrue;

import java.io.IOException;

import no.ntnu.qos.server.mediators.MediatorConstants;
import no.ntnu.qos.server.mediators.impl.ThrottleMediator;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.soap.SOAPProcessingException;
import org.apache.axis2.AxisFault;
import org.apache.synapse.MessageContext;
import org.apache.synapse.config.SynapseConfiguration;
import org.apache.synapse.core.axis2.Axis2MessageContext;
import org.junit.BeforeClass;
import org.junit.Test;

public class ThrottleMediatorTest {

	private static MessageContext synCtx;
	private static final ThrottleMediator tm = new ThrottleMediator();
	private static final long START_TIME = System.currentTimeMillis();
	private static final long BANDWIDTH = 100;
	private static final long TTL = 1000;
	private static final int PRIORITY = 10;
	
	@BeforeClass
	public static void setupMessageContext() throws AxisFault, SOAPProcessingException{
		synCtx = new Axis2MessageContext(new org.apache.axis2.context.MessageContext(), 
				new SynapseConfiguration(), null);
		synCtx.setProperty(MediatorConstants.QOS_TIME_ADDED, START_TIME);
		synCtx.setEnvelope(OMAbstractFactory.getSOAP12Factory().getDefaultEnvelope());
		synCtx.setProperty(MediatorConstants.QOS_BANDWIDTH, BANDWIDTH);
		synCtx.setProperty(MediatorConstants.QOS_USE_TTL, true);
		synCtx.setProperty(MediatorConstants.QOS_TTL, TTL);
		synCtx.setProperty(MediatorConstants.QOS_PRIORITY, PRIORITY);
		tm.setTimeout(100);
		tm.setMinBandwidthPerMessage(1000);
	}
	
	@Test
	public void test() throws IOException {
		assertTrue(tm.mediate(synCtx));
	}

}
