package no.ntnu.qos.server.mediators.impl;

import no.ntnu.qos.server.mediators.AbstractQosMediator;

import org.apache.axiom.om.OMElement;
import org.apache.synapse.MessageContext;
import org.apache.synapse.SynapseLog;

/**
 * This mediator is used to strip unwanted parts wrapping the SAML-Assertion.
 * @author mahou
 *
 */
public class EchoStripperMediator extends AbstractQosMediator {

	@Override
	protected boolean mediateImpl(MessageContext synCtx, SynapseLog synLog) {
		if(synCtx.getEnvelope()!=null && synCtx.getEnvelope().getBody()!=null){
			OMElement echo = synCtx.getEnvelope().getBody().getFirstElement();
			if(echo!=null){
				OMElement saml = echo.getFirstElement().getFirstElement();
				synCtx.getEnvelope().getBody().addChild(saml);				
				echo.detach();
				return true;
			}
		}
		return false;
	}

	@Override
	protected String getName() {
		return "EchoStripperMediator";
	}
	
}
