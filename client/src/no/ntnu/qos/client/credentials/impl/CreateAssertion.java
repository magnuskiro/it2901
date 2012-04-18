package no.ntnu.qos.client.credentials.impl;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.joda.time.DateTime;
import org.opensaml.Configuration;
import org.opensaml.DefaultBootstrap;
import org.opensaml.common.SAMLVersion;
import org.opensaml.saml2.core.Assertion;
import org.opensaml.saml2.core.Attribute;
import org.opensaml.saml2.core.AttributeStatement;
import org.opensaml.saml2.core.AttributeValue;
import org.opensaml.saml2.core.AuthnContext;
import org.opensaml.saml2.core.AuthnContextClassRef;
import org.opensaml.saml2.core.AuthnStatement;
import org.opensaml.saml2.core.Condition;
import org.opensaml.saml2.core.Conditions;
import org.opensaml.saml2.core.Issuer;
import org.opensaml.saml2.core.NameID;
import org.opensaml.saml2.core.Subject;
import org.opensaml.saml2.core.SubjectConfirmation;
import org.opensaml.saml2.core.SubjectConfirmationData;
import org.opensaml.saml2.core.impl.AssertionBuilder;
import org.opensaml.saml2.core.impl.AttributeBuilder;
import org.opensaml.saml2.core.impl.AttributeStatementBuilder;
import org.opensaml.saml2.core.impl.AuthnContextBuilder;
import org.opensaml.saml2.core.impl.AuthnContextClassRefBuilder;
import org.opensaml.saml2.core.impl.AuthnStatementBuilder;
import org.opensaml.saml2.core.impl.ConditionsBuilder;
import org.opensaml.saml2.core.impl.IssuerBuilder;
import org.opensaml.saml2.core.impl.NameIDBuilder;
import org.opensaml.saml2.core.impl.OneTimeUseBuilder;
import org.opensaml.saml2.core.impl.SubjectBuilder;
import org.opensaml.saml2.core.impl.SubjectConfirmationBuilder;
import org.opensaml.saml2.core.impl.SubjectConfirmationDataBuilder;
import org.opensaml.ws.soap.soap11.Body;
import org.opensaml.ws.soap.soap11.Envelope;
import org.opensaml.ws.soap.soap11.Header;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.XMLObjectBuilder;
import org.opensaml.xml.XMLObjectBuilderFactory;
import org.opensaml.xml.io.Marshaller;
import org.opensaml.xml.schema.XSString;
import org.opensaml.xml.util.XMLHelper;
import org.w3c.dom.Document;


public class CreateAssertion {
	
	static String strIssuer = "http://example.org";
	static String strNameID = "General Curly";
	static String strNameQualifier = "Example Qualifier";
	static String sessionID = "abcd1234";
	static String strAttrName = "";
	static String strAuthMethod = "";
	static int maxSessionTimeOutInMinutes = 60;
	static String authnContext = "urn:oasis:names:tc:SAML:2.0:ac:classes:Password";

	public String createSAML(String dest, String role) {
		String destination = dest;
		String friendlyName = "qosClientRole";
		String clientRole = role;
		
		Assertion myAssertion = createAssertion(destination, friendlyName, clientRole);
		Envelope envelope = buildSOAP11Envelope(myAssertion);
		
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder;
		try {
			builder = factory.newDocumentBuilder();
			Document dom = builder.newDocument();
			Marshaller out = Configuration.getMarshallerFactory ().getMarshaller(envelope);
			out.marshall(envelope, dom);
		String result = XMLHelper.prettyPrintXML(dom);
		return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
        return null;
	}
	
    private static Envelope buildSOAP11Envelope(XMLObject payload) {
        XMLObjectBuilderFactory bf = Configuration.getBuilderFactory();
        Envelope envelope = (Envelope) bf.getBuilder(Envelope.DEFAULT_ELEMENT_NAME).buildObject(Envelope.DEFAULT_ELEMENT_NAME);
        Body body = (Body) bf.getBuilder(Body.DEFAULT_ELEMENT_NAME).buildObject(Body.DEFAULT_ELEMENT_NAME);
        Header header = (Header) bf.getBuilder(Header.DEFAULT_ELEMENT_NAME).buildObject(Header.DEFAULT_ELEMENT_NAME);
        
        header.setSchemaLocation("hah");
        body.getUnknownXMLObjects().add(payload);
        envelope.setBody(body);
        envelope.setHeader(header);
        
        return envelope;
    }
	
	@SuppressWarnings("rawtypes")
	private Assertion createAssertion (String destination, String fName, String role) {
		
		String recipient = destination;
		String friendlyName = fName;
		String clientRole = role;
		try {
			DefaultBootstrap.bootstrap();

			/* Create NameID */
			NameIDBuilder nameIDBuilder = new NameIDBuilder();
			NameID myNameID = nameIDBuilder.buildObject();
			myNameID.setValue(strNameID);
			myNameID.setFormat(NameID.UNSPECIFIED);
			myNameID.setNameQualifier(strNameQualifier);
			
			/* Create timestamp */
			DateTime now = new DateTime();
			
			/* Create subject confirmation */
			SubjectConfirmationDataBuilder subjectConfirmationDataBuilder = 
					new SubjectConfirmationDataBuilder();
			SubjectConfirmationData mySubjectConfirmationData = 
					subjectConfirmationDataBuilder.buildObject();
			mySubjectConfirmationData.setNotBefore(now);
			mySubjectConfirmationData.setNotOnOrAfter(now.plusMinutes(maxSessionTimeOutInMinutes));
			mySubjectConfirmationData.setRecipient(recipient);
			
			SubjectConfirmationBuilder subjectConfirmationBuilder =
					new SubjectConfirmationBuilder();
			SubjectConfirmation mySubjectConfirmation = 
					subjectConfirmationBuilder.buildObject();
			mySubjectConfirmation.setSubjectConfirmationData(mySubjectConfirmationData);
			
			/* Create subject */
			SubjectBuilder subjectBuilder = new SubjectBuilder();
			Subject mySubject = subjectBuilder.buildObject();
			mySubject.setNameID(myNameID);
			mySubject.getSubjectConfirmations().add(mySubjectConfirmation);
			
			/* Create authentication statement */
			AuthnStatementBuilder authnStatementBuilder = 
					new AuthnStatementBuilder();
			AuthnStatement myAuthnStatement = 
					authnStatementBuilder.buildObject();
			
			DateTime now2 = new DateTime();
			myAuthnStatement.setAuthnInstant(now2);
			myAuthnStatement.setSessionIndex(sessionID);
			myAuthnStatement.setSessionNotOnOrAfter(now2.
					plus(maxSessionTimeOutInMinutes));
			
			AttributeStatementBuilder attributeStatementBuilder =
					new AttributeStatementBuilder();
			AttributeStatement myAttributeStatement = 
					attributeStatementBuilder.buildObject();
			AttributeBuilder myAttributeBuilder = new AttributeBuilder();
			Attribute myAttribute = myAttributeBuilder.buildObject();
			myAttribute.setFriendlyName(friendlyName);
			myAttribute.setName("urn:oid:1.3.6.1.4.1.5923.1.1.1.1");
			
			XMLObjectBuilderFactory builderFactory = Configuration.getBuilderFactory();
			XMLObjectBuilder stringBuilder = builderFactory.getBuilder(XSString.TYPE_NAME);
			XSString attrNewValue = (XSString) stringBuilder.buildObject(AttributeValue.DEFAULT_ELEMENT_NAME, XSString.TYPE_NAME);
			attrNewValue.setValue(clientRole);

			myAttribute.getAttributeValues().add(attrNewValue);
			myAttributeStatement.getAttributes().add(myAttribute);
			
			
			AuthnContextBuilder authnContextBuilder = 
					new AuthnContextBuilder();
			AuthnContext myAuthnContext = authnContextBuilder.buildObject();
			
			AuthnContextClassRefBuilder authnContextClassRefBuilder =
					new AuthnContextClassRefBuilder();
			AuthnContextClassRef myAuthnContextClassRef =
					authnContextClassRefBuilder.buildObject();
			myAuthnContextClassRef.setAuthnContextClassRef(authnContext);
			
			myAuthnContext.setAuthnContextClassRef(myAuthnContextClassRef);
			myAuthnStatement.setAuthnContext(myAuthnContext);
			
			/* Create the do-not-cache condition */
			OneTimeUseBuilder oneTimeUseBuilder = new OneTimeUseBuilder();
			Condition myCondition = oneTimeUseBuilder.buildObject();
			
			ConditionsBuilder conditionsBuilder = new ConditionsBuilder();
			Conditions conditions = conditionsBuilder.buildObject();
			conditions.getConditions().add(myCondition);
			
			/* Create issuer */
			IssuerBuilder issuerBuilder = new IssuerBuilder();
			Issuer myIssuer = issuerBuilder.buildObject();
			myIssuer.setValue(strIssuer);
			
			/* Create assertion */
			AssertionBuilder assertionBuilder = new AssertionBuilder();
			Assertion assertion = assertionBuilder.buildObject();
			assertion.setVersion(SAMLVersion.VERSION_20);
			assertion.setID(sessionID);
			assertion.setIssueInstant(now);
			assertion.setIssuer(myIssuer);
			assertion.setSubject(mySubject);
			assertion.getAuthnStatements().add(myAuthnStatement);
			assertion.setConditions(conditions);
			assertion.getAttributeStatements().add(myAttributeStatement);
			
			return assertion;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
}
