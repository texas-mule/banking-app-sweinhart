package com.revature;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import org.apache.log4j.Logger;
import com.revature.domain.AccountRequest;
import com.revature.domain.Bank;
import com.revature.domain.BankAccount;
import com.revature.domain.BankTransactions;
import com.revature.domain.UserAccount;
import com.revature.service.UserAccountDbSvcImpl;

public class MainMenuEmployee extends MainMenuClient {

	private static Scanner keyboard;
	private static Logger logger = Logger.getLogger(MainMenuEmployee.class);
	private UserAccount user;
	protected static DecimalFormat df = new DecimalFormat("0.00");

	public MainMenuEmployee(UserAccount user) {
		super(user);
		// TODO Auto-generated constructor stub
		this.user = user;
	}

	public void displayMainMenu() {
		logger.info("Starting Employee Inital Menu");
		keyboard  = new Scanner(System.in);
		System.out.println("\n\t\t\t" + Bank.getBankName() + "\t Routing Number: " + Bank.getRoutingNumber());
		int choice = 0;
		System.out.println("\nSelect Personal/Client Accounts");
		System.out.println("1 - Personal Accounts");
		System.out.println("2 - Client Accounts");
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
		case 9:
			LoginMenu login = new LoginMenu();
			login.displayMenu();
			break;
		default:
			System.out.println("Invalid Choice.");
			displayMainMenu();
		}
	}

	private void displayEmployeeMenu() {
		// TODO Auto-generated method stub
		logger.info("Starting Employee Menu");
		keyboard  = new Scanner(System.in);
		int choice = 0;
		System.out.println("\nEmployee Menu");
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
			user = displayClientSelectionMenu();
			displayUserAccountDetails(user);
			break;
		case 3:
			user = displayClientSelectionMenu();
			account = selectBankAccount(user);
			if (account != null)
				displayClientBankAccountDetails(account);
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

	protected void displayClientBankAccountDetails(BankAccount account) {
		// TODO Auto-generated method stub
		logger.info("Starting Bank Account Details");
		keyboard  = new Scanner(System.in);
		System.out.println("\nBank Account Details");
		System.out.println(account.getAccountType() + ": " + account.getAccountNumber() + "\tBalance $"
				+ df.format(account.getBalance()));
		System.out.println("Transaction History:");
		BankTransactions.viewAccountHistory(account);
	}

	protected BankAccount selectBankAccount(UserAccount user) {
		// TODO Auto-generated method stub
		logger.info("Starting Bank Account Selection Menu");
		keyboard  = new Scanner(System.in);
		int choice = 0;
		int index = 0;
		System.out.println("\nBank Account Selection Menu");
		if (user.getAccounts().size() == 0) {
			System.out.println("Client Has No Open Accounts");
			return null;
		}
		for (BankAccount acc : user.getAccounts()) {
			index++;
			System.out.println(index + " - " + acc.getAccountType() + ": " + acc.getAccountNumber() + "\tBalance $"
					+ df.format(acc.getBalance()));
		}
		System.out.print("Choice? ");
		try {
			choice = keyboard.nextInt();
		} catch (InputMismatchException e) {
			logger.info("Handling Input Mismatch Exception");
			choice = 0;
		}
		if (choice < 1 || choice > index) {
			System.out.println("Invalid Selection");
			selectBankAccount(user);
		}
		return user.getAccounts().get(choice);
	}

	protected UserAccount displayClientSelectionMenu() {
		// TODO Auto-generated method stub
		logger.info("Starting Client Account Selection Menu");
		keyboard  = new Scanner(System.in);
		System.out.println("\nBank Client List");
		List<UserAccount> clients = new ArrayList<UserAccount>();
		clients = Bank.getUserAccounts();
		Collections.sort(clients);
		int index = 0;
		for (UserAccount account : clients) {
			index++;
			System.out.print(index + " - " + account.getLastName() + ", " + account.getFirstName());
			System.out.print("\t");
			switch (account.getAccessLvl()) {
			case 1:
				System.out.println("Bank Client");
				break;
			case 2:
				System.out.println("Bank Employee");
				break;
			case 3:
				System.out.println("Bank Administrator");
				break;
			}			
		}
		Integer choice = 0;
		String choiceString = "";
		System.out.println("Select Client Account OR ESC to go Back: ");
		try (BufferedReader input = new BufferedReader(new InputStreamReader(System.in, "UTF-8"))) {
			char c;
			do {
				c = (char) input.read();
			} while (c != 13 && c != 27);
			if (c == 27)
				displayEmployeeMenu();
			else
				choiceString += c;
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage());
		}
		choice = Integer.valueOf(choiceString);
		if (choice == null || choice > index) {
			System.out.println("Invalid Selection");
			displayClientSelectionMenu();
		}
		UserAccount account = new UserAccount();
		account = clients.get(choice - 1);
		return account;
	}

	protected void displayUserAccountDetails(UserAccount account) {
		// TODO Auto-generated method stub
		logger.info("Starting Client Account Details");
		keyboard  = new Scanner(System.in);
		System.out.println("\nClient Details");
		System.out.print(account.getLastName() + account.getFirstName());
		System.out.print("\t");
		switch (account.getAccessLvl()) {
		case 1:
			System.out.println("Bank Client");
			break;
		case 2:
			System.out.println("Bank Employee");
			break;
		case 3:
			System.out.println("Bank Administrator");
			break;
		}

		System.out.print(account.getLastName() + ", " + account.getFirstName());
		System.out.print("\t\t\t");
		System.out.println();
		System.out.print(account.getAddress1() + " #" + account.getAddress2());
		System.out.print("\t\t\t\t");
		System.out.println();
		System.out.print(account.getCity() + ", " + account.getState() + " " + account.getZipcode());
		System.out.print("\t\t\t\t");
		System.out.println();
		System.out.print(account.getPhone());
		System.out.print("\t\t\t\t");
		System.out.println();
		System.out.print(account.getEmail());
		System.out.print("\t\t\t\t");
		System.out.println();
		System.out.print(account.getSocialSecurity());
		System.out.print("\t\t\t\t");
		System.out.println();
		System.out.print(account.getDlState());
		System.out.print(" License: " + account.getDlNumber());
		System.out.print("\t\t\t\t");
		System.out.println();
		System.out.print("Expires: ");
		System.out.print(account.getDlExp());
		System.out.print("\t\t\t\t");

		System.out.println("\nPress Any Key to Return to Menu");
		try (BufferedReader input = new BufferedReader(new InputStreamReader(System.in, "UTF-8"))) {
			char c;
			do {
				c = (char) input.read();
			} while (c != 0);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage());
		}
	}

	protected void displayOpenRequests() {
		// TODO Auto-generated method stub
		logger.info("Starting Client Account Requests View");
		keyboard  = new Scanner(System.in);
		List<AccountRequest.Request> openRequests = Bank.getAccountRequests();
		// Cannot approve your own requests
		List<AccountRequest.Request> requests = new ArrayList<AccountRequest.Request>();
		boolean valid = true;
		for (AccountRequest.Request request : openRequests) {
			for (Integer i : request.getUserIds()) {
				if (request.getUserIds().get(i) == this.user.getId())
					valid = false;
			}
			if (valid)
				requests.add(request);
			valid = true;
		}
		if (requests.size() == 0) {
			System.out.println("\nThere are Currently No Open Requests");
			displayEmployeeMenu();
		}
		System.out.println("\nCurrent Open Requests");
		int index = 0;
		int choice = 0;
		for (AccountRequest.Request request : requests) {
			index++;
			System.out.print(index + " - " + request.getDate() + " ");
			if (request.getUserIds().size() > 1)
				System.out.println("Joint");
			System.out.print(" " + request.getAccountType() + " Account Request with Opening ");
			System.out.println("Deposit $" + df.format(request.getDeposit()));
		}
		System.out.print("Select Request: ");
		try {
			choice = keyboard.nextInt();
		} catch (InputMismatchException e) {
			logger.info("Handling Input Mismatch Exception");
			choice = 0;
		}
		if (choice < 1 || choice > index) {
			System.out.println("Invalid Input");
			displayOpenRequests();
		}
		choice--;
		AccountRequest.Request request = requests.get(choice);
		System.out.println("Client Information");
		List<UserAccount> accounts = new ArrayList<UserAccount>();
		UserAccountDbSvcImpl impl = UserAccountDbSvcImpl.getInstance();
		for (Integer id : request.getUserIds()) {
			accounts.add(impl.getById(id));
		}

		for (int i = 0; i < accounts.size(); i++) {
			System.out.print(accounts.get(i).getLastName() + ", " + accounts.get(i).getFirstName());
			System.out.print("\t\t\t");
		}
		System.out.println();
		for (int i = 0; i < accounts.size(); i++) {
			System.out.print(accounts.get(i).getAddress1() + " #" + accounts.get(i).getAddress2());
			System.out.print("\t\t\t\t");
		}
		System.out.println();
		for (int i = 0; i < accounts.size(); i++) {
			System.out.print(
					accounts.get(i).getCity() + ", " + accounts.get(i).getState() + " " + accounts.get(i).getZipcode());
			System.out.print("\t\t\t\t");
		}
		System.out.println();
		for (int i = 0; i < accounts.size(); i++) {
			System.out.print(accounts.get(i).getPhone());
			System.out.print("\t\t\t\t");
		}
		System.out.println();
		for (int i = 0; i < accounts.size(); i++) {
			System.out.print(accounts.get(i).getEmail());
			System.out.print("\t\t\t\t");
		}
		System.out.println();
		for (int i = 0; i < accounts.size(); i++) {
			System.out.print(accounts.get(i).getSocialSecurity());
			System.out.print("\t\t\t\t");
		}
		System.out.println();
		for (int i = 0; i < accounts.size(); i++) {
			System.out.print(accounts.get(i).getDlState());
			System.out.print(" License: " + accounts.get(i).getDlNumber());
			System.out.print("\t\t\t\t");
		}
		System.out.println();
		for (int i = 0; i < accounts.size(); i++) {
			System.out.print("Expires: ");
			System.out.print(accounts.get(i).getDlExp());
			System.out.print("\t\t\t\t");
		}

		System.out.println("\n1 - Approve Request");
		System.out.println("2 - Deny Request");
		System.out.println("9 - Back to Employee Menu");
		System.out.print("Choice? ");
		try {
			choice = keyboard.nextInt();
		} catch (InputMismatchException e) {
			logger.info("Handling Input Mismatch Exception");
			choice = 0;
		}

		boolean approve;
		switch (choice) {
		case 1:
			approve = true;
			BankTransactions.approvePendingRequests(request, approve);
			break;
		case 2:
			approve = false;
			BankTransactions.approvePendingRequests(request, approve);
			break;
		case 9:
			displayEmployeeMenu();
			break;
		default:
			System.out.println("Invalid Choice.");
			displayOpenRequests();
		}
		displayEmployeeMenu();
	}
}
