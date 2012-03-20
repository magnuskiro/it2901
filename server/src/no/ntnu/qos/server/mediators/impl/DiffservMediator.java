package no.ntnu.qos.server.mediators.impl;

import java.net.Socket;
import java.net.SocketException;

import no.ntnu.qos.server.mediators.AbstractQosMediator;
import no.ntnu.qos.server.mediators.MediatorConstants;
import no.ntnu.qos.server.mediators.QosLogType;

import org.apache.axis2.Constants;
import org.apache.http.nio.NHttpServerConnection;
import org.apache.http.nio.reactor.SocketAccessor;
import org.apache.synapse.MessageContext;
import org.apache.synapse.SynapseLog;
import org.apache.synapse.core.axis2.Axis2MessageContext;
import org.apache.synapse.transport.nhttp.ServerWorker;

/**
 * This mediator sets the diffserv value in the underlying socket.
 * @author Ola Martin & Jørgen
 *
 */
public class DiffservMediator extends AbstractQosMediator {

	@Override
	protected boolean mediateImpl(MessageContext synCtx, SynapseLog synLog) {
		org.apache.axis2.context.MessageContext msgContext = 
				((Axis2MessageContext)synCtx).getAxis2MessageContext();
		ServerWorker worker = (ServerWorker) msgContext.getProperty(Constants.OUT_TRANSPORT_INFO);

		// DIFFSERV CHANGES
		NHttpServerConnection conn = worker.getConn();
		if(conn instanceof SocketAccessor){
			try{
				Object tc = synCtx.getProperty(MediatorConstants.QOS_DIFFSERV);
				if(tc != null && tc instanceof Integer){
					Socket soc = ((SocketAccessor)conn).getSocket();
					if(soc!=null){
						soc.setTrafficClass((Integer)tc);
						this.logMessage(synLog, "Successfully set diffserv to: " + tc, QosLogType.INFO);
					}else{
						this.logMessage(synLog, "Could not set Diffserv: Socket was null", QosLogType.WARN);
					}
				}else{
					this.logMessage(synLog, "Could not set Diffserv: " +
							"Diffserv value not integer:"+tc, QosLogType.WARN);
				}
			}catch(SocketException se){
				this.logMessage(synLog, "Could not set Diffserv: " +
					se.getMessage(), QosLogType.WARN);
			}catch(IllegalArgumentException iae){
				this.logMessage(synLog, "Diffserv value out of range: " +
						iae.getMessage(), QosLogType.WARN);
			}
		}else{
			this.logMessage(synLog, "Could not set Diffserv: NHttpServerConnection" +
					" not SocketAccessor", QosLogType.WARN);
		}
		return true;
	}

	@Override
	protected String getName() {
		return "Diffserv Mediator";
	}

}
