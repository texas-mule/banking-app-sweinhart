package com.revature;

import java.util.InputMismatchException;
import java.util.Scanner;
import org.apache.log4j.Logger;
import com.revature.domain.Bank;
import com.revature.domain.BankAccount;
import com.revature.domain.BankTransactions;
import com.revature.domain.UserAccount;
import com.revature.service.UserAccountDbSvcImpl;

public class MainMenuAdmin extends MainMenuEmployee {

	private static Scanner keyboard;
	private static Logger logger = Logger.getLogger(MainMenuAdmin.class);
	private UserAccount user;

	public MainMenuAdmin(UserAccount user) {
		super(user);
		// TODO Auto-generated constructor stub
		this.user = user;		
	}

	public void displayMainMenu() {
		logger.info("Starting Employee Inital Menu");
		keyboard = new Scanner(System.in);
		System.out.println("\n\t\t\t" + Bank.getBankName() + "\t Routing Number: " + Bank.getRoutingNumber());
		int choice = 0;
		System.out.println("\nSelect Personal/Client Accounts");
		System.out.println("1 - Personal Accounts");
		System.out.println("2 - Client Accounts");
		System.out.println("3 - Add Bank Employee");
		System.out.println("9 - Logout");
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
			addEmployeeAccount();
			break;
		case 9:
			LoginMenu login = new LoginMenu();
			login.displayMenu();
			break;
		default:
			System.out.println("Invalid Choice.");
			displayMainMenu();
		}
	}
	public void displayEmployeeMenu() {
		// TODO Auto-generated method stub
		logger.info("Starting Admin Menu");
		keyboard = new Scanner(System.in);
		int choice = 0;
		System.out.println("\nAdmin Menu");
		System.out.println("1 - View Open Account Requests");
		System.out.println("2 - View Customer Information");
		System.out.println("3 - View Customer Accounts");		
		System.out.println("9 - Return to Starting Menu");
		System.out.print("Choice? ");
		try {
			choice = keyboard.nextInt();
		} catch (InputMismatchException e) {
			logger.info("Handling Input Mismatch Exception");
			choice = 0;
		}
		UserAccount user;
		BankAccount account;
		switch (choice) {
		case 1:
			displayOpenRequests();
			break;
		case 2:
			user = displayClientSelectionMenu(false);
			displayUserAccountDetails(user);
			break;
		case 3:
			user = displayClientSelectionMenu(true);
			account = selectBankAccount(user);
			if (account != null) {
				displayClientBankAccountDetails(account);
				displayAdminOperations(user, account);
			}
			break;		
		case 9:
			displayMainMenu();
			break;
		default:
			System.out.println("Invalid Choice.");
			displayEmployeeMenu();
		}
		displayEmployeeMenu();
	}

	private void displayAdminOperations(UserAccount user, BankAccount account) {
		// TODO Auto-generated method stub
		logger.info("Starting Admin Operations");
		keyboard = new Scanner(System.in);
		System.out.println("\nAdmin Operations");
		System.out.println("1 - Deposit Into Client Account");
		System.out.println("2 - Withdraw From Client Account");
		System.out.println("3 - Account Funds Transfer");
		System.out.println("4 - Close Customer Account");
		System.out.println("9 - Return to Employee Menu");
		System.out.print("Choice? ");
		int choice;
		try {
			choice = keyboard.nextInt();
		} catch (InputMismatchException e) {
			logger.info("Handling Input Mismatch Exception");
			choice = 0;
		}
		switch (choice) {
		case 1:
			BankTransactions.makeDeposit(user, account);
			break;
		case 2:
			BankTransactions.makeWithdrawl(user, account);
			break;
		case 3:
			System.out.println("\nSelect User to Transfer Funds To");
			UserAccount toUser = displayClientSelectionMenu(true);
			BankAccount fromAccount = selectBankAccount(toUser);
			BankTransactions.transferFunds(this.user, toUser, fromAccount);
			break;
		case 4:
			BankTransactions.closeAccount(user, account);
			break;
		case 9:
			displayEmployeeMenu();
			break;
		default:
			System.out.println("Invalid Choice.");
			displayEmployeeMenu();
		}
		displayEmployeeMenu();
	}

	private void addEmployeeAccount() {
		// TODO Auto-generated method stub
		logger.info("Starting new Employee Menu");
		keyboard = new Scanner(System.in);
		System.out.println("\nAdd New Employee");
		System.out.println("Has Employee already created a User Account?");
		System.out.println("1 - Yes");
		System.out.println("2 - No");
		System.out.print("Choice? ");
		int choice;
		try {
			choice = keyboard.nextInt();
		} catch (InputMismatchException e) {
			logger.info("Handling Input Mismatch Exception");
			choice = 0;
		}
		keyboard = new Scanner(System.in);
		switch (choice) {
		case 1:
			String ssNumber;
			do {
				System.out.println("Enter Employee Social Security Number: ");
				ssNumber = keyboard.nextLine();
				if (!ssNumber.contains("-"))
					ssNumber = ssNumber.substring(0, 3) + "-" + ssNumber.substring(3, 5) + "-" + ssNumber.substring(5);
			} while (!Bank.validateSocialSecurity(ssNumber));
			UserAccount account = new UserAccount();
			UserAccountDbSvcImpl impl = UserAccountDbSvcImpl.getInstance();
			account = impl.getBySs(ssNumber);
			if (account.getId() == null) {
				System.out.println("Employee is not registered in the system.");
				displayMainMenu();
			}
			if (account.getAccessLvl() > 1) {
				System.out.println("Employee is already in the System");
				displayEmployeeMenu();
			}
			account.setAccessLvl(2);
			String password;
			do {
				System.out.print("Create Temporary Password for Employee: ");
				password = keyboard.nextLine();
			} while (!Bank.validatePassword(password));
			System.out.println("Temporary Password Set Successfully");
			account.getLogin().setPassword(password, 2);
			impl.update(account);
			break;
		case 2:
			account = Bank.createUserAccount(2);
			break;
		default:
			System.out.println("Invalid Selection");
			displayEmployeeMenu();
		}
		displayMainMenu();
	}

	protected void displayUserAccountDetails(UserAccount account) {
		// TODO Auto-generated method stub
		logger.info("Client Details");
		keyboard = new Scanner(System.in);
		System.out.println("\nClient Details");
		System.out.print(account.getLastName() + ", " + account.getFirstName());
		System.out.print("\t\t\t");
		System.out.println();
		System.out.print(account.getAddress1() + " " + account.getAddress2());
		System.out.print("\t\t\t");
		System.out.println();
		System.out.print(account.getCity() + ", " + account.getState() + " " + account.getZipcode());
		System.out.print("\t\t\t");
		System.out.println();
		System.out.print(account.getPhone());
		System.out.print("\t\t\t");
		System.out.println();
		System.out.print(account.getEmail());
		System.out.print("\t\t\t");
		System.out.println();
		System.out.print(account.getSocialSecurity());
		System.out.print("\t\t\t");
		System.out.println();
		System.out.print(account.getDlState());
		System.out.print(" License: " + account.getDlNumber());
		System.out.print("\t\t\t");
		System.out.println();
		System.out.print("Expires: ");
		System.out.print(account.getDlExp());
		System.out.print("\t\t\t");

		displayAdminUserOperations(account);
	}

	private void displayAdminUserOperations(UserAccount user) {
		// TODO Auto-generated method stub
		logger.info("Admin Client Operations");
		keyboard = new Scanner(System.in);
		System.out.println("\n\nAdmin Client Operations");
		System.out.println("1 - Edit User Account");
		System.out.println("2 - Delete User Account");
		System.out.println("3 - Back to Employee Menu");
		System.out.print("Choice? ");
		int choice;
		try {
			choice = keyboard.nextInt();
		} catch (InputMismatchException e) {
			logger.info("Handling Input Mismatch Exception");
			choice = 0;
		}
		switch (choice) {
		case 1:
			editUserAccount(user);
			break;
		case 2:
			deleteUserAccount(user);
			break;
		case 3:
			displayEmployeeMenu();
			break;
		default:
			displayAdminUserOperations(user);
			break;
		}
	}

	private void deleteUserAccount(UserAccount user) {
		// TODO Auto-generated method stub
		logger.info("Delete user Account");
		keyboard = new Scanner(System.in);
		System.out.println("\nWARNING: Deleting User Accounts will remove all associated Bank Accounts");
		System.out.println("Confirm Delete of User: " + user.getLastName() + ", " + user.getFirstName());
		System.out.println("1 - Yes");
		System.out.println("2 - No");
		System.out.println("Choice? ");
		int choice;
		try {
			choice = keyboard.nextInt();
		} catch (InputMismatchException e) {
			logger.info("Handling Input Mismatch Exception");
			choice = 0;
		}
		switch (choice) {
		case 1: {
			for (BankAccount account : user.getAccounts()) {
				BankTransactions.closeAccount(user, account);
			}
			UserAccountDbSvcImpl impl = UserAccountDbSvcImpl.getInstance();
			impl.delete(user);
		}
			if (user.equals(this.user)) {
				LoginMenu login = new LoginMenu();
				login.displayMenu();
			}
			break;
		case 2:
			displayEmployeeMenu();
			break;
		default:
			deleteUserAccount(user);
			break;
		}

	}

	private void editUserAccount(UserAccount user) {
		// TODO Auto-generated method stub
		UserAccountDbSvcImpl impl = UserAccountDbSvcImpl.getInstance();
		logger.info("Update User Account");
		keyboard = new Scanner(System.in);
		System.out.println("\nEdit User Account");
		System.out.println("1 - Change Client Username");
		System.out.println("2 - Change Client Password");
		System.out.println("3 - Change Client Name");
		System.out.println("4 - Change Client Address");
		System.out.println("5 - Change Client Phone Number");
		System.out.println("6 - Change Client Email");
		System.out.println("7 - Change Client Driver License");
		System.out.println("9 - Return to Employee Menu");
		System.out.println("Choice? ");
		int choice;
		String input = "";
		boolean change = false;
		try {
			choice = keyboard.nextInt();
		} catch (InputMismatchException e) {
			logger.info("Handling Input Mismatch Exception");
			choice = 0;
		}
		keyboard = new Scanner(System.in);
		switch (choice) {
		case 1:
			System.out.print("Enter New Client Username: ");
			input = keyboard.nextLine();
			if (Bank.validateUsername(input)) {
				change = true;
				user.getLogin().setUsername(input);
			}
			break;
		case 2:
			System.out.print("Enter New Client Password: ");
			input = keyboard.nextLine();
			if (Bank.validatePassword(input)) {
				change = true;
				user.getLogin().setUsername(input);
			}
			break;
		case 3:
			System.out.print("Enter New First Name: ");
			input = keyboard.nextLine();
			String[] firstName = { input.substring(0, 1), input.substring(1) };
			input = firstName[0].toUpperCase() + firstName[1].toLowerCase();
			if (Bank.validateName(input)) {
				change = true;
				user.setFirstName(input);
			}
			System.out.print("Enter New Last Name: ");
			input = keyboard.nextLine();
			String[] lastName = { input.substring(0, 1), input.substring(1) };
			input = lastName[0].toUpperCase() + lastName[1].toLowerCase();
			if (Bank.validateName(input)) {
				change = true;
				user.setLastName(input);
			}
			break;
		case 4:
			System.out.print("Enter New Client Address1: ");
			input = keyboard.nextLine();
			String[] address = input.split(" ");
			input = address[0] + " ";
			for (int i = 1; i < address.length; i++) {
				address[i] = address[i].substring(0, 1).toUpperCase() + address[i].substring(1).toLowerCase();
				input += address[i] + " ";
			}
			input = input.trim();
			if (Bank.validateAddress(input)) {
				change = true;
				user.setAddress1(input);
			}
			System.out.print("Enter New Client Address2: ");
			input = keyboard.nextLine();
			if (Bank.validateLettersAndNumbersOnly(input)) {
				change = true;
				user.setAddress2(input);
			} else
				System.out.println("Only Letters and Numbers are allowed");
			System.out.print("Enter New Client City: ");
			input = keyboard.nextLine();
			String[] city = { input.substring(0, 1), input.substring(1) };
			input = city[0].toUpperCase() + city[1].toLowerCase();
			if (Bank.validateCity(input)) {
				change = true;
				user.setCity(input);
			}
			System.out.print("Enter New Client State: ");
			input = keyboard.nextLine();
			if (Bank.validateState(input)) {
				change = true;
				user.setState(input);
			}
			System.out.print("Enter New Client ZipCode: ");
			input = keyboard.nextLine();
			if (Bank.validateZip(input)) {
				change = true;
				user.setZipCode(input);
			}
			break;
		case 5:
			System.out.print("Enter New Client Phone: ");
			input = keyboard.nextLine();
			Bank.modifyPhone(input);
			if (Bank.validatePhone(input)) {
				change = true;
				user.setPhone(input);
			}
			break;
		case 6:
			System.out.print("Enter New Client Email: ");
			input = keyboard.nextLine();
			if (Bank.validateEmail(input)) {
				change = true;
				user.setEmail(input);
			}
			break;
		case 7:
			System.out.print("Enter New Client Driver License State: ");
			String state = keyboard.nextLine();
			if (Bank.validateState(state)) {
				System.out.print("Enter New Client Driver License Number: ");
				String number = keyboard.nextLine();
				if (Bank.validateLicenseNumber(number)) {
					UserAccount temp = impl.getByDl(state, number);
					if (temp.getId() != null && temp.getId() != user.getId()) {
						System.out.println("Driver License is already in use");
						editUserAccount(user);
					} else {
						change = true;
						user.setDlState(state);
						user.setDlNumber(number);
					}
				}
			}
			break;
		case 9:
			displayEmployeeMenu();
			break;
		default:
			editUserAccount(user);
			break;
		}
		if (change) {
			impl.update(user);
			System.out.println("Account Updated");
		}
		editUserAccount(user);
	}

}
