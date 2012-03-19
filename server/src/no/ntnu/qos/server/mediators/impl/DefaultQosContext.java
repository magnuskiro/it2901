package no.ntnu.qos.server.mediators.impl;

import java.io.IOException;

import org.apache.synapse.MessageContext;

import no.ntnu.qos.server.mediators.AbstractQosContext;
import no.ntnu.qos.server.mediators.MediatorConstants;

public class DefaultQosContext extends AbstractQosContext {
	
	private final long estTimeSend;
	private final long startedMediate;
	private final long ttl;

	public DefaultQosContext(MessageContext synCtx) throws IOException {
		super(synCtx);
		
		long pBandwidth = (Long) synCtx.getProperty(MediatorConstants.QOS_BANDWIDTH);
		long bandwidth = pBandwidth != -1 ? pBandwidth : 0;
		this.estTimeSend = this.size() / bandwidth;
		
		this.startedMediate = (Long) synCtx.getProperty(MediatorConstants.QOS_TIME_ADDED);
		this.ttl = (Long) synCtx.getProperty(MediatorConstants.QOS_TTL);
	}

	@Override
	public long estimatedSendingTime() {
		return estTimeSend;
	}

	@Override
	public long timeToLive() {
		return this.startedMediate - System.currentTimeMillis() + this.ttl;
	}

	@Override
	public void preempt() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void sendImpl() {
		// TODO Auto-generated method stub

	}

}
