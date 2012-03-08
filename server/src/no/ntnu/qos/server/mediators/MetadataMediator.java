package no.ntnu.qos.server.mediators;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

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
						e.printStackTrace();
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
		if (synLog.isTraceOrDebugEnabled()) {
			synLog.traceOrDebug("Successfully added metadata to message context. " +
					"Added priority="+pri+", diffserv="+dif);
		}
		return true;
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
