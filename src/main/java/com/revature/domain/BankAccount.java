package com.revature.bankingApp.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.revature.bankingApp.service.TransactionDbSvcImpl;

public class BankAccount {
	private Integer id;
	private Integer accountNumber;
	private String accountType;  //checking or savings
	private Double balance = 0.0;
	private List<UserAccount> accountOwners = new ArrayList<UserAccount>();
	private List<Transaction> transactions = new ArrayList<Transaction>();
	
	public List<Transaction> getTransactions() {
		TransactionDbSvcImpl impl = TransactionDbSvcImpl.getInstance();
		transactions = impl.getAll(accountNumber);
		Collections.sort(transactions);
		return transactions;
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
	public List<UserAccount> getAccountOwners() {
		return accountOwners;
	}
	public void setAccountOwners(List<UserAccount> accountOwners) {
		this.accountOwners = accountOwners;
	}
	
}
