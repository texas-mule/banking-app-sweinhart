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

public class AccountRequestDbSvcImpl implements AccountRequestInterface {

	private static AccountRequestDbSvcImpl instance = new AccountRequestDbSvcImpl();
	private static Connection conn;
	private Integer integer;

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
		String sql = "CREATE TABLE IF NOT EXISTS AccountRequests (\n" + "	id integer PRIMARY KEY,\n"
				+ "	accountType text NOT NULL,\n" + "	deposit real NOT NULL,\n" + "	date text NOT NULL,\n"
				+ "	time text NOT NULL,\n" + "	client1 integer NOT NULL,\n" + "	client2 integer\n" + ");";
		connect();
		try {
			Statement stmt = conn.createStatement();
			stmt.execute(sql);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
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
			pstmt.setInt(5, request.getUserIds().get(0));
			if (request.getUserIds().size() > 5)
				pstmt.setInt(6, request.getUserIds().get(1));
			else {
				integer = (Integer) null;
				pstmt.setInt(6, integer);
			}
			pstmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			return false;
		}
		close();
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
			System.out.println(e.getMessage());
			return false;
		}
		close();
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
			AccountRequest accountRequest = new AccountRequest();
			while (rs.next()) {
				Request request = accountRequest.new Request();
				request.setId(rs.getInt("id"));
				request.setAccountType(rs.getString("accountType"));
				request.setDeposit(rs.getDouble("deposit"));
				request.setDate(rs.getString("date"));
				request.setTime(rs.getString("time"));
				request.getUserIds().add(rs.getInt("client1"));
				Integer integer = (Integer) rs.getInt("client2");
				if (integer != null)
					request.getUserIds().add(integer);
				list.add(request);
			}
		} catch (SQLException e) {
			System.out.println(e.getStackTrace());
			return null;
		}
		Collections.sort(list);
		return list;
	}

}
