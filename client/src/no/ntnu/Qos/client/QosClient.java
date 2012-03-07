
import java.util.*;


/**
 * Interface QosClient
 */
public interface QosClient {

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
   * @param        userName
   * @param        role
   * @param        password
   */
  public void setCredentials( String userName, String role, String password );


  /**
   * @return       RecieveObject
   * @param        data
   * @param        destination
   */
  public RecieveObject sendData( String data, Uri destination );


  /**
   * @param        dataListener
   */
  public void addListener( DataListener dataListener );


  /**
   * @param        dataListener
   */
  public void removeListener( DataListener dataListener );


}
