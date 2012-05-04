package no.ntnu.qos.client;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.SocketException;
import java.net.UnknownHostException;


/**
 * The client to use the client library HAS to implement this interface.
 * It will be used to send exceptions to the client.
 * @author Stig Tore
 *
 */
public interface ExceptionHandler {
	
	/**
	 * URI is malformed/invalid
	 * @param e UnknownHostException
	 */
	public void unknownHostExceptionThrown(UnknownHostException e);
	/**
	 * Problem reading variables, input, streams or strings
	 * @param e IOException
	 */
	public void ioExceptionThrown(IOException e);
	/**
	 * Problem with the HTTP connection in the form of timeouts, too many retries, etc.
	 * @param e org.apache.httpcomponent.HttpException, cast to generic Exception for convenience.
	 */
	public void httpExceptionThrown(Exception e);
	/**
	 * Problems with the socket, invalid SSL port or socket closed from service due to capacity problems.
	 * @param e SocketException
	 */
	public void socketExceptionThrown(SocketException e);
	/**
	 * Input message is invalid or malformed.
	 * @param e UnsupportedEncodingException
	 */
	public void unsupportedEncodingExceptionThrown(UnsupportedEncodingException e);
	

}
