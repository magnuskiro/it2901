package no.ntnu.qos.server.store;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.PriorityQueue;

import no.ntnu.qos.server.mediators.MediatorConstants;

import org.apache.synapse.MessageContext;
import org.apache.synapse.message.store.AbstractMessageStore;

/**
 * This message store prioritizes {@link MessageContext} 
 * on {@link QOS_PRIORITY} and {@link QOS_TIME_ADDED}
 * @author Ola Martin & Jørgen
 *
 */
public class PrioritizedMessageStore extends AbstractMessageStore {

	private final PriorityQueue<MessageContext> queue;
	
	public PrioritizedMessageStore() {
		super();
		queue = new PriorityQueue<MessageContext>(100, new Comparator<MessageContext>() {

			@Override
			public int compare(MessageContext mc1, MessageContext mc2) {
				long mc1time = (Long)mc1.getProperty(MediatorConstants.QOS_TIME_ADDED);
				long mc2time = (Long)mc2.getProperty(MediatorConstants.QOS_TIME_ADDED);
				int mc1pri = (Integer)mc1.getProperty(MediatorConstants.QOS_PRIORITY);
				int mc2pri = (Integer)mc2.getProperty(MediatorConstants.QOS_PRIORITY);
				if(mc1pri>mc2pri){
					return -1;
				}else if(mc2pri>mc1pri){
					return 1;
				}else if(mc1time>mc2time){
					return 1;
				}else if(mc2time>mc1time){
					return -1;
				}
				return 0;
			}
		});
	}
	
	@Override
	public void clear() {
		queue.clear();
	}

	@Override
	public MessageContext get(int index) {
		//Probably not used at all.
		Iterator<MessageContext> ittr = queue.iterator();
		int i = 0;
		MessageContext result = null;
		while(ittr.hasNext()){
			if(i==index){
				result = ittr.next();
			}else{
				ittr.next();
			}
			i++;
		}
		return result;
	}

	@Override
	public MessageContext get(String arg0) {
		//SHOULD NOT BE USED...
		return null;
	}

	@Override
	public List<MessageContext> getAll() {
		List<MessageContext> result = new ArrayList<MessageContext>();
		Iterator<MessageContext> ittr = queue.iterator();
		while(ittr.hasNext()){
			result.add(ittr.next());
		}
		return result;
	}

	@Override
	public boolean offer(MessageContext mc) {
		return queue.offer(mc);
		
	}

	@Override
	public MessageContext peek() {
		return queue.peek();
	}

	@Override
	public MessageContext poll() {
		return queue.poll();
	}

	@Override
	public MessageContext remove() throws NoSuchElementException {
		return queue.remove();
	}

	@Override
	public MessageContext remove(String arg0) {
		//SHOULD NOT BE USED
		return null;
	}

}
