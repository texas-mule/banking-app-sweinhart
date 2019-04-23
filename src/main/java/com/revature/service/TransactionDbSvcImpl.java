package com.revature.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.sql.ResultSet;
import com.revature.domain.Transaction;
import org.apache.log4j.Logger;

public class TransactionDbSvcImpl implements TransactionInterface{
	private static Logger logger = Logger.getLogger(TransactionDbSvcImpl.class);
	private static TransactionDbSvcImpl instance = new TransactionDbSvcImpl();
	private static Connection conn;

	
	public TransactionDbSvcImpl() {
		super();
		// TODO Auto-generated constructor stub
		connect();
        setup();
        close();
	}
	
	public static TransactionDbSvcImpl getInstance() {
		// TODO Auto-generated method stub
		return instance;
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
        String sql = "CREATE TABLE IF NOT EXISTS Transactions (\n"
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
        	logger.fatal("Transactions Table Creation Failed\n" + e.getMessage());
			System.exit(1);
        }
        close();
    }
	
	public boolean add(Transaction transaction) {
		// TODO Auto-generated method stub
		connect();
		String sql = "INSERT INTO Transactions(transType, accountNumber, date, time, transAmount) "
				+ "VALUES(?,?,?,?,?)";
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
		close();
		logger.info("Transaction added successfully");
		return true;
	}

	public boolean delete(Integer transId) {
		// TODO Auto-generated method stub
		connect();
		String sql = "DELETE FROM Transactions WHERE id = ?";
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, transId);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			logger.fatal("Transaction deletion Failed\n" + e.getMessage());
			System.exit(1);
		}
		close();
		logger.info("Transaction deleted successfully");
		return true;
	}

	public List<Transaction> getAll(Integer accountNumber) {
		// TODO Auto-generated method stub
		List<Transaction> list = new ArrayList<Transaction>();
		connect();
		String sql = "SELECT id, transType, accountNumber, date, time, transAmount "
				+ "FROM Transactions WHERE accountNumber = ?";
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, accountNumber);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				Transaction transaction = new Transaction();
				transaction.setId(rs.getInt("id"));
				transaction.setTransactionType(rs.getString("transType"));
				transaction.setAccountNumber(rs.getInt("accountNumber"));
				transaction.setDate(rs.getString("date"), rs.getString("time"));
				transaction.setTransactionAmount(rs.getDouble("transAmount"));
				list.add(transaction);
			}
		} catch (SQLException e) {
			logger.fatal("Transactions retrieval Failed\n" + e.getMessage());
			System.exit(1);
		}
		close();
		logger.info("Transactions retrieved successfully");
		return list;
	}

}
