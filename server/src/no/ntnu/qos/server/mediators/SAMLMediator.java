package no.ntnu.qos.server.mediators;

import org.apache.synapse.MessageContext;
import org.apache.synapse.mediators.AbstractMediator;

/**
 * This mediator is meant to parse the message as it is coming in through
 * the ESB and extract the SAML message to determine the role of the client.
 * This mediator will also extract the service the client is trying to invoke
 * and add both to the properties of the {@link MessageContext} so we can
 * retrieve in the outsequence. 
 * 
 * @author Jørgen & Ola Martin
 *
 */
public class SAMLMediator extends AbstractMediator {

	@Override
	public boolean mediate(MessageContext synCtx) {
		// TODO Auto-generated method stub
		return false;
	}

}
