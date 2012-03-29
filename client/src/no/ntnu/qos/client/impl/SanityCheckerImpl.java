package no.ntnu.qos.client.impl;

import no.ntnu.qos.client.DataObject;
import no.ntnu.qos.client.SanityChecker;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SanityCheckerImpl implements SanityChecker{

    private Pattern pattern;
    private Matcher matcher;

    private static final String USERNAME_PATTERN = "^[a-z0-9_-]{3,15}$";
    private static final String PASSWORD_PATTERN = "^[a-z0-9_-]{3,15}$";
    private static final String ROLE_PATTERN = "^[a-z0-9]{3,15}$";

	@Override
	public void isSane(DataObject data) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isSane(String userName, String password, String role) {
		//userName
        if(!validUser(userName)) return false;

        //password
        if(!validPassword(password)) return false;

        //role
        if(!validRole(role)) return false;

		return true;
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

}
