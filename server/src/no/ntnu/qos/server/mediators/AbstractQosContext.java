package no.ntnu.qos.server.mediators;

import java.io.IOException;

import org.apache.synapse.MessageContext;
import org.apache.synapse.core.axis2.Axis2MessageContext;
/**
 * This is an abstract implementation of QosContext, 
 * it implements some of the functionality needed for a QosContext
 * @author Ola Martin
 * @author Jørgen
 *
 */
public abstract class AbstractQosContext implements QosContext {
	
	private final MessageContext synContext;
	private final long size;
	private final boolean useTTL;
	private final int priority;
	private long startTime;
	
	public AbstractQosContext(MessageContext synCtx) throws IOException{
		this.synContext = synCtx;
		this.size = ((Axis2MessageContext)synContext).getAxis2MessageContext().getInboundContentLength();
		this.useTTL = (Boolean) synCtx.getProperty(MediatorConstants.QOS_USE_TTL);
		this.priority = (Integer) synCtx.getProperty(MediatorConstants.QOS_PRIORITY);
	}

	@Override
	public long getSendingStartTime() {
		return startTime;
	}

	@Override
	public int getPriority() {
		return priority;
	}

	@Override
	public long size() {
		return this.size;
	}

	/**
	 * This method should do anything necessary to prepare this message for sending
	 */
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
