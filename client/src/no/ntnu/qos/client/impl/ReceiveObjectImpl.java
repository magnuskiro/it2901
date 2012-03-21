package no.ntnu.qos.client.impl;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import no.ntnu.qos.client.ReceiveObject;

/**
 * @author Magnus Kirø
 *
 * The implementation of the ReceiveObject.
 */
public class ReceiveObjectImpl implements ReceiveObject {
	boolean buffered;
	BlockingQueue<String> reply;
	String bufferedReply;
	public ReceiveObjectImpl() {
		buffered = false;
		reply = new LinkedBlockingQueue<String>();
	}
	public void setReply(String rep) throws InterruptedException {
		reply.put(rep);
	}
    @Override
    public String receive() throws InterruptedException {
    	if(buffered) {
    		return bufferedReply;
    	}
    	bufferedReply = reply.take();
    	buffered = true;
    	return bufferedReply;
    }
}
