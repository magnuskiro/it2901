package Creds;

import java.util.*;


/**
 * Class SamlCommunicator
 */
public class SamlCommunicator {

  //
  // Fields
  //

  private HttpClient httpClient;
  
  //
  // Constructors
  //
  public SamlCommunicator () { };
  
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

  /**
   * @return       Creds.Token
   * @param        destination
   * @param        userName
   * @param        role
   * @param        password
   */
  public Creds.Token getToken( Uri destination, String userName, String role, String password )
  {
  }


}
