package no.ntnu.qos.client;

/**
 * interface for checking whether or not a data set is obviously
 * invalid, e.g. not xml ++
 * @author Håvard
 *
 */
public interface SanityChecker {

	/**
	 * checks the given DataObject for correctness and calls setSane with the result
	 * @param data	- the DataObject to check
	 */
	public void isSane(DataObject data);
	
	/**mk3-4_yong
	 * checks the given credential triplet for obvious faults
	 * @param userName	- client username
	 * @param password	- client password
	 * @param role		- client role
	 * @return	true if sane, else: false
	 */
	public boolean isSane(String userName, String password, String role);

}
