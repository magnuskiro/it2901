package no.ntnu.qos.server.mediators.impl;

import java.util.Iterator;

import javax.xml.namespace.QName;

import no.ntnu.qos.server.mediators.AbstractQosMediator;
import no.ntnu.qos.server.mediators.MediatorConstants;
import no.ntnu.qos.server.mediators.QosLogType;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.soap.SOAPEnvelope;
import org.apache.synapse.MessageContext;
import org.apache.synapse.SynapseLog;

/**
 * This mediator is meant to parse the message as it is coming in through
 * the ESB and extract the SAML message to determine the role of the client.
 * This mediator will also extract the service the client is trying to invoke
 * and add both to the properties of the {@link MessageContext} so we can
 * retrieve in the outsequence. 
 * 
 * @author Jørgen
 * @author Ola Martin
 *
 */
public class SAMLMediator extends AbstractQosMediator {
	private static final QName friendlyName = new QName("FriendlyName");
	/**
	 * Set a variable to detach the SAML assertion
	 */
	private static boolean detachAssertion = true;

	@Override
	public boolean mediateImpl(MessageContext synCtx, SynapseLog synLog) {

		final String service = this.getService(synCtx);
		final String clientRole = this.getClientRole(synCtx);

		if(clientRole.isEmpty() || clientRole.trim().isEmpty()){
			this.logMessage(synLog, "Could not " +
					"find a valid client role in SAML assertion.\n" +
					"Envelope was:\n" + synCtx.getEnvelope(), QosLogType.WARN);
			return false;
		}
		if(service.isEmpty() || service.trim().isEmpty()){
			this.logMessage(synLog, "Could not " +
					"find a valid service endpoint in SAML assertion.\n" +
					"Envelope was:\n" + synCtx.getEnvelope(), QosLogType.WARN);
			return false;
		}

		synCtx.setProperty(MediatorConstants.QOS_SERVICE, service);
		synCtx.setProperty(MediatorConstants.QOS_CLIENT_ROLE, clientRole);

		this.logMessage(synLog, "Set client role to: " + 
				clientRole + ", set service to: " + service, QosLogType.INFO);
		if(detachAssertion){
			this.stripSAML(synCtx, synLog);
		}
		return true;
	}

	private void stripSAML(MessageContext synCtx, SynapseLog synLog) {
		OMElement sa = getSAMLAssertion(synCtx.getEnvelope());
		if(sa!=null){
			sa.detach();
			this.logMessage(synLog, "Stripped SAMLAssertion from message", QosLogType.INFO);
		}
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
		if(samlAssertion != null){
			Iterator<OMElement> iter = samlAssertion.getChildElements();
			return getOMElement(iter, "AttributeStatement");
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	private String getClientRole(MessageContext ctx){
		String result = "";
		OMElement as = getSAMLAttributeStatement(getSAMLAssertion(ctx.getEnvelope()));
		if(as != null){
			//This is ugly, but there is no way to create an empty OMElement
			//use null instead
			Iterator<OMElement> iter = as.getChildElements();
			while(iter.hasNext()){
				OMElement attribute = iter.next();
				if(attribute.getAttributeValue(friendlyName).equalsIgnoreCase(MediatorConstants.QOS_CLIENT_ROLE)){
					result = attribute.getFirstElement().getText();
				}
			}
		}
		return result;
	}
	
	private String getService(MessageContext ctx){
		String result = "";
		return result;
	}

	@Override
	protected String getName() {
		return "SAMLMediator";
	}

	public static boolean isDetachAssertion() {
		return detachAssertion;
	}

	public static void setDetachAssertion(boolean detachAssertion) {
		SAMLMediator.detachAssertion = detachAssertion;
	}
}