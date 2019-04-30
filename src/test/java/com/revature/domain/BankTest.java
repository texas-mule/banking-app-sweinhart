package com.revature.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotEquals;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import com.revature.domain.AccountRequest.Request;
import com.revature.service.AccountRequestDAO;

public class BankTest {
	
	@Test
	public void testBankGet() {
		String resultString = Bank.getBankName();
		String expectedResultString = "Revature Bank";
		assertEquals(expectedResultString, resultString);
		expectedResultString = "123456789";
		resultString = Bank.getRoutingNumber().toString();
		assertEquals(expectedResultString, resultString);
		int expectedResultInt = 10000000;
		int resultInt = Bank.getNextAccountNumber();
		assertEquals(expectedResultInt, resultInt);
		AccountRequest accountRequest = new AccountRequest();
		Request testRequest = accountRequest.new Request();		
		testRequest.addUserSSNumber("123-45-6789");
		testRequest.setAccountType("Test");
		testRequest.setDeposit(1.0);
		Bank.addAccountRequest(testRequest);
		Request testRequest1 = Bank.getAccountRequests().get(0);
		assertEquals(testRequest.getDate(), (testRequest1.getDate()));
		assertEquals(testRequest.getTime(), (testRequest1.getTime()));
		AccountRequestDAO impl = AccountRequestDAO.getConnection();
		assertNotEquals(testRequest1.getId(), null);
		assertEquals(true, impl.delete(testRequest1));
		UserAccount testUserAccount = new UserAccount();
		BankAccount testBankAccount = new BankAccount();
		testBankAccount.setAccountNumber(Bank.getNextAccountNumber());
		testUserAccount.getAccounts().add(testBankAccount);
		testUserAccount.setId(1);
		List<UserAccount> testUserAccounts = new ArrayList<UserAccount>();
		testUserAccounts.add(testUserAccount);
		Bank.setUserAccounts(testUserAccounts);
		UserAccount testUserAccount1 = Bank.getUserAccounts().get(0);
		assertEquals(testUserAccount, testUserAccount1);		
	}
	
	@Test
	public void testValidateLicenseNumber() {
		assertFalse(Bank.validateLicenseNumber("123"));
		assertFalse(Bank.validateLicenseNumber("ABC"));
		assertTrue(Bank.validateLicenseNumber("1234"));
		assertTrue(Bank.validateLicenseNumber("ABCD"));
		assertTrue(Bank.validateLicenseNumber("1234ABCD"));
		assertFalse(Bank.validateLicenseNumber("1234ABCD!"));
	}
	
	@Test
	public void testValidatePhone() {
		assertFalse(Bank.validatePhone("123"));
		assertFalse(Bank.validatePhone("123-4567890"));
		assertFalse(Bank.validatePhone("(123)456-7890"));
		assertFalse(Bank.validatePhone("123-ABC-7890"));
		assertFalse(Bank.validatePhone("123-!23-7890"));
		assertTrue(Bank.validatePhone("123-456-7890"));
	}
	
	@Test
	public void testModifyPhone() {
		String phone = Bank.modifyPhone("1234567890");
		assertEquals("123-456-7890", phone);
		phone = Bank.modifyPhone("(123)456-7890");
		assertEquals("123-456-7890", phone);
		phone = Bank.modifyPhone("(123)456");
		assertEquals("(123)456", phone);
		phone = Bank.modifyPhone("abcdefghij");
		assertEquals("abc-def-ghij", phone);
	}
	
	@Test
	public void testValidateZip() {
		assertFalse(Bank.validateZip("1234"));
		assertTrue(Bank.validateZip("12345"));
		assertFalse(Bank.validateZip("1234A"));
		assertFalse(Bank.validateZip("123456"));
		assertFalse(Bank.validateZip("12345-678"));
		assertTrue(Bank.validateZip("12345-6789"));
		assertFalse(Bank.validateZip("1234A-6789"));
		assertFalse(Bank.validateZip("12345-A789"));
	}
	
	@Test
	public void testValidateState() {
		States states = new States();
		for (String state : states.getStateAbr()) {
			assertTrue(Bank.validateState(state));
		}
		assertFalse(Bank.validateState("AW"));
		assertFalse(Bank.validateState("test"));
		assertFalse(Bank.validateState("123"));
	}
	
	@Test
	public void testValidateCity() {
		assertTrue(Bank.validateCity("AW"));
		assertTrue(Bank.validateCity("aw"));
		assertFalse(Bank.validateCity("A1"));
		assertFalse(Bank.validateCity("A"));
	}
	
	@Test
	public void testValidateName() {
		assertTrue(Bank.validateName("AW"));
		assertTrue(Bank.validateName("aw"));
		assertFalse(Bank.validateName("A1"));
		assertFalse(Bank.validateName("A"));
	}
	
	@Test
	public void testValidateUserName() {
		assertTrue(Bank.validateUsername("abcde"));
		assertTrue(Bank.validateUsername("ABCDE"));
		assertTrue(Bank.validateUsername("ABCD1"));
		assertFalse(Bank.validateUsername("ABCD"));
		assertFalse(Bank.validateUsername("abcd"));
		assertFalse(Bank.validateUsername("abcd!"));
	}
	
	@Test
	public void testValidatePassword() {
		assertTrue(Bank.validatePassword("1234Abc!"));
		assertFalse(Bank.validatePassword("123Abc!"));
		assertFalse(Bank.validatePassword("1234abc!"));
		assertFalse(Bank.validatePassword("1234Abcd"));
		assertFalse(Bank.validatePassword("abcdABCD"));
		assertFalse(Bank.validatePassword("1234ABCD!"));
	}
	
	@Test
	public void testValidateSocialSecurity() {
		assertTrue(Bank.validateSocialSecurity("123-45-6789"));
		assertFalse(Bank.validateSocialSecurity("123456789"));
		assertFalse(Bank.validateSocialSecurity("A23-45-6789"));
		assertFalse(Bank.validateSocialSecurity("ABCDEFGHI"));
		assertFalse(Bank.validateSocialSecurity("123-456789"));
		assertFalse(Bank.validateSocialSecurity("123-45-678!"));
	}
	
	@Test
	public void testValidateExpiration() {
		Date date = new Date();
		String dateString = date.toString();
		String [] dateToArray = dateString.split(" ");
		Integer month = Integer.valueOf(Months.getMonths().get(dateToArray[1]));
		Integer day = Integer.valueOf(dateToArray[2]);
		Integer year = Integer.valueOf(dateToArray[5]);
		String [] testDateArray = new String [3];
		if (month.toString().length() == 1)
			testDateArray[0] = "0" + month.toString();
		else
			testDateArray[0] = month.toString();
		if (day.toString().length() == 1)
			testDateArray[1] = "0" + day.toString();
		else
			testDateArray[1] = day.toString();
		testDateArray[2] = year.toString();
		String todayDate = testDateArray[0] + "/" + testDateArray[1] + "/" + testDateArray[2];
		assertFalse(Bank.validateExpiration(testDateArray[0] + testDateArray[1] + testDateArray[2]));
		//calculate yesterday
		if (month == 1 && day == 1) {
			year--;
			month = 12;
			day = 31;
		}else if (day == 1) {
			month--;
			day = 28;
		} else
			day--;
		testDateArray[0] = month.toString();
		testDateArray[1] = day.toString();
		testDateArray[2] = year.toString();
		String yesterdate = testDateArray[0] + "/" + testDateArray[1] + "/" + testDateArray[2];
		assertTrue(Bank.validateExpiration(todayDate));
		assertFalse(Bank.validateExpiration(yesterdate));
		assertFalse(Bank.validateExpiration("AB/AB/2028"));
	}
	
	@Test
	public void testValidateAddress() {
		assertTrue(Bank.validateAddress("1234 ADBC"));
		assertTrue(Bank.validateAddress("1234 ADBC DR"));
		assertFalse(Bank.validateAddress("1234 1234"));
		assertFalse(Bank.validateAddress("ABCD ABCD"));
		assertFalse(Bank.validateAddress("ABCD 1234"));
		assertFalse(Bank.validateAddress("1234ABCD Ln"));
		assertFalse(Bank.validateAddress("1234 ABCD1"));
		assertFalse(Bank.validateAddress("1234 ADBC !"));
	}
	
	@Test
	public void testValidateLettersOnly() {
		Character c;
		for (int i = 0; i < 255; i++) {
			c = (char) i;
			if (c < 65 || c > 90 && c < 97 || c > 122)
				assertFalse(Bank.validateLettersOnly(c.toString()));
			else
				assertTrue(Bank.validateLettersOnly(c.toString()));
		}
		assertFalse(Bank.validateLettersOnly(""));
	}
	
	@Test
	public void testValidateNumbersOnly() {
		Character c;
		for (int i = 0; i < 255; i++) {
			c = (char) i;
			if (c < 48 || c > 57)
				assertFalse(Bank.validateNumbersOnly(c.toString()));
			else
				assertTrue(Bank.validateNumbersOnly(c.toString()));
		}
		assertFalse(Bank.validateNumbersOnly(""));
	}
	
	@Test
	public void testValidateLettersAndNumbersOnly() {
		Character c;
		for (int i = 0; i < 255; i++) {
			c = (char) i;
			if (c < 48 || c > 57 && c < 65 || c > 90 && c < 97 || c > 122)
				assertFalse(Bank.validateLettersAndNumbersOnly(c.toString()));
			else
				assertTrue(Bank.validateLettersAndNumbersOnly(c.toString()));
		}
		assertFalse(Bank.validateLettersAndNumbersOnly(""));
	}
	
	@Test
	public void testValidateEmail() {
		assertTrue(Bank.validateEmail("sw@test.edu"));
		assertTrue(Bank.validateEmail("sw@test.com"));
		assertTrue(Bank.validateEmail("sw@test.org"));
		assertTrue(Bank.validateEmail("sw@test.gov"));
		assertTrue(Bank.validateEmail("sw@test.net"));
		assertTrue(Bank.validateEmail("sw@test.mil"));
		assertFalse(Bank.validateEmail("swtest.edu"));
		assertFalse(Bank.validateEmail("sw@edu"));
		assertFalse(Bank.validateEmail("sw@.edu"));
		assertFalse(Bank.validateEmail(""));
	}
}
