package no.ntnu.qos.server.mediators;

import org.apache.synapse.MessageContext;
import org.apache.synapse.SynapseLog;
import org.apache.synapse.mediators.AbstractMediator;

/**
 * This is an abstract class intended to be extended by implementations of each mediator 
 * in our QoS project. This class makes logging information easier and also creates
 * a uniform way for each mediator to log its starting procedure.
 * @author Jørgen
 * @author Ola Martin
 *
 */
public abstract class AbstractQosMediator extends AbstractMediator {

	MessageContext synCtx;
	@Override
	public boolean mediate(MessageContext synCtx) {
		this.synCtx=synCtx;
		SynapseLog log = this.getLog(synCtx);
		this.logMessage(log, MediatorConstants.DEBUG_START + 
				this.getName(), QosLogType.INFO);
		boolean res = false;
		try{
			res = this.mediateImpl(synCtx, log);
		}catch(Exception e){
			this.logMessage(log, "Mediator: '" +
					this.getName() + "' threw exception:\n" + 
					e.toString() + " at: " +
					e.getStackTrace()[0].getClassName()+":"+
					e.getStackTrace()[0].getLineNumber(),QosLogType.WARN);
			return false;
		}
		if(res){
			this.logMessage(log, MediatorConstants.DEBUG_END + 
					this.getName(), QosLogType.INFO);
		}else{
			this.logMessage(log, "Mediator '" + 
					this.getName() + "' returned false", QosLogType.WARN);
		}
		return res;
	}

	/**
	 * Implementation of mediate for the mediator
	 * @param synCtx - The message context the mediator will work on
	 * @return - True if the mediator was successful and false otherwise.
	 */
	protected abstract boolean mediateImpl(MessageContext synCtx, SynapseLog synLog);

	/**
	 * Get the name of the mediator, this is used as a human readable way to tell
	 * which mediator is working
	 * @return - A {@link String} containing the name of the mediator
	 */
	protected abstract String getName();

	/**
	 * Log a message using SynapseLog
	 * @param log - The log to use, this is connected to the message context  
	 * @param messageID - A unique name connected to the message, this is used to
	 * follow the mediation of each message
	 * @param message - The message to output, this should contain all information
	 * one would want to log
	 * @param type - The type of message, {@link QosLogType.INFO} or {@link QosLogType.WARN}
	 */
	protected void logMessage(SynapseLog log, String message, QosLogType type){
		if(log.isTraceOrDebugEnabled()){
			String from = "";
			if(synCtx.getProperty(MediatorConstants.QOS_FROM_ADDR) != null){
				from = (String) synCtx.getProperty(MediatorConstants.QOS_FROM_ADDR);
			}
			switch(type){
			case INFO:
				log.traceOrDebug(from+" "+message); break;
			case WARN:
				log.traceOrDebugWarn(MediatorConstants.DEBUG_ERROR +from+" "+ message); break;
			}
		}
	}
}
