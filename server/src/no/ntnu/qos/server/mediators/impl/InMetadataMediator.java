package no.ntnu.qos.server.mediators.impl;


import javax.xml.namespace.QName;

import no.ntnu.qos.server.mediators.AbstractQosMediator;
import no.ntnu.qos.server.mediators.MediatorConstants;
import no.ntnu.qos.server.mediators.QosLogType;

import org.apache.axiom.om.OMElement;
import org.apache.synapse.MessageContext;
import org.apache.synapse.SynapseLog;
import org.apache.synapse.core.axis2.Axis2MessageContext;

/**
 * This mediator sets the from-endpoint as a QOS_FROM_ADDR property in the message context.
 * It also sets the QOS_USE_TTL and QOS_TTL properties based on a soap header.
 * @author mahou
 *
 */
public class InMetadataMediator extends AbstractQosMediator {

	private static final QName QOSTTL = new QName(MediatorConstants.QOS_TTL);

	@Override
	protected boolean mediateImpl(MessageContext synCtx) {
		SynapseLog synLog = getLog(synCtx);
		Object from = ((Axis2MessageContext)synCtx).getAxis2MessageContext().getProperty(
				org.apache.axis2.context.MessageContext.REMOTE_ADDR);
		
		if(from!=null){
			synCtx.setProperty(MediatorConstants.QOS_FROM_ADDR, from.toString());
		}

		boolean useTtl = false;
		long ttl = -1;

		if(synCtx.getEnvelope().getHeader()!=null){
			OMElement ttlEle = synCtx.getEnvelope().getHeader().getFirstChildWithName(
					QOSTTL);
			if(ttlEle != null && !ttlEle.getText().equals("-1")){
				useTtl = true;
				ttl = Long.parseLong(ttlEle.getText());
			}
		}
		synCtx.setProperty(MediatorConstants.QOS_USE_TTL, useTtl);
		synCtx.setProperty(MediatorConstants.QOS_TTL, ttl);
		this.logMessage(synLog, "Successfully " +
				"added metadata to message context. " +
				"Added useTtl="+useTtl+", ttl="+ttl+", from="+from, QosLogType.INFO);
		return true;
	}

	@Override
	protected String getName() {
		return "InMetadataMediator";
	}

}
