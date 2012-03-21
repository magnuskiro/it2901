package no.ntnu.qos.client.test;

import static org.junit.Assert.*;

import no.ntnu.qos.client.impl.ReceiveObjectImpl;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class ReplyObjectImplTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Test
	public void test() throws InterruptedException {
		String result = "dumdee";
		ReceiveObjectImpl rec = new ReceiveObjectImpl();
		RunningBugger runner = new RunningBugger(rec, result);
		new Thread(runner).start();
		String test = rec.receive();
		assertTrue(test.equals(result));
	}
	
	
	private class RunningBugger implements Runnable {
		ReceiveObjectImpl recieve;
		String result;
		public RunningBugger(ReceiveObjectImpl rec, String result) {
			this.recieve = rec;
			this.result = result;
		}
		@Override
		public void run() {
			try {
				synchronized (this) {
					this.wait(3000);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			try {
				recieve.setReply(result);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}
}
