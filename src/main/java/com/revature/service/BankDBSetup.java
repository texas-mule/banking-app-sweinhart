package com.revature.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;

public class BankDBSetup {
	private static final String url = "jdbc:postgresql://localhost:5432/postgres";
	private static final String username = "sweinhart";
	private static final String password = "82002Sdw!";
	private static Logger logger = Logger.getLogger(BankDBSetup.class);
	static Connection connection;

	public static void createBankAppTables() {
		try (Connection connection = DriverManager.getConnection(url, username, password)) {
			String sql = "CREATE TABLE IF NOT EXISTS account_requests (\n" 
					+ "	id SERIAL PRIMARY KEY,\n"
					+ "	account_type text NOT NULL,\n" 
					+ "	deposit real NOT NULL,\n" 
					+ "	date text NOT NULL,\n"
					+ "	time text NOT NULL,\n" 
					+ "	client1 text NOT NULL,\n" 
					+ "	client2 text\n" 
					+ ");";
			Statement statement = connection.createStatement();
			statement.execute(sql);
			sql = "CREATE TABLE IF NOT EXISTS bank_accounts (\n" 
					+ " id SERIAL PRIMARY KEY,\n"
					+ "	account_type text NOT NULL,\n" 
					+ "	account_number int NOT NULL,\n"
					+ "	client1 text NOT NULL,\n" 
					+ "	client2 text\n" 
					+ ");";
			statement.execute(sql);
			sql = "CREATE TABLE IF NOT EXISTS transactions (\n"
	                + "	id SERIAL PRIMARY KEY,\n"
	                + "	trans_type text NOT NULL,\n"
	                + "	account_number integer NOT NULL,\n"
	                + "	date text NOT NULL,\n"
	                + "	time text NOT NULL,\n"
	                + "	trans_amount real NOT NULL\n"
	                + ");";
			statement.execute(sql);
			sql = "CREATE TABLE IF NOT EXISTS user_accounts (\n"
	                + "	id SERIAL PRIMARY KEY,\n"
	                + "	username text NOT NULL,\n"
	                + "	password text NOT NULL,\n"
	                + "	first_name text NOT NULL,\n"
	                + "	last_name text NOT NULL,\n"
	                + "	address1 text NOT NULL,\n"
	                + "	address2 text,\n"
	                + "	city text NOT NULL,\n"
	                + "	state text NOT NULL,\n"
	                + "	zipcode text NOT NULL,\n"
	                + "	phone text NOT NULL,\n"
	                + "	email text NOT NULL,\n"
	                + "	ss_number text NOT NULL,\n"
	                + "	dl_state text NOT NULL,\n"
	                + "	dl_number text NOT NULL,\n"
	                + "	dl_exp text NOT NULL\n"
	                + ");";
			statement.execute(sql);
			close(connection);
		} catch (SQLException ex) {
			logger.fatal("Unable to open database\n" + ex.getMessage());
		}
	}

	private static void close(Connection connection) {
		try {
			connection.close();
		} catch (SQLException e) {
			logger.fatal("Unable to close database\n" + e.getMessage());
			System.exit(1);
		}
	}
}
