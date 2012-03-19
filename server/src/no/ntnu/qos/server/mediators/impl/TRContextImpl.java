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
 *
 */
public class TRContextImpl implements TRContext {
	
	private long initialMessageCapacity = 0;
	private long usedCapacity = 0;
	private final Queue<QosContext> queue = new PriorityQueue<QosContext>();
	
	public TRContextImpl(long availableBandwidth){
		this.initialMessageCapacity = availableBandwidth;
	}

	@Override
	public void add(QosContext qCtx) {
		queue.add(qCtx);
		usedCapacity++;
	}

	@Override
	public long availableBandwidth() {
		return initialMessageCapacity - usedCapacity;
	}

	@Override
	public List<QosContext> preemptContexts(QosContext qCtx) {
		List<QosContext> result = new ArrayList<QosContext>();
		while(this.availableBandwidth() <= 0){
			if(this.queue.peek().getPriority() < qCtx.getPriority()){
				
			}
		}
		
		return result;
	}

	@Override
	public void clearFinished() {
		// TODO Auto-generated method stub

	}

	@Override
	public long nextEvent() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	public int size(){
		return this.queue.size();
	}

	@Override
	public void setAvailableBandwidth(long bandwidth) {
		this.initialMessageCapacity = bandwidth;		
	}

}
