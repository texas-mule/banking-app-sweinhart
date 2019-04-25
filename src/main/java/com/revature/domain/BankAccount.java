package com.revature.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.revature.service.TransactionDbSvcImpl;

public class BankAccount implements Comparable<BankAccount>{
	private Integer id;
	private Integer accountNumber;
	private String accountType;  //checking or savings
	private Double balance = 0.0;
	private List<String> accountOwnerSSnumbers = new ArrayList<String>();
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
	public List<String> getAccountOwners() {
		return accountOwnerSSnumbers;
	}
	public void setAccountOwners(List<String> accountOwners) {
		this.accountOwnerSSnumbers = accountOwners;
	}
	@Override
	public int compareTo(BankAccount o) {
		// TODO Auto-generated method stub
		return accountNumber.compareTo(o.accountNumber);
	}
	
}
