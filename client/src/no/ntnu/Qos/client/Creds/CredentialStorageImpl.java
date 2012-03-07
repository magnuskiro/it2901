package Creds;

import java.util.*;


/**
 * Class CredentialStorageImpl
 */
public class CredentialStorageImpl implements CredentialStorage {

  //
  // Fields
  //

  private Creds.Token tokens;
  
  //
  // Constructors
  //
  public CredentialStorageImpl () { };
  
  //
  // Methods
  //


  //
  // Accessor methods
  //

  /**
   * Set the value of tokens
   * @param newVar the new value of tokens
   */
  private void setTokens ( Creds.Token newVar ) {
    tokens = newVar;
  }

  /**
   * Get the value of tokens
   * @return the value of tokens
   */
  private Creds.Token getTokens ( ) {
    return tokens;
  }

  //
  // Other methods
  //

  /**
   * @return       boolean
   * @param        destination
   */
  public boolean hasToken( Uri destination )
  {
  }


  /**
   * @return       Creds.Token
   * @param        destination
   */
  public Creds.Token getToken( Uri destination )
  {
  }


  /**
   */
  public void getCredentials(  )
  {
  }


  /**
   * @param        token
   */
  public void storeToken( Creds.Token token )
  {
  }


  /**
   * @param        userName
   * @param        role
   * @param        password
   */
  public void setCredentials( String userName, String role, String password )
  {
  }


  /**
   */
  public void flushTokens(  )
  {
  }


}
