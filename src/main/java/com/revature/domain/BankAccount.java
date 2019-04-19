package com.revature.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.revature.service.TransactionDbSvcImpl;

public class BankAccount {
	private Integer id;
	private Integer accountNumber;
	private String accountType;  //checking or savings
	private Double balance = 0.0;
	private List<Integer> accountOwnerIds = new ArrayList<Integer>();
	private List<Transaction> transactions = new ArrayList<Transaction>();
	
	public List<Transaction> getTransactions() {
		Collections.sort(transactions);
		return transactions;
	}
	public void setTransactions(List<Transaction> transactions) {
		this.transactions = transactions;
	}
	public void addTransaction(Transaction transaction) {
		this.transactions.add(transaction);
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(Integer accountNumber) {
		this.accountNumber = accountNumber;
	}
	public String getAccountType() {
		return accountType;
	}
	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	public Double getBalance() {
		balance = 0.0;
		TransactionDbSvcImpl impl = TransactionDbSvcImpl.getInstance();
		transactions = impl.getAll(accountNumber);
		for (Transaction transaction : transactions)
			balance += transaction.getTransactionAmount();
		return balance;
	}
	public void setBalance(Double balance) {
		this.balance = balance;
	}
	public List<Integer> getAccountOwners() {
		return accountOwnerIds;
	}
	public void setAccountOwners(List<Integer> accountOwners) {
		this.accountOwnerIds = accountOwners;
	}
	
}
