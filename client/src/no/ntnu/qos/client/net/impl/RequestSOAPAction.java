package no.ntnu.qos.client.net.impl;

import java.io.IOException;

import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.ProtocolException;
import org.apache.http.protocol.HttpContext;

public class RequestSOAPAction implements HttpRequestInterceptor {

	public static String SOAP_Action = "SOAPAction";
	
	public RequestSOAPAction() {
		super();
	}
	
	@Override
	public void process(HttpRequest request, HttpContext context)
			throws HttpException, IOException {
		if(request==null){
			throw new IllegalArgumentException("HTTP request may not be null");
		}
		if(request instanceof HttpEntityEnclosingRequest){
			if(request.containsHeader(SOAP_Action)){
				throw new ProtocolException("SOAPAction header already present");				
			}
			if(request.getParams().getParameter(SOAP_Action)!=null){
				request.addHeader(SOAP_Action,(String) request.getParams().getParameter(SOAP_Action));
			}
		}
	}

}
