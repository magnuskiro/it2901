package no.ntnu.qos.server.mediators.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import no.ntnu.qos.server.mediators.AbstractQosMediator;
import no.ntnu.qos.server.mediators.MediatorConstants;
import no.ntnu.qos.server.mediators.QosContext;
import no.ntnu.qos.server.mediators.QosLogType;
import no.ntnu.qos.server.mediators.TRContext;

import org.apache.synapse.MessageContext;
import org.apache.synapse.SynapseLog;
/**
 * This mediator finds out if there is enough bandwidth to send a message, 
 * if not, it will check if other sending messages has lower priority and should be stopped,
 * if not, it will wait until a message finishes sending. 
 * If the message is timed out this mediator will return false, otherwise true.
 * @author Ola Martin
 *
 */
public class ThrottleMediator extends AbstractQosMediator{

	private static long minBandwidthPerMessage;
	private static long timeout;
	private static final Map<String, TRContext> trCtxs = new HashMap<String, TRContext>();

	@Override
	protected boolean mediateImpl(MessageContext synCtx) {
		SynapseLog synLog = getLog(synCtx);

		String lastTR = (String) synCtx.getProperty(MediatorConstants.QOS_LAST_TR);
		TRContext trCtx = trCtxs.get(lastTR);
		long initialCapacity = (Long) synCtx.getProperty(MediatorConstants.QOS_BANDWIDTH)
				/minBandwidthPerMessage;
		QosContext qCtx;
		long timeStarted = System.currentTimeMillis();
		try {
			qCtx = new DefaultQosContext(synCtx);
			if(trCtx==null){
				trCtx = new TRContextImpl(initialCapacity>1 ? initialCapacity:1);
				trCtxs.put(lastTR, trCtx);
			}else{
				trCtx.setAvailableBandwidth(initialCapacity>1 ? initialCapacity:1);
			}
			while(System.currentTimeMillis()-timeStarted<timeout && 
					(!qCtx.useTTL() || qCtx.getTimeToLive()>0)){
				if(send(qCtx, trCtx, synLog)){
					return true;
				}else{
					List<QosContext> preemptees = trCtx.preemptContexts(qCtx);
					for(QosContext preempted:preemptees){
						this.logMessage(synLog, "Preempted Message:" +
								preempted.getMessageContext().getMessageID(), QosLogType.INFO);
					}
					if(send(qCtx, trCtx, synLog)){
						return true;
					}
				}
				this.logMessage(synLog, "Message has to wait for "+trCtx.nextEvent()+
						"ms before trying again.", QosLogType.INFO);
				try {
					Thread.sleep(trCtx.nextEvent());
				} catch (InterruptedException e) {
					this.logMessage(synLog, "Could not sleep", QosLogType.WARN);
				}
			}
			if(System.currentTimeMillis()-timeStarted>=timeout){
				this.logMessage(synLog, "Message timed out: mediator timeout exceeded", QosLogType.INFO);
			}else if(qCtx.useTTL() && qCtx.getTimeToLive()<=0){
				this.logMessage(synLog, "Message timed out: message time to live exceeded", QosLogType.INFO);
			}
		} catch (IOException e) {
			this.logMessage(synLog, "Error reading message data: could not get it's size", QosLogType.WARN);
		}
		return false;
	}

	private boolean send(QosContext qCtx,TRContext trCtx, SynapseLog synLog){
		trCtx.clearFinished();
		if(trCtx.availableBandwidth()>0){
			trCtx.add(qCtx);
			this.logMessage(synLog, "Successfully " +
					"added message context to TRContext.", QosLogType.INFO);
			return true;
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

	public static long getTimeout() {
		return timeout;
	}

	public static void setTimeout(long timeout) {
		ThrottleMediator.timeout = timeout;
	}

}
