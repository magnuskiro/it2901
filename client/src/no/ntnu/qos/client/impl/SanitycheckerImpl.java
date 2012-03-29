package no.ntnu.qos.client.impl;

import java.io.UnsupportedEncodingException;

import no.ntnu.qos.client.DataObject;
import no.ntnu.qos.client.SanityChecker;

public class SanitycheckerImpl implements SanityChecker{
	
	public SanitycheckerImpl() {
		
	}
	@Override
	public void isSane(DataObject data) {
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
			return;
		}
		
	}

	@Override
	public boolean isSane(String userName, String password, String role) {
		if(!userName.trim().equals("") && !password.trim().equals("")) {
			return true;
		}
		return false;
	}
	
	public boolean isSane(String data) {
		if (data.trim().startsWith("<?") && (data.toLowerCase().contains("s:envelope") || data.toLowerCase().contains("soap:envelope"))) {
			return true;
		} else {
			return false;
		}
	}

}
