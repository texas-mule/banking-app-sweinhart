package com.revature.service;

import com.revature.domain.Login;
import com.revature.domain.UserAccount;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.sql.ResultSet;
import org.apache.log4j.Logger;

public final class UserAccountDAO implements UserAccountInterface{
	private static Logger logger = Logger.getLogger(UserAccountDAO.class);
	private static UserAccountDAO instance = new UserAccountDAO();
	private static final String url = "jdbc:postgresql://localhost:5432/postgres";
	private static final String username = "sweinhart";
	private static final String password = "Password";

	public static UserAccountDAO getConnection() {
		return instance;
	}

	public boolean add(UserAccount account) {
		// TODO Auto-generated method stub
		String sql = "INSERT INTO user_accounts (username, password, first_name, last_name, address1, "
                + "address2, city, state, zipcode, phone, email, ss_number, dl_state, dl_number, dl_exp) "
                + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		try (Connection connection = DriverManager.getConnection(url, username, password)) {
			try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
	            pstmt.setString(1, account.getLogin().getUsername());
	            pstmt.setString(2, account.getLogin().getPassword());
	            pstmt.setString(3, account.getFirstName());
	            pstmt.setString(4, account.getLastName());
	            pstmt.setString(5, account.getAddress1());
	            pstmt.setString(6, account.getAddress2());
	            pstmt.setString(7, account.getCity());
	            pstmt.setString(8, account.getState());
	            pstmt.setString(9, account.getZipCode());
	            pstmt.setString(10, account.getPhone());
	            pstmt.setString(11, account.getEmail());
	            pstmt.setString(12, account.getSocialSecurity());
	            pstmt.setString(13, account.getDlState());
	            pstmt.setString(14, account.getDlNumber());
	            pstmt.setString(15, account.getDlExp());
	            pstmt.executeUpdate();
	            connection.close();
	        } catch (SQLException e) {
	        	logger.fatal("UserAccount addition Failed\n" + e.getMessage());
	        	System.exit(1);
	        }
		} catch (SQLException ex) {
			logger.fatal("Unable to open database\n" + ex.getMessage());
		}
        logger.info("UserAccount added successfully");
		return true;
	}

	public boolean delete(UserAccount account) {
		// TODO Auto-generated method stub
		String sql = "DELETE FROM user_accounts WHERE id = ?";
		try (Connection conn = DriverManager.getConnection(url, username, password)) {
			try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
	            pstmt.setInt(1, account.getId());
	            pstmt.executeUpdate();
	            conn.close();
	        } catch (SQLException e) {
	        	logger.fatal("UserAccount deletion Failed\n" + e.getMessage());
	        	System.exit(1);
	        }
		} catch (SQLException ex) {
			logger.fatal("Unable to open database\n" + ex.getMessage());
		}
        logger.info("UserAccount deleted successfully");
		return true;
	}

	public boolean update(UserAccount account) {
		// TODO Auto-generated method stub
        String sql = "UPDATE user_accounts SET username = ? , password = ? , "
                + "first_name = ? , last_name = ? , address1 = ? , address2 = ? , "
                + "city = ? , state = ? , zipcode = ? , phone = ? , email = ? , "
                + "ss_number = ? , dl_state = ? , dl_number = ? , dl_exp = ? "
                + "WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(url, username, password)) {
        	try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, account.getLogin().getUsername());
                pstmt.setString(2, account.getLogin().getPassword());
                pstmt.setString(3, account.getFirstName());
                pstmt.setString(4, account.getLastName());
                pstmt.setString(5, account.getAddress1());
                pstmt.setString(6, account.getAddress2());
                pstmt.setString(7, account.getCity());
                pstmt.setString(8, account.getState());
                pstmt.setString(9, account.getZipCode());
                pstmt.setString(10, account.getPhone());
                pstmt.setString(11, account.getEmail());
                pstmt.setString(12, account.getSocialSecurity());
                pstmt.setString(13, account.getDlState());
                pstmt.setString(14, account.getDlNumber());
                pstmt.setString(15, account.getDlExp());
                pstmt.setInt(16, account.getId());
                pstmt.executeUpdate();
                conn.close();
            } catch (SQLException e) {
            	logger.fatal("UserAccount update Failed\n" + e.getMessage());
            	System.exit(1);
            }
		} catch (SQLException ex) {
			logger.fatal("Unable to open database\n" + ex.getMessage());
		}        
        logger.info("UserAccount updated successfully");
		return true;
	}


	public UserAccount getById(Integer id) {
		// TODO Auto-generated method stub
		UserAccount account = new UserAccount();
		Login login = new Login();
        String sql = "SELECT id, username, password, first_name, last_name, address1, "
                + "address2, city, state, zipcode, phone, email, "
                + "ss_number, dl_state, dl_number, dl_exp "
                + "FROM user_accounts WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(url, username, password)) {
        	try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, id);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                	account.setId(rs.getInt("id"));
                    login.setUsername(rs.getString("username"));
                    login.setPassword(rs.getString("password"));
                    account.setLogin(login);
                    account.setFirstName(rs.getString("first_name"));
                    account.setLastName(rs.getString("last_name"));
                    account.setAddress1(rs.getString("address1"));
                    account.setAddress2(rs.getString("address2"));
                    account.setCity(rs.getString("city"));
                    account.setState(rs.getString("state"));
                    account.setZipCode(rs.getString("zipcode"));
                    account.setPhone(rs.getString("phone"));
                    account.setEmail(rs.getString("email"));
                    account.setSocialSecurity(rs.getString("ss_number"));
                    account.setDlState(rs.getString("dl_state"));
                    account.setDlNumber(rs.getString("dl_number"));
                    account.setDlExp(rs.getString("dl_exp"));
                }                
                conn.close();
            } catch (SQLException e) {
            	logger.info("UserAccount (id) retrieval Failed\n" + e.getMessage());
            }
		} catch (SQLException ex) {
			logger.fatal("Unable to open database\n" + ex.getMessage());
		}
        logger.info("UserAccount retrieved successfully");
        return account;
	}


	public UserAccount getByUsername(String username) {
		// TODO Auto-generated method stub
		UserAccount account = new UserAccount();
		Login login = new Login();
        String sql = "SELECT id, username, password, first_name, last_name, address1, "
                + "address2, city, state, zipcode, phone, email, "
                + "ss_number, dl_state, dl_number, dl_exp "
                + "FROM user_accounts WHERE username = ?";
        try (Connection conn = DriverManager.getConnection(url, UserAccountDAO.username, password)) {
        	try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, username);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                	account.setId(rs.getInt("id"));
                    login.setUsername(rs.getString("username"));
                    login.setPassword(rs.getString("password"));
                    account.setLogin(login);
                    account.setFirstName(rs.getString("first_name"));
                    account.setLastName(rs.getString("last_name"));
                    account.setAddress1(rs.getString("address1"));
                    account.setAddress2(rs.getString("address2"));
                    account.setCity(rs.getString("city"));
                    account.setState(rs.getString("state"));
                    account.setZipCode(rs.getString("zipcode"));
                    account.setPhone(rs.getString("phone"));
                    account.setEmail(rs.getString("email"));
                    account.setSocialSecurity(rs.getString("ss_number"));
                    account.setDlState(rs.getString("dl_state"));
                    account.setDlNumber(rs.getString("dl_number"));
                    account.setDlExp(rs.getString("dl_exp"));
                }                
                conn.close();
            } catch (SQLException e) {
            	logger.info("UserAccount (username) retrieval Failed\n" + e.getMessage());
            }
		} catch (SQLException ex) {
			logger.fatal("Unable to open database\n" + ex.getMessage());
		}
        logger.info("UserAccount retrieved successfully");
        return account;
	}


	public UserAccount getBySs(String ssNum) {
		// TODO Auto-generated method stub
		UserAccount account = new UserAccount();
		Login login = new Login();
        String sql = "SELECT id, username, password, first_name, last_name, address1, "
                + "address2, city, state, zipcode, phone, email, "
                + "ss_number, dl_state, dl_number, dl_exp "
                + "FROM user_accounts WHERE ss_number = ?";
        try (Connection conn = DriverManager.getConnection(url, username, password)) {
        	try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, ssNum);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                	account.setId(rs.getInt("id"));
                    login.setUsername(rs.getString("username"));
                    login.setPassword(rs.getString("password"));
                    account.setLogin(login);
                    account.setFirstName(rs.getString("first_name"));
                    account.setLastName(rs.getString("last_name"));
                    account.setAddress1(rs.getString("address1"));
                    account.setAddress2(rs.getString("address2"));
                    account.setCity(rs.getString("city"));
                    account.setState(rs.getString("state"));
                    account.setZipCode(rs.getString("zipcode"));
                    account.setPhone(rs.getString("phone"));
                    account.setEmail(rs.getString("email"));
                    account.setSocialSecurity(rs.getString("ss_number"));
                    account.setDlState(rs.getString("dl_state"));
                    account.setDlNumber(rs.getString("dl_number"));
                    account.setDlExp(rs.getString("dl_exp"));
                }                
                conn.close();
            } catch (SQLException e) {
            	logger.info("UserAccount (ssNumber) retrieval Failed\n" + e.getMessage());
            }
		} catch (SQLException ex) {
			logger.fatal("Unable to open database\n" + ex.getMessage());
		}
        logger.info("UserAccount retrieved successfully");
        return account;
	}

	public List<UserAccount> getAll() {
		// TODO Auto-generated method stub
		List<UserAccount> list = new ArrayList<UserAccount>();
        String sql = "SELECT id, username, password, first_name, last_name, address1, "
                + "address2, city, state, zipcode, phone, email, "
                + "ss_number, dl_state, dl_number, dl_exp "
                + "FROM user_accounts";
        try (Connection conn = DriverManager.getConnection(url, username, password)) {
        	try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                ResultSet rs = pstmt.executeQuery();
                while (rs.next()) {
                    UserAccount account = new UserAccount();
                    Login login = new Login();
                    account.setId(rs.getInt("id"));
                    login.setUsername(rs.getString("username"));
                    login.setPassword(rs.getString("password"));
                    account.setLogin(login);
                    account.setFirstName(rs.getString("first_name"));
                    account.setLastName(rs.getString("last_name"));
                    account.setAddress1(rs.getString("address1"));
                    account.setAddress2(rs.getString("address2"));
                    account.setCity(rs.getString("city"));
                    account.setState(rs.getString("state"));
                    account.setZipCode(rs.getString("zipcode"));
                    account.setPhone(rs.getString("phone"));
                    account.setEmail(rs.getString("email"));
                    account.setSocialSecurity(rs.getString("ss_number"));
                    account.setDlState(rs.getString("dl_state"));
                    account.setDlNumber(rs.getString("dl_number"));
                    account.setDlExp(rs.getString("dl_exp"));
                    list.add(account);
                }
                conn.close();
            } catch (SQLException e) {
            	logger.info("UserAccounts retrieval Failed\n" + e.getMessage());
            }
		} catch (SQLException ex) {
			logger.fatal("Unable to open database\n" + ex.getMessage());
		}
        logger.info("UserAccounts retrieved successfully");
        Collections.sort(list);
        return list;
	}

	public UserAccount getByDl(String dlState, String dlNumber) {
		// TODO Auto-generated method stub
		UserAccount account = new UserAccount();
		Login login = new Login();
        String sql = "SELECT id, username, password, first_name, last_name, address1, "
                + "address2, city, state, zipcode, phone, email, "
                + "ss_number, dl_state, dl_number, dl_exp "
                + "FROM user_accounts WHERE dl_state = ? AND dl_number = ?";
        try (Connection conn = DriverManager.getConnection(url, username, password)) {
        	try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, dlState);
                pstmt.setString(2, dlNumber);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                	account.setId(rs.getInt("id"));
                    login.setUsername(rs.getString("username"));
                    login.setPassword(rs.getString("password"));
                    account.setLogin(login);
                    account.setFirstName(rs.getString("first_name"));
                    account.setLastName(rs.getString("last_name"));
                    account.setAddress1(rs.getString("address1"));
                    account.setAddress2(rs.getString("address2"));
                    account.setCity(rs.getString("city"));
                    account.setState(rs.getString("state"));
                    account.setZipCode(rs.getString("zipcode"));
                    account.setPhone(rs.getString("phone"));
                    account.setEmail(rs.getString("email"));
                    account.setSocialSecurity(rs.getString("ss_number"));
                    account.setDlState(rs.getString("dl_state"));
                    account.setDlNumber(rs.getString("dl_number"));
                    account.setDlExp(rs.getString("dl_exp"));
                }          
                conn.close();
            } catch (SQLException e) {
            	logger.info("UserAccount (ssNumber) retrieval Failed\n" + e.getMessage());
            }
		} catch (SQLException ex) {
			logger.fatal("Unable to open database\n" + ex.getMessage());
		}
        logger.info("UserAccount retrieved successfully");
        return account;
	}
}
