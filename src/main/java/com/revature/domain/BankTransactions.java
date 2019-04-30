package com.revature.domain;

import java.text.DecimalFormat;
import java.util.Scanner;
import org.apache.log4j.Logger;
import com.revature.AdminMenu;
import com.revature.ClientMenu;
import com.revature.EmployeeMenu;
import com.revature.domain.AccountRequest.Request;
import com.revature.service.AccountRequestDAO;
import com.revature.service.BankAccountDAO;
import com.revature.service.TransactionDAO;
import com.revature.service.UserAccountDAO;
import java.util.InputMismatchException;

public class BankTransactions {

	private static Scanner keyboard;
	private static Logger logger = Logger.getLogger(BankTransactions.class);
	private static DecimalFormat df = new DecimalFormat("0.00");

	public BankTransactions() {
		super();
		// TODO Auto-generated constructor stub
	}

	public static void transferFunds(UserAccount user, BankAccount fromAccount, BankAccount toAccount) {
		// TODO Auto-generated method stub
		logger.info("Transfer Funds Started");
		if (fromAccount.getAccountNumber().equals(toAccount.getAccountNumber())) {
			System.out.println("Can't Transfer Funds to the same Account");
			returnToMainMenu(user);
		}
		boolean valid;
		Double transferAmt = 0.0;
		keyboard = new Scanner(System.in);
		do {
			valid = true;
			System.out.print("Enter Amount to Transfer: $");
			try {
				transferAmt = keyboard.nextDouble();
			} catch (InputMismatchException e) {
				logger.info("Handling Input Mismatch Exception");
				System.out.println("Invalid Input. Returning to Main Menu.");
				returnToMainMenu(user);
			}
			if (transferAmt > fromAccount.getBalance()) {
				valid = false;
				System.out.println("Transfer Amount Can Not Exceed the Account Balance");
			}
			if (transferAmt <= 0) {
				valid = false;
				System.out.println("Transfer Amount Must be Greater than $0");
			}
		} while (!valid);
		Transaction transaction = new Transaction();
		System.out.println("Transfer $" + df.format(transferAmt) + "\nFrom " + fromAccount.getAccountType() + ": "
				+ fromAccount.getAccountNumber() + " To " + toAccount.getAccountType() + ": "
				+ toAccount.getAccountNumber() + "?");
		System.out.println("1 - Yes");
		System.out.println("2 - No");
		System.out.print("Choice? ");
		int choice = 0;
		try {
			choice = keyboard.nextInt();
		} catch (InputMismatchException e) {
			logger.info("Handling Input Mismatch Exception");
			System.out.println("Invalid Input. Returning to Main Menu.");
			returnToMainMenu(user);
		}
		if (choice == 1) {
			TransactionDAO impl = TransactionDAO.getConnection();
			transaction.setTransactionType(Transaction.transfer);
			transaction.setAccountNumber(toAccount.getAccountNumber());
			transaction.setTransactionAmount(transferAmt);
			toAccount.addTransaction(transaction);
			impl.add(transaction);
			transaction = new Transaction();
			transaction.setTransactionType(Transaction.transfer);
			transaction.setAccountNumber(fromAccount.getAccountNumber());
			transaction.setTransactionAmount(-transferAmt);
			fromAccount.addTransaction(transaction);
			impl.add(transaction);
			System.out.println("Transfer Complete");
		}
		returnToMainMenu(user);
	}

	public static void makeDeposit(UserAccount user, BankAccount account) {
		// TODO Auto-generated method stub
		logger.info("Make Deposit Started");
		keyboard = new Scanner(System.in);
		Double deposit = 0.0;
		Transaction transaction = new Transaction();
		System.out.println(
				"\nDeposit Funds into " + account.getAccountType() + " Account " + account.getAccountNumber() + "?");
		System.out.println("1 - Yes");
		System.out.println("2 - No");
		System.out.print("Choice? ");
		int choice = 0;
		try {
			choice = keyboard.nextInt();
		} catch (InputMismatchException e) {
			logger.info("Handling Input Mismatch Exception");
			System.out.println("Invalid Input. Returning to Main Menu.");
			returnToMainMenu(user);
		}
		if (choice < 1 || choice > 2) {
			System.out.println("Invalid Input. Returning to Main Menu.");
			returnToMainMenu(user);
		}
		if (choice != 1)
			returnToMainMenu(user);
		else {
			System.out.print("Enter Amount to Deposit $");
			try {
				deposit = keyboard.nextDouble();
			} catch (InputMismatchException e) {
				logger.info("Handling Input Mismatch Exception");
				System.out.println("Invalid Input. Returning to Main Menu.");
				returnToMainMenu(user);
			}
			transaction.setTransactionType(Transaction.deposit);
			transaction.setAccountNumber(account.getAccountNumber());
			transaction.setTransactionAmount(deposit);
			account.addTransaction(transaction);
			account.setBalance(account.getBalance() + deposit);
			TransactionDAO transImpl = TransactionDAO.getConnection();
			transImpl.add(transaction);
			System.out.println("Deposit Completed");
		}
	}

	private static void returnToMainMenu(UserAccount user) {
		// TODO Auto-generated method stub
		switch (user.getAccessLvl()) {
		case 1:
			ClientMenu clientMenu = new ClientMenu(user);
			clientMenu.displayMainMenu();
			break;
		case 2:
			EmployeeMenu emplMenu = new EmployeeMenu(user);
			emplMenu.displayEmployeeMenu();
			break;
		case 3:
			AdminMenu adminMenu = new AdminMenu(user);
			adminMenu.displayEmployeeMenu();
			break;
		default:
			logger.info("Invalid user access level argument. Exiting Application!");
			System.out.println("Oops! Something went wrong. Exiting Application");
			System.exit(1);
		}
	}

	public static void makeWithdrawl(UserAccount user, BankAccount account) {
		// TODO Auto-generated method stub
		logger.info("Make Withdrawl Started");
		keyboard = new Scanner(System.in);
		Double withdrawl = 0.0;
		Transaction transaction = new Transaction();
		System.out.println(
				"\nWithdraw Funds from " + account.getAccountType() + " Account " + account.getAccountNumber() + "?");
		System.out.println("1 - Yes");
		System.out.println("2 - No");
		System.out.print("Choice? ");
		int choice = 0;
		try {
			choice = keyboard.nextInt();
		} catch (InputMismatchException e) {
			logger.info("Handling Input Mismatch Exception");
			System.out.println("Invalid Input. Returning to Main Menu.");
			returnToMainMenu(user);
		}
		if (choice < 1 || choice > 2) {
			System.out.println("Invalid Input. Returning to Main Menu.");
			returnToMainMenu(user);
		}
		if (choice != 1)
			returnToMainMenu(user);

		System.out.print("Enter Amount to Withdraw $");
		try {
			withdrawl = keyboard.nextDouble();
		} catch (InputMismatchException e) {
			logger.info("Handling Input Mismatch Exception");
			System.out.println("Invalid Input. Returning to Main Menu.");
			makeWithdrawl(user, account);
		}
		if (withdrawl > account.getBalance()) {
			System.out.println("Cannot Withdraw more than Account Balance");
			makeWithdrawl(user, account);
		}
		if (withdrawl <= 0) {
			System.out.println("Invalid Withdrawl Amount");
			makeWithdrawl(user, account);
		}
		transaction.setAccountNumber(account.getAccountNumber());
		transaction.setTransactionType(Transaction.withdrawl);
		transaction.setTransactionAmount(-withdrawl);
		account.addTransaction(transaction);
		account.setBalance(account.getBalance() - withdrawl);
		TransactionDAO transImpl = TransactionDAO.getConnection();
		transImpl.add(transaction);
		System.out.println("Withdrawl Completed");
		logger.info("Make Withdrawl Completed");
	}

	public static void viewAccountHistory(BankAccount account) {
		// TODO Auto-generated method stub
		logger.info("View Account History Started");
		Double tempBalance = 0.0;
		System.out.println("Beginning Balance\t\t\t\t$" + df.format(tempBalance));
		for (Transaction transaction : account.getTransactions()) {
			tempBalance += transaction.getTransactionAmount();
			System.out.print(transaction.getDate() + " - " + transaction.getTransactionType());
			if (transaction.getTransactionType().equals(Transaction.transfer))
				System.out.print("\t\t\t");
			else
				System.out.print("\t\t\t\t");
			if (transaction.getTransactionAmount() < 0) {
				System.out.print("-$");
				System.out.println(df.format(Math.abs(transaction.getTransactionAmount())));
			} else {
				System.out.print("+$");
				System.out.println(df.format(transaction.getTransactionAmount()));
			}
			System.out.println("Ending Balance\t\t\t\t\t$" + df.format(tempBalance));
		}
		logger.info("View History Completed");
	}

	public static void approvePendingRequests(Request request, boolean approve) {
		// TODO Auto-generated method stub
		logger.info("Approve Pending Request Started");
		AccountRequestDAO requestImpl = AccountRequestDAO.getConnection();
		UserAccountDAO userImpl = UserAccountDAO.getConnection();
		UserAccount account = new UserAccount();
		if (approve) {
			BankAccount newAccount = new BankAccount();
			newAccount.setAccountType(request.getAccountType());
			newAccount = Bank.assignAccountNumber(newAccount);
			for (String id : request.getUserSSNumbers()) {
				newAccount.getAccountOwners().add(id);
				account = userImpl.getBySs(id);
				for (UserAccount u : Bank.getUserAccounts()) {
					if (u.getId() == account.getId())
						u.getAccounts().add(newAccount);
				}
			}
			Transaction transaction = new Transaction();
			transaction.setAccountNumber(newAccount.getAccountNumber());
			transaction.setTransactionType(Transaction.deposit);
			transaction.setTransactionAmount(request.getDeposit());
			TransactionDAO transImpl = TransactionDAO.getConnection();
			transImpl.add(transaction);
			BankAccountDAO impl = BankAccountDAO.getConnection();
			impl.add(newAccount);
			newAccount = impl.getAccount(newAccount.getAccountNumber());
			newAccount = impl.getAccount(newAccount.getAccountNumber());
			newAccount.addTransaction(transaction);
			Bank.getAccounts().add(newAccount);
			System.out.println("\n" + newAccount.getAccountType() + " Account Has Been Opened" + " - Account Number "
					+ newAccount.getAccountNumber() + " with " + "Opening deposit of $"
					+ df.format(newAccount.getBalance()));
		}
		Bank.getAccountRequests().remove(request);
		requestImpl.delete(request);
		for (String id : request.getUserSSNumbers()) {
			account = userImpl.getBySs(id);
			for (UserAccount u : Bank.getUserAccounts()) {
				if (u.getId() == account.getId()) {
					u.getPendingRequests().getAccountRequests().remove(request);
				}
			}
		}
	}

	public static void closeAccount(UserAccount user, BankAccount account) {
		// TODO Auto-generated method stub
		logger.info("Close Account Started");
		Bank.deleteAccount(account);
		System.out.println(account.getAccountType() + " Account: " + account.getAccountNumber() + " - Closed");
	}
}
