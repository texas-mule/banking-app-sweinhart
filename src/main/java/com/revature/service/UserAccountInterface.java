package com.revature.service;

import java.util.List;

import com.revature.domain.UserAccount;

public interface UserAccountInterface {
	boolean add(UserAccount account);
	boolean delete(UserAccount account);
	UserAccount getById(Integer id);
	UserAccount getByUsername(String username);
	UserAccount getBySs(String ssNum);
	List<UserAccount> getAll();
	boolean update(UserAccount account);
}
