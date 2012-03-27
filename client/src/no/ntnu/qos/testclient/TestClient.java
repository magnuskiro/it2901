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

public class TestClient implements ExceptionHandler{
	private enum LogType{
		INFO, WARN, SEVERE;
	}
	private final String DO_LOG = "doLog";
	private final String FALSE = "false";
	private final String USERNAME = "username";
	private final String PASSWORD = "password";
	private final String ROLE = "role";
	private final String SERVICE = "service";
	private final String INTERVAL = "interval";
	private final String DELAY = "delay";
	private final String DATA = "request";
	private final long DEFAULT_DELAY = 0;


	private static final AtomicInteger requestID = new AtomicInteger(0);
	private String configFile = "client.config";
	private Map<String, String> config = new HashMap<String, String>();
	private QoSClient connection;
	private URI destination;
	private static String prevResponse = "";


	public TestClient() {
		start();
	}

	public TestClient(String configFile) {
		this.configFile=configFile;
		start();
	}

	private void start(){
		readConfig();
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
		connection = new QoSClientImpl(config.get(USERNAME),
				config.get(ROLE), config.get(PASSWORD), this);

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

		Timer timer = new Timer();
		TimerTask requestTask = new TimerTask() {

			@Override
			public void run() {
				sendRequest();

			}
		};
		if(interval > 0){
			logLine("Sending requests with "+DELAY+"="+delay+" and "+INTERVAL+"="+interval);
			timer.scheduleAtFixedRate(requestTask, delay, interval);			
		}else{
			logLine("Sending request with "+DELAY+"="+delay);
			timer.schedule(requestTask, delay);
		}
	}

	private void sendRequest(){
		int reqID = requestID.getAndIncrement();
		logLine("Sending "+DATA+" "+reqID+" to "+config.get(SERVICE));
		ReceiveObject ro = connection.sendData(config.get(DATA), destination);
		try {
			logLine("Waiting for response "+reqID);
			String response = ro.receive();
			logLine("Got response "+reqID);
			checkResponse(response, reqID);
		} catch (InterruptedException e) {
			logLine("Could not receive response "+reqID+": interuptedException", LogType.WARN);
		}
	}

	private synchronized void checkResponse(String response, int reqID){
		if(!prevResponse.equals(response)){
			prevResponse = response;
			logLine("Response "+reqID+": "+response);
		}else{
			logLine("Response "+reqID+": as previous");
		}
	}

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

	private void log(String toLog, LogType type){
		if(config.containsKey(DO_LOG) && config.get(DO_LOG).equals(FALSE)){
			return;
		}else{
			System.out.print(type+": "+toLog);
		}

	}

	private void logLine(String toLog, LogType type){
		log(toLog+"\n");
	}

	private void logLine(String toLog){
		logLine(toLog, LogType.INFO);
	}

	private void log(String toLog){
		log(toLog, LogType.INFO);
	}


	public static void main(String[] args) {
		if(args.length>0){
			new TestClient(args[0]);
		}else{
			new TestClient();
		}
	}

	@Override
	public void fireUnknownHostException(UnknownHostException e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void fireIOException(IOException e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void fireHttpException(Exception e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void fireSocketException(SocketException e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void fireUnsupportedEncodingException(UnsupportedEncodingException e) {
		// TODO Auto-generated method stub
		
	}
	
	public void logException(Exception e){
		logLine("Got exception: "+e.toString()+", "+e.getMessage(), LogType.WARN);
	}
}
