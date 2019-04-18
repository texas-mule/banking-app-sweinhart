package com.revature.service;

import com.revature.domain.Bank;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

public class BankDbSvcImpl implements BankInterface{
	
	private static BankDbSvcImpl instance = new BankDbSvcImpl();
    private static Connection conn;

    
	public BankDbSvcImpl() {
		super();
		// TODO Auto-generated constructor stub
	}


	public boolean add(String bankName) {
		// TODO Auto-generated method stub
		return false;
	}


	public boolean delete(String bankName) {
		// TODO Auto-generated method stub
		return false;
	}


	public boolean get(String bankName) {
		// TODO Auto-generated method stub
		return false;
	}


	public boolean update(String bankName) {
		// TODO Auto-generated method stub
		return false;
	}


	public static BankDbSvcImpl getInstance() {
		// TODO Auto-generated method stub
		return instance;
	}


}
