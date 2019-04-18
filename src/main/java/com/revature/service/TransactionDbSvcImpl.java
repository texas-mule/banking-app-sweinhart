package com.revature.bankingApp.service;

import java.util.List;

import com.revature.bankingApp.domain.Transaction;

public class TransactionDbSvcImpl implements TransactionInterface{
	
	private static TransactionDbSvcImpl instance = new TransactionDbSvcImpl();

	public boolean add(Transaction transaction) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean delete(Integer accountNumber) {
		// TODO Auto-generated method stub
		return false;
	}

	public List<Transaction> getAll(Integer accountNumber) {
		// TODO Auto-generated method stub
		return null;
	}

	public static TransactionDbSvcImpl getInstance() {
		// TODO Auto-generated method stub
		return instance;
	}

}
