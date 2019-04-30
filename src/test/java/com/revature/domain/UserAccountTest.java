package com.revature.domain;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.revature.service.AccountRequestDAO;

public class UserAccountTest {
	@Test
	public void testUserAccount() {
		UserAccount userAccount = new UserAccount();
		Login login = new Login();
		AccountRequests accountRequest = new AccountRequests();
		AccountRequests.Request request = accountRequest.new Request();
		BankAccount bankAccount = new BankAccount();
		userAccount.setId(1);
		String username = "username";
		String firstname = "firstname";
		String lastname = "lastname";
		String address1 = "Revature Dr";
		String address2 = "#1";
		String city = "Dallas";
		String state = "TX";
		String zipcode = "12345";
		String phone = "123-456-7890";
		String email = "Test@test.edu";
		String ss = "123-45-6789";
		String dlstate = "TX";
		String dlnum = "1234ABCD";
		String dlexp = "01/01/2001";
		login.setUsername(username);
		userAccount.setLogin(login);
		userAccount.setAccessLvl(1);
		userAccount.setFirstName(firstname);
		userAccount.setLastName(lastname);
		userAccount.setAddress1(address1);
		userAccount.setAddress2(address2);
		userAccount.setCity(city);
		userAccount.setState(state);
		userAccount.setZipCode(zipcode);
		userAccount.setPhone(phone);
		userAccount.setEmail(email);
		userAccount.setSocialSecurity(ss);
		userAccount.setDlState(dlstate);
		userAccount.setDlNumber(dlnum);
		userAccount.setDlExp(dlexp);
		bankAccount.setId(1);
		userAccount.getAccounts().add(bankAccount);
		request.setAccountType("Checking");
		request.addUserSSNumber(ss);
		request.setDeposit(200.0);
		AccountRequestDAO impl = AccountRequestDAO.getConnection();
		impl.add(request);
		request.setId(1);
		accountRequest.addAccountRequest(request);
		userAccount.setPendingRequests(accountRequest);
		UserAccount newUser = userAccount;
		assertEquals((Integer) 1, newUser.getId());
		assertEquals((Integer) 1, newUser.getAccessLvl());
		assertEquals(userAccount.getLogin().getUsername(), newUser.getLogin().getUsername());
		assertEquals(userAccount.getFirstName(), newUser.getFirstName());
		assertEquals(userAccount.getLastName(), newUser.getLastName());
		assertEquals(userAccount.getFirstName(), newUser.getFirstName());
		assertEquals(userAccount.getCity(), newUser.getCity());
		assertEquals(userAccount.getState(), newUser.getState());
		assertEquals(userAccount.getZipCode(), newUser.getZipCode());
		assertEquals(userAccount.getPhone(), newUser.getPhone());
		assertEquals(userAccount.getEmail(), newUser.getEmail());
		assertEquals(userAccount.getSocialSecurity(), newUser.getSocialSecurity());
		assertEquals(userAccount.getDlState(), newUser.getDlState());
		assertEquals(userAccount.getDlNumber(), newUser.getDlNumber());
		assertEquals(userAccount.getDlExp(), newUser.getDlExp());
		assertEquals((Integer) 1, newUser.getAccounts().get(0).getId());
		assertEquals(userAccount.getPendingRequests().getAccountRequests().get(0).getId(), newUser.getPendingRequests().getAccountRequests().get(0).getId());
		impl.delete(request);
	}
}
