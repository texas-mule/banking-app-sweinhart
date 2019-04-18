package com.revature.bankingApp.service;

import java.util.List;

import com.revature.bankingApp.domain.Transaction;

public interface TransactionInterface {
	boolean add(Transaction transaction);
	boolean delete(Integer accountNumber);
	List<Transaction> getAll(Integer accountNumber);
}
