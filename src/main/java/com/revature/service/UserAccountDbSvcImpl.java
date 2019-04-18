package com.revature.service;

import com.revature.domain.UserAccount;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.sql.ResultSet;

public final class UserAccountDbSvcImpl implements UserAccountInterface{
	
	private static UserAccountDbSvcImpl instance = new UserAccountDbSvcImpl();
    private static Connection conn;

    
	public UserAccountDbSvcImpl() {
		super();
		// TODO Auto-generated constructor stub
	}
	

	public static UserAccountDbSvcImpl getInstance() {
		return instance;
	}


	public boolean add(UserAccount account) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean delete(UserAccount account) {
		// TODO Auto-generated method stub
		return false;
	}

	public UserAccount get(String username) {
		// TODO Auto-generated method stub
		UserAccount account = new UserAccount();
		return account;
	}

	public boolean update(UserAccount account) {
		// TODO Auto-generated method stub
		return false;
	}


	public UserAccount getById(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}


	public UserAccount getByUsername(String username) {
		// TODO Auto-generated method stub
		return null;
	}


	public UserAccount getBySs(String ssNum) {
		// TODO Auto-generated method stub
		return null;
	}


	public List<UserAccount> getAll() {
		// TODO Auto-generated method stub
		return null;
	}

}
