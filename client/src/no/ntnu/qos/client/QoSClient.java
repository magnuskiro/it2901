package no.ntnu.qos.client;

import java.net.URI;

/**
 * 
 * @author Stig Tore
 * 
 * Main interface towards the client using the QoSClient library.
 */
public interface QoSClient {

	/**
	 * Set the credentials the client wishes to use towards services
	 * @param username client username
	 * @param role client role
	 * @param password client password
	 */
	public void setCredentials(String username, String role, String password);
	
	/**
	 * Send data to a service
	 * @param data the XML data to send (Valid SOAP only)
	 * @param destination the URI to the service
	 * @return Returns a ReceiveObject where the specific reply will be made available.
	 */
	public ReceiveObject sendData(String data, URI destination);

	/**
	 * Add a listener for any reply data the client library receives
	 * @param listener a class implementing the DataListener interface
	 */
	public void addListener(DataListener listener);
	/**
	 * Remove a listener from the list of listeners
	 * @param listener a class implementing the DataListener interface
	 */
	public void removeListener(DataListener listener);

    /**
     *
     * @return Sequencer - the sequencer for this instance of client library.
     */
    public Sequencer getSequencer();
    
    /**
     * Informs the interface that the replyObject now contains a reply.
     * Should trigger a listener at the client
     * @param recObj	- the ReceiveObject that has received a reply
     */
    public void receive(ReceiveObject recObj);
    
    /**
     * Inform the ConfigManager whether to log with fine details or not.
     * @param on
     */
    public void setFineLogging(boolean on);
    
    /**
     * Inform the ConfigManager whether to log to file or not.
     * @param on
     */
    public void setLogToFile(boolean on);
    
    /**
     * Inform the ConfigManager whether to log to console or not.
     * @param on
     */
    public void setLogToConsole(boolean on);


}