import Creds.Token;


/**
 * Interface CredentialStorage
 */
public interface CredentialStorage {

  //
  // Fields
  //

  
  //
  // Methods
  //


  //
  // Accessor methods
  //

  //
  // Other methods
  //

  /**
   * @return       boolean
   * @param        destination
   */
  public boolean hasToken( Uri destination );


  /**
   * @return       Creds.Token
   * @param        destination
   */
  public Creds.Token getToken( Uri destination );


  /**
   */
  public void getCredentials(  );


  /**
   * @param        token
   */
  public void storeToken( Creds.Token token );


  /**
   * @param        userName
   * @param        role
   * @param        password
   */
  public void setCredentials( String userName, String role, String password );


  /**
   */
  public void flushTokens(  );


}
