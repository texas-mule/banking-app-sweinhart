package com.revature.service;

import com.revature.domain.Login;
import com.revature.domain.UserAccount;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.sql.ResultSet;
import org.apache.log4j.Logger;

public final class UserAccountDbSvcImpl implements UserAccountInterface{
	private static Logger logger = Logger.getLogger(UserAccountDbSvcImpl.class);
	private static UserAccountDbSvcImpl instance = new UserAccountDbSvcImpl();
    private static Connection conn;
    
	public UserAccountDbSvcImpl() {
		super();
		// TODO Auto-generated constructor stub
		connect();
        setup();
        close();
	}
	
	public static UserAccountDbSvcImpl getInstance() {
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
        String sql = "CREATE TABLE IF NOT EXISTS UserAccounts (\n"
                + "	id integer PRIMARY KEY,\n"
                + "	username text NOT NULL,\n"
                + "	password text NOT NULL,\n"
                + "	firstName text NOT NULL,\n"
                + "	lastName text NOT NULL,\n"
                + "	address1 text NOT NULL,\n"
                + "	address2 text,\n"
                + "	city text NOT NULL,\n"
                + "	state text NOT NULL,\n"
                + "	zipCode text NOT NULL,\n"
                + "	phone text NOT NULL,\n"
                + "	email text NOT NULL,\n"
                + "	ssNumber text NOT NULL,\n"
                + "	dlState text NOT NULL,\n"
                + "	dlNumber text NOT NULL,\n"
                + "	dlExp text NOT NULL\n"
                + ");";
        connect();
        try {
            Statement stmt = conn.createStatement();
            stmt.execute(sql);
        } catch (SQLException e) {
        	logger.fatal("UserAccounts Table Creation Failed\n" + e.getMessage());
			System.exit(1);
        }
        close();
    }


	public boolean add(UserAccount account) {
		// TODO Auto-generated method stub
		connect();
        String sql = "INSERT INTO UserAccounts(username, password, firstName, lastName, address1, "
                + "address2, city, state, zipCode, phone, email, ssNumber, dlState, dlNumber, dlExp) "
                + "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, account.getLogin().getUsername());
            pstmt.setString(2, account.getLogin().getPassword());
            pstmt.setString(3, account.getFirstName());
            pstmt.setString(4, account.getLastName());
            pstmt.setString(5, account.getAddress1());
            pstmt.setString(6, account.getAddress2());
            pstmt.setString(7, account.getCity());
            pstmt.setString(8, account.getState());
            pstmt.setString(9, account.getZipcode());
            pstmt.setString(10, account.getPhone());
            pstmt.setString(11, account.getEmail());
            pstmt.setString(12, account.getSocialSecurity());
            pstmt.setString(13, account.getDlState());
            pstmt.setString(14, account.getDlNumber());
            pstmt.setString(15, account.getDlExp());
            pstmt.executeUpdate();
        } catch (SQLException e) {
        	logger.fatal("UserAccount addition Failed\n" + e.getMessage());
        	System.exit(1);
        }
        close();
        logger.info("UserAccount added successfully");
		return true;
	}

	public boolean delete(UserAccount account) {
		// TODO Auto-generated method stub
		connect();
        String sql = "DELETE FROM UserAccounts WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, account.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
        	logger.fatal("UserAccount deletion Failed\n" + e.getMessage());
        	System.exit(1);
        }
        close();
        logger.info("UserAccount deleted successfully");
		return true;
	}

	public boolean update(UserAccount account) {
		// TODO Auto-generated method stub
		connect();
        String sql = "UPDATE UserAccounts SET username = ? , password = ? , "
                + "firstName = ? , lastName = ? , address1 = ? , address2 = ? , "
                + "city = ? , state = ? , zipCode = ? , phone = ? , email = ? , "
                + "ssNumber = ? , dlState = ? , dlNumber = ? , dlExp = ? "
                + "WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, account.getLogin().getUsername());
            pstmt.setString(2, account.getLogin().getPassword());
            pstmt.setString(3, account.getFirstName());
            pstmt.setString(4, account.getLastName());
            pstmt.setString(5, account.getAddress1());
            pstmt.setString(6, account.getAddress2());
            pstmt.setString(7, account.getCity());
            pstmt.setString(8, account.getState());
            pstmt.setString(9, account.getZipcode());
            pstmt.setString(10, account.getPhone());
            pstmt.setString(11, account.getEmail());
            pstmt.setString(12, account.getSocialSecurity());
            pstmt.setString(13, account.getDlState());
            pstmt.setString(14, account.getDlNumber());
            pstmt.setString(15, account.getDlExp());
            pstmt.setInt(16, account.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
        	logger.fatal("UserAccount update Failed\n" + e.getMessage());
        	System.exit(1);
        }
        close();
        logger.info("UserAccount updated successfully");
		return true;
	}


	public UserAccount getById(Integer id) {
		// TODO Auto-generated method stub
		UserAccount account = new UserAccount();
		Login login = new Login();
        connect();
        String sql = "SELECT id, username, password, firstName, lastName, address1, "
                + "address2, city, state, zipCode, phone, email, "
                + "ssNumber, dlState, dlNumber, dlExp "
                + "FROM UserAccounts WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            account.setId(rs.getInt("id"));
            login.setUsername(rs.getString("username"));
            login.setPassword(rs.getString("password"));
            account.setLogin(login);
            account.setFirstName(rs.getString("firstName"));
            account.setLastName(rs.getString("lastName"));
            account.setAddress1(rs.getString("address1"));
            account.setAddress2(rs.getString("address2"));
            account.setCity(rs.getString("city"));
            account.setState(rs.getString("state"));
            account.setZipCode(rs.getString("zipCode"));
            account.setPhone(rs.getString("phone"));
            account.setEmail(rs.getString("email"));
            account.setSocialSecurity(rs.getString("ssNumber"));
            account.setDlState(rs.getString("dlState"));
            account.setDlNumber(rs.getString("dlNumber"));
            account.setDlExp(rs.getString("dlExp"));
        } catch (SQLException e) {
        	logger.info("UserAccount (id) retrieval Failed\n" + e.getMessage());
        }
        close();
        logger.info("UserAccount retrieved successfully");
        return account;
	}


	public UserAccount getByUsername(String username) {
		// TODO Auto-generated method stub
		UserAccount account = new UserAccount();
		Login login = new Login();
        connect();
        String sql = "SELECT id, username, password, firstName, lastName, address1, "
                + "address2, city, state, zipCode, phone, email, "
                + "ssNumber, dlState, dlNumber, dlExp "
                + "FROM UserAccounts WHERE username = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            account.setId(rs.getInt("id"));
            login.setUsername(rs.getString("username"));
            login.setPassword(rs.getString("password"));
            account.setLogin(login);
            account.setFirstName(rs.getString("firstName"));
            account.setLastName(rs.getString("lastName"));
            account.setAddress1(rs.getString("address1"));
            account.setAddress2(rs.getString("address2"));
            account.setCity(rs.getString("city"));
            account.setState(rs.getString("state"));
            account.setZipCode(rs.getString("zipCode"));
            account.setPhone(rs.getString("phone"));
            account.setEmail(rs.getString("email"));
            account.setSocialSecurity(rs.getString("ssNumber"));
            account.setDlState(rs.getString("dlState"));
            account.setDlNumber(rs.getString("dlNumber"));
            account.setDlExp(rs.getString("dlExp"));
        } catch (SQLException e) {
        	logger.info("UserAccount (username) retrieval Failed\n" + e.getMessage());
        }
        close();
        logger.info("UserAccount retrieved successfully");
        return account;
	}


	public UserAccount getBySs(String ssNum) {
		// TODO Auto-generated method stub
		UserAccount account = new UserAccount();
		Login login = new Login();
        connect();
        String sql = "SELECT id, username, password, firstName, lastName, address1, "
                + "address2, city, state, zipCode, phone, email, "
                + "ssNumber, dlState, dlNumber, dlExp "
                + "FROM UserAccounts WHERE ssNumber = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, ssNum);
            ResultSet rs = pstmt.executeQuery();
            account.setId(rs.getInt("id"));
            login.setUsername(rs.getString("username"));
            login.setPassword(rs.getString("password"));
            account.setLogin(login);
            account.setFirstName(rs.getString("firstName"));
            account.setLastName(rs.getString("lastName"));
            account.setAddress1(rs.getString("address1"));
            account.setAddress2(rs.getString("address2"));
            account.setCity(rs.getString("city"));
            account.setState(rs.getString("state"));
            account.setZipCode(rs.getString("zipCode"));
            account.setPhone(rs.getString("phone"));
            account.setEmail(rs.getString("email"));
            account.setSocialSecurity(rs.getString("ssNumber"));
            account.setDlState(rs.getString("dlState"));
            account.setDlNumber(rs.getString("dlNumber"));
            account.setDlExp(rs.getString("dlExp"));
        } catch (SQLException e) {
        	logger.info("UserAccount (ssNumber) retrieval Failed\n" + e.getMessage());
        }
        close();
        logger.info("UserAccount retrieved successfully");
        return account;
	}

	public List<UserAccount> getAll() {
		// TODO Auto-generated method stub
		List<UserAccount> list = new ArrayList<UserAccount>();
        connect();
        String sql = "SELECT id, username, password, firstName, lastName, address1, "
                + "address2, city, state, zipCode, phone, email, "
                + "ssNumber, dlState, dlNumber, dlExp "
                + "FROM UserAccounts";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                UserAccount account = new UserAccount();
                Login login = new Login();
                account.setId(rs.getInt("id"));
                login.setUsername(rs.getString("username"));
                login.setPassword(rs.getString("password"));
                account.setLogin(login);
                account.setFirstName(rs.getString("firstName"));
                account.setLastName(rs.getString("lastName"));
                account.setAddress1(rs.getString("address1"));
                account.setAddress2(rs.getString("address2"));
                account.setCity(rs.getString("city"));
                account.setState(rs.getString("state"));
                account.setZipCode(rs.getString("zipCode"));
                account.setPhone(rs.getString("phone"));
                account.setEmail(rs.getString("email"));
                account.setSocialSecurity(rs.getString("ssNumber"));
                account.setDlState(rs.getString("dlState"));
                account.setDlNumber(rs.getString("dlNumber"));
                account.setDlExp(rs.getString("dlExp"));
                list.add(account);
            }
        } catch (SQLException e) {
        	logger.info("UserAccounts retrieval Failed\n" + e.getMessage());
        }
        close();
        logger.info("UserAccounts retrieved successfully");
        Collections.sort(list);
        return list;
	}

	public UserAccount getByDl(String dlState, String dlNumber) {
		// TODO Auto-generated method stub
		UserAccount account = new UserAccount();
		Login login = new Login();
        connect();
        String sql = "SELECT id, username, password, firstName, lastName, address1, "
                + "address2, city, state, zipCode, phone, email, "
                + "ssNumber, dlState, dlNumber, dlExp "
                + "FROM UserAccounts WHERE dlState = ? AND dlNumber = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, dlState);
            pstmt.setString(2, dlNumber);
            ResultSet rs = pstmt.executeQuery();
            account.setId(rs.getInt("id"));
            login.setUsername(rs.getString("username"));
            login.setPassword(rs.getString("password"));
            account.setLogin(login);
            account.setFirstName(rs.getString("firstName"));
            account.setLastName(rs.getString("lastName"));
            account.setAddress1(rs.getString("address1"));
            account.setAddress2(rs.getString("address2"));
            account.setCity(rs.getString("city"));
            account.setState(rs.getString("state"));
            account.setZipCode(rs.getString("zipCode"));
            account.setPhone(rs.getString("phone"));
            account.setEmail(rs.getString("email"));
            account.setSocialSecurity(rs.getString("ssNumber"));
            account.setDlState(rs.getString("dlState"));
            account.setDlNumber(rs.getString("dlNumber"));
            account.setDlExp(rs.getString("dlExp"));
        } catch (SQLException e) {
        	logger.info("UserAccount (ssNumber) retrieval Failed\n" + e.getMessage());
        }
        close();
        logger.info("UserAccount retrieved successfully");
        return account;
	}
}
