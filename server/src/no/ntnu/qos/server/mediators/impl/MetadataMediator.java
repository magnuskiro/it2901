package no.ntnu.qos.server.mediators.impl;

import java.io.FileNotFoundException;

import javax.xml.namespace.QName;

import org.apache.axiom.om.OMAbstractFactory;
import no.ntnu.qos.server.mediators.AbstractQosMediator;
import no.ntnu.qos.server.mediators.MediatorConstants;
import no.ntnu.qos.server.mediators.QosLogType;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.impl.llom.OMElementImpl;
import org.apache.axiom.soap.impl.llom.soap12.SOAP12Factory;
import org.apache.synapse.MessageContext;
import org.apache.synapse.SynapseLog;

/**
 * This mediator adds priority metadata to Message Context based on client role and service.
 * @author Ola Martin
 * @author Jørgen
 *
 */
public class MetadataMediator extends AbstractQosMediator {


	private final static PersistentPriorityData ppd = new PersistentPriorityData();
	//private final List<MediatorProperty> properties = new ArrayList<MediatorProperty>();
	private static String ppdFilename;
	@Override
	public boolean mediateImpl(MessageContext synCtx) {

		SynapseLog synLog = getLog(synCtx);

		//Check if data is available, if not, try reading it, if it fails return false
		if(!ppd.isDataAvailable()){
			if(ppdFilename!=null){
				ppd.setFilename(ppdFilename);
				this.logMessage(synLog, "Set Filename " +
						"in Persistent Data Store, filename=" + ppd.getFilename(), 
						QosLogType.INFO);
				try {
					ppd.readData();
				} catch (FileNotFoundException e) {
					//This means that the supplied file could not be found.
					this.logMessage(synLog, 
							e.getLocalizedMessage(), QosLogType.WARN);
					return false;
				}
				this.logMessage(synLog, "Successfully " +
						"read file into persistent storage", QosLogType.INFO);
			}        	
		}

		//This is the work this mediator does.
		final String clientRole = (String)synCtx.getProperty(MediatorConstants.QOS_CLIENT_ROLE);
		final String service = (String)synCtx.getProperty(MediatorConstants.QOS_SERVICE);
		final int pri = ppd.getPriority(clientRole, service);
		final int dif = ppd.getDiffserv(clientRole, service);
		synCtx.setProperty(MediatorConstants.QOS_PRIORITY, pri);
		synCtx.setProperty(MediatorConstants.QOS_DIFFSERV, dif);
		synCtx.setProperty(MediatorConstants.QOS_TIME_ADDED, System.currentTimeMillis());

		addOrUpdateSOAPHeaders(pri, dif, synCtx);
		
		this.logMessage(synLog, "Successfully " +
					"added metadata to message context. " +
					"Added priority="+pri+", diffserv="+dif, QosLogType.INFO);
		
		return true;
	}

	private void addOrUpdateSOAPHeaders(int pri, int dif, MessageContext synCtx){
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
	}



	//	public void addProperty(MediatorProperty mp){
	//		properties.add(mp);
	//	}
	//
	//	public void addAllProperties(List<MediatorProperty> lmp){
	//		properties.addAll(lmp);
	//	}
	//
	//	public List<MediatorProperty> getProperties(){
	//		return properties;
	//	}

	public void setPpdFilename(String ppdFilename) {
		MetadataMediator.ppdFilename = ppdFilename;
	}

	public String getPpdFilename() {
		return ppdFilename;
	}

	@Override
	protected String getName() {
		return "MetadataMediator";
	}

}
