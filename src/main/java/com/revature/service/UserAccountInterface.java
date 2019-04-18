package com.revature.bankingApp.service;

import java.util.List;

import com.revature.bankingApp.domain.UserAccount;

public interface UserAccountInterface {
	boolean add(UserAccount account);
	boolean delete(UserAccount account);
	UserAccount getById(Integer id);
	UserAccount getByUsername(String username);
	UserAccount getBySs(String ssNum);
	List<UserAccount> getAll();
	boolean update(UserAccount account);
}
