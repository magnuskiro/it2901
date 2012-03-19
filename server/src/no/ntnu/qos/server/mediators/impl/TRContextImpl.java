package no.ntnu.qos.server.mediators.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

import no.ntnu.qos.server.mediators.QosContext;
import no.ntnu.qos.server.mediators.TRContext;

/**
 * This is the implementation of TRContext
 * @author Jørgen
 * @author Ola Martin
 *
 */
public class TRContextImpl implements TRContext {
	
	private long initialMessageCapacity = 0;
	private long usedCapacity = 0;
	private long nextEvent = Long.MAX_VALUE;
	private QosContext nextContext = null;
	private final Queue<QosContext> queue = new PriorityQueue<QosContext>();
	
	public TRContextImpl(long availableBandwidth){
		this.initialMessageCapacity = availableBandwidth;
	}

	@Override
	public void add(QosContext qCtx) {
		queue.add(qCtx);
		usedCapacity++;
		long posNext = (long) (qCtx.getEstimatedSendingTime() + System.currentTimeMillis());
		if(posNext < nextEvent){
			nextEvent = posNext;
			nextContext = qCtx;
		}
	}

	@Override
	public long availableBandwidth() {
		return initialMessageCapacity - usedCapacity;
	}

	@Override
	public List<QosContext> preemptContexts(QosContext qCtx) {
		List<QosContext> result = new ArrayList<QosContext>();
		while(this.availableBandwidth() <= 0 && !this.queue.isEmpty()){
			QosContext next = this.queue.peek();
			if(next != null){
				if(next.getPriority() < qCtx.getPriority()){
					//TODO implement fanzy pantsy preempt logic using time
					next.preempt();
					result.add(this.queue.poll());
					usedCapacity--;
				}
			}else{
				break;
			}
		}
		
		return result;
	}

	@Override
	public void clearFinished() {
		List<QosContext> toRemove = new ArrayList<QosContext>();
		for(QosContext qc : this.queue){
			if(qc.isFinishedSending()){
				toRemove.add(qc);
			}
		}
		this.queue.removeAll(toRemove);
		this.usedCapacity -= toRemove.size();
		
		if(toRemove.contains(nextContext)){
			long now = System.currentTimeMillis();
			for(QosContext qc : this.queue){
				if(qc.getEstimatedSendingTime() + now < nextEvent){
					nextEvent = (long) (qc.getEstimatedSendingTime() + now);
					nextContext = qc;
				}
			}
		}
	}

	@Override
	public long nextEvent() {
		return nextEvent - System.currentTimeMillis();
	}
	
	public int size(){
		return this.queue.size();
	}

	@Override
	public void setAvailableBandwidth(long bandwidth) {
		this.initialMessageCapacity = bandwidth;		
	}

}
