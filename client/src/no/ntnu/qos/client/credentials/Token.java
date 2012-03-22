package no.ntnu.qos.client.credentials;

import java.net.URI;

/**
 * The interface for a token object, should contain all the information needed to validate and output a valid token
 * @author Stig Tore
 *
 */
public interface Token {
	/**
	 * Method to get the XML representation of the token
	 * @return XML data
	 */
	public String getXML();
	/**
	 * Should check to see if a token is valid
	 * @return true if valid, false if not.
	 */
	public boolean isValid();
	/**
	 * Returns the server this token is valid for
	 * @return URI of the server/service
	 */
	public URI getDestination();
	/**
	 * Returns the value of the DiffServ field
	 * @return int of the DiffServ
	 */
	public int getDiffServ();
	/**
	 * Returns the priority of the connection
	 * @return int priority
	 */
	public int getPriority();

    /**
     * Allows the configuration of the time a token is valid.
     * expirationTimeBuffer is the long value which is used in (currentTimeMillis() + expirationTimeBuffer) to decide when a token has to be renewed.
     * @param expirationTimeBuffer - the buffer time a token is alive and needs to be renewed.
     */
    public void setExpirationTimeBuffer(long expirationTimeBuffer);

    /**
     * Allows the configuration of the time a token is valid.
     * expirationTimeBuffer is the long value which is used in (currentTimeMillis() + expirationTimeBuffer) to decide when a token has to be renewed.
     * @return expirationTimeBuffer - the buffer time a token is alive and needs to be renewed.
     */
    public long getExpirationTimeBuffer();
}
