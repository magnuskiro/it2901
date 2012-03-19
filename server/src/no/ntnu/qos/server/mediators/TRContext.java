package no.ntnu.qos.server.mediators;

import java.util.List;

/**
 * Contains a priority queue of QosContexts
 * @author Ola Martin
 *
 */
public interface TRContext {	
	
	/**
	 * Adds a QosContext to the priority queue
	 * @param qCtx, the QosContext to add to the queue
	 */
	public void add(QosContext qCtx);
	
	/**
	 * Calculates the available bandwidth
	 * @return the available bandwidth
	 */
	public long availableBandwidth();
	
	/**
	 * Stops sending of QosContext's with lower priority than qCtx, so qCtx can be sent.
	 * @param qCtx, a QosContext to be sent.
	 * @return A list of all the stopped QosContext's
	 */
	public List<QosContext> preemptContexts(QosContext qCtx);
	
	/**
	 * Removes all the QosContext's that should be finished sending.
	 */
	public void clearFinished();
	
	/**
	 * Should return the estimated time until the next message is done sending
	 * @return time until next message is sent.
	 */
	public long nextEvent();
	
	/**
	 * Set the available bandwidth which is available
	 * @param bandwidth
	 */
	public void setAvailableBandwidth(long bandwidth);
	
}
