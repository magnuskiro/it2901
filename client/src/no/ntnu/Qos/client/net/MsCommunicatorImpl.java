package net;


/**
 * Class MsCommunicatorImpl
 */
public class MsCommunicatorImpl extends MsCommunicator {

  //
  // Fields
  //

  private HttpClient httpClient;
  
  //
  // Constructors
  //
  public MsCommunicatorImpl () { };
  
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
