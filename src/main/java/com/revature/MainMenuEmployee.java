package com.revature;

import java.util.InputMismatchException;
import java.util.Scanner;
import org.apache.log4j.Logger;
import com.revature.domain.Bank;
import com.revature.domain.UserAccount;

public class MainMenuEmployee extends MainMenuClient {

	private static Scanner keyboard = new Scanner(System.in);
	private static Logger logger = Logger.getLogger(MainMenuEmployee.class);
	private UserAccount user;

	public MainMenuEmployee(UserAccount user) {
		super(user);
		// TODO Auto-generated constructor stub
		this.user = user;
	}
	

	public void displayMainMenu() {
		logger.info("Starting Employee Main Menu");
		System.out.println("\t\t\t\t" + Bank.getBankName() + "\t\t Routing Number: " + Bank.getRoutingNumber());
		int choice = 0;
		System.out.println("Select Accounts");
		System.out.println("1 - Personal Accounts");
		System.out.println("2 - Client Accounts");
		System.out.println("3 - Logout");
		System.out.println("9 - Exit");
		System.out.print("Choice? ");
		try {
			choice = keyboard.nextInt();
		} catch (InputMismatchException e) {
			logger.info("Handling Input Mismatch Exception");
			choice = 0;
		}
		switch (choice) {
		case 1:
			displayClientMenu();
			break;
		case 2:
			displayEmployeeMenu();
			break;
		case 3:
			LoginMenu login = new LoginMenu();
			login.displayMenu();
			break;
		case 9:
			logger.info("User Selected Exit.");
			System.out.println("Exiting Application");
			System.exit(0);
		default:
			System.out.println("Invalid Choice.");
			displayMainMenu();
		}
	}
	
	public void displayClientMenu() {
		logger.info("Starting Client Main Menu");
		System.out.println("\t\t\t\t" + Bank.getBankName() + "\t\t Routing Number: " + Bank.getRoutingNumber());
		int choice = 0;
		boolean hasAccounts;
		if (user.getAccounts().isEmpty())
			hasAccounts = false;
		else
			hasAccounts = true;
		System.out.println("Client Starting Menu");
		System.out.println("1 - Request to Open New Account");
		if (hasAccounts) {
			System.out.println("2 - View Accounts");
		}
		System.out.println("3 - Employee Starting Menu");
		System.out.println("4 - Logout");
		System.out.println("9 - Exit");
		System.out.print("Choice? ");
		try {
			choice = keyboard.nextInt();
		} catch (InputMismatchException e) {
			logger.info("Handling Input Mismatch Exception");
			choice = 0;
		}
		switch (choice) {
		case 1:
			requestAccountMenu();
			break;
		case 2:
			if (hasAccounts) {
				viewAccounts();
			} else {
				System.out.println("Invalid Choice.");
				displayClientMenu();
			}
			break;
		case 3:
			displayMainMenu();
			break;
		case 4:
			LoginMenu login = new LoginMenu();
			login.displayMenu();
			break;
		case 9:
			logger.info("User Selected Exit.");
			System.out.println("Exiting Application");
			System.exit(0);
		default:
			System.out.println("Invalid Choice.");
			displayClientMenu();
		}
	}
	
	private void displayEmployeeMenu() {
		// TODO Auto-generated method stub
		
	}

}
