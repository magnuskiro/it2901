package no.ntnu.qos.client;

import java.io.IOException;
import java.net.UnknownHostException;

import org.apache.http.HttpException;

/**
 * The client to use the client library HAS to implement this interface.
 * It will be used to send exceptions to the client.
 * @author Stig Tore
 *
 */
public interface ExceptionHandler {
    // no such element exception
	//TODO: UnknownHostException, IOException, HttpException

}
