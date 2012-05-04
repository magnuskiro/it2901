package no.ntnu.qos.server.mediators.impl;

import org.apache.synapse.MessageContext;
import org.apache.synapse.SynapseLog;

import no.ntnu.qos.server.mediators.AbstractQosMediator;
/**
 * This mediator sets response=true and sets the client as the recipient.
 * This way the Message Context will be sent through 'out' mediator sequence
 * and the send mediator will send it back to the client.
 * @author Ola Martin
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
