package no.ntnu.qos.client.credentials.impl;

import no.ntnu.qos.client.ExceptionHandler;
import no.ntnu.qos.client.Sequencer;
import no.ntnu.qos.client.credentials.SAMLCommunicator;
import no.ntnu.qos.client.credentials.SAMLParser;
import no.ntnu.qos.client.credentials.Token;
import no.ntnu.qos.client.impl.ConfigManager;
import no.ntnu.qos.client.impl.ReceiveObjectImpl;
import no.ntnu.qos.client.net.impl.RequestSOAPAction;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.SocketException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.ParseException;
import org.apache.http.entity.AbstractHttpEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.DefaultHttpClientConnection;
import org.apache.http.message.BasicHttpEntityEnclosingRequest;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.params.SyncBasicHttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpProcessor;
import org.apache.http.protocol.HttpRequestExecutor;
import org.apache.http.protocol.ImmutableHttpProcessor;
import org.apache.http.protocol.RequestConnControl;
import org.apache.http.protocol.RequestContent;
import org.apache.http.protocol.RequestExpectContinue;
import org.apache.http.protocol.RequestTargetHost;
import org.apache.http.protocol.RequestUserAgent;
import org.apache.http.util.EntityUtils;


public class SAMLCommunicatorImpl implements SAMLCommunicator {

	private SAMLParser samlParser;
	private ExceptionHandler exceptionHandler;
	private ReceiveObjectImpl recObj;
	
	//Socket
	private SSLSocket socket;
	
	//HttpCore-related variables
	private HttpParams params;
	private HttpProcessor httpProcessor;
	
	//need an http-thingamajig and possibly other stuff
	private Sequencer sequencer;
	
	private URI destination;
	
	public SAMLCommunicatorImpl(){
		samlParser = new SAMLParserImpl();
	}
	
//	public static void main(String[] args) {
//		
//		CreateAssertion assertionGenerator = new CreateAssertion();
//		String soap = assertionGenerator.createSAML("lalala", "ROLFOL");
//		System.out.println(soap);
//	}
	public Token getToken(URI destination, String userName, String role,
			String password) {
        // TODO write the rest of the method. which probably includes the 
		// network communication with the identity server.
		
		String dest = "https://"+destination.getHost() + destination.getPath();
		try {
			this.destination = new URI("https://"+destination.getHost()+":"+destination.getPort()+"/services/IdentityServer");
		} catch (URISyntaxException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		// Create assertion and wrap it in SOAP, return as String
		CreateAssertion assertionGenerator = new CreateAssertion();
		String soap = assertionGenerator.createSAML(dest, role);
//		String soap = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><S:Envelope xmlns:S=\"http://schemas.xmlsoap.org/soap/envelope/\"><S:Header/><S:Body><ns2:echo xmlns:ns2=\"http://this.should.work.org\"><textToEcho><saml2:Assertion xmlns:saml2=\"urn:oasis:names:tc:SAML:2.0:assertion\" ID=\"abcd1234\" IssueInstant=\"2012-04-18T10:07:27.304Z\" Version=\"2.0\"> <saml2:Issuer>http://example.org</saml2:Issuer> <saml2:Subject> <saml2:NameID Format=\"urn:oasis:names:tc:SAML:1.1:nameid-format:unspecified\" NameQualifier=\"Example Qualifier\">General Curly</saml2:NameID> <saml2:SubjectConfirmation> <saml2:SubjectConfirmationData NotBefore=\"2012-04-18T10:07:27.304Z\" NotOnOrAfter=\"2012-04-18T10:09:27.304Z\" Recipient=\"https://78.91.9.62/services/EchoService\" /> </saml2:SubjectConfirmation> </saml2:Subject> <saml2:Conditions> <saml2:OneTimeUse /> </saml2:Conditions> <saml2:AuthnStatement AuthnInstant=\"2012-04-18T10:07:27.422Z\" SessionIndex=\"abcd1234\" SessionNotOnOrAfter=\"2012-04-18T10:07:27.482Z\"> <saml2:AuthnContext> <saml2:AuthnContextClassRef>urn:oasis:names:tc:SAML:2.0:ac:classes:Password</saml2:AuthnContextClassRef> </saml2:AuthnContext> </saml2:AuthnStatement> <saml2:AttributeStatement> <saml2:Attribute FriendlyName=\"qosClientRole\" Name=\"urn:oid:1.3.6.1.4.1.5923.1.1.1.1\"> <saml2:AttributeValue xmlns:xs=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:type=\"xs:string\">clientRole1</saml2:AttributeValue> </saml2:Attribute> </saml2:AttributeStatement> </saml2:Assertion></textToEcho></ns2:echo></S:Body></S:Envelope>";
		String reply = run(this.destination, soap);
		Token replyToken;
		try {
			replyToken = samlParser.tokenize(reply, destination);
			return replyToken;
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public String run(URI destination, String message) {
		ConfigManager.LOGGER.info("Running message sender to: " 
				+destination.getHost()+destination.getPath());
		setParams();
		createProcessor();
		//Create executor;
		HttpRequestExecutor httpRequestExecutor = new HttpRequestExecutor();
		//Set up host
		HttpHost host = new HttpHost(destination.getHost(), destination.getPort());
		//set up connection;
		DefaultHttpClientConnection conn = new DefaultHttpClientConnection();
		//Set up Context
		HttpContext context = new BasicHttpContext(null);
		context.setAttribute(ExecutionContext.HTTP_CONNECTION, conn);
		context.setAttribute(ExecutionContext.HTTP_TARGET_HOST, host);

		//Set data to be sent!
		HttpEntity body = null;
		try {
			body = new StringEntity(message);
		} catch (UnsupportedEncodingException e) {
			exceptionHandler.unsupportedEncodingExceptionThrown(e);
			ConfigManager.LOGGER.warning("Illegal message syntax");
			try {
				recObj.setReply("UnsupportedEncodingException");
			} catch (InterruptedException e1) {
				ConfigManager.LOGGER.severe("Something went horribly wrong " +
						"while setting reply in receiveObject");
				e1.printStackTrace();
			}
			return e.getLocalizedMessage();
		}
		((AbstractHttpEntity)body).setContentType("text/xml");
		try {
			setupSSLSocket();
		} catch (KeyManagementException e1) {
			//This should never happen
			ConfigManager.LOGGER.severe("KEY MANAGER BROKEN!");
			e1.printStackTrace();
		} catch (NoSuchAlgorithmException e1) {
			// This is near impossible
			ConfigManager.LOGGER.severe("TLS algorithm non-existant!");
			e1.printStackTrace();
		} catch (UnknownHostException e1) {
			exceptionHandler.unknownHostExceptionThrown(e1);
			ConfigManager.LOGGER.warning("Invalid host");
			try {
				recObj.setReply("UnknownHostException");
			} catch (InterruptedException e2) {
				ConfigManager.LOGGER.severe("Something went horribly wrong " +
						"while setting reply in receiveObject");
				e2.printStackTrace();
			}
			return e1.getLocalizedMessage();
		} catch (IOException e1) {
			exceptionHandler.ioExceptionThrown(e1);
			ConfigManager.LOGGER.warning("IO Exception on making SSL socket");
			try {
				recObj.setReply("IOException");
			} catch (InterruptedException e2) {
				ConfigManager.LOGGER.severe("Something went horribly wrong while" +
						" setting reply in receiveObject");
				e2.printStackTrace();
			}
			return e1.getLocalizedMessage();
		}
		//Set Traffic class and get certificate
//		try {
//			socket.setTrafficClass(diffServ);
//		} catch (SocketException e) {
//			exceptionHandler.socketExceptionThrown(e);
//			ConfigManager.LOGGER.warning("Socket Exception while setting traffic class");
//			try {
//				recObj.setReply("SocketException");
//			} catch (InterruptedException e1) {
//				ConfigManager.LOGGER.severe("Something went horribly wrong while setting reply in receiveObject");
//				e1.printStackTrace();
//			}
//			return;
//		}
		try {
			socket.startHandshake();
			//Bind the shiny socket to be used in the connection
			conn.bind(socket, params);
		} catch (IOException e) {
			exceptionHandler.ioExceptionThrown(e);
			ConfigManager.LOGGER.warning("IO exception handshaking or binding" +
					" socket to connection");
			try {
				recObj.setReply("IOException");
			} catch (InterruptedException e1) {
				ConfigManager.LOGGER.severe("Something went horribly wrong while" +
						" setting reply in receiveObject");
				e1.printStackTrace();
			}
			return e.getLocalizedMessage();
		}
		//Create the request, set parameters and insert message into body.
		BasicHttpEntityEnclosingRequest request = 
				new BasicHttpEntityEnclosingRequest("POST", destination.getPath());
		request.setParams(params);
		request.setEntity(body);

		//Pre-process the request, making sure headers are there and are correct
		try {
			httpRequestExecutor.preProcess(request, httpProcessor, context);
		} catch (HttpException e) {
			exceptionHandler.httpExceptionThrown(e);
			ConfigManager.LOGGER.warning("HttpException preprocessing request");
			try {
				recObj.setReply("HttpException");
			} catch (InterruptedException e1) {
				ConfigManager.LOGGER.severe("Something went horribly wrong while" +
						" setting reply in receiveObject");
				e1.printStackTrace();
			}
			return e.getLocalizedMessage();
		} catch (IOException e) {
			exceptionHandler.ioExceptionThrown(e);
			ConfigManager.LOGGER.warning("IOException proprocessing request");
			try {
				recObj.setReply("IOException");
			} catch (InterruptedException e1) {
				ConfigManager.LOGGER.severe("Something went horribly wrong while" +
						" setting reply in receiveObject");
				e1.printStackTrace();
			}
			return e.getLocalizedMessage();
		}
		ConfigManager.LOGGER.info("Ready to send to: " +destination.getHost()
				+destination.getPath());
		//Execute request!
		HttpResponse response = null;
		try {
			response = httpRequestExecutor.execute(request, conn, context);
		} catch (IOException e) {
			exceptionHandler.ioExceptionThrown(e);
			ConfigManager.LOGGER.warning("IOException while executing request," +
					" connection closed?");
			try {
				recObj.setReply("IOException");
			} catch (InterruptedException e1) {
				ConfigManager.LOGGER.severe("Something went horribly wrong while" +
						" setting reply in receiveObject");
				e1.printStackTrace();
			}
			return e.getLocalizedMessage();
		} catch (HttpException e) {
			exceptionHandler.httpExceptionThrown(e);
			ConfigManager.LOGGER.warning("HttpException while executing request," +
					" connection closed?");
			try {
				recObj.setReply("HttpException");
			} catch (InterruptedException e1) {
				ConfigManager.LOGGER.severe("Something went horribly wrong while" +
						" setting reply in receiveObject");
				e1.printStackTrace();
			}
			return e.getLocalizedMessage();
		}
		//Process the response
		ConfigManager.LOGGER.info("Response received from: "+destination.getHost()
				+destination.getPath()+" Processing.");
		try {
			httpRequestExecutor.postProcess(response, httpProcessor, context);
		} catch (HttpException e) {
			exceptionHandler.httpExceptionThrown(e);
			ConfigManager.LOGGER.warning("HttpException processing reply");
			try {
				recObj.setReply("HttpException");
			} catch (InterruptedException e1) {
				ConfigManager.LOGGER.severe("Something went horribly wrong while" +
						" setting reply in receiveObject");
				e1.printStackTrace();
			}
			return e.getLocalizedMessage();
		} catch (IOException e) {
			exceptionHandler.ioExceptionThrown(e);
			ConfigManager.LOGGER.warning("IOException processing reply");
			try {
				recObj.setReply("IOException");
			} catch (InterruptedException e1) {
				ConfigManager.LOGGER.severe("Something went horribly wrong while" +
						" setting reply in receiveObject");
				e1.printStackTrace();
			}
			return e.getLocalizedMessage();
		}

		//Unknown if the reply code is needed by the client
//		String replyCode = response.getStatusLine().toString();
		//Get the body of the reply
		String replyBody = null;
		try {
			replyBody = EntityUtils.toString(response.getEntity());
		} catch (ParseException e) {
			// Service messed up!
			e.printStackTrace();
			ConfigManager.LOGGER.severe("Reply not parseable!");
			try {
				recObj.setReply("ParseException");
			} catch (InterruptedException e1) {
				ConfigManager.LOGGER.severe("Something went horribly wrong while" +
						" setting reply in receiveObject");
				e1.printStackTrace();
			}
			return e.getLocalizedMessage();
		} catch (IOException e) {
			exceptionHandler.ioExceptionThrown(e);
			ConfigManager.LOGGER.warning("IOException while parsing reply!");
			try {
				recObj.setReply("IOException");
			} catch (InterruptedException e1) {
				ConfigManager.LOGGER.severe("Something went horribly wrong while" +
						" setting reply in receiveObject");
				e1.printStackTrace();
			}
			return e.getLocalizedMessage();
		}
		//Close connection
		ConfigManager.LOGGER.info("Processing completed, closing connection");
		try {
			conn.close();
		} catch (IOException e) {
			exceptionHandler.ioExceptionThrown(e);
			ConfigManager.LOGGER.warning("IOException closing connection");
			try {
				recObj.setReply("IOException");
			} catch (InterruptedException e1) {
				ConfigManager.LOGGER.severe("Something went horribly wrong while" +
						" setting reply in receiveObject");
				e1.printStackTrace();
			}
			return e.getLocalizedMessage();
		}
		//Set reply in receiveObject
		ConfigManager.LOGGER.info("Setting reply and forwarding it");
		try {
			recObj.setReply(replyBody);
		} catch (InterruptedException e) {
			// Should only happen if either client or client lib halted!
			e.printStackTrace();
			ConfigManager.LOGGER.severe("Interrupted while setting reply!!");
		}
		//informs the sequencer of a reply
		sequencer.returnData(recObj);
		return replyBody;
	}

	private void setParams() {
		params = new SyncBasicHttpParams();
		HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
		HttpProtocolParams.setContentCharset(params, "UTF-8");
		HttpProtocolParams.setUserAgent(params, "HttpComponents/1.1");
		HttpProtocolParams.setUseExpectContinue(params, false);
		params.setParameter(RequestSOAPAction.SOAP_Action, 
				(destination!=null && destination.getPath()!=null) ? "\""+destination.getPath()+"\"":"");
	}

	private void createProcessor() {
		httpProcessor = new ImmutableHttpProcessor(new HttpRequestInterceptor[] {
				// Required protocol interceptors
				new RequestContent(),
				new RequestTargetHost(),
				// Recommended protocol interceptors
				new RequestConnControl(),
				new RequestUserAgent(),
				new RequestExpectContinue(),
				new RequestSOAPAction()
		});
	}

	private void setupSSLSocket() throws NoSuchAlgorithmException, KeyManagementException, UnknownHostException, IOException {
		SSLContext ctx = SSLContext.getInstance("TLS");
		ctx.init(new KeyManager[0], new TrustManager[] {new PermissiveTrustManager()}, new SecureRandom());
		SSLContext.setDefault(ctx);
		SSLSocketFactory factory = ctx.getSocketFactory(); 
		socket = (SSLSocket)factory.createSocket(destination.getHost(), destination.getPort());
	}

	//WARNING This trust manager accepts ALL certificates!
	private class PermissiveTrustManager implements X509TrustManager {
		@Override
		public java.security.cert.X509Certificate[] getAcceptedIssuers() {
			return null;
		}
		@Override
		public void checkClientTrusted(	java.security.cert.X509Certificate[] arg0, String arg1)	throws CertificateException {	
		}
		@Override
		public void checkServerTrusted(	java.security.cert.X509Certificate[] arg0, String arg1)	throws CertificateException {
		}
	}
}

