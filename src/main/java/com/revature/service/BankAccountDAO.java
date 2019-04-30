package com.revature.service;

import com.revature.domain.Bank;
import com.revature.domain.BankAccount;
import com.revature.domain.Transaction;
import com.revature.domain.UserAccount;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.ResultSet;
import org.apache.log4j.Logger;

public final class BankAccountDAO implements BankAccountInterface {
	private static Logger logger = Logger.getLogger(BankAccountDAO.class);
	private static BankAccountDAO instance = new BankAccountDAO();
	private static final String url = "jdbc:postgresql://localhost:5432/postgres";
	private static final String username = "sweinhart";
	private static final String password = "Password";	

	public static BankAccountDAO getConnection() {
		// TODO Auto-generated method stub
		return instance;
	}

	public boolean add(BankAccount account) {
		// TODO Auto-generated method stub
		String sql = "INSERT INTO bank_accounts (account_type, account_number, client1, client2) "
				+ "VALUES (?,?,?,?)";
		try (Connection conn = DriverManager.getConnection(url, username, password)) {
			try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
				pstmt.setString(1, account.getAccountType());
				pstmt.setInt(2, account.getAccountNumber());
				pstmt.setString(3, account.getAccountOwners().get(0));
				if (account.getAccountOwners().size() > 1)
					pstmt.setString(4, account.getAccountOwners().get(1));
				else
					pstmt.setString(4, "0");
				pstmt.executeUpdate();
			} catch (SQLException e) {
				logger.fatal("BankAccount Addition Failed\n" + e.getMessage());
				System.exit(1);
			}
			conn.close();
		} catch (SQLException ex) {
			logger.fatal("Unable to open database\n" + ex.getMessage());
		}
		logger.info("BankAccount added successfully");
		return true;
	}

	public boolean delete(BankAccount account) {
		// TODO Auto-generated method stub
		boolean success = false;
		String sql = "DELETE FROM bank_accounts WHERE id = ?";
		try (Connection conn = DriverManager.getConnection(url, username, password)) {
			try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
				pstmt.setInt(1, account.getId());
				pstmt.executeUpdate();
			} catch (SQLException e) {
				logger.fatal("BankAccount Deletion Failed\n" + e.getMessage());
				System.exit(1);
			}
			conn.close();
		} catch (SQLException ex) {
			logger.fatal("Unable to open database\n" + ex.getMessage());
		}

		UserAccountDAO userImpl = UserAccountDAO.getConnection();
		UserAccount user = new UserAccount();
		TransactionDAO transImpl = TransactionDAO.getConnection();
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
		UserAccountDAO impl = UserAccountDAO.getConnection();
		UserAccount account = new UserAccount();
		account = impl.getByUsername(username);
		List<BankAccount> list = new ArrayList<BankAccount>();
		String sql = "SELECT id, account_type, account_number, client1, client2 "
				+ "FROM bank_accounts WHERE client1 = ? OR client2 = ?";
		try (Connection conn = DriverManager.getConnection(url, BankAccountDAO.username, password)) {
			try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
				pstmt.setString(1, account.getSocialSecurity());
				pstmt.setString(2, account.getSocialSecurity());
				ResultSet rs = pstmt.executeQuery();
				while (rs.next()) {
					BankAccount bankAccount = new BankAccount();
					bankAccount.setId(rs.getInt("id"));
					bankAccount.setAccountType(rs.getString("account_type"));
					bankAccount.setAccountNumber(rs.getInt("account_number"));
					bankAccount.getAccountOwners().add(rs.getString("client1"));
					String ssNum = rs.getString("client2");
					if (!ssNum.equals("0"))
						bankAccount.getAccountOwners().add(ssNum);
					bankAccount.setBalance(bankAccount.getBalance());
					list.add(bankAccount);
				}
			} catch (SQLException e) {
				logger.info("BankAccounts (client) retrieval Failed\n" + e.getMessage());
			}
			conn.close();
		} catch (SQLException ex) {
			logger.fatal("Unable to open database\n" + ex.getMessage());
		}
		logger.info("BankAccounts retrieved successfully");
		return list;
	}

	public BankAccount getAccount(Integer accountNumber) {
		String sql = "SELECT id, account_type, account_number, client1, client2 "
				+ "FROM bank_accounts WHERE account_number = ?";
		BankAccount bankAccount = new BankAccount();
		try (Connection conn = DriverManager.getConnection(url, username, password)) {
			try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
				pstmt.setInt(1, accountNumber);
				ResultSet rs = pstmt.executeQuery();
				if (rs.next()) {
					bankAccount.setId(rs.getInt("id"));
					bankAccount.setAccountType(rs.getString("account_type"));
					bankAccount.setAccountNumber(rs.getInt("account_number"));
					bankAccount.getAccountOwners().add(rs.getString("client1"));
					String ssNum = rs.getString("client2");
					if (!ssNum.equals("0"))
						bankAccount.getAccountOwners().add(ssNum);
					bankAccount.setBalance(bankAccount.getBalance());
				}
			} catch (SQLException e) {
				logger.info("BankAccount retrieval Failed\n" + e.getMessage());
			}
			conn.close();
		} catch (SQLException ex) {
			logger.fatal("Unable to open database\n" + ex.getMessage());
		}
		logger.info("BankAccount retrieved successfully");
		return bankAccount;
	}

	public List<BankAccount> getAll() {
		// TODO Auto-generated method stub
		List<BankAccount> list = new ArrayList<BankAccount>();
		String sql = "SELECT id, account_type, account_number, client1, client2 " + "FROM bank_accounts";
		try (Connection conn = DriverManager.getConnection(url, username, password)) {
			try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
				ResultSet rs = pstmt.executeQuery();
				while (rs.next()) {
					BankAccount bankAccount = new BankAccount();
					bankAccount.setId(rs.getInt("id"));
					bankAccount.setAccountType(rs.getString("account_type"));
					bankAccount.setAccountNumber(rs.getInt("account_number"));
					bankAccount.getAccountOwners().add(rs.getString("client1"));
					String ssNum = rs.getString("client2");
					if (!ssNum.equals("0"))
						bankAccount.getAccountOwners().add(ssNum);
					bankAccount.setBalance(bankAccount.getBalance());
					list.add(bankAccount);
				}
			} catch (SQLException e) {
				logger.info("BankAccounts (all) retrieval Failed\n" + e.getMessage());
			}
			conn.close();
		} catch (SQLException ex) {
			logger.fatal("Unable to open database\n" + ex.getMessage());
		}
		logger.info("BankAccounts retrieved successfully");
		return list;
	}

	public boolean update(BankAccount account) {
		// TODO Auto-generated method stub
		String sql = "UPDATE bank_accounts SET account_type = ? , account_number = ? , "
				+ "client1 = ? , client2 = ? " + "WHERE id = ?";
		try (Connection conn = DriverManager.getConnection(url, username, password)) {
			try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
				pstmt.setString(1, account.getAccountType());
				pstmt.setInt(2, account.getAccountNumber());
				pstmt.setString(3, account.getAccountOwners().get(0));
				if (account.getAccountOwners().size() > 1)
					pstmt.setString(4, account.getAccountOwners().get(1));
				else
					pstmt.setString(4, "0");			
				pstmt.setInt(5, account.getId());
				pstmt.executeUpdate();
			} catch (SQLException e) {
				logger.fatal("BankAccount update Failed\n" + e.getMessage());
				System.exit(1);
			}
			conn.close();
		} catch (SQLException ex) {
			logger.fatal("Unable to open database\n" + ex.getMessage());
		}
		logger.info("BankAccount updated successfully");
		return true;
	}

}
