package com.revature.domain;

import java.text.DecimalFormat;
import java.util.Scanner;
import org.apache.log4j.Logger;
import com.revature.MainMenuAdmin;
import com.revature.MainMenuClient;
import com.revature.MainMenuEmployee;
import com.revature.domain.AccountRequest.Request;
import com.revature.service.AccountRequestDbSvcImpl;
import com.revature.service.BankAccountDbSvcImpl;
import com.revature.service.UserAccountDbSvcImpl;

import java.util.InputMismatchException;
import java.util.List;

public class BankTransactions {

	private static Logger logger = Logger.getLogger(BankTransactions.class);
	private static DecimalFormat df = new DecimalFormat("0.00");

	public static void transferFunds(UserAccount user, BankAccount account) {
		// TODO Auto-generated method stub
		logger.info("Transfer Funds Started");
		Double transferAmt = 0.0;
		List<BankAccount> accounts = user.getAccounts();
		int index = 1;
		int choice1 = 0;
		int choice2 = 0;
		Scanner keyboard = new Scanner(System.in);
		boolean valid = true;
		BankAccount fromAccount = new BankAccount();
		BankAccount toAccount = new BankAccount();
		do {
			System.out.println("\nTransfer Funds");
			for (BankAccount acc : accounts) {
				System.out.println(index + " - " + acc.getAccountType() + ": " + acc.getAccountNumber()
						+ "\tBalance:\t$" + acc.getBalance());
				index++;
			}
			System.out.print("Select Account to Transfer From: ");
			try {
				choice1 = keyboard.nextInt();
			} catch (InputMismatchException e) {
				logger.info("Handling Input Mismatch Exception");
				System.out.println("Invalid Input. Returning to Main Menu.");
				returnToMainMenu(user);
			}
			System.out.print("Select Account to Transfer To: ");
			try {
				choice2 = keyboard.nextInt();
			} catch (InputMismatchException e) {
				logger.info("Handling Input Mismatch Exception");
				System.out.println("Invalid Input. Returning to Main Menu.");
				returnToMainMenu(user);
			}
			if (choice1 < 1 || choice2 < 1 || choice1 > index || choice2 > index) {
				valid = false;
				System.out.println("Invalid Selections");
			}
			if (choice1 == choice2) {
				valid = false;
				System.out.println("Cannot Transfer Funds to Same Account");
			}
		} while (!valid);
		choice1--;
		choice2--;
		try {
			fromAccount = accounts.get(choice1);
		} catch (IndexOutOfBoundsException e) {
			logger.info("Transfer Funds: from Account " + e.getMessage());
			System.out.println("Oops! An Error Caused the Program to Shut Down");
			System.exit(1);
		}
		try {
			toAccount = accounts.get(choice2);
		} catch (IndexOutOfBoundsException e) {
			logger.info("Transfer Funds: to Account " + e.getMessage());
			System.out.println("Oops! An Error Caused the Program to Shut Down");
			System.exit(1);
		}
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
		} while (!valid);
		Transaction transaction = new Transaction();
		System.out.print("Transfer $" + df.format(transferAmt) + " From " + fromAccount.getAccountType() + ": "
				+ fromAccount.getAccountNumber() + " To " + toAccount.getAccountType() + ": "
				+ toAccount.getAccountNumber() + "? ");
		char confirm = keyboard.nextLine().toUpperCase().charAt(0);
		if (confirm == 'Y') {
			BankAccountDbSvcImpl impl = BankAccountDbSvcImpl.getInstance();
			transaction.setTransactionType(Transaction.transfer);
			transaction.setAccountNumber(toAccount.getAccountNumber());
			transaction.setTransactionAmount(transferAmt);
			transaction.setDateInstance();
			toAccount.addTransaction(transaction);			
			impl.update(toAccount);
			transaction.setTransactionType(Transaction.transfer);
			transaction.setAccountNumber(fromAccount.getAccountNumber());
			transaction.setTransactionAmount(-transferAmt);
			transaction.setDateInstance();
			fromAccount.addTransaction(transaction);			
			impl.update(fromAccount);
		}
		keyboard.close();
	}

	public static void makeDeposit(UserAccount user, BankAccount account) {
		// TODO Auto-generated method stub
		logger.info("Make Deposit Started");
		Double deposit = 0.0;
		Transaction transaction = new Transaction();
		System.out.print(
				"Deposit Funds into " + account.getAccountType() + " Account: " + account.getAccountNumber() + "? ");
		Scanner keyboard = new Scanner(System.in);
		char confirm = keyboard.nextLine().toUpperCase().charAt(0);
		if (confirm == 'Y') {
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
			transaction.setDateInstance();
			account.addTransaction(transaction);
			BankAccountDbSvcImpl impl = BankAccountDbSvcImpl.getInstance();
			impl.update(account);
		}
		keyboard.close();
	}

	private static void returnToMainMenu(UserAccount user) {
		// TODO Auto-generated method stub
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

	public static void makeWithdrawl(UserAccount user, BankAccount account) {
		// TODO Auto-generated method stub
		logger.info("Make Withdrawl Started");
		Double withdrawl = 0.0;
		Transaction transaction = new Transaction();
		System.out.print(
				"Withdraw Funds from " + account.getAccountType() + " Account: " + account.getAccountNumber() + "? ");
		Scanner keyboard = new Scanner(System.in);
		char confirm = keyboard.nextLine().toUpperCase().charAt(0);
		if (confirm == 'Y') {
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
			transaction.setDateInstance();
			account.addTransaction(transaction);
			BankAccountDbSvcImpl impl = BankAccountDbSvcImpl.getInstance();
			impl.update(account);
		}
		keyboard.close();
		logger.info("Make Withdrawl Completed");
	}

	public static void viewAccountHistory(BankAccount account) {
		// TODO Auto-generated method stub
		logger.info("View Account Started");
		Double tempBalance = 0.0;
		System.out.println("Beginning Balance\t\t\t\t\t$" + df.format(tempBalance));
		for (Transaction transaction : account.getTransactions()) {
			tempBalance += transaction.getTransactionAmount();
			System.out.print(transaction.getDate() + " - " + transaction.getTransactionType() + "\t\t\t\t");
			if (transaction.getTransactionAmount() < 0)
				System.out.print("-$");
			else
				System.out.print("+$");
			System.out.println(df.format(transaction.getTransactionAmount()));
			System.out.println("Ending Balance\t\t\t\t\t$" + df.format(tempBalance));
		}
		logger.info("View History Completed");
	}

	public static void approvePendingRequests(Request request, boolean approve) {
		// TODO Auto-generated method stub
		AccountRequestDbSvcImpl requestImpl = AccountRequestDbSvcImpl.getInstance();
		UserAccountDbSvcImpl userImpl = UserAccountDbSvcImpl.getInstance();
		UserAccount account = new UserAccount();
		if (approve) {
			BankAccount newAccount = new BankAccount();
			newAccount.setAccountType(request.getAccountType());
			newAccount = Bank.assignAccountNumber(newAccount);
			for (Integer i : request.getUserIds()) {
				newAccount.getAccountOwners().add(request.getUserIds().get(i));
				account = userImpl.getById(i);
				for (UserAccount u : Bank.getUserAccounts()) {
					if (u.getId() == account.getId())
						u.getAccounts().add(newAccount);
				}
			}			
		}
		Bank.getAccountRequests().remove(request);
		requestImpl.delete(request);
		for (Integer i : request.getUserIds()) {
			account = userImpl.getById(i);
			for (UserAccount u : Bank.getUserAccounts()) {
				if (u.getId() == account.getId()) {
					u.getPendingRequests().getAccountRequests().remove(request);
				}
			}
		}
	}

	public static void closeAccount(UserAccount user, BankAccount account) {
		// TODO Auto-generated method stub
		BankAccountDbSvcImpl impl = BankAccountDbSvcImpl.getInstance();
		impl.delete(account);
	}
}
