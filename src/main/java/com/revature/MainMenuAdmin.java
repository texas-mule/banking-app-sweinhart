package com.revature.bankingApp;

import java.util.Scanner;

import org.apache.log4j.Logger;

import com.revature.bankingApp.domain.Bank;
import com.revature.bankingApp.domain.UserAccount;

public class MainMenuAdmin extends MainMenuEmployee {

	private static Scanner keyboard = new Scanner(System.in);
	private static Logger logger = Logger.getLogger(MainMenuAdmin.class);
	private UserAccount user;
	
	public MainMenuAdmin(UserAccount user) {
		super(user);
		// TODO Auto-generated constructor stub
		this.user = user;
	}
	
	public void displayMainMenu() {
		displayAdminMenu();
	}

	public void displayAdminMenu() {
		// TODO Auto-generated method stub
		
	}
}
