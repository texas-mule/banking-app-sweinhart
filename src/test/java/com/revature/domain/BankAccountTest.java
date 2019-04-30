package com.revature.domain;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;


public class BankAccountTest {
	@Test
	public void testBankAccount() {
		BankAccount account = new BankAccount();
		Transaction transaction = new Transaction();
		account.setId(1);
		account.setAccountNumber(Bank.getNextAccountNumber());
		String checking = "Checking";
		account.setAccountType(checking);
		account.setBalance(20.0);
		List<Transaction> transactions = new ArrayList<Transaction>();
		List<String> owners = new ArrayList<String>();
		String ownerSS = "123-45-6789";
		owners.add(ownerSS);
		transaction.setId(1);
		transaction.setAccountNumber(account.getAccountNumber());
		transactions.add(transaction);
		account.setAccountOwners(owners);
		account.setTransactions(transactions);
		BankAccount newAccount = account;
		assertEquals(checking, newAccount.getAccountType());
		assertEquals((Integer) 1, newAccount.getId());
		assertEquals((Double) 20.0, newAccount.getBalance());
		assertEquals(ownerSS, newAccount.getAccountOwners().get(0));
		assertEquals(Bank.getNextAccountNumber(), newAccount.getAccountNumber());
		assertEquals((Integer) 1, newAccount.getTransactions().get(0).getId());
		assertEquals(account.getAccountNumber(), newAccount.getTransactions().get(0).getAccountNumber());
	}
}
