package com.revature.bankingApp.service;

import com.revature.bankingApp.domain.BankAccount;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.sql.ResultSet;

public final class BankAccountDbSvcImpl implements BankAccountInterface{
	
	private static BankAccountDbSvcImpl instance = new BankAccountDbSvcImpl();
    private static Connection conn;



	public boolean add(BankAccount account) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean delete(BankAccount account) {
		// TODO Auto-generated method stub
		return false;
	}

	public List<BankAccount> get(String username) {
		// TODO Auto-generated method stub
		List<BankAccount> accounts = new ArrayList<BankAccount>();
		return accounts;
	}

	public boolean update(BankAccount account) {
		// TODO Auto-generated method stub
		return false;
	}

	public static BankAccountDbSvcImpl getInstance() {
		// TODO Auto-generated method stub
		return instance;
	}

	public List<BankAccount> getAll() {
		// TODO Auto-generated method stub
		return null;
	}

}