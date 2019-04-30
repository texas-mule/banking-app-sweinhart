package com.revature.service;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.revature.domain.Login;
import com.revature.domain.UserAccount;

public class UserAccountDAOTest {
	UserAccount user = new UserAccount();
	UserAccountDAO impl = UserAccountDAO.getConnection();
	
	@Before
	public void init() {
		Login login = new Login();
		login.setUsername("username");
		login.setPassword("password", 1);
		user.setLogin(login);
		user.setFirstName("firstname");
		user.setLastName("lastname");
		user.setAddress1("1234 test");
		user.setAddress2("");
		user.setCity("city");
		user.setState("state");
		user.setZipCode("12345");
		user.setPhone("123-456-7890");
		user.setEmail("test@test.net");
		user.setSocialSecurity("123-45-6789");
		user.setDlState("TX");
		user.setDlNumber("123456");
		user.setDlExp("EXP");
	}
	
	@Test
	public void testUserAccountDAO() {		
		assertTrue(impl.add(user));
		user = impl.getBySs(user.getSocialSecurity());
		boolean test = false;
		if (user.getId() != null)
			test = true;
		assertTrue(test);
		test = false;
		user = impl.getById(user.getId());
		if (user.getId() != null)
			test = true;
		assertTrue(test);
		test = false;
		user = impl.getByDl(user.getDlState(), user.getDlNumber());
		if (user.getId() != null)
			test = true;
		assertTrue(test);
		test = false;
		user = impl.getByUsername(user.getLogin().getUsername());
		if (user.getId() != null)
			test = true;
		assertTrue(test);
		test = false;
		user.setFirstName("NewFirstName");
		assertTrue(impl.update(user));
		user = impl.getById(user.getId());
		if (user.getFirstName().contentEquals("NewFirstName"))
			test = true;
		else
			test = false;
		assertTrue(test);
		assertTrue(impl.delete(user));
	}
}
