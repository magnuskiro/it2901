package Creds;

import java.util.*;


/**
 * Class TokenManagerImpl
 */
public class TokenManagerImpl implements TokenManager {

  //
  // Fields
  //

  private Creds.Token tokens;
  
  //
  // Constructors
  //
  public TokenManagerImpl () { };
  
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
   * @param        dataObj
   */
  public void getToken( DataObject dataObj )
  {
  }


}
