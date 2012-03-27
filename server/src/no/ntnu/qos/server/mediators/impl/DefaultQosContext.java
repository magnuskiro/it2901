package no.ntnu.qos.server.mediators.impl;

import java.io.IOException;
import java.net.Socket;

import no.ntnu.qos.server.mediators.AbstractQosContext;
import no.ntnu.qos.server.mediators.MediatorConstants;
import no.ntnu.qos.server.mediators.QosContext;

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
	private final Socket soc;

	public DefaultQosContext(MessageContext synCtx) throws IOException {
		super(synCtx);

		long pBandwidth = (Long) synCtx.getProperty(MediatorConstants.QOS_BANDWIDTH);
		//We have to make sure that if we are on a local network we don't use
		//-1 for bandwidth
		long bandwidth = pBandwidth != -1 ? pBandwidth : Integer.MAX_VALUE;
		this.estTimeSend = this.size() / bandwidth;

		this.startedMediate = (Long) synCtx.getProperty(MediatorConstants.QOS_TIME_ADDED);
		this.ttl = (Long) synCtx.getProperty(MediatorConstants.QOS_TTL);
		this.soc = getSocket();
		
	}
	
	private Socket getSocket(){
		Socket result = null;
		
		org.apache.axis2.context.MessageContext msgContext = 
				((Axis2MessageContext)this.getMessageContext()).getAxis2MessageContext();
		ServerWorker worker = (ServerWorker) msgContext.getProperty(Constants.OUT_TRANSPORT_INFO);

		if(worker != null){
			NHttpServerConnection conn = worker.getConn();
			if(conn instanceof SocketAccessor){
                result = ((SocketAccessor)conn).getSocket();
			}
		}
		return result;
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
		if(this.soc != null){
			try {
				this.soc.close();
			} catch (IOException e) {
				//Got to catch 'em all
			}
		}
	}

	@Override
	protected void sendImpl() {
		//Do nothing at the moment
	}

	@Override
	public int compareTo(QosContext o) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isFinishedSending() {
		return this.soc != null ? this.soc.isClosed() : true;
	}

}
