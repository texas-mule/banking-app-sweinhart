package com.revature.domain;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.log4j.Logger;

public class Login {
	private String username;
	private String password;
	static Logger logger = Logger.getLogger(Login.class);

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String encodedPassword) {
		this.password = encodedPassword;
	}
	
	public void setPassword(String password, Integer accessLvl) {
		this.password = encodePassword(password, accessLvl);
	}

	private static String encodePassword(String password, Integer accessLvl) {
		logger.info("Encoding password and access level");
		String encPassword = null;
		String plainPassword = password + accessLvl.toString();
		try {
			encPassword = md5(plainPassword);
		} catch (NoSuchAlgorithmException e) {
			logger.error("Encoding error " + e.getMessage());
			System.exit(1);	
		} catch (UnsupportedEncodingException e) {
			logger.error("Encoding error " + e.getMessage());
			System.exit(1);	
		}
		if (encPassword.isEmpty()) {
			System.exit(1);			
		}
		return encPassword;
	}
	
	public static Integer getPasswordAccessLvlMatch(String password, String storedPasswordMd5) {
		logger.info("Decoding password and access level");
		int accessLvl = 0;
		if (storedPasswordMd5.isEmpty())
			return 0;
		String testPassword;
		String [] access = {"1", "2", "3"};
		for (int i = 0; i < 3; i++) {
			testPassword = password + access[i];
			try {
				if (md5(testPassword).equals(storedPasswordMd5)) {
					accessLvl = i + 1;
				}
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				logger.error("Encoding error " + e.getMessage());
				System.exit(1);	
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				logger.error("Encoding error " + e.getMessage());
				System.exit(1);	
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
