package com.revature.bankingApp;

import java.util.InputMismatchException;
import java.util.Scanner;
import org.apache.log4j.Logger;
import com.revature.bankingApp.domain.Bank;
import com.revature.bankingApp.domain.UserAccount;
import com.revature.bankingApp.service.UserAccountDbSvcImpl;

public class LoginMenu {
	private static Scanner keyboard = new Scanner(System.in);
	private static Logger logger = Logger.getLogger(LoginMenu.class);

	public LoginMenu() {
		// TODO Auto-generated constructor stub
	}

	public void displayMenu() {
		// TODO Auto-generated method stub
		UserAccount user = new UserAccount();
		System.out.println("\n\t\t\t\t" + Bank.getBankName() + "\t\t Routing Number: " + Bank.getRoutingNumber());
		int choice = 0;
		System.out.println("Login Menu");
		System.out.println("1 - Login");
		System.out.println("2 - New Account");
		System.out.println("9 - Exit");
		System.out.print("Choice? ");
		try {
		choice = keyboard.nextInt();
		} catch (InputMismatchException e) {
			logger.info("Handling Input Mismatch Exception");
			System.out.println("Invalid Input");
			displayMenu();
		}
		switch (choice) {
		case 1:
			user = login();
		case 2:
			user = newAccount(1);
		case 9:
			logger.info("User Selected Exit Application!");
			keyboard.close();
			System.exit(0);
		default:
			System.out.println("Invalid Selection");
			displayMenu();
		}

		if (user.getId() == null) {
			logger.info("Logged in User returned null account");
		}
		logger.info("Logged in User with access level " + user.getId());
		keyboard.close();
		// Go to main screen
		switch (user.getAccessLvl()) {
		case 1:
			MainMenuClient clientMenu = new MainMenuClient(user);
			clientMenu.displayMainMenu();
			break;
		case 2:
			MainMenuEmployee emplMenu = new MainMenuEmployee(user);
			emplMenu.displayMainMenu();
			break;
		case 3:
			MainMenuAdmin adminMenu = new MainMenuAdmin(user);
			adminMenu.displayMainMenu();
			break;
		default:
			logger.info("Invalid user access level argument. Exiting Application!");
			System.out.println("Oops! Something went wrong. Exiting Application");
			System.exit(1);
		}
	}

	private UserAccount newAccount(Integer accessLvl) {
		// TODO Auto-generated method stub
		UserAccount account = new UserAccount();
		account = Bank.createAccount(accessLvl);
		return account;
	}

	private UserAccount login() {
		// TODO Auto-generated method stub
		UserAccount account = new UserAccount();
		String username;
		String password;
		System.out.print("Username: ");
		username = keyboard.nextLine();
		System.out.print("Password: ");
		password = keyboard.nextLine();
		UserAccountDbSvcImpl impl = UserAccountDbSvcImpl.getInstance();
		account = impl.get(username);
		if (account.getId() == null || !password.equals(account.getLogin().getPassword())) {
			System.out.println("Invalid username or password");
			displayMenu();
		}
		return account;
	}

}