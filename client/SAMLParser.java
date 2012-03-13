package samlParserTest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.velocity.anakia.NodeList;
import org.joda.time.DateTime;
import org.opensaml.Configuration;
import org.opensaml.DefaultBootstrap;
import org.opensaml.common.SAMLObjectBuilder;
import org.opensaml.common.SAMLVersion;
import org.opensaml.common.binding.BasicSAMLMessageContext;
import org.opensaml.saml1.binding.decoding.HTTPSOAP11Decoder;
import org.opensaml.saml2.core.Assertion;
import org.opensaml.saml2.core.Attribute;
import org.opensaml.saml2.core.AttributeStatement;
import org.opensaml.saml2.core.AttributeValue;
import org.opensaml.saml2.core.AuthnContext;
import org.opensaml.saml2.core.AuthnContextClassRef;
import org.opensaml.saml2.core.AuthnQuery;
import org.opensaml.saml2.core.AuthnStatement;
import org.opensaml.saml2.core.Condition;
import org.opensaml.saml2.core.Conditions;
import org.opensaml.saml2.core.Issuer;
import org.opensaml.saml2.core.NameID;
import org.opensaml.saml2.core.OneTimeUse;
import org.opensaml.saml2.core.Response;
import org.opensaml.saml2.core.Subject;
import org.opensaml.saml2.core.SubjectConfirmation;
import org.opensaml.saml2.core.SubjectConfirmationData;
import org.opensaml.saml2.core.impl.AssertionMarshaller;
import org.opensaml.saml2.core.impl.AuthnContextClassRefBuilder;
import org.opensaml.xml.ConfigurationException;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.XMLObjectBuilder;
import org.opensaml.xml.XMLObjectBuilderFactory;
import org.opensaml.xml.io.Marshaller;
import org.opensaml.xml.io.Unmarshaller;
import org.opensaml.xml.schema.XSString;
import org.opensaml.xml.util.XMLHelper;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.opensaml.ws.message.MessageContext;
import org.opensaml.ws.soap.client.BasicSOAPMessageContext;
import org.opensaml.ws.soap.soap11.Body;
import org.opensaml.ws.soap.soap11.Envelope;
import org.opensaml.ws.soap.soap11.Header;
import org.opensaml.ws.soap.soap11.decoder.SOAP11Decoder;
public class SAMLParser {

	static String strIssuer = "http://example.org";
	static String strNameID = "General Curly";
	static String strNameQualifier = "Example Qualifier";
	static String sessionID = "abcd1234";
	static String strAttrName = "";
	static String strAuthMethod = "";
	static int maxSessionTimeOutInMinutes = 15; /* 15 is default */
	static Map attributes;
	
	public static void main(String[] args) {
		
		try {
			Assertion exampleAssertion = buildExampleAssertion();
			
			/* Print the assertion to standard output */
			AssertionMarshaller marshaller = new AssertionMarshaller();
			Element element = marshaller.marshall(exampleAssertion);
			
			System.out.println("Element and element.getLastChild():");
			System.out.println("------");
			System.out.println(element);
			System.out.println(element.getLastChild());
			System.out.println();
			
			String originalAssertionString = XMLHelper.nodeToString(element);
			System.out.println("XMLHelper.nodeToString(element):");
			System.out.println("------");
			System.out.println(originalAssertionString);
			System.out.println();
			
			System.out.println("XMLHelper.prettyPrintXML(element)");
			System.out.println("------");
			System.out.println(XMLHelper.prettyPrintXML(element));
			
//			buildXMLObject();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
//	public static void buildXMLObject() {
//		XMLObjectBuilderFactory builderFactory = Configuration.getBuilderFactory();
//		
//		SOA
//	}
	
	/* Add new builders in this method as needed */
	public static Assertion buildExampleAssertion() {
		
		/* Set up OpenSAML 2.5 */
		try {
			DefaultBootstrap.bootstrap();
		} catch (ConfigurationException e) {
			e.printStackTrace();
		}
		
		try {
			/* Get builder factory */
			XMLObjectBuilderFactory builderFactory = Configuration.getBuilderFactory();
			
			/* Create the nameidentifier */
			SAMLObjectBuilder nameIdBuilder = (SAMLObjectBuilder) builderFactory.getBuilder(NameID.DEFAULT_ELEMENT_NAME);
			NameID nameId = (NameID) nameIdBuilder.buildObject();
			nameId.setValue(strNameID);
			nameId.setNameQualifier(strNameQualifier);
			nameId.setFormat(NameID.UNSPECIFIED);
			
			/* Get timestamp */
			DateTime now = new DateTime();
			
			/* Create subjectconfirmation */
			SAMLObjectBuilder confirmationMethodBuilder = (SAMLObjectBuilder) builderFactory.getBuilder(SubjectConfirmationData.DEFAULT_ELEMENT_NAME);
			SubjectConfirmationData confirmationMethod = (SubjectConfirmationData) confirmationMethodBuilder.buildObject();
			confirmationMethod.setNotBefore(now);
			confirmationMethod.setNotOnOrAfter(now.plusMinutes(2));
			
			SAMLObjectBuilder subjectConfirmationBuilder = (SAMLObjectBuilder) builderFactory.getBuilder(SubjectConfirmation.DEFAULT_ELEMENT_NAME);
			SubjectConfirmation subjectConfirmation = (SubjectConfirmation) subjectConfirmationBuilder.buildObject();
			subjectConfirmation.setSubjectConfirmationData(confirmationMethod);
			
			/* Create the subject */
			SAMLObjectBuilder subjectBuilder = (SAMLObjectBuilder) builderFactory.getBuilder(Subject.DEFAULT_ELEMENT_NAME);
			Subject subject = (Subject) subjectBuilder.buildObject();
			subject.setNameID(nameId);
			subject.getSubjectConfirmations().add(subjectConfirmation);
			
			/* Create authentication statement */
			SAMLObjectBuilder authnStatementBuilder = (SAMLObjectBuilder) builderFactory.getBuilder(AuthnStatement.DEFAULT_ELEMENT_NAME);
			AuthnStatement authnStatement = (AuthnStatement) authnStatementBuilder.buildObject();
			
			DateTime now2 = new DateTime();		/* New timestamp */
			authnStatement.setAuthnInstant(now2);
			authnStatement.setSessionIndex(sessionID);
			authnStatement.setSessionNotOnOrAfter(now2.plus(maxSessionTimeOutInMinutes));
			
			SAMLObjectBuilder authContextBuilder = (SAMLObjectBuilder) builderFactory.getBuilder(AuthnContext.DEFAULT_ELEMENT_NAME);
			AuthnContext authnContext = (AuthnContext) authContextBuilder.buildObject();
			
			SAMLObjectBuilder authContextClassRefBuilder = (SAMLObjectBuilder) builderFactory.getBuilder(AuthnContextClassRef.DEFAULT_ELEMENT_NAME);
			AuthnContextClassRef authnContextClassRef = (AuthnContextClassRef) authContextClassRefBuilder.buildObject();
			authnContextClassRef.setAuthnContextClassRef("urn:oasis:names:tc:SAML:2.0:ac:classes:Password");
			
			authnContext.setAuthnContextClassRef(authnContextClassRef);
			authnStatement.setAuthnContext(authnContext);
			
			/* Builder attributes */
			SAMLObjectBuilder attrStatementBuilder = (SAMLObjectBuilder) builderFactory.getBuilder(AttributeStatement.DEFAULT_ELEMENT_NAME);
			AttributeStatement attrStatement = (AttributeStatement) attrStatementBuilder.buildObject();
			
			/* Create the do-not-cache condition */
			SAMLObjectBuilder doNotCacheConditionBuilder = (SAMLObjectBuilder) builderFactory.getBuilder(OneTimeUse.DEFAULT_ELEMENT_NAME);
			Condition condition = (Condition) doNotCacheConditionBuilder.buildObject();
			
			SAMLObjectBuilder conditionsBuilder = (SAMLObjectBuilder) builderFactory.getBuilder(Conditions.DEFAULT_ELEMENT_NAME);
			Conditions conditions = (Conditions) conditionsBuilder.buildObject();
			conditions.getConditions().add(condition);
			
			/* Create issuer */
			SAMLObjectBuilder issuerBuilder = (SAMLObjectBuilder) builderFactory.getBuilder(Issuer.DEFAULT_ELEMENT_NAME);
			Issuer issuer = (Issuer) issuerBuilder.buildObject();
			issuer.setValue(strIssuer);
			
			/* Create assertion */
			SAMLObjectBuilder assertionBuilder = (SAMLObjectBuilder) builderFactory.getBuilder(Assertion.DEFAULT_ELEMENT_NAME);
			Assertion assertion = (Assertion) assertionBuilder.buildObject();
			assertion.setIssuer(issuer);
			assertion.setSubject(subject);
			assertion.setIssueInstant(now);
			assertion.setVersion(SAMLVersion.VERSION_20);
			
			assertion.getAuthnStatements().add(authnStatement);
			assertion.getAttributeStatements().add(attrStatement);
			assertion.setConditions(conditions);
			
			return assertion;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
