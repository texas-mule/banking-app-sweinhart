package com.revature.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.sql.ResultSet;
import com.revature.domain.AccountRequest;
import com.revature.domain.AccountRequest.Request;
import org.apache.log4j.Logger;

public class AccountRequestDbSvcImpl implements AccountRequestInterface {
	private static Logger logger = Logger.getLogger(AccountRequestDbSvcImpl.class);
	private static AccountRequestDbSvcImpl instance = new AccountRequestDbSvcImpl();
	private static Connection conn;

	public AccountRequestDbSvcImpl() {
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
		String sql = "CREATE TABLE IF NOT EXISTS AccountRequests (\n" 
				+ "	id integer PRIMARY KEY,\n"
				+ "	accountType text NOT NULL,\n" 
				+ "	deposit real NOT NULL,\n" 
				+ "	date text NOT NULL,\n"
				+ "	time text NOT NULL,\n" 
				+ "	client1 text NOT NULL,\n" 
				+ "	client2 text\n" 
				+ ");";
		connect();
		try {
			Statement stmt = conn.createStatement();
			stmt.execute(sql);
		} catch (SQLException e) {
			logger.fatal("AccountRequests Table Creation Failed\n" + e.getMessage());
			System.exit(1);
		}
		close();
	}

	public static AccountRequestDbSvcImpl getInstance() {
		return instance;
	}

	public boolean add(Request request) {
		// TODO Auto-generated method stub
		connect();
		String sql = "INSERT INTO AccountRequests(accountType, deposit, date, time, client1, client2) "
				+ "VALUES(?,?,?,?,?,?)";
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, request.getAccountType());
			pstmt.setDouble(2, request.getDeposit());
			pstmt.setString(3, request.getDate());
			pstmt.setString(4, request.getTime());
			pstmt.setString(5, request.getUserSSNumbers().get(0));
			if (request.getUserSSNumbers().size() > 1)
				pstmt.setString(6, request.getUserSSNumbers().get(1));
			else
				pstmt.setInt(6, 0);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			logger.fatal("AccountRequest addition Failed\n" + e.getMessage());
			System.exit(1);
		}
		close();
		logger.info("AccountRequest added successfully");
		return true;
	}

	public boolean delete(Request request) {
		// TODO Auto-generated method stub
		connect();
		String sql = "DELETE FROM AccountRequests WHERE id = ?";
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, request.getId());
			pstmt.executeUpdate();
		} catch (SQLException e) {
			logger.fatal("AccountRequest deletion Failed\n" + e.getMessage());
			System.exit(1);
		}
		close();
		logger.info("AccountRequest deleted successfully");
		return true;
	}

	public List<Request> getAll() {
		// TODO Auto-generated method stub
		List<Request> list = new ArrayList<Request>();
		// TODO Auto-generated method stub
		connect();
		String sql = "SELECT id, accountType, deposit, date, time, client1, client2 "
				+ "FROM AccountRequests";
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				AccountRequest accountRequest = new AccountRequest();
				Request request = accountRequest.new Request();
				request.setId(rs.getInt("id"));
				request.setAccountType(rs.getString("accountType"));
				request.setDeposit(rs.getDouble("deposit"));
				request.setDate(rs.getString("date"));
				request.setTime(rs.getString("time"));
				request.getUserSSNumbers().add(rs.getString("client1"));
				String ssNum = rs.getString("client2");
				if (!ssNum.equals(""))
					request.getUserSSNumbers().add(ssNum);
				list.add(request);
			}
		} catch (SQLException e) {
			logger.info("AccountRequests retrieval Failed\n" + e.getMessage());
		}
		Collections.sort(list);
		close();
		logger.info("AccountRequests retrieved successfully");
		return list;
	}

}
