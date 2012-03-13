package no.ntnu.qos.client;

/**
 * interface for checking whether or not a data set is obviously
 * invalid, e.g. not xml ++
 * @author Håvard
 *
 */
public interface SanityChecker {

	public void isSane(DataObject data);
	
	public boolean isSane(String userName, String password, String role);
}
