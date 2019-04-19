package com.revature.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.sql.ResultSet;
import java.util.List;
import com.revature.domain.Transaction;

public class TransactionDbSvcImpl implements TransactionInterface{
	
	private static TransactionDbSvcImpl instance = new TransactionDbSvcImpl();
	private static Connection conn;

	public boolean add(Transaction transaction) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean delete(Integer accountNumber) {
		// TODO Auto-generated method stub
		return false;
	}

	public List<Transaction> getAll(Integer accountNumber) {
		// TODO Auto-generated method stub
		return null;
	}

	public static TransactionDbSvcImpl getInstance() {
		// TODO Auto-generated method stub
		return instance;
	}

}
