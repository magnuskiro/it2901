
import java.util.*;
import Creds.Token;


/**
 * Class DataObject
 */
public class DataObject {

  //
  // Fields
  //

  private Sequencer sequencer;
  private Uri destination;
  
  //
  // Constructors
  //
  public DataObject () { };
  
  //
  // Methods
  //


  //
  // Accessor methods
  //

  /**
   * Set the value of sequencer
   * @param newVar the new value of sequencer
   */
  private void setSequencer ( Sequencer newVar ) {
    sequencer = newVar;
  }

  /**
   * Get the value of sequencer
   * @return the value of sequencer
   */
  private Sequencer getSequencer ( ) {
    return sequencer;
  }

  /**
   * Set the value of destination
   * @param newVar the new value of destination
   */
  private void setDestination ( Uri newVar ) {
    destination = newVar;
  }

  /**
   * Get the value of destination
   * @return the value of destination
   */
  private Uri getDestination ( ) {
    return destination;
  }

  //
  // Other methods
  //

  /**
   * @param        sequencer
   */
  public void DataObject( Sequencer sequencer )
  {
  }


  /**
   * @param        sane
   */
  public void setSane( boolean sane )
  {
  }


  /**
   * @param        bandwidth
   */
  public void setBandwidth( int bandwidth )
  {
  }


  /**
   * @param        token
   */
  public void setToken( Creds.Token token )
  {
  }


  /**
   * @return       String
   */
  public String getSoap(  )
  {
  }


}
