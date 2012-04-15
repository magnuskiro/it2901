package no.ntnu.qos.server.mediators.test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import no.ntnu.qos.server.mediators.MediatorConstants;
import no.ntnu.qos.server.mediators.QosContext;
import no.ntnu.qos.server.mediators.TRContext;
import no.ntnu.qos.server.mediators.impl.DefaultQosContext;
import no.ntnu.qos.server.mediators.impl.TRContextImpl;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.synapse.MessageContext;
import org.apache.synapse.config.SynapseConfiguration;
import org.apache.synapse.core.axis2.Axis2MessageContext;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class TRContextTest {

	private static TRContext trc = null;
	private static MessageContext synCtx;
	private static MessageContext synCtx2;
	private static final long START_TIME = System.currentTimeMillis();
	private static final long BANDWIDTH = 100;
	private static final long AVAILABLE_BANDWIDTH = 10000;
	private static final long TTL = 1000;
	private static final long ERROR_MARGIN = 5;
	private static final int NUMBER_OF_ADDS = 10;
	private static final int PRIORITY = 10;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		synCtx = new Axis2MessageContext(new org.apache.axis2.context.MessageContext(), 
				new SynapseConfiguration(), null);
		synCtx.setProperty(MediatorConstants.QOS_TIME_ADDED, START_TIME);
		synCtx.setEnvelope(OMAbstractFactory.getSOAP12Factory().getDefaultEnvelope());
		synCtx.setProperty(MediatorConstants.QOS_BANDWIDTH, BANDWIDTH);
		synCtx.setProperty(MediatorConstants.QOS_USE_TTL, true);
		synCtx.setProperty(MediatorConstants.QOS_TTL, TTL);
		synCtx.setProperty(MediatorConstants.QOS_PRIORITY, PRIORITY);
		
		synCtx2 = new Axis2MessageContext(new org.apache.axis2.context.MessageContext(), 
				new SynapseConfiguration(), null);
		synCtx2.setProperty(MediatorConstants.QOS_TIME_ADDED, START_TIME);
		synCtx2.setEnvelope(OMAbstractFactory.getSOAP12Factory().getDefaultEnvelope());
		synCtx2.setProperty(MediatorConstants.QOS_BANDWIDTH, BANDWIDTH + 100);
		synCtx2.setProperty(MediatorConstants.QOS_USE_TTL, true);
		synCtx2.setProperty(MediatorConstants.QOS_TTL, TTL + 10);
		synCtx2.setProperty(MediatorConstants.QOS_PRIORITY, PRIORITY + 10);
	}

	@Before
	public void init(){
		trc = new TRContextImpl(AVAILABLE_BANDWIDTH);
	}
	
	@Test
	public void testPriority() throws IOException{
		QosContext lowPri = new DefaultQosContext(synCtx);
		trc.add(lowPri);
		trc.add(new DefaultQosContext(synCtx2));
		assertEquals(lowPri, ((TRContextImpl)trc).getQueue().peek());
	}

	@Test
	public void testAdd() throws IOException {
		assertEquals("No contexts have been added", 
				0, ((TRContextImpl)trc).size());

		for(int i = 0; i < NUMBER_OF_ADDS; i++){
			trc.add(new DefaultQosContext(synCtx));
		}

		assertEquals("Contexts should have been added now", 
				NUMBER_OF_ADDS, ((TRContextImpl)trc).size());
	}

	@Test
	public void testAvailableBandwidth() throws IOException {
		assertEquals("No bandwidth should have been used", AVAILABLE_BANDWIDTH, 
				trc.availableBandwidth());
		
		int numberOf = 10;
		int newBandwidth = 1000;
		for(int i = 0; i < numberOf; i++){
			trc.add(new DefaultQosContext(synCtx));
		}

		assertEquals("Some bandwidth should be used", 
				AVAILABLE_BANDWIDTH -numberOf, trc.availableBandwidth());
		
		trc.setAvailableBandwidth(newBandwidth);
		
		assertEquals("Some bandwidth should be used", 
				newBandwidth -numberOf, trc.availableBandwidth());
	}

	@Test
	public void testPreemptContexts() throws IOException {
		List<QosContext> list = new ArrayList<QosContext>();
		for(int i = 0; i < NUMBER_OF_ADDS; i++){
			list.add(new DefaultQosContext(synCtx));
			trc.add(list.get(i));
		}
		trc.setAvailableBandwidth(1);
		assertTrue("Test preempt", trc.preemptContexts(new DefaultQosContext(synCtx2)).containsAll(list));
		assertTrue("Since all are preempted and no new additions the size " +
				"should be empty", ((TRContextImpl)trc).size() == 0);
	}

	@Test
	public void testClearFinished() throws IOException {
		assertEquals("Empty context", 0, ((TRContextImpl)trc).size());
		QosContext qc = null;
		for(int i = 0; i < NUMBER_OF_ADDS; i++){
			qc = new DefaultQosContext(synCtx);
			trc.add(qc);
		}
		assertEquals("Filled context", NUMBER_OF_ADDS, ((TRContextImpl)trc).size());
		trc.clearFinished();
		assertEquals("Empty context again", 0, ((TRContextImpl)trc).size());
	}

	@Test
	public void testNextEvent() throws IOException {
		assertEquals("Next event should be Long.MAX_VALUE when no contexts " +
				"are added", Long.MAX_VALUE - System.currentTimeMillis(), 
				trc.nextEvent(), ERROR_MARGIN);
		QosContext qc = null;
		for(int i = 0; i < NUMBER_OF_ADDS; i++){
			qc = new DefaultQosContext(synCtx);
			trc.add(qc);
		}
		assertEquals("Next event",  
				qc.getEstimatedSendingTime(), trc.nextEvent(), ERROR_MARGIN);
		
		qc = new DefaultQosContext(synCtx2);
		trc.add(qc);
		
		assertEquals("Next event", 
				qc.getEstimatedSendingTime(), trc.nextEvent(), ERROR_MARGIN);
	}

}
