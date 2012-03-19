package no.ntnu.qos.server.mediators;

import org.apache.synapse.MessageContext;

public interface QosContext extends Comparable<QosContext> {

	/**
	 * This method should return the time when the message started sending
	 * @return - long time when message started sending
	 */
	public long getSendingStartTime();

	/**
	 * This method should return a conservative estimate of how long time this message
	 * will need in order for it to be sent over the network
	 * @return - long estimated time
	 */
	public double getEstimatedSendingTime();

	/**
	 * This method should return the priority of the {@link MessageContext}
	 * @return - the priority
	 */
	public int getPriority();

	/**
	 * The estimated size of the message
	 * @return - the size of the message
	 */
	public long size();

	/**
	 * This method should record all information regarding that the message is being
	 * sent
	 */
	public void send();

	/**
	 * Return whether or not this message uses time to live
	 * @return - true if the message uses TTL, false otherwise
	 */
	public boolean useTTL();

	/**
	 * Return the time remaining for this message to live
	 * @return - time to live
	 */
	public long getTimeToLive();

	/**
	 * This method should stop this message from sending and catch any exceptions
	 * which could occur because of this preempting.
	 */
	public void preempt();
	
	/**
	 * This method should return the underlying message context
	 * @return - the internal message context
	 */
	public MessageContext getMessageContext();
	
	/**
	 * Return whether or not this message is done sending
	 * @return - True if done sending, else false
	 */
	public boolean isFinishedSending();

}
