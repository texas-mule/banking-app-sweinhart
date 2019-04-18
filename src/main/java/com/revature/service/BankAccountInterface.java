package com.revature.bankingApp.service;

import java.util.List;
import com.revature.bankingApp.domain.BankAccount;

public interface BankAccountInterface {
	boolean add(BankAccount account);
	boolean delete(BankAccount account);
	boolean update(BankAccount account);
	List<BankAccount> get(String username);
	List<BankAccount> getAll();
}
