
import java.util.*;


/**
 * Interface Sequencer
 */
public interface Sequencer {

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
  public void setCredentials_( String userName, String role, String password );


  /**
   * @param        data
   * @param        destination
   */
  public void sendData( String data, Uri destination );


  /**
   * @param        dataObj
   */
  public void sendData( DataObject dataObj );


  /**
   * @param        data
   */
  public void returnData( String data );


}
