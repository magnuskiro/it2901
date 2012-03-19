package no.ntnu.qos.server.mediators.impl;

import javax.xml.namespace.QName;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.impl.llom.OMElementImpl;
import org.apache.axiom.soap.impl.llom.soap12.SOAP12Factory;
import org.apache.synapse.MessageContext;
import org.apache.synapse.SynapseLog;

import no.ntnu.qos.server.mediators.AbstractQosMediator;
import no.ntnu.qos.server.mediators.MediatorConstants;
import no.ntnu.qos.server.mediators.QosLogType;

public class SoapPriorityMediator extends AbstractQosMediator {

	@Override
	protected boolean mediateImpl(MessageContext synCtx) {
		SynapseLog synLog = getLog(synCtx);
		int pri = (Integer) synCtx.getProperty(MediatorConstants.QOS_PRIORITY);
		int dif = (Integer) synCtx.getProperty(MediatorConstants.QOS_DIFFSERV);
		
		OMElement header = synCtx.getEnvelope().getHeader();
		if(header==null){
			synCtx.getEnvelope().addChild(OMAbstractFactory.getSOAP12Factory().createSOAPHeader());
			header = synCtx.getEnvelope().getHeader();
		}
		/*
		 * Sets the Priority value in the SOAP header.
		 */
		QName priName = new QName(MediatorConstants.QOS_PRIORITY);
		OMElement priHeader = header.getFirstChildWithName(priName);
		if(priHeader==null){
			priHeader = new OMElementImpl(new QName(MediatorConstants.QOS_PRIORITY), 
					synCtx.getEnvelope().getHeader(), new SOAP12Factory());			
			synCtx.getEnvelope().getHeader().addChild(priHeader);	
		}
		priHeader.setText(pri+"");

		/*
		 * Sets the Diffserv value in the SOAP header.
		 */
		QName difName = new QName(MediatorConstants.QOS_DIFFSERV);
		OMElement difHeader = header.getFirstChildWithName(difName);
		if(difHeader==null){
			difHeader = new OMElementImpl(new QName(MediatorConstants.QOS_DIFFSERV), 
					synCtx.getEnvelope().getHeader(), new SOAP12Factory());			
			synCtx.getEnvelope().getHeader().addChild(difHeader);
		}
		difHeader.setText(dif+"");
		
		this.logMessage(synLog, "Successfully " +
				"added metadata to soap header. " +
				"Added priority="+pri+", diffserv="+dif, QosLogType.INFO);
		return true;
	}

	@Override
	protected String getName() {
		return "SOAP Priority Mediator";
	}

}
