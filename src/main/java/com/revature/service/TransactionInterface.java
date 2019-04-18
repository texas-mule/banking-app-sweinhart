package com.revature.service;

import java.util.List;

import com.revature.domain.Transaction;

public interface TransactionInterface {
	boolean add(Transaction transaction);
	boolean delete(Integer accountNumber);
	List<Transaction> getAll(Integer accountNumber);
}
