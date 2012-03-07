package no.ntnu.qos.server.mediators;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

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
	private boolean useDefault = false;
	private Map<String, Map<String, Integer>> priorities = new HashMap<String, Map<String,Integer>>();
	private Map<String, Map<String, Integer>> diffservs = new HashMap<String, Map<String,Integer>>();
	private int defaultPri = -1;
	private int defaultDif = -1;
	
	/**
	 * Sets the name of the file to read from.
	 * @param filename, full path to file.
	 */
	public void setFilename(String filename) {
		this.filename = filename;
	}
	/**
	 * Gets the name of the file to read from.
	 * default is ""
	 * @return {@link String} the full path to the current file.
	 */
	public String getFilename() {
		return filename;
	}
	/*
	 * Reads data from xml file, filename, and puts it in priorities and diffservs maps.
	 */
	private void readData() throws FileNotFoundException{
		InputStream in = new FileInputStream(new File(filename));
		OMXMLParserWrapper builder = OMXMLBuilderFactory.createOMBuilder(in);
		servicesElement = builder.getDocumentElement();
		
		Iterator<OMContainer> serviceIterator = servicesElement.getChildrenWithLocalName("service");
		QName name = new QName("name");
		QName role = new QName("role");
		while(serviceIterator.hasNext()){
			OMElement service = (OMElement)serviceIterator.next();
			Map<String, Integer> pri = new HashMap<String, Integer>();
			Map<String, Integer> dif = new HashMap<String, Integer>();
			priorities.put(service.getAttributeValue(name), pri);
			diffservs.put(service.getAttributeValue(name), dif);
			
			Iterator<OMContainer> clientIterator = service.getChildrenWithLocalName("client");
			while(clientIterator.hasNext()){
				OMElement client = (OMElement)clientIterator.next();
				
				Iterator clientpriIterator = client.getChildrenWithLocalName("priority");
				OMElement priority = (OMElement)clientpriIterator.next();
				pri.put(client.getAttributeValue(role), Integer.parseInt(priority.getText()));
				
				Iterator clientdifIterator = client.getChildrenWithLocalName("diffserv");
				OMElement diffserv = (OMElement)clientdifIterator.next();
				dif.put(client.getAttributeValue(role), Integer.parseInt(diffserv.getText()));
				
			}
		}
		
		OMElement defaultPriority = (OMElement) servicesElement.getChildrenWithLocalName("defaultPriority").next();
		
		Iterator clientpriIterator = defaultPriority.getChildrenWithLocalName("priority");
		OMElement priority = (OMElement)clientpriIterator.next();
		defaultPri = Integer.parseInt(priority.getText());
		
		Iterator clientdifIterator = defaultPriority.getChildrenWithLocalName("diffserv");
		OMElement diffserv = (OMElement)clientdifIterator.next();
		defaultDif = Integer.parseInt(diffserv.getText());
		
		useDefault = defaultPriority.getAttributeValue(new QName("usedefault")).trim().equals("true");
	}
	
	/**
	 * If user has set filename, this method checks if data is loaded
	 * if it is not, it tries to read it from disk.
	 * Data is stored in XML.
	 * 
	 * @return {@link Boolean} true if filename is non-empty, else false
	 * @throws FileNotFoundException
	 */
	public boolean isDataAvailable() throws FileNotFoundException{
		if(filename.isEmpty() || filename.trim().equals("")){
			return false;
		}
		if(servicesElement==null){
			readData();
		}
		return true;
	}
	/**
	 * Get the priority for messages between client and service.
	 * @param clientRole - the role of the client
	 * @param service - the service
	 * @return {@link Integer} the priority if client-service pair exists, otherwise, 
	 * if useDefault is true, return default priority, else -1.
	 */
	public int getPriority(String clientRole, String service){
		if(priorities.containsKey(service)){
			if(priorities.get(service).containsKey(clientRole)){
				return priorities.get(service).get(clientRole);
			}
		}
		if(useDefault) return defaultPri;
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
		if(diffservs.containsKey(service)){
			if(diffservs.get(service).containsKey(clientRole)){
				return diffservs.get(service).get(clientRole);
			}
		}
		if(useDefault) return defaultDif;
		return -1;
	}
	/**
	 * Check if we should use default values if client-service pair does not exist.
	 * This is configured in the XML file.
	 * @return whether or not to use default values.
	 */
	public boolean isUseDefault() {
		return useDefault;
	}
}
