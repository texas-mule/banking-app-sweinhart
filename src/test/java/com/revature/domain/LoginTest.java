package com.revature.domain;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class LoginTest {
	@Test
	public void testLogin() {
		String username = "TestUsername";
		String password = "TestPassword";
		String encodedPassword;
		Integer accessLvl;
		Login login = new Login();
		login.setUsername(username);
		encodedPassword = login.setPassword(password, 3);
		assertEquals(username, login.getUsername());
		assertEquals(encodedPassword, login.getPassword());
		accessLvl = Login.getPasswordAccessLvlMatch(password, encodedPassword);
		assertEquals((Integer) 3, accessLvl);
	}
}
