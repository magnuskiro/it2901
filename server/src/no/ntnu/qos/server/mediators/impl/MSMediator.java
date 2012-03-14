package no.ntnu.qos.server.mediators.impl;

import java.net.URI;
import java.net.URISyntaxException;

import no.ntnu.qos.ms.MSCommunicator;
import no.ntnu.qos.ms.RoutingInfo;
import no.ntnu.qos.ms.impl.MSCommunicatorImpl;
import no.ntnu.qos.server.mediators.AbstractQosMediator;
import no.ntnu.qos.server.mediators.MediatorConstants;
import no.ntnu.qos.server.mediators.QosLogType;

import org.apache.synapse.MessageContext;
import org.apache.synapse.SynapseLog;

/**
 * This mediator contacts a Monitoring Service to get routing information.
 * Sends endpoint IP-address, and gets the bandwidth of the weakest link
 * and the GUID of the last Tactical Router on the path to endpoint.
 * Sets the bandwidth and TR GUID as properties in the MessageContext.
 * @author Ola Martin
 *
 */
public class MSMediator extends AbstractQosMediator {

	private static final MSCommunicator msc = new MSCommunicatorImpl("ms.xml");
	@Override
	public boolean mediateImpl(MessageContext synCtx) {

		SynapseLog synLog = getLog(synCtx);

		try {
			String endpoint = (String) synCtx.getProperty(MediatorConstants.QOS_FROM_ADDR);
			this.logMessage(synLog, synCtx.getMessageID(), "Endpoint address: " 
					+ endpoint, QosLogType.INFO);
			RoutingInfo ri = msc.getRoutingInfo(new URI(endpoint));
			synCtx.setProperty(MediatorConstants.QOS_BANDWIDTH, ri.getBandwidth());
			synCtx.setProperty(MediatorConstants.QOS_LAST_TR, ri.getLastTR());
			this.logMessage(synLog, synCtx.getMessageID(), "Successfully set " +
					"Routing Info: bandwidth="+ri.getBandwidth()+
					", lastTR="+ri.getLastTR(), QosLogType.INFO);
		} catch (URISyntaxException e) {
			this.logMessage(synLog, synCtx.getMessageID(), "Could not set set " +
					"Routing Info, Illegal endpoint syntax.\n" + e.getMessage(), 
					QosLogType.WARN);
		}

		return true;
	}
	@Override
	protected String getName() {
		return "MSMediator";
	}

}
