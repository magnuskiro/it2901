package no.ntnu.qos.server.mediators.impl;

import org.apache.synapse.MessageContext;
import org.apache.synapse.SynapseLog;

import no.ntnu.qos.server.mediators.AbstractQosMediator;

public class EchoStripperMediator extends AbstractQosMediator {

	@Override
	protected boolean mediateImpl(MessageContext synCtx, SynapseLog synLog) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected String getName() {
		return "EchoStripperMediator";
	}
	
}
