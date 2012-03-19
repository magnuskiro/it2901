package no.ntnu.qos.server.mediators.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import no.ntnu.qos.server.mediators.MediatorConstants;
import no.ntnu.qos.server.mediators.QosContext;
import no.ntnu.qos.server.mediators.impl.DefaultQosContext;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.soap.SOAPProcessingException;
import org.apache.synapse.MessageContext;
import org.apache.synapse.config.SynapseConfiguration;
import org.apache.synapse.core.axis2.Axis2MessageContext;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class QosContextTest {

	private static MessageContext synCtx;
	private static final long BANDWIDTH = 100;
	private static final long TTL = 100;
	private static final long START_TIME = System.currentTimeMillis();
	private static final int PRIORITY = 25;
	private static QosContext qc = null;
	private static double size = 0;
	private static long sendingTime;

	@BeforeClass
	public static void setup() throws SOAPProcessingException, IOException{
		synCtx = new Axis2MessageContext(new org.apache.axis2.context.MessageContext(), 
				new SynapseConfiguration(), null);
		//Configure for estimated sending time
		synCtx.setProperty(MediatorConstants.QOS_TIME_ADDED, START_TIME);
		synCtx.setEnvelope(OMAbstractFactory.getSOAP12Factory().getDefaultEnvelope());
		size = ((Axis2MessageContext)synCtx).getAxis2MessageContext().getInboundContentLength();
		synCtx.setProperty(MediatorConstants.QOS_BANDWIDTH, BANDWIDTH);
		synCtx.setProperty(MediatorConstants.QOS_USE_TTL, true);
		synCtx.setProperty(MediatorConstants.QOS_TTL, TTL);
		
		//Configure for priority
		synCtx.setProperty(MediatorConstants.QOS_PRIORITY, PRIORITY);

	}
	
	@Before
	public void beforeEach() throws IOException{
		qc = new DefaultQosContext(synCtx);
	}

	@Test
	public void testEstimatedSendingTime(){
		assertEquals("Estimated sending time", size / BANDWIDTH, 
				qc.getEstimatedSendingTime(), 0);
	}

	@Test
	public void testTimeToLive(){
		assertEquals("Time to live", START_TIME - System.currentTimeMillis() + TTL, 
				qc.getTimeToLive(), 5);
	}

	@Test
	public void testSendingStartTime(){
		assertEquals("Sending start time before send method", 0, qc.getSendingStartTime());
		qc.send();
		sendingTime = System.currentTimeMillis();
		assertEquals("Sending start time", sendingTime, qc.getSendingStartTime());
	}

	@Test
	public void testPriority() {
		assertEquals("Test priority", PRIORITY, qc.getPriority());
	}

	@Test
	public void testSize() {
		assertEquals("Test size", size, qc.size(), 0);
	}

	@Test
	public void testUseTTL() throws IOException {
		assertTrue("Assert use TTL", qc.useTTL());
		synCtx.setProperty(MediatorConstants.QOS_USE_TTL, false);
		qc = new DefaultQosContext(synCtx);
		assertFalse("Assert not use TTL", qc.useTTL());
	}

	@Test
	public void testGetMessageContext() {
		assertEquals("Message context is equal", synCtx, qc.getMessageContext());
	}

}
