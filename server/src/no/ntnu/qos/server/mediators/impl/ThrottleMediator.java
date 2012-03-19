package no.ntnu.qos.server.mediators.impl;

import java.util.HashMap;
import java.util.Map;

import no.ntnu.qos.server.mediators.AbstractQosMediator;
import no.ntnu.qos.server.mediators.MediatorConstants;
import no.ntnu.qos.server.mediators.TRContext;

import org.apache.synapse.MessageContext;
/**
 * This mediator does throttle related things.
 * @author Ola Martin
 *
 */
public class ThrottleMediator extends AbstractQosMediator{

	private static long minBandwidthPerMessage;
	private static final Map<String, TRContext> trCtxs = new HashMap<String, TRContext>();
	
	@Override
	protected boolean mediateImpl(MessageContext synCtx) {
		String lastTR = (String) synCtx.getProperty(MediatorConstants.QOS_LAST_TR);
		if(!trCtxs.containsKey(lastTR)){
			TRContext trCtx;
			trCtxs.put(lastTR, trCtx);
		}
		return false;
	}

	@Override
	protected String getName() {
		return "Throttle Mediator";
	}
	
	
	public static void setMinBandwidthPerMessage(long minBandwidthPerMessage) {
		ThrottleMediator.minBandwidthPerMessage = minBandwidthPerMessage;
	}
	
	public static long getMinBandwidthPerMessage() {
		return minBandwidthPerMessage;
	}

}
