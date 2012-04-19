package no.ntnu.qos.client.credentials.impl;

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

import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import no.ntnu.qos.client.DataObject;
import no.ntnu.qos.client.ExceptionHandler;
import no.ntnu.qos.client.credentials.SAMLCommunicator;
import no.ntnu.qos.client.credentials.SAMLParser;
import no.ntnu.qos.client.credentials.Token;
import no.ntnu.qos.client.impl.ConfigManager;
import no.ntnu.qos.client.impl.ReceiveObjectImpl;
import no.ntnu.qos.client.net.impl.RequestSOAPAction;

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
	
	//Socket
	private SSLSocket socket;
	
	//HttpCore-related variables
	private HttpParams params;
	private HttpProcessor httpProcessor;
	
	private URI destination;
	
	public SAMLCommunicatorImpl(){
		samlParser = new SAMLParserImpl();
	}
	

	public Token getToken(URI destination, String userName, String role,
			String password, DataObject dataObj) throws Exception {
		exceptionHandler = dataObj.getExceptionHandler();

		
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
		String reply = run(this.destination, soap, (ReceiveObjectImpl)dataObj.getReceiveObject());
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
	
	public String run(URI destination, String message, ReceiveObjectImpl recObj) throws Exception {
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
			try {
				recObj.setReply("Illegal message syntax");
			} catch (InterruptedException e1) {
				ConfigManager.LOGGER.severe("Something went horribly wrong while setting reply in receiveObject");
				e1.printStackTrace();
			}
			exceptionHandler.unsupportedEncodingExceptionThrown(e);
			ConfigManager.LOGGER.warning("Illegal message syntax");
			throw new Exception("Error getting Token from IS");
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
				ConfigManager.LOGGER.severe("Something went horribly wrong while setting reply in receiveObject");
				e2.printStackTrace();
			}
			throw new Exception("Error getting Token from IS");
		} catch (IOException e1) {
			exceptionHandler.ioExceptionThrown(e1);
			ConfigManager.LOGGER.warning("IO Exception on making SSL socket");
			try {
				recObj.setReply("IOException");
			} catch (InterruptedException e2) {
				ConfigManager.LOGGER.severe("Something went horribly wrong while setting reply in receiveObject");
				e2.printStackTrace();
			}
			throw new Exception("Error getting Token from IS");
		}
		//Set Traffic class and get certificate
		try {
			socket.setTrafficClass(ConfigManager.DIFFSERV);
		} catch (SocketException e) {
			exceptionHandler.socketExceptionThrown(e);
			ConfigManager.LOGGER.warning("Socket Exception while setting traffic class");
			try {
				recObj.setReply("SocketException");
			} catch (InterruptedException e1) {
				ConfigManager.LOGGER.severe("Something went horribly wrong while setting reply in receiveObject");
				e1.printStackTrace();
			}
			throw new Exception("Error getting Token from IS");
		}
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
				ConfigManager.LOGGER.severe("Something went horribly wrong while setting reply in receiveObject");
				e1.printStackTrace();
			}
			throw new Exception("Error getting Token from IS");
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
				ConfigManager.LOGGER.severe("Something went horribly wrong while setting reply in receiveObject");
				e1.printStackTrace();
			}
			throw new Exception("Error getting Token from IS");
		} catch (IOException e) {
			exceptionHandler.ioExceptionThrown(e);
			ConfigManager.LOGGER.warning("IOException proprocessing request");
			try {
				recObj.setReply("IOException");
			} catch (InterruptedException e1) {
				ConfigManager.LOGGER.severe("Something went horribly wrong while setting reply in receiveObject");
				e1.printStackTrace();
			}
			throw new Exception("Error getting Token from IS");
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
				ConfigManager.LOGGER.severe("Something went horribly wrong while setting reply in receiveObject");
				e1.printStackTrace();
			}
			throw new Exception("Error getting Token from IS");
		} catch (HttpException e) {
			exceptionHandler.httpExceptionThrown(e);
			ConfigManager.LOGGER.warning("HttpException while executing request," +
					" connection closed?");
			try {
				recObj.setReply("HttpException");
			} catch (InterruptedException e1) {
				ConfigManager.LOGGER.severe("Something went horribly wrong while setting reply in receiveObject");
				e1.printStackTrace();
			}
			throw new Exception("Error getting Token from IS");
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
				ConfigManager.LOGGER.severe("Something went horribly wrong while setting reply in receiveObject");
				e1.printStackTrace();
			}
			throw new Exception("Error getting Token from IS");
		} catch (IOException e) {
			exceptionHandler.ioExceptionThrown(e);
			ConfigManager.LOGGER.warning("IOException processing reply");
			try {
				recObj.setReply("IOException");
			} catch (InterruptedException e1) {
				ConfigManager.LOGGER.severe("Something went horribly wrong while setting reply in receiveObject");
				e1.printStackTrace();
			}
			throw new Exception("Error getting Token from IS");
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
				ConfigManager.LOGGER.severe("Something went horribly wrong while setting reply in receiveObject");
				e1.printStackTrace();
			}
			throw new Exception("Error getting Token from IS");
		} catch (IOException e) {
			exceptionHandler.ioExceptionThrown(e);
			ConfigManager.LOGGER.warning("IOException while parsing reply!");
			try {
				recObj.setReply("IOException");
			} catch (InterruptedException e1) {
				ConfigManager.LOGGER.severe("Something went horribly wrong while setting reply in receiveObject");
				e1.printStackTrace();
			}
			throw new Exception("Error getting Token from IS");
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
				ConfigManager.LOGGER.severe("Something went horribly wrong while setting reply in receiveObject");
				e1.printStackTrace();
			}
			throw new Exception("Error getting Token from IS");
		}
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

