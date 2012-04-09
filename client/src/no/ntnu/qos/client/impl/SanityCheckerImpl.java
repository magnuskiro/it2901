package no.ntnu.qos.client.impl;

import no.ntnu.qos.client.DataObject;
import no.ntnu.qos.client.SanityChecker;

import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SanityCheckerImpl implements SanityChecker{

	private Pattern pattern;
	private Matcher matcher;

	private static final String USERNAME_PATTERN = "^[a-zA-Z0-9_-]{3,15}$";
	private static final String PASSWORD_PATTERN = "^[a-zA-Z0-9_-]{3,15}$";
	private static final String ROLE_PATTERN = "^[a-zA-Z0-9_]{3,15}$";

	@Override
	public Runnable isSane(DataObject data) {
		return new RunningSanityChecker(data);
	}
	public boolean isSane(String data) {
		return data.trim().startsWith("<?") && (data.toLowerCase().contains("s:envelope") || data.toLowerCase().contains("soap:envelope"));
	}

	@Override
	public boolean isSane(String userName, String password, String role) {
		//userName
		return (validUser(userName) && validPassword(password) && validRole(role));

	}

	public boolean validUser(String userName){
		pattern = Pattern.compile(USERNAME_PATTERN);
		matcher = pattern.matcher(userName);
		return matcher.matches();
	}

	public boolean validPassword(String password){
		pattern = Pattern.compile(PASSWORD_PATTERN);
		matcher = pattern.matcher(password);
		return matcher.matches();
	}

	public boolean validRole(String role){
		pattern = Pattern.compile(ROLE_PATTERN);
		matcher = pattern.matcher(role);
		return matcher.matches();
	}

	class RunningSanityChecker implements Runnable {
		DataObject data;
		
		public RunningSanityChecker(DataObject dataObj) {
			data = dataObj;
		}

		@Override
		public void run() {
			if(isSane(data.getSoap())) {
				data.setSane(true);
			} else {
				try {
					((ReceiveObjectImpl)data.getReceiveObject()).setReply("UnsupportedEncodingException");
				} catch (InterruptedException e) {
					//Should never happen
					e.printStackTrace();
				}
				UnsupportedEncodingException e = new UnsupportedEncodingException("Sanity Check of message failed");
				data.getExceptionHandler().unsupportedEncodingExceptionThrown(e);
			}
		}

	}

}
