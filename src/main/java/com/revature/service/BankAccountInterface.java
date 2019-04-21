package com.revature.service;

import java.util.List;
import com.revature.domain.BankAccount;

public interface BankAccountInterface {
	boolean add(BankAccount account);
	boolean delete(BankAccount account);
	BankAccount getAccount(Integer accountNumber);
	List<BankAccount> getClientAccounts(String username);
	List<BankAccount> getAll();
}
