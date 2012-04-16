package no.ntnu.qos.client.credentials.impl;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import org.apache.axiom.om.OMAttribute;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.impl.builder.StAXOMBuilder;
import no.ntnu.qos.client.credentials.SAMLParser;
import no.ntnu.qos.client.credentials.Token;

public class SAMLParserImpl implements SAMLParser{

	@SuppressWarnings("unchecked")
	@Override
	public Token tokenize(String tokenString, URI destination) throws UnsupportedEncodingException {
		ByteArrayInputStream stream = new ByteArrayInputStream(tokenString.getBytes());
		StAXOMBuilder builder = null;
		try {
			builder = new StAXOMBuilder(stream);
		} catch (XMLStreamException e) {
			// TODO Throw exceptions and stuff!
			e.printStackTrace();
		}
		OMElement root = builder.getDocumentElement();
				
		OMElement header = null;
		OMElement qosElem = null;
		OMElement diffElem = null;
		
		//Get relevant Header elements
		Iterator<OMElement> iter = null;
		iter = root.getChildrenWithLocalName("Header");
		if(iter.hasNext()) {
			header = iter.next();
			iter = header.getChildrenWithLocalName("qosPriority");
			if(iter.hasNext()) {
				qosElem = iter.next();
			}
			iter = header.getChildrenWithLocalName("qosDiffserv");
			if(iter.hasNext()) {
				diffElem = iter.next();
			} 
		} 
	
		//Get assertion
		OMElement body = null;
		OMElement assertion = null;
		OMElement auth = null;
		Date date = null;
		iter = root.getChildrenWithLocalName("Body");
		if(iter.hasNext()) {
			body = iter.next();
			iter = body.getChildrenWithLocalName("Assertion");
			if(iter.hasNext()) {
				assertion = iter.next();
				//Get element containing SessionNotOnOrAfter
				iter = assertion.getChildrenWithLocalName("AuthnStatement");
				if(iter.hasNext()) {
					auth = iter.next();
					OMAttribute notOnOrAfter = auth.getAttribute(new QName("SessionNotOnOrAfter"));
					String validUntilString = notOnOrAfter.getAttributeValue();
					//fixing formating issue
					validUntilString = validUntilString.replace("Z", "+0000");
					try {
						date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").parse(validUntilString);
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			}
		}
		}
		
		//If all the required elements were available
		if(qosElem!=null && diffElem!=null && assertion!=null && date!=null) {
			//Make sure it's built and nice first!
			assertion.build();
			assertion.toString();
			Token token = new TokenImpl(assertion, date.getTime(), destination);
			token.setDiffServ(Integer.parseInt(diffElem.getText()));
			token.setPriority(Integer.parseInt(qosElem.getText()));
			return token;
		} else {
			throw new UnsupportedEncodingException();
		}
	}

}
