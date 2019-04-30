package com.revature.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.ResultSet;
import com.revature.domain.Transaction;
import org.apache.log4j.Logger;

public class TransactionDAO implements TransactionInterface{
	private static Logger logger = Logger.getLogger(TransactionDAO.class);
	private static TransactionDAO instance = new TransactionDAO();
	private static final String url = "jdbc:postgresql://localhost:5432/postgres";
	private static final String username = "sweinhart";
	private static final String password = "Password";

	
	public static TransactionDAO getConnection() {
		// TODO Auto-generated method stub
		return instance;
	}
	
	public boolean add(Transaction transaction) {
		// TODO Auto-generated method stub
		String sql = "INSERT INTO transactions (trans_type, account_number, date, time, trans_amount) "
				+ "VALUES (?,?,?,?,?)";
		try (Connection conn = DriverManager.getConnection(url, username, password)) {
			try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
				pstmt.setString(1, transaction.getTransactionType());
				pstmt.setInt(2, transaction.getAccountNumber());
				pstmt.setString(3, transaction.getDate());
				pstmt.setString(4, transaction.getTime());
				pstmt.setDouble(5, transaction.getTransactionAmount());
				pstmt.executeUpdate();
			} catch (SQLException e) {
				logger.fatal("Transaction addition Failed\n" + e.getMessage());
				System.exit(1);
			}
			conn.close();
		} catch (SQLException ex) {
			logger.fatal("Unable to open database\n" + ex.getMessage());
		}
		logger.info("Transaction added successfully");
		return true;
	}

	public boolean delete(Integer accountNumber) {
		// TODO Auto-generated method stub
		List<Transaction> list = getAll(accountNumber);		
		try (Connection conn = DriverManager.getConnection(url, username, password)) {
			for (Transaction transaction : list) {
				String sql = "DELETE FROM transactions WHERE id = ?";
				try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
					pstmt.setInt(1, transaction.getId());
					pstmt.executeUpdate();
				} catch (SQLException e) {
					logger.fatal("Transaction deletion Failed\n" + e.getMessage());
					System.exit(1);
				}
			}			
			conn.close();
		} catch (SQLException ex) {
			logger.fatal("Unable to open database\n" + ex.getMessage());
		}
		logger.info("Transaction deleted successfully");
		return true;
	}

	public List<Transaction> getAll(Integer accountNumber) {
		// TODO Auto-generated method stub
		List<Transaction> list = new ArrayList<Transaction>();
		String sql = "SELECT id, trans_type, account_number, date, time, trans_amount "
				+ "FROM transactions WHERE account_number = ?";
		try (Connection conn = DriverManager.getConnection(url, username, password)) {
			try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
				if (accountNumber == null) {
					conn.close();
					return list;
				}
				pstmt.setInt(1, accountNumber);
				ResultSet rs = pstmt.executeQuery();
				while (rs.next()) {
					Transaction transaction = new Transaction();
					transaction.setId(rs.getInt("id"));
					transaction.setTransactionType(rs.getString("trans_type"));
					transaction.setAccountNumber(rs.getInt("account_number"));
					transaction.setDate(rs.getString("date"), rs.getString("time"));
					transaction.setTransactionAmount(rs.getDouble("trans_amount"));
					list.add(transaction);
				}
			} catch (SQLException e) {
				logger.fatal("Transactions retrieval Failed\n" + e.getMessage());
				System.exit(1);
			}
			conn.close();
		} catch (SQLException ex) {
			logger.fatal("Unable to open database\n" + ex.getMessage());
		}
		logger.info("Transactions retrieved successfully");
		return list;
	}

}
