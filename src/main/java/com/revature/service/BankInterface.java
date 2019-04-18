package com.revature.service;

import com.revature.domain.Bank;

public interface BankInterface {
	boolean add(String bankName);
	boolean delete(String bankName);
	boolean get(String bankName);
	boolean update(String bankName);
}
