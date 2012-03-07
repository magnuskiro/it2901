package net;

import java.util.*;


/**
 * Class MessageHandlerImpl
 */
public class MessageHandlerImpl extends MessageHandler {

  //
  // Fields
  //

  private HttpClient httpClient;
  
  //
  // Constructors
  //
  public MessageHandlerImpl () { };
  
  //
  // Methods
  //


  //
  // Accessor methods
  //

  /**
   * Set the value of httpClient
   * @param newVar the new value of httpClient
   */
  private void setHttpClient ( HttpClient newVar ) {
    httpClient = newVar;
  }

  /**
   * Get the value of httpClient
   * @return the value of httpClient
   */
  private HttpClient getHttpClient ( ) {
    return httpClient;
  }

  //
  // Other methods
  //

}
