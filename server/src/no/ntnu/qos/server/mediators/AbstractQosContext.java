package no.ntnu.qos.server.mediators;

import java.io.IOException;

import org.apache.synapse.MessageContext;
import org.apache.synapse.core.axis2.Axis2MessageContext;

public abstract class AbstractQosContext implements QosContext {
	
	private final MessageContext synContext;
	private final long size;
	private final boolean useTTL;
	private long startTime;
	
	public AbstractQosContext(MessageContext synCtx) throws IOException{
		this.synContext = synCtx;
		this.size = ((Axis2MessageContext)synContext).getAxis2MessageContext().getInboundContentLength();
		this.useTTL = (Boolean) synCtx.getProperty(MediatorConstants.QOS_USE_TTL);
	}

	@Override
	public long sendingStartTime() {
		return startTime;
	}

	@Override
	public int priority() {
		return (Integer) this.synContext.getProperty(MediatorConstants.QOS_PRIORITY);
	}

	@Override
	public long size() {
		return this.size;
	}

	protected abstract void sendImpl();
	
	@Override
	public void send() {
		this.startTime = System.currentTimeMillis();
		sendImpl();
	}

	@Override
	public boolean useTTL() {
		return useTTL;
	}

	@Override
	public MessageContext getMessageContext() {
		return this.synContext;
	}

}
