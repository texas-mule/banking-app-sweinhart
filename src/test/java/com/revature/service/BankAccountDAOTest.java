package com.revature.service;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.revature.domain.BankAccount;

public class BankAccountDAOTest {
	BankAccount account = new BankAccount();
	BankAccountDAO impl = BankAccountDAO.getConnection();
	
	@Before
	public void init() {
		account.setAccountNumber(12345);
		account.getAccountOwners().add("123-45-6789");
		account.setAccountType("Checking");
		account.setBalance(200.0);		
	}

	@Test
	public void testBankAccountDAO() {
		assertTrue(impl.add(account));
		BankAccount newAccount = impl.getAccount(account.getAccountNumber());
		account = newAccount;
		boolean test = false;
		if (account.getId() != null)
			test = true;
		assertTrue(test);
		List<BankAccount> list = impl.getAll();
		if (list.isEmpty())
			test = false;
		else
			test = true;
		assertTrue(test);
		test = true;
		if (account.getAccountOwners().size() != 1)
			test = false;
		assertTrue(test);
		account.getAccountOwners().add("234-56-7890");
		impl.update(account);
		account = impl.getAccount(account.getAccountNumber());
		if (account.getAccountOwners().size() != 2)
			test = false;
		assertTrue(test);
		assertTrue(impl.delete(account));
	}

}
