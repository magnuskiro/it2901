package no.ntnu.qos.server.mediators.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicLong;

import no.ntnu.qos.server.mediators.QosContext;
import no.ntnu.qos.server.mediators.TRContext;

/**
 * This is the implementation of TRContext
 * @author Jørgen
 * @author Ola Martin
 *
 */
public class TRContextImpl implements TRContext {

	private long usedCapacity = 0;
	private long nextEvent = Long.MAX_VALUE;
	private final Queue<QosContext> queue = new PriorityQueue<QosContext>();
	private final AtomicLong initialMessageCapacity;

	public TRContextImpl(long availableBandwidth){
		if(availableBandwidth > 0){
			initialMessageCapacity = new AtomicLong(availableBandwidth);
		}else{
			throw new IllegalArgumentException("Available bandwidth must " +
					"be larger than 0");
		}
	}

	@Override
	public void add(QosContext qCtx) {
		synchronized (queue) {
			queue.add(qCtx);
			usedCapacity++;
			long posNext = (long) (qCtx.getEstimatedSendingTime() + System.currentTimeMillis());
			if(posNext < nextEvent){
				nextEvent = posNext;
			}
		}
	}

	@Override
	public long availableBandwidth() {
		synchronized (initialMessageCapacity) {
			return initialMessageCapacity.get() - usedCapacity;
		}
	}

	@Override
	public List<QosContext> preemptContexts(QosContext qCtx) {
		if(qCtx != null){
			synchronized (queue) {
				List<QosContext> result = new ArrayList<QosContext>();
				while(this.availableBandwidth() <= 0 && !this.queue.isEmpty()){
					QosContext next = this.queue.peek();
					if(next != null){
						if(next.getPriority() < qCtx.getPriority()){
							//TODO implement fanzy pantsy preempt logic using time
							next.preempt();
							result.add(this.queue.poll());
							usedCapacity--;
						}else{
							break;
						}
					}else{
						break;
					}
				}

				return result;
			}
		}else{
			throw new IllegalArgumentException("Argument can not be null");
		}
	}

	@Override
	public void clearFinished() {
		synchronized (queue) {
			List<QosContext> toRemove = new ArrayList<QosContext>();
			for(QosContext qc : this.queue){
				if(qc.isFinishedSending()){
					toRemove.add(qc);
				}
			}
			if(toRemove.size() > 0){
				this.queue.removeAll(toRemove);
				this.usedCapacity -= toRemove.size();
			}

			long now = System.currentTimeMillis();
			long nextEv = Long.MAX_VALUE;
			for(QosContext qc : this.queue){
				if(qc.getEstimatedSendingTime() + now < nextEv){
					nextEv = (long) (qc.getEstimatedSendingTime() + now);
				}
			}
			nextEvent = nextEv;
		}
	}

	@Override
	public long nextEvent() {
		long n = nextEvent - System.currentTimeMillis();
		return n >= 0 ? n : 0;
	}

	public int size(){
		return this.queue.size();
	}

	@Override
	public void setAvailableBandwidth(long bandwidth) {
		synchronized (initialMessageCapacity) {
			if(bandwidth > 0){
				this.initialMessageCapacity.set(bandwidth);
			}else{
				throw new IllegalArgumentException("TR bandwidth can not be less " +
						"than one, argument was: " + bandwidth);
			}
		}
	}

}
