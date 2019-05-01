package com.revature;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import org.apache.log4j.Logger;
import com.revature.domain.AccountRequests;
import com.revature.domain.Bank;
import com.revature.domain.BankAccount;
import com.revature.domain.BankTransactions;
import com.revature.domain.UserAccount;
import com.revature.service.AccountRequestDAO;
import com.revature.service.BankAccountDAO;
import com.revature.service.UserAccountDAO;

public class AdminMenu extends EmployeeMenu {

	private static Scanner keyboard;
	private static Logger logger = Logger.getLogger(AdminMenu.class);
	private UserAccount user;

	public AdminMenu(UserAccount user) {
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
		System.out.println("4 - Add Bank Admin");
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
			addEmployeeAccount(2);
			break;
		case 4:
			addEmployeeAccount(3);
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
			user = displayClientSelectionMenu();
			displayUserAccountDetails(user);
			break;
		case 3:
			account = displayAccountSelectionMenu();
			if (account != null) {
				displayClientBankAccountDetails(account);
				String ss = account.getAccountOwners().get(0);
				if (ss.equals("0"))
					ss = account.getAccountOwners().get(1);
				UserAccountDAO impl = UserAccountDAO.getConnection();
				user = impl.getBySs(ss);
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
		if (Bank.getAccounts().size() >= 2) {
			System.out.println("3 - Account Funds Transfer");
		}
		System.out.println("4 - Close Customer Account");
		int numAccounts = 0;
		for (String ss : account.getAccountOwners())
			if (!ss.equals("0"))
				numAccounts++;
		if (numAccounts > 1) {
			System.out.println("5 - Remove Owner from Account");
		}
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
			BankTransactions.makeDeposit(this.user, account);
			break;
		case 2:
			BankTransactions.makeWithdrawl(this.user, account);
			break;
		case 3:
			if (Bank.getAccounts().size() < 2) {
				System.out.println("Invalid Choice.");
				displayEmployeeMenu();
			}
			System.out.println("\nSelect Account to Transfer Funds To");
			BankAccount fromAccount = account;
			BankAccount toAccount = displayAccountSelectionMenu();
			if (toAccount != null)
				BankTransactions.transferFunds(this.user, fromAccount, toAccount);
			break;
		case 4:
			closeAccount(account);
			break;
		case 5:
			removeAccountOwner(account);
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

	private void closeAccount(BankAccount account) {
		// TODO Auto-generated method stub
		keyboard = new Scanner(System.in);
		System.out.println("\nConfirm Close " + account.getAccountType() 
			+ " Account " + account.getAccountNumber() + "?");
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
		if (choice < 1 || choice > 2) {
			System.out.println("Invalid Input");
			displayEmployeeMenu();
		}
		if (choice == 2)
			displayEmployeeMenu();
		BankTransactions.closeAccount(this.user, account);
	}

	private void removeAccountOwner(BankAccount account) {
		// TODO Auto-generated method stub
		keyboard = new Scanner(System.in);
		UserAccountDAO impl = UserAccountDAO.getConnection();
		List<UserAccount> users = new ArrayList<UserAccount>();
		int index = 0;
		System.out.println();
		for (String ss : account.getAccountOwners()) {
			users.add(impl.getBySs(ss));
			index++;
			System.out.print(index + " - ");
			System.out.print(users.get(index - 1).getLastName() + ", " + users.get(index - 1).getFirstName() + " ");
			System.out.println("Tax ID: " + users.get(index - 1).getSocialSecurity());
		}
		System.out.println("0 - Back");
		System.out.print("Choice? ");
		int choice = 0;
		try {
			choice = keyboard.nextInt();
		} catch (InputMismatchException e) {
			logger.info("Handling Input Mismatch Exception");
			choice = 0;
		}
		if (choice < 0 || choice > index) {
			System.out.println("Invalid Input");
			displayEmployeeMenu();
		}
		if (choice == 0) {
			displayEmployeeMenu();
		}
		UserAccount user = users.get(choice - 1);
		System.out.print("\nRemove " + user.getLastName() + ", " + user.getFirstName() + " ");
		System.out.println("Tax ID: " + user.getSocialSecurity());
		System.out.println("From " + account.getAccountType() + " Account: " + account.getAccountNumber() + "?");
		System.out.println("1 - Yes");
		System.out.println("2 - No");
		System.out.println("Choice? ");
		try {
			choice = keyboard.nextInt();
		} catch (InputMismatchException e) {
			logger.info("Handling Input Mismatch Exception");
			choice = 0;
		}
		if (choice < 1 || choice > 2) {
			System.out.println("Invalid Input");
			displayEmployeeMenu();
		}
		if (choice == 2) {
			displayEmployeeMenu();
		}
		modifyAccount(account, user.getSocialSecurity(), "0");
		System.out.println("Account Updated Successfully");
	}

	private void addEmployeeAccount(int accessLvl) {
		// TODO Auto-generated method stub
		logger.info("Starting new Employee Menu");
		keyboard = new Scanner(System.in);
		System.out.println("\nAdd New Employee");
		keyboard = new Scanner(System.in);
		String ssNumber;
		boolean valid;
		do {
			System.out.println("Enter Employee Social Security Number: ");
			ssNumber = keyboard.nextLine();
			valid = true;
			if (ssNumber.length() < 8)
				valid = false;
			if (valid && !ssNumber.contains("-"))
				ssNumber = ssNumber.substring(0, 3) + "-" + ssNumber.substring(3, 5) + "-" + ssNumber.substring(5);
			if (valid)
				valid = validateSocialSecurity(ssNumber);
		} while (!valid);
		UserAccount account = new UserAccount();
		UserAccountDAO impl = UserAccountDAO.getConnection();
		account = impl.getBySs(ssNumber);
		if (account.getId() == null) {
			System.out.println("Employee is not registered in the system.");
			account = Bank.createUserAccount(accessLvl);
			displayMainMenu();
		}
		account.setAccessLvl(accessLvl);
		String password;
		do {
			System.out.print("Create Temporary Password for Employee: ");
			password = keyboard.nextLine();
		} while (!Bank.validatePassword(password));
		System.out.println("Temporary Password Set Successfully");
		account.getLogin().setPassword(password, accessLvl);
		impl.update(account);
		displayEmployeeMenu();
	}

	private boolean validateSocialSecurity(String ssNumber) {
		// TODO Auto-generated method stub
		boolean valid = true;
		String[] ss = ssNumber.split("-");
		if (ss.length != 3)
			valid = false;
		if (valid && (!Bank.validateNumbersOnly(ss[0]) || !Bank.validateNumbersOnly(ss[1])
				|| !Bank.validateNumbersOnly(ss[2])))
			valid = false;
		if (!valid) {
			System.out.println("Invalid Social Security Number.");
			valid = false;
		}
		return valid;
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
		System.out.print(account.getCity() + ", " + account.getState() + " " + account.getZipCode());
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
		boolean exists = false;
		for (AccountRequests.Request request : Bank.getAccountRequests()) {
			for (String ss : request.getUserSSNumbers()) {
				if (ss.equals(user.getSocialSecurity()))
					exists = true;
			}
		}
		if (exists) {
			System.out.println("\nUser is associated with a current account request.");
			System.out.println("Cannot Delete User Account");
			displayEmployeeMenu();
		}
		for (BankAccount account : Bank.getAccounts()) {
			for (String ss : account.getAccountOwners()) {
				if (ss.equals(user.getSocialSecurity()))
					exists = true;
			}
		}
		if (exists) {
			System.out.println("\nUser is associated with a current bank account.");
			System.out.println("Cannot Delete User Account");
			displayEmployeeMenu();
		}
		System.out.println("\nConfirm Delete of User: " + user.getLastName() + ", " + user.getFirstName());
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
		if (choice < 1 || choice > 2) {
			System.out.println("Invalid Input");
			displayEmployeeMenu();
		}
		if (choice == 2)
			displayEmployeeMenu();
		if (user.getId() == this.user.getId()) {
			System.out.println("Cannot Delete Your Own Account");
			displayEmployeeMenu();
		}
		UserAccountDAO impl = UserAccountDAO.getConnection();
		impl.delete(user);
		Bank.getUserAccounts().remove(user);
		System.out.println("User Account has been Deleted");
	}

	private void editUserAccount(UserAccount user) {
		// TODO Auto-generated method stub
		UserAccountDAO impl = UserAccountDAO.getConnection();
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
		System.out.println("8 - Change Client Social Security Number");
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
			if (Bank.validateName(input)) {
				change = true;
				String[] firstName = { input.substring(0, 1), input.substring(1) };
				input = firstName[0].toUpperCase() + firstName[1].toLowerCase();
				user.setFirstName(input);
			} else
				editUserAccount(user);
			System.out.print("Enter New Last Name: ");
			input = keyboard.nextLine();
			if (Bank.validateName(input)) {
				change = true;
				String[] lastName = { input.substring(0, 1), input.substring(1) };
				input = lastName[0].toUpperCase() + lastName[1].toLowerCase();
				user.setLastName(input);
			} else
				editUserAccount(user);
			break;
		case 4:
			System.out.print("Enter New Client Address1: ");
			input = keyboard.nextLine();
			input.trim();
			String[] address = input.split(" ");
			if (address.length < 2) {
				System.out.println("Address is Invalid");
				editUserAccount(user);
			}
			input = address[0] + " ";
			for (int i = 1; i < address.length; i++) {
				address[i] = address[i].substring(0, 1).toUpperCase() + address[i].substring(1).toLowerCase();
				input += address[i] + " ";
			}
			input = input.trim();
			if (Bank.validateAddress(input)) {
				change = true;
				user.setAddress1(input);
			} else
				editUserAccount(user);
			System.out.print("Enter New Client Address2: ");
			input = keyboard.nextLine();
			if (Bank.validateLettersAndNumbersOnly(input, true)) {
				change = true;
				user.setAddress2(input);
			} else {
				editUserAccount(user);
			}
			System.out.print("Enter New Client City: ");
			input = keyboard.nextLine();
			if (Bank.validateCity(input)) {
				change = true;
				String[] city = { input.substring(0, 1), input.substring(1) };
				input = city[0].toUpperCase() + city[1].toLowerCase();
				user.setCity(input);
			} else
				editUserAccount(user);
			System.out.print("Enter New Client State: ");
			input = keyboard.nextLine();
			input = input.toUpperCase().trim();
			if (Bank.validateState(input)) {
				change = true;
				user.setState(input);
			} else
				editUserAccount(user);
			System.out.print("Enter New Client ZipCode: ");
			input = keyboard.nextLine();
			if (Bank.validateZip(input)) {
				change = true;
				user.setZipCode(input);
			} else
				editUserAccount(user);
			break;
		case 5:
			System.out.print("Enter New Client Phone: ");
			input = keyboard.nextLine();
			input = Bank.modifyPhone(input);
			if (Bank.validatePhone(input)) {
				change = true;
				user.setPhone(input);
			} else
				editUserAccount(user);
			break;
		case 6:
			System.out.print("Enter New Client Email: ");
			input = keyboard.nextLine();
			if (Bank.validateEmail(input)) {
				change = true;
				user.setEmail(input);
			} else
				editUserAccount(user);
			break;
		case 7:
			System.out.print("Enter New Client Driver License State: ");
			String state = keyboard.nextLine();
			state = state.toUpperCase().trim();
			if (Bank.validateState(state)) {
				System.out.print("Enter New Client Driver License Number: ");
				String number = keyboard.nextLine();
				number = number.toUpperCase().trim();
				if (Bank.validateLicenseNumber(number)) {
					UserAccount temp = impl.getByDl(state, number);
					if (temp.getId() != null && temp.getId() != user.getId()) {
						System.out.println("Driver License is already in use");
						editUserAccount(user);
					} else {
						System.out.print("Enter Driver License Expiration (##/##/####): ");
						String dlExp = keyboard.nextLine();
						dlExp = dlExp.trim();
						boolean valid = true;
						if (dlExp.length() < 8) {
							valid = false;
						}
						if (valid && !dlExp.contains("/"))
							dlExp = dlExp.substring(0, 2) + "/" + dlExp.substring(2, 4) + "/" + dlExp.substring(4);
						valid = Bank.validateExpiration(dlExp);
						if (valid) {
							change = true;
							user.setDlState(state);
							user.setDlNumber(number);
						} else {
							System.out.println("Invalid License Expiration");
							editUserAccount(user);
						}
					}
				} else
					editUserAccount(user);
			} else
				editUserAccount(user);
			break;
		case 8:
			System.out.print("Enter New Client Social Security Number: ");
			String ssNumber = keyboard.nextLine();
			ssNumber = ssNumber.trim();
			boolean valid = true;
			if (ssNumber.equals(user.getSocialSecurity())) {
				valid = true;
				break;
			}
			if (ssNumber.length() < 8)
				valid = false;
			if (valid && !ssNumber.contains("-"))
				ssNumber = ssNumber.substring(0, 3) + "-" + ssNumber.substring(3, 5) + "-" + ssNumber.substring(5);
			if (valid)
				valid = Bank.validateSocialSecurity(ssNumber);
			if (valid) {
				modifyRequests(user.getSocialSecurity(), ssNumber);
				modifyAccounts(user.getSocialSecurity(), ssNumber);
				change = true;
				user.setSocialSecurity(ssNumber);
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

	private void modifyRequests(String oldSSNumber, String newSSNumber) {
		// TODO Auto-generated method stub
		AccountRequestDAO reqImpl = AccountRequestDAO.getConnection();
		List<AccountRequests.Request> requests = Bank.getAccountRequests();
		boolean modify = false;
		for (AccountRequests.Request request : requests) {
			for (String ss : request.getUserSSNumbers()) {
				if (ss.equals(oldSSNumber)) {
					request.getUserSSNumbers().remove(ss);
					request.getUserSSNumbers().add(newSSNumber);
					modify = true;
				}
				if (modify)
					reqImpl.update(request);
			}
		}
	}
	
	private void modifyAccounts(String oldSSNumber, String newSSNumber) {
		// TODO Auto-generated method stub
		BankAccountDAO bankImpl = BankAccountDAO.getConnection();
		boolean modify = false;
		for (BankAccount account : Bank.getAccounts()) {
			for (String ss : account.getAccountOwners()) {
				if (ss.equals(oldSSNumber)) {
					account.getAccountOwners().remove(ss);
					account.getAccountOwners().add(newSSNumber);
					modify = true;
				}
				if (modify)
					bankImpl.update(account);
			}
		}
	}
	
	private void modifyAccount(BankAccount account, String oldSSNumber, String newSSNumber) {
		// TODO Auto-generated method stub
		BankAccountDAO bankImpl = BankAccountDAO.getConnection();
		for (BankAccount acc : Bank.getAccounts()) {
			if (acc.getAccountNumber().equals(account.getAccountNumber())) {
				acc.getAccountOwners().remove(oldSSNumber);
				acc.getAccountOwners().add(newSSNumber);
				bankImpl.update(acc);
				break;
			}
		}
	}

}
