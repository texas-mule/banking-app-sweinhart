package com.revature;

import java.text.DecimalFormat;
import java.util.InputMismatchException;
import java.util.Scanner;
import org.apache.log4j.Logger;
import com.revature.domain.AccountRequest;
import com.revature.domain.Bank;
import com.revature.domain.BankAccount;
import com.revature.domain.BankTransactions;
import com.revature.domain.Login;
import com.revature.domain.UserAccount;
import com.revature.service.UserAccountDbSvcImpl;

public class MainMenuClient {

	private UserAccount user;
	private static Scanner keyboard;
	private static Logger logger = Logger.getLogger(MainMenuClient.class);
	DecimalFormat df = new DecimalFormat("0.00");

	public MainMenuClient(UserAccount user) {
		// TODO Auto-generated constructor stub
		this.user = user;
	}

	public void displayMainMenu() {
		displayClientMenu();
	}

	public void displayClientMenu() {
		keyboard = new Scanner(System.in);
		logger.info("Starting Client Main Menu");
		System.out.println("\n\t\t\t" + Bank.getBankName() + "\t Routing Number: " + Bank.getRoutingNumber());
		int choice = 0;
		boolean hasAccounts;
		if (user.getAccounts().isEmpty())
			hasAccounts = false;
		else
			hasAccounts = true;
		System.out.println("Client Menu");
		System.out.println("1 - Request to Open New Account");
		System.out.println("2 - View Accounts");
		System.out.println("3 - Change Your Password");
		if (user.getAccessLvl() > 1)
			System.out.println("4 - Main Menu");
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
			requestAccount();
			break;
		case 2:
			if (hasAccounts) {
				viewAccounts();
			} else {
				System.out.println("You Have No Open Accounts");
				displayClientMenu();
			}
			break;
		case 3:
			changePassword();
			break;
		case 4:
			if (user.getAccessLvl() == 1) {
				System.out.println("Invalid Choice.");
				displayClientMenu();
			} else
				displayMainMenu();
			break;
		case 9:
			LoginMenu login = new LoginMenu();
			login.displayMenu();
			break;
		default:
			System.out.println("Invalid Choice.");
			displayClientMenu();
		}
	}

	private void changePassword() {
		// TODO Auto-generated method stub
		keyboard = new Scanner(System.in);
		logger.info("Starting Password Change");
		System.out.println("\nEnter Old Password: ");
		String password = keyboard.nextLine();
		if (Login.getPasswordAccessLvlMatch(password, this.user.getLogin().getPassword()) == 0) {
			System.out.println("Password does not match");
			displayClientMenu();
		}
		do {
			System.out.println("Enter New Password: ");
			password = keyboard.nextLine();
		} while (!Bank.validatePassword(password));
		System.out.println("Confirm New Password: ");
		String confirm = keyboard.nextLine();
		if (confirm.equals(password)) {
			this.user.getLogin().setPassword(password, this.user.getAccessLvl());
			UserAccountDbSvcImpl impl = UserAccountDbSvcImpl.getInstance();
			impl.update(this.user);
			logger.info("Password Change Successful");
			System.out.println("Password has been Updated");
		} else
			System.out.println("Passwords did not match");
		displayClientMenu();
	}

	protected void viewAccounts() {
		// TODO Auto-generated method stub
		keyboard = new Scanner(System.in);
		logger.info("Starting Account Selection View");
		int choice;
		int index = 0;
		do {
			System.out.println("\nSelect Account");
			for (BankAccount account : user.getAccounts()) {
				index++;
				System.out
						.println(index + " - " + account.getAccountType() + " Account: " + account.getAccountNumber());
			}
			System.out.println("Choice? ");
			try {
				choice = keyboard.nextInt();
			} catch (InputMismatchException e) {
				logger.info("Handling Input Mismatch Exception");
				choice = 0;
			}
		} while (choice < 1 || choice > index);
		choice--;
		BankAccount account = user.getAccounts().get(choice);
		accountOperationsMenu(account);
	}

	protected void accountOperationsMenu(BankAccount account) {
		// TODO Auto-generated method stub
		keyboard = new Scanner(System.in);
		int choice;
		logger.info("Account Operations Menu Started");
		System.out.print("\n" + account.getAccountType() + " Account: " + account.getAccountNumber());
		System.out.print("\t\tBalance: $" + df.format(account.getBalance()));
		System.out.println("\nAccount Operations Menu");
		System.out.println("1 - View Account History");
		System.out.println("2 - Make a Withdrawl");
		System.out.println("3 - Make a Deposit");
		if (user.getAccounts().size() > 1) {
			System.out.println("4 - Transfer Funds");
		}
		System.out.println("9 - Back");
		System.out.print("Choice? ");
		try {
			choice = keyboard.nextInt();
		} catch (InputMismatchException e) {
			logger.info("Handling Input Mismatch Exception");
			choice = 0;
		}
		switch (choice) {
		case 1:
			BankTransactions.viewAccountHistory(account);
			accountOperationsMenu(account);
			break;
		case 2:
			BankTransactions.makeWithdrawl(user, account);
			accountOperationsMenu(account);
			break;
		case 3:
			BankTransactions.makeDeposit(user, account);
			accountOperationsMenu(account);
			break;
		case 4:
			if (user.getAccounts().size() > 1) {
				BankAccount fromAccount = account;
				int index = 0;
				System.out.println("Transfer To:");
				for (BankAccount acc : user.getAccounts()) {
					if (!acc.getAccountNumber().equals(acc.getAccountNumber())) {
						index++;
						System.out.println(index + " - " + acc.getAccountType() + " Account: " + acc.getAccountNumber()
								+ "\t\tBalance $" + df.format(acc.getBalance()));
					}
				}
				System.out.println("0 - Back");
				System.out.print("Choice? ");
				try {
					choice = keyboard.nextInt();
				} catch (InputMismatchException e) {
					logger.info("Handling Input Mismatch Exception");
					choice = 0;
				}
				if (choice <= 0 || choice > index)
					accountOperationsMenu(account);
				choice --;
				BankAccount toAccount = user.getAccounts().get(choice);
				BankTransactions.transferFunds(user, fromAccount, toAccount);
				accountOperationsMenu(account);
			} else {
				System.out.println("Invalid Choice.");
				accountOperationsMenu(account);
			}
			break;
		case 9:
			displayClientMenu();
			break;
		default:
			System.out.println("Invalid Choice.");
			accountOperationsMenu(account);
		}
	}

	protected void requestAccount() {
		// TODO Auto-generated method stub
		keyboard = new Scanner(System.in);
		logger.info("Starting Account Request Menu");
		String accountType = "";
		if (user.getPendingRequests().getAccountRequests().size() > 0) {
			System.out.println("\nCurrent Account Requests Not Yet Approved");
			for (AccountRequest.Request request : user.getPendingRequests().getAccountRequests()) {
				System.out.println("Date Requested: " + request.getDate() + "\tAccount Type: "
						+ request.getAccountType() + "\tDeposit Ammount: $" + df.format(request.getDeposit()));
			}
		}
		System.out.println("\nNew Account Request");
		System.out.println("1 - Request Checking Account");
		System.out.println("2 - Request Savings Account");
		System.out.println("9 - Back");
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
			accountType = user.getPendingRequests().checking;
			break;
		case 2:
			accountType = user.getPendingRequests().saving;
			break;
		case 9:
			displayClientMenu();
			break;
		default:
			System.out.println("Invalid Choice.");
			requestAccount();
		}
		System.out.println("\n1 - Single");
		System.out.println("2 - Joint");
		System.out.println("9 - Back");
		System.out.print("Choice? ");
		try {
			choice = keyboard.nextInt();
		} catch (InputMismatchException e) {
			logger.info("Handling Input Mismatch Exception");
			choice = 0;
		}
		AccountRequest userRequest = user.getPendingRequests();
		AccountRequest.Request request = userRequest.new Request();
		UserAccountDbSvcImpl impl = UserAccountDbSvcImpl.getInstance();
		switch (choice) {
		case 1:
			request.setAccountType(accountType);
			request.addUserSSNumber(user.getSocialSecurity());
			request.setDeposit(getDeposit(accountType));
			userRequest.addAccountRequest(request);
			Bank.addAccountRequest(request);
			user.setPendingRequests(userRequest);
			break;
		case 2:
			boolean valid;
			String ssNumber;
			do {
				keyboard = new Scanner(System.in);
				valid = true;
				System.out.print("Enter Social Security Number of Joint Account Holder: ");
				ssNumber = keyboard.nextLine();
				ssNumber = ssNumber.trim();
				if (!ssNumber.contains("-"))
					ssNumber = ssNumber.substring(0, 3) + "-" + ssNumber.substring(3, 5) + "-" + ssNumber.substring(5);
				String[] ss = ssNumber.split("-");
				if (ss.length != 3)
					valid = false;
				if (valid && (!Bank.validateNumbersOnly(ss[0]) || !Bank.validateNumbersOnly(ss[1])
						|| !Bank.validateNumbersOnly(ss[2])))
					valid = false;
				if (!valid)
					System.out.println("Invalid Social Security Number.");
			} while (!valid);
			UserAccount jointUser = new UserAccount();
			jointUser = impl.getBySs(ssNumber);
			if (jointUser.getId() == this.user.getId()) {
				System.out.println("Joint Account Holder must be a different Person");
				displayClientMenu();
			}
			if (jointUser.getId() == null) {
				System.out.println("User Does Not Exist in the System");
				jointUser = Bank.createUserAccount(1);
				jointUser = impl.getBySs(jointUser.getSocialSecurity());
			}
			request.setAccountType(accountType);
			request.addUserSSNumber(user.getSocialSecurity());
			request.addUserSSNumber(jointUser.getSocialSecurity());
			request.setDeposit(getDeposit(accountType));
			userRequest.addAccountRequest(request);
			user.setPendingRequests(userRequest);
			AccountRequest jointUserRequest = jointUser.getPendingRequests();
			jointUserRequest.addAccountRequest(request);
			jointUser.setPendingRequests(jointUserRequest);
			Bank.addAccountRequest(request);
			break;
		case 3:
			requestAccount();
			break;
		default:
			System.out.println("Invalid Choice.");
			requestAccount();
		}
		System.out.println("Your Request has been sent to the Bank");
		displayClientMenu();
	}

	protected Double getDeposit(String accountType) {
		// TODO Auto-generated method stub
		keyboard = new Scanner(System.in);
		logger.info("Setting minimum deposit");
		Double deposit;
		Double requiredDeposit = 0.0;
		do {
			if (accountType.equals("Checking")) {
				requiredDeposit = 50.0;
				System.out.println("Your Minimum Required Deposit is $50.00");
			} else if (accountType.equals("Savings")) {
				requiredDeposit = 5.0;
				System.out.println("Your Minimum Required Deposit is $5.00");
			} else {
				logger.info("Unable to determine accountType in getDeposit Method");
				System.exit(1);
			}
			System.out.print("Enter Deposit Amount: $");
			try {
				deposit = keyboard.nextDouble();
			} catch (InputMismatchException e) {
				logger.info("Handling Input Mismatch Exception");
				deposit = 0.0;
			}
		} while (deposit < requiredDeposit);
		return deposit;
	}
}
