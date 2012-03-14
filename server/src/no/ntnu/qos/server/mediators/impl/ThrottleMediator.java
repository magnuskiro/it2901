package no.ntnu.qos.server.mediators.impl;

import java.io.IOException;

import no.ntnu.qos.server.mediators.AbstractQosMediator;

import org.apache.synapse.MessageContext;
import org.apache.synapse.core.axis2.Axis2MessageContext;
/**
 * This mediator does throttle related things.
 * @author Ola Martin
 *
 */
public class ThrottleMediator extends AbstractQosMediator{

	@Override
	protected boolean mediateImpl(MessageContext synCtx) {
		// TODO Throttling
		try {
			System.out.println(((Axis2MessageContext)synCtx).getAxis2MessageContext()
					.getInboundContentLength());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	@Override
	protected String getName() {
		return "Throttle Mediator";
	}
}
