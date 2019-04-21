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

	
	public TransactionDbSvcImpl() {
		super();
		// TODO Auto-generated constructor stub
		connect();
        setup();
        close();
	}
	
	private static void connect() {
        try {
            String url = "jdbc:sqlite:BankingApp.db";
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
	
	private static void close() {
        try {
            conn.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
	
	private static void setup() {
        String sql = "CREATE TABLE IF NOT EXISTS Transaction (\n"
                + "	id integer PRIMARY KEY,\n"
                + "	transType text NOT NULL,\n"
                + "	accountNumber integer NOT NULL,\n"
                + "	date text NOT NULL,\n"
                + "	time text NOT NULL,\n"
                + "	transAmount real NOT NULL\n"
                + ");";
        connect();
        try {
            Statement stmt = conn.createStatement();
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        close();
    }

	public static TransactionDbSvcImpl getInstance() {
		// TODO Auto-generated method stub
		return instance;
	}
	
	public boolean add(Transaction transaction) {
		// TODO Auto-generated method stub
		boolean success = false;
		return success;
	}

	public boolean delete(Integer transId) {
		// TODO Auto-generated method stub
		boolean success = false;
		return success;
	}

	public List<Transaction> getAll(Integer accountNumber) {
		// TODO Auto-generated method stub
		return null;
	}

}
