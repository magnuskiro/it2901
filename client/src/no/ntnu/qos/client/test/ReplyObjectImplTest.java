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
		String result = "This is a string";
		ReceiveObjectImpl receiveObject = new ReceiveObjectImpl();
		RunningBugger runner = new RunningBugger(receiveObject, result);
		new Thread(runner).start();
		String test = receiveObject.receive();
		assertTrue(test.equals(result));
	}

	private class RunningBugger implements Runnable {
		ReceiveObjectImpl receive;
		String result;

		public RunningBugger(ReceiveObjectImpl rec, String result) {
			this.receive = rec;
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
                receive.setReply(result);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}
}
