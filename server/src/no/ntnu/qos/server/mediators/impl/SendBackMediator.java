package no.ntnu.qos.server.mediators.impl;

import org.apache.synapse.MessageContext;
import org.apache.synapse.SynapseLog;

import no.ntnu.qos.server.mediators.AbstractQosMediator;
/**
 * This mediator is used to reply to the client, basically just echos.
 * @author mahou
 *
 */
public class SendBackMediator extends AbstractQosMediator {

	@Override
	protected boolean mediateImpl(MessageContext synCtx, SynapseLog synLog) {
		synCtx.setResponse(true);
        synCtx.setTo(synCtx.getReplyTo());
        return true;
	}

	@Override
	protected String getName() {
		return "SendBackMediator";
	}

}
