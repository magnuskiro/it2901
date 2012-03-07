package no.ntnu.qos.server.mediators;

import java.util.ArrayList;
import java.util.List;

import org.apache.synapse.MessageContext;
import org.apache.synapse.SynapseLog;
import org.apache.synapse.mediators.AbstractMediator;
import org.apache.synapse.mediators.MediatorProperty;

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
        if(ppd.getFilename()==null){
        	for(MediatorProperty mp:properties){
        		if(mp.getName().equals(MediatorConstants.PRIORITY_DATA_FILENAME)){
        			ppd.setFilename(mp.getValue());
        			break;
        		}
        	}        	
        }
        
        final String clientRole = (String)synCtx.getProperty(MediatorConstants.CLIENT_ROLE);
        final String service = (String)synCtx.getProperty(MediatorConstants.SERVICE);
        
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
