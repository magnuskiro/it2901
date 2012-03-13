package no.ntnu.qos.server.mediators;

import java.util.Iterator;

import javax.xml.namespace.QName;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.soap.SOAPEnvelope;
import org.apache.synapse.MessageContext;
import org.apache.synapse.SynapseLog;
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

	private SynapseLog synLog;
	private static final QName friendlyName = new QName("FriendlyName"); 

	@Override
	public boolean mediate(MessageContext synCtx) {
		synLog = this.getLog(synCtx);

		if(synLog.isTraceOrDebugEnabled()){
			synLog.traceOrDebug(MediatorConstants.DEBUG_START + "SAML Mediator");
			if (synLog.isTraceTraceEnabled()) {
				synLog.traceTrace("Message : " + synCtx.getEnvelope());
			}
		}

		final String service = synCtx.getTo().getAddress();
		final String clientRole = this.getClientRole(synCtx);

		if(clientRole.isEmpty() || clientRole.trim().isEmpty()){
			if(synLog.isTraceOrDebugEnabled()){
				synLog.traceOrDebugWarn(MediatorConstants.DEBUG_ERROR + "Could not " +
						"find a valid client role in SAML assertion.\n" +
						"Envelope was:\n" + synCtx.getEnvelope());
			}
			return false;
		}

		synCtx.setProperty(MediatorConstants.QOS_SERVICE, service);
		synCtx.setProperty(MediatorConstants.QOS_CLIENT_ROLE, clientRole);

		if(synLog.isTraceOrDebugEnabled()){
			synLog.traceOrDebug(MediatorConstants.DEBUG_END + "Set client role to: " + 
					clientRole + ", set service to: " + service);
		}
		return true;
	}

	@SuppressWarnings("unchecked")
	private OMElement getSAMLAssertion(SOAPEnvelope soapEnvl){
		Iterator<OMElement> iter = soapEnvl.getBody().getChildElements();
		return getOMElement(iter, "Assertion");
	}
	
	private OMElement getOMElement(final Iterator<OMElement> iter, final String localName){
		OMElement result = null;
		while(iter.hasNext()){
			OMElement ch = (OMElement) iter.next();
			if(ch.getLocalName().equalsIgnoreCase(localName)){
				result = ch;
				break;
			}
		}
		return result;
	}
	
	@SuppressWarnings("unchecked")
	private OMElement getSAMLAttributeStatement(OMElement samlAssertion){
		Iterator<OMElement> iter = samlAssertion.getChildElements();
		return getOMElement(iter, "AttributeStatement");
	}

	@SuppressWarnings("unchecked")
	private String getClientRole(MessageContext ctx){
		String result = "";
		OMElement as = getSAMLAttributeStatement(getSAMLAssertion(ctx.getEnvelope()));
		Iterator<OMElement> iter = as.getChildElements();
		while(iter.hasNext()){
			OMElement attribute = iter.next();
			if(attribute.getAttributeValue(friendlyName).equalsIgnoreCase(MediatorConstants.QOS_CLIENT_ROLE)){
				result = attribute.getFirstElement().getText();
			}
		}
		return result;
	}

}
