package com.revature.service;

import com.revature.domain.Bank;
import com.revature.domain.BankAccount;
import com.revature.domain.Transaction;
import com.revature.domain.UserAccount;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.sql.ResultSet;

public final class BankAccountDbSvcImpl implements BankAccountInterface {

	private static BankAccountDbSvcImpl instance = new BankAccountDbSvcImpl();
	private static Connection conn;
	private Integer integer;

	public BankAccountDbSvcImpl() {
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
		String sql = "CREATE TABLE IF NOT EXISTS BankAccounts (\n"
				+ " id integer PRIMARY KEY,\n"
				+ "	accountType text NOT NULL,\n" 
				+ "	accountNumber integer NOT NULL,\n"
				+ "	balance real NOT NULL,\n" 
				+ "	client1 integer NOT NULL,\n" 
				+ "	client2 integer\n" 
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

	public static BankAccountDbSvcImpl getInstance() {
		// TODO Auto-generated method stub
		return instance;
	}

	public boolean add(BankAccount account) {
		// TODO Auto-generated method stub
		connect();
		String sql = "INSERT INTO BankAccounts(accountType, accountNumber, balance, client1, client2) "
				+ "VALUES(?,?,?,?,?)";
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, account.getAccountType());
			pstmt.setInt(2, account.getAccountNumber());
			pstmt.setDouble(3, account.getBalance());
			pstmt.setInt(4, account.getAccountOwners().get(0));
			if (account.getAccountOwners().size() > 1)
				pstmt.setInt(5, account.getAccountOwners().get(1));
			else {
				integer = (Integer) null;
				pstmt.setInt(5, integer);
			}
			pstmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			return false;
		}
		close();
		return true;
	}

	public boolean delete(BankAccount account) {
		// TODO Auto-generated method stub
		boolean success = false;
		connect();
		String sql = "DELETE FROM BankAccount WHERE id = ?";
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, account.getId());
			pstmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			return false;
		}
		close();
		
		UserAccountDbSvcImpl userImpl = UserAccountDbSvcImpl.getInstance();
		UserAccount user = new UserAccount();
		TransactionDbSvcImpl transImpl = TransactionDbSvcImpl.getInstance();
		int index = 0;
		for (Integer ownerId : account.getAccountOwners()) {
			user = userImpl.getById(ownerId);
			for (UserAccount u : Bank.getUserAccounts()) {
				if (u.getId() == user.getId())
					Bank.getUserAccounts().get(index).getAccounts().remove(account);
				index++;
				break;
			}
			for (Transaction transaction : account.getTransactions()) {
				transImpl.delete(transaction.getId());
			}
			success = true;
		}
		return success;
	}

	public List<BankAccount> getClientAccounts(String username) {
		// TODO Auto-generated method stub
		List<BankAccount> accounts = new ArrayList<BankAccount>();
		return accounts;
	}

	public BankAccount getAccount(Integer accountNumber) {
		BankAccount account = new BankAccount();
		//getTransactions
		return account;
	}

	public List<BankAccount> getAll() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean update(BankAccount toAccount) {
		// TODO Auto-generated method stub
		boolean success = false;
		return success;
	}

}
