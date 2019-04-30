package com.revature.domain;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import org.junit.Test;

public class TransactionTest {
	@Test
	public void testTransaction() {
		Date now = new Date();
		Transaction transaction = new Transaction();
		String [] dateArray = now.toString().split(" ");
		int month = Integer.valueOf(Months.getMonths().get(dateArray[1]));
		int day = Integer.valueOf(dateArray[2]);
		int year = Integer.valueOf(dateArray[5]);
		String date = month + "/" + day + "/" + year;
		String time = dateArray[3];
		transaction.setAccountNumber(Bank.getNextAccountNumber());
		transaction.setTransactionAmount(5.0);
		transaction.setId(1);
		transaction.setTransactionType(Transaction.deposit);
		Transaction newTransaction = transaction;
		newTransaction.setDate(date, time);
		assertEquals(transaction.getAccountNumber(), newTransaction.getAccountNumber());
		assertEquals(transaction.getId(), newTransaction.getId());
		assertEquals(transaction.getTransactionType(), newTransaction.getTransactionType());
		assertEquals(transaction.getTransactionAmount(), newTransaction.getTransactionAmount());
		assertEquals(transaction.getDate(), newTransaction.getDate());
		assertEquals(transaction.getTime(), newTransaction.getTime());
	}
}
