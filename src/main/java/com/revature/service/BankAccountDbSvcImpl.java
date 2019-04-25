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
import org.apache.log4j.Logger;

public final class BankAccountDbSvcImpl implements BankAccountInterface {
	private static Logger logger = Logger.getLogger(BankAccountDbSvcImpl.class);
	private static BankAccountDbSvcImpl instance = new BankAccountDbSvcImpl();
	private static Connection conn;
	
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
        	logger.fatal("Unable to open database\n" + e.getMessage());
			System.exit(1);
        }
    }
	
	private static void close() {
        try {
            conn.close();
        } catch (SQLException e) {
        	logger.fatal("Unable to close database\n" + e.getMessage());
			System.exit(1);
        }
    }

	private static void setup() {
		String sql = "CREATE TABLE IF NOT EXISTS BankAccounts (\n" 
				+ " id integer PRIMARY KEY,\n"
				+ "	accountType text NOT NULL,\n" 
				+ "	accountNumber integer NOT NULL,\n"
				+ "	client1 text NOT NULL,\n" 
				+ "	client2 text\n" 
				+ ");";
		connect();
		try {
			Statement stmt = conn.createStatement();
			stmt.execute(sql);
		} catch (SQLException e) {
			logger.fatal("BankAccounts Table Creation Failed\n" + e.getMessage());
			System.exit(1);
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
		String sql = "INSERT INTO BankAccounts(accountType, accountNumber, client1, client2) "
				+ "VALUES(?,?,?,?)";
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, account.getAccountType());
			pstmt.setInt(2, account.getAccountNumber());
			pstmt.setString(3, account.getAccountOwners().get(0));
			if (account.getAccountOwners().size() > 1)
				pstmt.setString(4, account.getAccountOwners().get(1));
			else
				pstmt.setInt(4, 0);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			logger.fatal("BankAccount Addition Failed\n" + e.getMessage());
			System.exit(1);
		}
		close();
		logger.info("BankAccount added successfully");
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
			logger.fatal("BankAccount Deletion Failed\n" + e.getMessage());
			System.exit(1);
		}
		close();

		UserAccountDbSvcImpl userImpl = UserAccountDbSvcImpl.getInstance();
		UserAccount user = new UserAccount();
		TransactionDbSvcImpl transImpl = TransactionDbSvcImpl.getInstance();
		int index = 0;
		for (String ownerId : account.getAccountOwners()) {
			user = userImpl.getBySs(ownerId);
			for (UserAccount u : Bank.getUserAccounts()) {
				if (u.getSocialSecurity().equals(user.getSocialSecurity()))
					Bank.getUserAccounts().get(index).getAccounts().remove(account);
				index++;
				break;
			}
			for (Transaction transaction : account.getTransactions()) {
				transImpl.delete(transaction.getId());
			}
			success = true;
		}
		logger.info("BankAccount deleted successfully");
		return success;
	}

	public List<BankAccount> getClientAccounts(String username) {
		// TODO Auto-generated method stub
		UserAccountDbSvcImpl impl = UserAccountDbSvcImpl.getInstance();
		UserAccount account = new UserAccount();
		account = impl.getByUsername(username);
		List<BankAccount> list = new ArrayList<BankAccount>();
		connect();
		String sql = "SELECT id, accountType, accountNumber, client1, client2 "
				+ "FROM BankAccounts WHERE client1 = ? OR client2 = ?";
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, account.getSocialSecurity());
			pstmt.setString(2, account.getSocialSecurity());
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				BankAccount bankAccount = new BankAccount();
				bankAccount.setId(rs.getInt("id"));
				bankAccount.setAccountType(rs.getString("accountType"));
				bankAccount.setAccountNumber(rs.getInt("accountNumber"));
				bankAccount.getAccountOwners().add(rs.getString("client1"));
				String ssNum = rs.getString("client2");
				if (!ssNum.equals(""))
					bankAccount.getAccountOwners().add(ssNum);
				bankAccount.setBalance(bankAccount.getBalance());
				list.add(bankAccount);
			}
		} catch (SQLException e) {
			logger.info("BankAccounts (client) retrieval Failed\n" + e.getMessage());
		}
		close();
		logger.info("BankAccounts retrieved successfully");
		return list;
	}

	public BankAccount getAccount(Integer accountNumber) {
		connect();
		String sql = "SELECT id, accountType, accountNumber, client1, client2 "
				+ "FROM BankAccounts WHERE accountNumber = ?";
		BankAccount bankAccount = new BankAccount();
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, accountNumber);
			ResultSet rs = pstmt.executeQuery();
			bankAccount.setId(rs.getInt("id"));
			bankAccount.setAccountType(rs.getString("accountType"));
			bankAccount.setAccountNumber(rs.getInt("accountNumber"));
			bankAccount.getAccountOwners().add(rs.getString("client1"));
			String ssNum = rs.getString("client2");
			if (!ssNum.equals(""))
				bankAccount.getAccountOwners().add(ssNum);
			bankAccount.setBalance(bankAccount.getBalance());
		} catch (SQLException e) {
			logger.info("BankAccount retrieval Failed\n" + e.getMessage());
		}
		close();
		logger.info("BankAccount retrieved successfully");
		return bankAccount;
	}

	public List<BankAccount> getAll() {
		// TODO Auto-generated method stub
		List<BankAccount> list = new ArrayList<BankAccount>();
		connect();
		String sql = "SELECT id, accountType, accountNumber, client1, client2 " + "FROM BankAccounts";
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				BankAccount bankAccount = new BankAccount();
				bankAccount.setId(rs.getInt("id"));
				bankAccount.setAccountType(rs.getString("accountType"));
				bankAccount.setAccountNumber(rs.getInt("accountNumber"));
				bankAccount.getAccountOwners().add(rs.getString("client1"));
				String ssNum = rs.getString("client2");
				if (!ssNum.equals(""))
					bankAccount.getAccountOwners().add(ssNum);
				bankAccount.setBalance(bankAccount.getBalance());
				list.add(bankAccount);
			}
		} catch (SQLException e) {
			logger.info("BankAccounts (all) retrieval Failed\n" + e.getMessage());
		}
		close();
		logger.info("BankAccounts retrieved successfully");
		return list;
	}

	public boolean update(BankAccount account) {
		// TODO Auto-generated method stub
		connect();
		String sql = "UPDATE BankAccounts SET accountType = ? , accountNumber = ? , "
				+ "client1 = ? , client2 = ? " + "WHERE id = ?";
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, account.getAccountType());
			pstmt.setInt(2, account.getAccountNumber());
			pstmt.setString(3, account.getAccountOwners().get(0));
			if (account.getAccountOwners().size() > 1)
				pstmt.setString(4, account.getAccountOwners().get(1));
			else
				pstmt.setString(4, "");			
			pstmt.setInt(5, account.getId());
			pstmt.executeUpdate();
		} catch (SQLException e) {
			logger.fatal("BankAccount update Failed\n" + e.getMessage());
			System.exit(1);
		}
		close();
		logger.info("BankAccount updated successfully");
		return true;
	}

}
