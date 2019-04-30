package com.revature.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.sql.ResultSet;
import com.revature.domain.AccountRequest;
import com.revature.domain.AccountRequest.Request;
import org.apache.log4j.Logger;

public class AccountRequestDAO implements AccountRequestInterface {
	private static Logger logger = Logger.getLogger(AccountRequestDAO.class);
	private static AccountRequestDAO instance = new AccountRequestDAO();
	private static final String url = "jdbc:postgresql://localhost:5432/postgres";
	private static final String username = "sweinhart";
	private static final String password = "Password";

	public static AccountRequestDAO getConnection() {
		return instance;
	}

	public boolean add(Request request) {
		// TODO Auto-generated method stub
		String sql = "INSERT INTO account_requests (account_type, deposit, date, time, client1, client2) "
				+ "VALUES (?,?,?,?,?,?)";
		try (Connection conn = DriverManager.getConnection(url, username, password)) {
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
			conn.close();
		} catch (SQLException ex) {
			logger.fatal("Unable to open database\n" + ex.getMessage());
		}
		logger.info("AccountRequest added successfully");
		return true;
	}

	public boolean delete(Request request) {
		// TODO Auto-generated method stub
		String sql = "DELETE FROM account_requests WHERE id = ?";
		try (Connection conn = DriverManager.getConnection(url, username, password)) {
			try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
				pstmt.setInt(1, request.getId());
				pstmt.executeUpdate();
			} catch (SQLException e) {
				logger.fatal("AccountRequest deletion Failed\n" + e.getMessage());
				System.exit(1);
			}
			conn.close();
		} catch (SQLException ex) {
			logger.fatal("Unable to open database\n" + ex.getMessage());
		}
		logger.info("AccountRequest deleted successfully");
		return true;
	}

	public List<Request> getAll() {
		// TODO Auto-generated method stub
		List<Request> list = new ArrayList<Request>();
		// TODO Auto-generated method stub
		String sql = "SELECT id, account_type, deposit, date, time, client1, client2 " + "FROM account_requests";
		try (Connection conn = DriverManager.getConnection(url, username, password)) {
			try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
				ResultSet rs = pstmt.executeQuery();
				while (rs.next()) {
					AccountRequest accountRequest = new AccountRequest();
					Request request = accountRequest.new Request();
					request.setId(rs.getInt("id"));
					request.setAccountType(rs.getString("account_type"));
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
			conn.close();
		} catch (SQLException ex) {
			logger.fatal("Unable to open database\n" + ex.getMessage());
		}
		Collections.sort(list);
		logger.info("AccountRequests retrieved successfully");
		return list;
	}

	public boolean update(Request request) {
		// TODO Auto-generated method stub
		String sql = "UPDATE account_requests SET account_type = ? , "
				+ "deposit = ? , date = ? , time = ? , client1 = ?, client2 = ? "
				+ "WHERE id = ?";
		try (Connection conn = DriverManager.getConnection(url, username, password)) {
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
				pstmt.setInt(7, request.getId());
				pstmt.executeUpdate();
			} catch (SQLException e) {
				logger.fatal("AccountRequest addition Failed\n" + e.getMessage());
				System.exit(1);
			}
			conn.close();
		} catch (SQLException ex) {
			logger.fatal("Unable to open database\n" + ex.getMessage());
		}
		logger.info("AccountRequest added successfully");
		return true;
	}

}
