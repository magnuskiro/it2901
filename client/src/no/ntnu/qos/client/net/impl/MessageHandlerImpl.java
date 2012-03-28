package no.ntnu.qos.client.net.impl;

import java.net.SocketException;
import java.net.URI;
import java.net.UnknownHostException;
import org.apache.http.HttpVersion;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.params.SyncBasicHttpParams;
import org.apache.http.ConnectionReuseStrategy;
import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.entity.AbstractHttpEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.DefaultConnectionReuseStrategy;
import org.apache.http.impl.DefaultHttpClientConnection;
import org.apache.http.message.BasicHttpEntityEnclosingRequest;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HttpProcessor;
import org.apache.http.protocol.HttpRequestExecutor;
import org.apache.http.protocol.ImmutableHttpProcessor;
import org.apache.http.protocol.RequestConnControl;
import org.apache.http.protocol.RequestContent;
import org.apache.http.protocol.RequestExpectContinue;
import org.apache.http.protocol.RequestTargetHost;
import org.apache.http.protocol.RequestUserAgent;
import org.apache.http.util.EntityUtils;
import no.ntnu.qos.client.DataObject;
import no.ntnu.qos.client.ExceptionHandler;
import no.ntnu.qos.client.Sequencer;
import no.ntnu.qos.client.impl.ReceiveObjectImpl;
import no.ntnu.qos.client.net.MessageHandler;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
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


public class MessageHandlerImpl implements MessageHandler{

	//need an http-thingamajig and possibly other stuff
	private Sequencer sequencer;
	
	public MessageHandlerImpl(Sequencer sequencer){
		//I is built!
		this.sequencer = sequencer;
	}

	@Override
	public Runnable sendData(DataObject data) {
		return new MessageSender(data);
	}

	//how should this receive replies from httpComponents?
	private class MessageSender implements Runnable {
		//Message-related variables
		private String message;
		private int diffServ;
		private URI destination;
		private ReceiveObjectImpl recObj;
		private ExceptionHandler exceptionHandler;

		//HttpCore-related variables
		private HttpParams params;
		private HttpProcessor httpproc;
		
		//Socket
		private SSLSocket socket;


		public MessageSender(DataObject data) {
			message = data.getSoap();
			diffServ = data.getDiffServ();
			destination = data.getDestination();
			recObj = (ReceiveObjectImpl)(data.getReceiveObject());
			exceptionHandler = data.getExceptionHandler();
		}

		@Override
		public void run() {
			setParams();
			createProcessor();
			//Create executor;
			HttpRequestExecutor httpexecutor = new HttpRequestExecutor();
			//Set up host
			HttpHost host = new HttpHost(destination.getHost(), destination.getPort());
			//set up connection;
			DefaultHttpClientConnection conn = new DefaultHttpClientConnection();
			ConnectionReuseStrategy connStrategy = new DefaultConnectionReuseStrategy();
			//Set up Context
			HttpContext context = new BasicHttpContext(null);
			context.setAttribute(ExecutionContext.HTTP_CONNECTION, conn);
			context.setAttribute(ExecutionContext.HTTP_TARGET_HOST, host);

			//Set data to be sent!
			HttpEntity body = null;
			try {
				body = new StringEntity(message);
			} catch (UnsupportedEncodingException e) {
				exceptionHandler.fireUnsupportedEncodingException(e);
				//TODO: Log it
			}
			//TODO Fix this to actual propper content type!!
			((AbstractHttpEntity)body).setContentType("text/xml");
			try {
				setupSSLSocket();
			} catch (KeyManagementException e1) {
				// This should never happen!
				e1.printStackTrace();
				//TODO: Log it
			} catch (NoSuchAlgorithmException e1) {
				// This is near impossible
				e1.printStackTrace();
				//TODO: Log it!
			} catch (UnknownHostException e1) {
				exceptionHandler.fireUnknownHostException(e1);
				//TODO: Log it
			} catch (IOException e1) {
				exceptionHandler.fireIOException(e1);
				//TODO: Log it
			}
			//Set Traffic class and get certificate
			try {
				socket.setTrafficClass(diffServ);
			} catch (SocketException e) {
				exceptionHandler.fireSocketException(e);
				//TODO: Log it
			}
			try {
				socket.startHandshake();
				//Bind the shiny socket to be used in the connection
				conn.bind(socket, params);
			} catch (IOException e) {
				exceptionHandler.fireIOException(e);
				//TODO: Log it
			}
			//Create the request, set parameters and insert message into body.
			BasicHttpEntityEnclosingRequest request = new BasicHttpEntityEnclosingRequest("POST", destination.getPath());
			request.setParams(params);
			request.setEntity(body);

			//Pre-process the request, making sure headers are there and are correct
			try {
				httpexecutor.preProcess(request, httpproc, context);
			} catch (HttpException e) {
				exceptionHandler.fireHttpException(e);
				//TODO: Log it
			} catch (IOException e) {
				exceptionHandler.fireIOException(e);
				//TODO: Log it
			}
			//Execute request!
			HttpResponse response = null;
			try {
				response = httpexecutor.execute(request, conn, context);
			} catch (IOException e) {
				exceptionHandler.fireIOException(e);
				//TODO: Log it
			} catch (HttpException e) {
				exceptionHandler.fireHttpException(e);
				//TODO: Log it
			}
			//Process the response
			try {
				httpexecutor.postProcess(response, httpproc, context);
			} catch (HttpException e) {
				exceptionHandler.fireHttpException(e);
				//TODO: Log it
			} catch (IOException e) {
				exceptionHandler.fireIOException(e);
				//TODO: Log it
			}

			//Unknown if the reply code is needed by the client
//			String replyCode = response.getStatusLine().toString();
			//Get the body of the reply
			String replyBody = null;
			try {
				replyBody = EntityUtils.toString(response.getEntity());
			} catch (ParseException e) {
				// Service messed up!
				e.printStackTrace();
				//TODO: Log it
			} catch (IOException e) {
				exceptionHandler.fireIOException(e);
				//TODO: Log it
			}
			//Close connection
			try {
				conn.close();
			} catch (IOException e) {
				exceptionHandler.fireIOException(e);
				//TODO: Log it
			}
			//Set reply in receiveObject
			try {
				recObj.setReply(replyBody);
			} catch (InterruptedException e) {
				// Should only happen if either client or client lib halted!
				e.printStackTrace();
				//TODO: Log it
			}
			//informs the sequencer of a reply
			sequencer.returnData(recObj);

		}

		private void setParams() {
			params = new SyncBasicHttpParams();
			HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
			HttpProtocolParams.setContentCharset(params, "UTF-8");
			HttpProtocolParams.setUserAgent(params, "HttpComponents/1.1");
			HttpProtocolParams.setUseExpectContinue(params, false);
		}

		private void createProcessor() {
			httpproc = new ImmutableHttpProcessor(new HttpRequestInterceptor[] {
					// Required protocol interceptors
					new RequestContent(),
					new RequestTargetHost(),
					// Recommended protocol interceptors
					new RequestConnControl(),
					new RequestUserAgent(),
					new RequestExpectContinue()
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
}

