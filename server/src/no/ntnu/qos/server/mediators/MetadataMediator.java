package no.ntnu.qos.server.mediators;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.impl.llom.OMElementImpl;
import org.apache.axiom.soap.impl.llom.soap12.SOAP12Factory;
import org.apache.synapse.MessageContext;
import org.apache.synapse.SynapseLog;
import org.apache.synapse.mediators.AbstractMediator;
import org.apache.synapse.mediators.MediatorProperty;

/**
 * This mediator adds priority metadata to Message Context based on client role and service.
 * @author Ola Martin & Jørgen
 *
 */
public class MetadataMediator extends AbstractMediator {


	private final static PersistentPriorityData ppd = new PersistentPriorityData();
	private final List<MediatorProperty> properties = new ArrayList<MediatorProperty>();
	@Override
	public boolean mediate(MessageContext synCtx) {

		SynapseLog synLog = getLog(synCtx);

		if (synLog.isTraceOrDebugEnabled()) {
			synLog.traceOrDebug("Start : Metadata mediator");

			if (synLog.isTraceTraceEnabled()) {
				synLog.traceTrace("Message : " + synCtx.getEnvelope());
			}
		}
		
		//Check if data is available, if not, try reading it, if it fails return false
		if(!ppd.isDataAvailable()){
			for(MediatorProperty mp:properties){
				if(mp.getName().equals(MediatorConstants.PRIORITY_DATA_FILENAME)){
					ppd.setFilename(mp.getValue());
					if (synLog.isTraceOrDebugEnabled()) {
						synLog.traceOrDebug("Set Filename in Persistent Data Store, filename="+ppd.getFilename());
					}
					try {
						ppd.readData();
					} catch (FileNotFoundException e) {
						//This means that the supplied file could not be found.
						if (synLog.isTraceOrDebugEnabled()) {
							e.printStackTrace();
						}
						return false;
					}
					if (synLog.isTraceOrDebugEnabled()) {
						synLog.traceOrDebug("Successfully read file into persistent storage");
					}
					break;
				}
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
		
		if (synLog.isTraceOrDebugEnabled()) {
			synLog.traceOrDebug("Successfully added metadata to message context. " +
					"Added priority="+pri+", diffserv="+dif);
		}
		return true;
	}
	
	private void addOrUpdateSOAPHeaders(int pri, int dif, MessageContext synCtx){
		OMElement header = synCtx.getEnvelope().getHeader();
		
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
	
	

	public void addProperty(MediatorProperty mp){
		properties.add(mp);
	}

	public void addAllProperties(List<MediatorProperty> lmp){
		properties.addAll(lmp);
	}

	public List<MediatorProperty> getProperties(){
		return properties;
	}

}
