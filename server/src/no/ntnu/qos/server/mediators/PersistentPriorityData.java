package no.ntnu.qos.server.mediators;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.xml.namespace.QName;

import org.apache.axiom.om.OMContainer;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMXMLBuilderFactory;
import org.apache.axiom.om.OMXMLParserWrapper;

/**
 * This class stores persistent data regarding priorities.
 * @author Ola Martin & Jørgen
 *
 */
public class PersistentPriorityData {
	private String filename="";
	private OMElement servicesElement = null;
	final private Map<String, Map<String, Integer>> priorities = new HashMap<String, Map<String,Integer>>();
	final private Map<String, Map<String, Integer>> diffservs = new HashMap<String, Map<String,Integer>>();
	final private Map<String, Boolean> useDefaults = new HashMap<String, Boolean>();
	private final Lock lock = new ReentrantLock();

	/**
	 * Sets the name of the file to read from.
	 * @param filename, full path to file.
	 */
	public  void setFilename(String filename) {
		lock.lock();
		try{
			if(this.filename.isEmpty() || this.filename.trim().equals("")){
				this.filename = filename;
			}
		}finally{
			lock.unlock();
		}
	}
	/**
	 * Gets the name of the file to read from.
	 * default is ""
	 * @return {@link String} the full path to the current file.
	 */
	public String getFilename() {
		lock.lock();
		try{
			return filename;
		}finally{
			lock.unlock();
		}
	}
	/**
	 * Reads priority data from XML-file filename.
	 * 
	 * @throws FileNotFoundException
	 */
	public  void readData() throws FileNotFoundException{
		lock.lock();
		try{
			//Check if someone else has already read the data
			if(this.priorities.isEmpty() 
					&& this.diffservs.isEmpty() 
					&& this.useDefaults.isEmpty()){
				//Clear data in case of multiple simultaneous calls
				this.priorities.clear();
				this.diffservs.clear();
				this.useDefaults.clear();

				InputStream in = new FileInputStream(new File(filename));
				OMXMLParserWrapper builder = OMXMLBuilderFactory.createOMBuilder(in);
				servicesElement = builder.getDocumentElement();

				Iterator<OMContainer> serviceIterator = 
						servicesElement.getChildrenWithLocalName("service");
				QName name = new QName("name");
				QName role = new QName("role");
				QName useDefault = new QName("useDefault");
				while(serviceIterator.hasNext()){
					OMElement service = (OMElement)serviceIterator.next();
					Map<String, Integer> pri = new HashMap<String, Integer>();
					Map<String, Integer> dif = new HashMap<String, Integer>();
					priorities.put(service.getAttributeValue(name), pri);
					diffservs.put(service.getAttributeValue(name), dif);
					useDefaults.put(service.getAttributeValue(name), 
							Boolean.parseBoolean(service.getAttributeValue(useDefault)));

					Iterator<OMContainer> clientIterator = service.getChildrenWithLocalName("client");
					while(clientIterator.hasNext()){
						OMElement client = (OMElement)clientIterator.next();
						String clientRole = client.getAttributeValue(role);

						Iterator clientpriIterator = client.getChildrenWithLocalName("priority");
						OMElement priority = (OMElement)clientpriIterator.next();

						Iterator clientdifIterator = client.getChildrenWithLocalName("diffserv");
						OMElement diffserv = (OMElement)clientdifIterator.next();

						if(clientRole.equalsIgnoreCase("default")){
							pri.put(MediatorConstants.QOS_DEFAULT_CLIENT_ROLE, 
									Integer.parseInt(priority.getText()));
							dif.put(MediatorConstants.QOS_DEFAULT_CLIENT_ROLE,
									Integer.parseInt(diffserv.getText()));
						}else{
							pri.put(clientRole, Integer.parseInt(priority.getText()));
							dif.put(clientRole, Integer.parseInt(diffserv.getText()));
						}
					}
					if(!pri.containsKey(MediatorConstants.QOS_DEFAULT_CLIENT_ROLE) 
							|| !dif.containsKey(MediatorConstants.QOS_DEFAULT_CLIENT_ROLE)){
						throw new IllegalArgumentException("No Default client for service: " + 
							service.getAttributeValue(name) + ", in file: " + this.getFilename());
					}
				}
				//
				//				OMElement defaultPriority = (OMElement) servicesElement.getChildrenWithLocalName("defaultPriority").next();
				//
				//				Iterator clientpriIterator = defaultPriority.getChildrenWithLocalName("priority");
				//				OMElement priority = (OMElement)clientpriIterator.next();
				//				defaultPri = Integer.parseInt(priority.getText());
				//
				//				Iterator clientdifIterator = defaultPriority.getChildrenWithLocalName("diffserv");
				//				OMElement diffserv = (OMElement)clientdifIterator.next();
				//				defaultDif = Integer.parseInt(diffserv.getText());
				//
				//				useDefault = defaultPriority.getAttributeValue(new QName("usedefault")).trim().equals("true");
			}
		}finally{
			lock.unlock();
		}
	}

	/**
	 * Checks whether or not data is loaded in to memory.
	 * 
	 * @return {@link Boolean} true if data is loaded, otherwise false.
	 */
	public boolean isDataAvailable(){
		lock.lock();
		try{
			return servicesElement != null;
		}finally{
			lock.unlock();
		}
	}
	/**
	 * Get the priority for messages between client and service.
	 * @param clientRole - the role of the client
	 * @param service - the service
	 * @return {@link Integer} the priority if client-service pair exists, otherwise, 
	 * if useDefault is true, return default priority, else -1.
	 */
	public int getPriority(String clientRole, String service){
		lock.lock();
		try{
			if(priorities.containsKey(service)){
				if(priorities.get(service).containsKey(clientRole)){
					return priorities.get(service).get(clientRole);
				}
			}
			if(useDefaults.get(service)){
				return priorities.get(service).get(MediatorConstants.QOS_DEFAULT_CLIENT_ROLE);
			}
		}finally{
			lock.unlock();
		}
		return -1;
	}
	/**
	 * Get the diffserv value for messages between client and service.
	 * @param clientRole - the role of the client
	 * @param service - the service
	 * @return {@link Integer} the diffserv value if client-service pair exists, otherwise, 
	 * if useDefault is true, return default diffserv value, else -1.
	 */
	public int getDiffserv(String clientRole, String service){
		lock.lock();
		try{
			if(diffservs.containsKey(service)){
				if(diffservs.get(service).containsKey(clientRole)){
					return diffservs.get(service).get(clientRole);
				}
			}
			if(useDefaults.get(service)){
				return diffservs.get(service).get(MediatorConstants.QOS_DEFAULT_CLIENT_ROLE);
			}
		}finally{
			lock.unlock();
		}
		return -1;
	}
	/**
	 * Check if we should use default values if client-service pair does not exist.
	 * This is configured in the XML file.
	 * @param service - the service to check against
	 * @return whether or not to use default values.
	 */
	public boolean isUseDefault(String service) {
		lock.lock();
		try{
			return this.useDefaults.get(service);
		}finally{
			lock.unlock();
		}
	}
}
