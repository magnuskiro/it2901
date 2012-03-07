
import java.util.*;


/**
 * Class QosClientImpl
 */
public class QosClientImpl implements QosClient {

  //
  // Fields
  //

  
  //
  // Constructors
  //
  public QosClientImpl () { };
  
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
   * @param        exceptionHandler
   */
  public void QosClientImpl( String userName, String role, String password, ExceptionHandler exceptionHandler )
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
   * @return       RecieveObject
   * @param        data
   * @param        destination
   */
  public RecieveObject sendData( String data, Uri destination )
  {
  }


  /**
   * @param        dataListener
   */
  public void addListener( DataListener dataListener )
  {
  }


  /**
   * @param        dataListener
   */
  public void removeListener( DataListener dataListener )
  {
  }


}
