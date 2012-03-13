package no.ntnu.qos.server.mediators;

import java.io.IOException;

import org.apache.synapse.MessageContext;
import org.apache.synapse.core.axis2.Axis2MessageContext;
import org.apache.synapse.mediators.AbstractMediator;
/**
 * This mediator does throttle related things.
 * @author Ola Martin
 *
 */
public class ThrottleMediator extends AbstractMediator{
	
	@Override
	public boolean mediate(MessageContext synCtx) {
		// TODO Throttling
		try {
			System.out.println(((Axis2MessageContext)synCtx).getAxis2MessageContext()
					.getInboundContentLength());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}
}
