package no.ntnu.qos.server.mediators;

import java.net.URI;
import java.net.URISyntaxException;

import no.ntnu.qos.ms.MSCommunicator;
import no.ntnu.qos.ms.RoutingInfo;
import no.ntnu.qos.ms.impl.MSCommunicatorImpl;

import org.apache.synapse.MessageContext;
import org.apache.synapse.SynapseLog;
import org.apache.synapse.mediators.AbstractMediator;

/**
 * This mediator contacts a Monitoring Service to get routing information.
 * Sends endpoint IP-address, and gets the bandwidth of the weakest link
 * and the GUID of the last Tactical Router on the path to endpoint.
 * Sets the bandwidth and TR GUID as properties in the MessageContext.
 * @author Ola Martin
 *
 */
public class MSMediator extends AbstractMediator {

	private static final MSCommunicator msc = new MSCommunicatorImpl("ms.xml");
	@Override
	public boolean mediate(MessageContext synCtx) {
		
		SynapseLog synLog = getLog(synCtx);

		if (synLog.isTraceOrDebugEnabled()) {
			synLog.traceOrDebug("Start : MS mediator");

			if (synLog.isTraceTraceEnabled()) {
				synLog.traceTrace("Message : " + synCtx.getEnvelope());
			}
		}
		
		try {
			String endpoint = new URI(synCtx.getTo().getAddress()).getHost();
			RoutingInfo ri = msc.getRoutingInfo(new URI(endpoint));
			synCtx.setProperty(MediatorConstants.QOS_BANDWIDTH, ri.getBandwidth());
			synCtx.setProperty(MediatorConstants.QOS_LAST_TR, ri.getLastTR());
			if(synLog.isTraceOrDebugEnabled()){
				synLog.traceOrDebug("Successfully set Routing Info: bandwidth="+ri.getBandwidth()+
						", lastTR="+ri.getLastTR());
			}
		} catch (URISyntaxException e) {
			if(synLog.isTraceOrDebugEnabled()){
				synLog.traceOrDebugWarn("Could not set set Routing Info, Illegal endpoint syntax.");
				e.printStackTrace();				
			}
		}
		
		return true;
	}

}
