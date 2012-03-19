package no.ntnu.qos.server.mediators.impl;

import java.io.IOException;
import java.net.Socket;

import no.ntnu.qos.server.mediators.AbstractQosContext;
import no.ntnu.qos.server.mediators.MediatorConstants;

import org.apache.axis2.Constants;
import org.apache.http.nio.NHttpServerConnection;
import org.apache.http.nio.reactor.SocketAccessor;
import org.apache.synapse.MessageContext;
import org.apache.synapse.core.axis2.Axis2MessageContext;
import org.apache.synapse.transport.nhttp.ServerWorker;

public class DefaultQosContext extends AbstractQosContext {
	
	private final double estTimeSend;
	private final long startedMediate;
	private final long ttl;

	public DefaultQosContext(MessageContext synCtx) throws IOException {
		super(synCtx);
		
		long pBandwidth = (Long) synCtx.getProperty(MediatorConstants.QOS_BANDWIDTH);
		//We have to make sure that if we are on a local network we don't use
		//-1 for bandwidth
		long bandwidth = pBandwidth != -1 ? pBandwidth : Integer.MAX_VALUE;
		this.estTimeSend = this.size() / bandwidth;
		
		this.startedMediate = (Long) synCtx.getProperty(MediatorConstants.QOS_TIME_ADDED);
		this.ttl = (Long) synCtx.getProperty(MediatorConstants.QOS_TTL);
	}

	@Override
	public double getEstimatedSendingTime() {
		return estTimeSend;
	}

	@Override
	public long getTimeToLive() {
		return this.startedMediate - System.currentTimeMillis() + this.ttl;
	}

	@Override
	public void preempt() {
		org.apache.axis2.context.MessageContext msgContext = 
				((Axis2MessageContext)this.getMessageContext()).getAxis2MessageContext();
		ServerWorker worker = (ServerWorker) msgContext.getProperty(Constants.OUT_TRANSPORT_INFO);

		// DIFFSERV CHANGES
		NHttpServerConnection conn = worker.getConn();
		if(conn instanceof SocketAccessor){
			try{
				Socket soc = ((SocketAccessor)conn).getSocket();
				if(soc != null){
					soc.close();
				}
			}catch(Exception e){
				//Catch all exceptions
				//for now silently discard
			}
		}
	}

	@Override
	protected void sendImpl() {
		//Do nothing at the moment
	}

}
