package com.revature.service;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.revature.domain.Transaction;

public class TransactionDAOTest {
	Transaction transaction = new Transaction();
	TransactionDAO impl = TransactionDAO.getConnection();
	
	@Before
	public void init() {
		transaction.setAccountNumber(12345);
		transaction.setTransactionType(Transaction.deposit);
		transaction.setTransactionAmount(200.0);
	}
	
	@Test
	public void testTransactionDAO() {
		assertTrue(impl.add(transaction));
		boolean test = false;
		List<Transaction> list = impl.getAll(transaction.getAccountNumber());
		if (list.size() > 0)
			test = true;
		assertTrue(test);
		assertTrue(impl.delete(transaction.getAccountNumber()));
	}
}
