package com.revature.bankingApp.service;

import com.revature.bankingApp.domain.Bank;

public interface BankInterface {
	boolean add(String bankName);
	boolean delete(String bankName);
	boolean get(String bankName);
	boolean update(String bankName);
}
