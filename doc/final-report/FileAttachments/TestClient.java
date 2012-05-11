package no.ntnu.qos.testclient;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.SocketException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;

import no.ntnu.qos.client.ExceptionHandler;
import no.ntnu.qos.client.QoSClient;
import no.ntnu.qos.client.ReceiveObject;
import no.ntnu.qos.client.impl.QoSClientImpl;
/**
 * TestClient is a test Web service client using the client library to request Web services.
 * @author Ola Martin
 *
 */
public class TestClient implements ExceptionHandler{
	/**
	 * Enum to define log type.
	 */
	private enum LogType{
		INFO, WARN, SEVERE;
	}
	/**
	 * finals to use when getting configuration entries.
	 */
	private final String DO_LOG = "doLog";
	private final String LOG_TO_FILE = "logToFile";
	private final String FALSE = "false";
	private final String TRUE = "true";
	private final String USERNAME = "username";
	private final String PASSWORD = "password";
	private final String ROLE = "role";
	private final String SERVICE = "service";
	private final String INTERVAL = "interval";
	private final String DELAY = "delay";
	private final String DATA = "request";
	private final String NOFREQUESTS = "nofreqs";
	private final String REQID = "REQID";
	private final long DEFAULT_DELAY = 0;
	
	/**
	 * Integer to count how many responses are received.
	 */
	private static final AtomicInteger responses = new AtomicInteger(0);
	/**
	 * The ID of the request, incremented once for every request.
	 */
	private static final AtomicInteger requestID = new AtomicInteger(0);
	/**
	 * The file to read configuration from, may be overridden with the only command line argument.
	 */
	private String configFile = "client.config";
	/**
	 * HashMap containing all the entries in configFile.
	 */
	private Map<String, String> config = new HashMap<String, String>();
	/**
	 * The client library
	 */
	private QoSClient connection;
	/**
	 * The service URI
	 */
	private URI destination;
	/**
	 * Timer used to schedule tasks
	 */
	private static Timer timer;
	/**
	 * The number of requests to send.
	 */
	private int nofRequests = 0;

	/**
	 * Starts the TestClient using the default configFile.
	 */
	public TestClient() {
		start();
	}
	/**
	 * Starts the TestClient using the parameter as configFile
	 * @param configFile
	 */
	public TestClient(String configFile) {
		this.configFile=configFile;
		start();
	}

	/**
	 * Starts the TestClient
	 */
	private void start(){
		
		//read the configuration
		readConfig();
		
		//Checks whether or not needed variables are present in configuration.
		if(!config.containsKey(USERNAME)){
			logLine("Config must contain: "+USERNAME, LogType.SEVERE);
			return;
		}
		if(!config.containsKey(ROLE)){
			logLine("Config must contain: "+ROLE, LogType.SEVERE);
			return;
		}
		if(!config.containsKey(PASSWORD)){
			logLine("Config must contain: "+PASSWORD, LogType.SEVERE);
			return;
		}
		if(!config.containsKey(SERVICE)){
			logLine("Config must contain: "+SERVICE, LogType.SEVERE);
			return;
		}else{
			try {
				destination = new URI(config.get(SERVICE));
			} catch (URISyntaxException e) {
				logLine(SERVICE+" syntax faulty: "+config.get(SERVICE), LogType.SEVERE);
				return;
			}
		}
		if(!config.containsKey(DATA)){
			logLine("Config must contain: "+DATA, LogType.SEVERE);
			return;
		}
		logLine(USERNAME+" set to: "+config.get(USERNAME)+
				", "+ROLE+" set to: "+config.get(ROLE)+
				", "+PASSWORD+" set to: "+config.get(PASSWORD)+
				", "+SERVICE+" set to: "+config.get(SERVICE)+
				", "+DATA+" set to: "+config.get(DATA));
		
		//Makes an implementation of the client library.
		connection = new QoSClientImpl(config.get(USERNAME),
				config.get(ROLE), config.get(PASSWORD), this);
		
		//Sets whether or not to log in the client library
		connection.setFineLogging(true);
		connection.setLogToFile(config.containsKey(LOG_TO_FILE)&&config.get(LOG_TO_FILE).equals(TRUE));
		connection.setLogToConsole(false);
		
		//Gets the delay before starting sending requests from the configuration.
		//If not present DEFAULT_DELAY will be used.
		long delay = DEFAULT_DELAY;
		if(config.containsKey(DELAY)){
			try{
				delay = Long.parseLong(config.get(DELAY));
				logLine(DELAY+" set to: "+delay);
			}catch(NumberFormatException nfe){
				logLine(DELAY+" not int, using default delay: "+DEFAULT_DELAY, LogType.WARN);
			}
		}else{
			logLine(DELAY+"  not specified, using default delay: "+DEFAULT_DELAY);
		}

		//Gets the Interval between sending requests from the configuration.
		//If not present, only one request will be sent.
		long interval = -1;
		if(config.containsKey(INTERVAL)){
			try{
				interval = Long.parseLong(config.get(INTERVAL));
				logLine(INTERVAL+" set to: "+interval);
			}catch(NumberFormatException nfe){
				logLine(INTERVAL+" not int, send only one request", LogType.WARN);
			}
		}else{
			logLine(INTERVAL+" not specified, send only one request");
		}
		
		//Gets the number of requests to be sent from the configuration.
		//If not present, will send until killed, unless interval is not defined.
		if(config.containsKey(NOFREQUESTS)){
			try{
				nofRequests = Integer.parseInt(config.get(NOFREQUESTS));
				logLine(NOFREQUESTS+" set to: "+nofRequests);
			}catch(NumberFormatException nfe){
				logLine(NOFREQUESTS+" not int, send forever unless no interval", LogType.WARN);
			}
		}else{
			logLine(NOFREQUESTS+" not specified, send forever unless no interval");
		}
		
		//The timerTask used for starting sending of requests.
		TimerTask requestTask = new TimerTask() {
			@Override
			public void run() {
				new Thread(new RequestSender()).start();

			}
		};
		
		//Schedules sending of requests.
		timer = new Timer();
		if(interval > 0){
			logLine("Sending requests with "+DELAY+"="+delay+" and "+INTERVAL+"="+interval);
			timer.scheduleAtFixedRate(requestTask, delay, interval);			
		}else{
			logLine("Sending request with "+DELAY+"="+delay);
			timer.schedule(requestTask, delay);
		}
	}

	/**
	 * Used to send requests to the destination using the client library
	 * 
	 * @author Ola Martin
	 *
	 */
	private class RequestSender implements Runnable {
		/**
		 * 
		 */
		@Override
		public void run() {
			//Increments and gets the request ID
			int reqID = requestID.incrementAndGet();
			
			//If this is the last request to be sent, the timer is stopped, so no more requests are sent.
			if(reqID==nofRequests){
				timer.cancel();
			}
			logLine("Sending "+DATA+" "+reqID+" to "+config.get(SERVICE));
			
			//takes a note of what the current time is.
			long sendTime = System.currentTimeMillis();
			
			//Uses connection.sendData(data, destination) to send a request.
			//Here {REQID} in the request found in clientConfig is replaced with {REQID=reqID}
			//The client library will put the response in the returned ReceiveObject when it is received.
			ReceiveObject ro = connection.sendData(
					config.get(DATA).replace("{"+REQID+"}", "{"+REQID+"="+reqID+"}"), destination);
			try {
				logLine("Waiting for response "+reqID);
				
				//Calls the blocking method ReceiveObject.receive() to get the response.
				//An alternative to this would be to make a DataListener and add it to connection.
				//this way, every listener would receive all the responses, 
				//and would be unsuited for this test
				String response = ro.receive();
				
				//Checks the response for validity.
				checkResponse(response, reqID, System.currentTimeMillis()-sendTime);
			} catch (InterruptedException e) {
				logLine("Could not receive response "+reqID+": interuptedException", LogType.WARN);
			}
			//checks if program should exit
			exitCheck();
		}
		
	}

	/**
	 * Checks a response for validity.
	 * @param response, the response
	 * @param reqID, the ID number of the request
	 * @param time when request was sent.
	 */
	private synchronized void checkResponse(String response, int reqID, long time){
		//finds the position of '{REQID=reqId}' in the response.
		int start = response.indexOf("{"+REQID+"=")+("{"+REQID+"=").length();
		int end = response.indexOf("}", start);
		//checks whether or not the reqId is found.
		if(end>start && start>0){
			//finds the responses ID, so it can be logged if we get the response for a different request.
			int responseReqID = Integer.parseInt(response.substring(start, end));
			//Logging of response ID and the length of the response
			if(reqID==responseReqID){
				logLine(time+"ms: Got response for "+reqID+", size="+response.length());
			}else{
				logLine(time+"ms: Request "+reqID+" got response for "+responseReqID+
						", size="+response.length(), LogType.SEVERE);
			}			
		}else{
			//If we get a response without reqId we log this, as well as the response.
			//The client library will respond with an exception name if an exception is catched.
			logLine(time+"ms: Got faulty response for "+reqID+", "+response);
		}
	}

	/**
	 * Reads the configuration file
	 * ignores lines starting with '%'
	 * adds lines containing ':' to the config HashMap 
	 * with what is before ':' as Key and what is after as Value.
	 * stops when a line without '%' or ':' is encountered.
	 */
	private void readConfig(){
		logLine("Reading config from file: "+configFile);
		try {
			BufferedReader br = new BufferedReader(new FileReader(new File(configFile)));
			String in = br.readLine();
			while(in != null){
				if(!in.startsWith("%")){
					if(in.contains(":")){
						String[] property = in.split(":", 2);
						config.put(property[0], property[1]);					
					}else{
						break;
					}
				}
				in = br.readLine();
			}
			logLine("Config read from file: "+configFile);
		} catch (FileNotFoundException e) {
			logLine("Could not read config: file "+configFile+
					" not found.",LogType.WARN);
			e.printStackTrace();
		} catch (IOException e) {
			logLine("Could not read config: IOException, file "+configFile+
					" might contain error?.",LogType.WARN);
			e.printStackTrace();
		}
	}
	
	/**
	 * Checks whether or not we have received all of our responses, and exits if we have.
	 */
	private void exitCheck(){
		if(responses.incrementAndGet()==nofRequests){
			logLine("Test Client Complete, terminating");
			System.exit(0);
		}
	}

	/**
	 * Logs to System.out if logging is enabled.
	 * @param toLog, what to log
	 * @param type, what type of log entry it is.
	 */
	private void log(String toLog, LogType type){
		if(config.containsKey(DO_LOG) && config.get(DO_LOG).equals(FALSE)){
			return;
		}else{
			System.out.print(type+": "+toLog);
		}

	}

	/**
	 * Logs a line using log(String,LogType)
	 * @param toLog
	 * @param type
	 */
	private void logLine(String toLog, LogType type){
		log(toLog+"\n");
	}

	/**
	 * Logs a line using logLine(String,LogType.INFO)
	 * @param toLog
	 */
	private void logLine(String toLog){
		logLine(toLog, LogType.INFO);
	}

	/**
	 * Logs using log(String, LogType.INFO)
	 * @param toLog
	 */
	private void log(String toLog){
		log(toLog, LogType.INFO);
	}

	/**
	 * Starts the program.
	 * @param args, clientConfig filename to use if present.
	 */
	public static void main(String[] args) {
		if(args.length>0){
			new TestClient(args[0]);
		}else{
			new TestClient();
		}
	}
	
	/**
	 * Logs an Exception
	 * @param e, the Exception to log
	 */
	public void logException(Exception e){
		logLine("Got exception: "+e.toString()+", "+e.getMessage(), LogType.WARN);
	}

	@Override
	public void unknownHostExceptionThrown(UnknownHostException e) {
		logException(e);
	}

	@Override
	public void ioExceptionThrown(IOException e) {
		logException(e);		
	}

	@Override
	public void httpExceptionThrown(Exception e) {
		logException(e);		
	}

	@Override
	public void socketExceptionThrown(SocketException e) {
		logException(e);		
	}

	@Override
	public void unsupportedEncodingExceptionThrown(
			UnsupportedEncodingException e) {
		logException(e);		
	}
}
