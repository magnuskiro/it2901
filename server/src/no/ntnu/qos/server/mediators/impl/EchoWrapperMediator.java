package no.ntnu.qos.server.mediators.impl;

import java.io.ByteArrayInputStream;

import no.ntnu.qos.server.mediators.AbstractQosMediator;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMXMLBuilderFactory;
import org.apache.synapse.MessageContext;
import org.apache.synapse.SynapseLog;

/**
 * This mediator is used to wrap the SAML-Assertion, to make it compatible with the SAMLEcho Service.
 * @author mahou
 *
 */
public class EchoWrapperMediator extends AbstractQosMediator {
	private final static String wrapping = 
			"<ns2:echo xmlns:ns2=\"http://this.should.work.org\">" +
			"<textToEcho></textToEcho>" +
			"</ns2:echo>";

	@Override
	protected boolean mediateImpl(MessageContext synCtx, SynapseLog synLog) {
		if(synCtx.getEnvelope()!=null && synCtx.getEnvelope().getBody()!=null){
			if(synCtx.getEnvelope().getHeader()==null){
				synCtx.getEnvelope().addChild(OMAbstractFactory.getSOAP12Factory().createSOAPHeader());
			}
			OMElement saml = synCtx.getEnvelope().getBody().getFirstElement();
			OMElement echo = (OMElement) OMXMLBuilderFactory.createOMBuilder(
					new ByteArrayInputStream(wrapping.getBytes())).getDocumentElement();
			if(saml!=null){
				saml.detach();
				echo.getFirstElement().addChild(saml);
				synCtx.getEnvelope().getBody().addChild(echo);
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
