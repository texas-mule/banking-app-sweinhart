package com.revature.domain;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.log4j.Logger;

public class Login {
	static private String username;
	static private String password;
	static Logger logger = Logger.getLogger(Login.class);

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		Login.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String encodedPassword) {
		Login.password = encodedPassword;
	}
	
	public void setPassword(String password, Integer accessLvl) {
		Login.password = encodePassword(password, accessLvl);
	}

	private static String encodePassword(String password, Integer accessLvl) {
		String encPassword = null;
		String plainPassword = password + accessLvl.toString();
		try {
			encPassword = md5(plainPassword);
		} catch (NoSuchAlgorithmException ex) {
			logger.fatal(ex.getStackTrace());
		} catch (UnsupportedEncodingException ex) {
			logger.fatal(ex.getStackTrace());
		}
		if (encPassword.isEmpty()) {
			System.exit(1);			
		}
		return encPassword;
	}
	
	public static Integer getPasswordAccessLvlMatch(String password, String storedPasswordMd5) {
		int accessLvl = 0;
		if (storedPasswordMd5.isEmpty())
			return 0;
		String testPassword;
		String [] access = {"1", "2", "3"};
		for (int i = 1; i < 3; i++) {
			testPassword = password + access[i-1];
			try {
				if (md5(testPassword).equals(storedPasswordMd5)) {
					accessLvl = i;
				}
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return accessLvl;
	}
	
	public static String md5(final String input) 
            throws NoSuchAlgorithmException, UnsupportedEncodingException {
        
        final MessageDigest md = MessageDigest.getInstance("MD5");
        //"UTF-8"
        final byte[] messageDigest = md.digest(input.getBytes("UTF-8"));
        final BigInteger number = new BigInteger(1, messageDigest);
        return String.format("%032x", number);
    }
}
