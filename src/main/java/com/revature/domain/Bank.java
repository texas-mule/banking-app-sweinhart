package com.revature.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import org.apache.log4j.Logger;
import com.revature.service.AccountRequestDAO;
import com.revature.service.BankAccountDAO;
import com.revature.service.TransactionDAO;
import com.revature.service.UserAccountDAO;

public class Bank {
	static Logger logger = Logger.getLogger(Bank.class);
	private static final String bankName = "Revature Bank";
	private static final Integer routingNumber = 123456789;
	private static Integer nextAccountNumber = 10000000;
	private static List<UserAccount> userAccounts = new ArrayList<UserAccount>();
	private static List<AccountRequest.Request> accountRequests = new ArrayList<AccountRequest.Request>();
	private static List<BankAccount> bankAccounts = new ArrayList<BankAccount>();
	private static Scanner keyboard;
	private static States states = new States();

	public static void initializeBank() {
		// TODO Auto-generated constructor stub
		logger.info("Initializing Bank");
		AccountRequestDAO requestImpl = AccountRequestDAO.getConnection();
		BankAccountDAO accountImpl = BankAccountDAO.getConnection();
		TransactionDAO transImpl = TransactionDAO.getConnection();
		UserAccountDAO userImpl = UserAccountDAO.getConnection();
		accountRequests = requestImpl.getAll();
		UserAccount userAccount = new UserAccount();
		for (AccountRequest.Request request : accountRequests) {
			for (String id : request.getUserSSNumbers()) {
				userAccount = userImpl.getBySs(id);
				AccountRequest userRequest = new AccountRequest();
				userRequest = userAccount.getPendingRequests();
				userRequest.addAccountRequest(request);
				userAccount.setPendingRequests(userRequest);

			}
		}
		bankAccounts = accountImpl.getAll();
		int accountNumber = 10000000;
		for (BankAccount account : bankAccounts) {
			List<Transaction> transactions = transImpl.getAll(account.getAccountNumber());
			for (Transaction transaction : transactions)
				account.setBalance(account.getBalance() + transaction.getTransactionAmount());
			account.setTransactions(transImpl.getAll(account.getAccountNumber()));
			for (String ss : account.getAccountOwners()) {
				userAccount = userImpl.getBySs(ss);
				userAccount.getAccounts().add(account);
			}
			if (account.getAccountNumber() > accountNumber)
				accountNumber = account.getAccountNumber();
		}
		nextAccountNumber = accountNumber;
		List<UserAccount> users = new ArrayList<UserAccount>();
		users = userImpl.getAll();
		for (UserAccount user : users) {
			if (!user.getLogin().getUsername().equals("BankAdmin")) {
				for (BankAccount account : bankAccounts) {
					for (String ss : account.getAccountOwners())
						if (user.getSocialSecurity().equals(ss))
							user.getAccounts().add(account);
				}
				userAccounts.add(user);
			}
		}
	}

	public static List<AccountRequest.Request> getAccountRequests() {
		AccountRequestDAO impl = AccountRequestDAO.getConnection();
		accountRequests = impl.getAll();
		Collections.sort(accountRequests);
		return accountRequests;
	}

	public static void addAccountRequest(AccountRequest.Request request) {
		accountRequests.add(request);
		AccountRequestDAO impl = AccountRequestDAO.getConnection();
		if (!impl.add(request)) {
			System.out.println("Failed to Add Account Request. Exiting System.");
			logger.info("Failed to Add Account Request to DB");
			System.exit(1);
		}
	}
	
	public static void deleteAccount(BankAccount account) {
		// TODO Auto-generated method stub
		bankAccounts.remove(account);
		for (UserAccount user : userAccounts)
			for (BankAccount acc : user.getAccounts()) {
				if (acc.getAccountNumber().equals(account.getAccountNumber())) {
					user.getAccounts().remove(account);
					break;
				}
			}
		TransactionDAO transImpl = TransactionDAO.getConnection();
		transImpl.delete(account.getAccountNumber());
		BankAccountDAO impl = BankAccountDAO.getConnection();
		impl.delete(account);
	}

	public static String getBankName() {
		return bankName;
	}

	public static Integer getRoutingNumber() {
		return routingNumber;
	}

	public static Integer getNextAccountNumber() {
		return nextAccountNumber;
	}

	public static List<UserAccount> getUserAccounts() {
		return userAccounts;
	}

	public static void setUserAccounts(List<UserAccount> userAccounts) {
		Bank.userAccounts = userAccounts;
	}

	public static List<BankAccount> getAccounts() {
		return bankAccounts;
	}

	public static BankAccount assignAccountNumber(BankAccount account) {
		nextAccountNumber++;
		account.setAccountNumber(nextAccountNumber);
		return account;
	}

	public static void addAccount(BankAccount account) {
		bankAccounts.add(account);
	}

	public static void setAccounts(List<BankAccount> accounts) {
		Bank.bankAccounts = accounts;
	}

	public static UserAccount createUserAccount(int accessLvl) {
		// TODO Auto-generated method stub
		logger.info("Creating New User Account");
		keyboard = new Scanner(System.in);
		UserAccount account = new UserAccount();
		UserAccountDAO impl = UserAccountDAO.getConnection();
		Login login = new Login();
		String firstName = "";
		String lastName = "";
		String address1 = "";
		String address2 = "";
		String city = "";
		String state = "";
		String zip = "";
		String phone = "";
		String username = "";
		String password = "";
		String email = "";
		String dlState = "";
		String dlNumber = "";
		String dlExp = "";
		String ssNumber = "";
		keyboard = new Scanner(System.in);
		System.out.println("\nAccount Setup");
		boolean valid = true;
		do {
			System.out.print("Create Username: ");
			username = keyboard.nextLine();
			username = username.trim();
			valid = validateUsername(username);
		} while (!valid);
		String confirm = "";
		do {
			do {
				System.out.print("Create Password: ");
				password = keyboard.nextLine();
				password = password.trim();
				valid = validatePassword(password);
			} while (!valid);
			System.out.print("Confirm Password: ");
			confirm = keyboard.nextLine();
			if (!confirm.equals(password)) {
				System.out.println("Passwords do not match. Please try again");
			}
		} while (!confirm.equals(password));
		do {
			System.out.print("Enter First Name: ");
			firstName = keyboard.nextLine();
			firstName = firstName.trim();
			String[] name = { firstName.substring(0, 1), firstName.substring(1) };
			firstName = name[0].toUpperCase() + name[1].toLowerCase();
			valid = validateName(firstName);
		} while (!valid);
		do {

			System.out.print("Enter Last Name: ");
			lastName = keyboard.nextLine();
			lastName = lastName.trim();
			String[] name = { lastName.substring(0, 1), lastName.substring(1) };
			lastName = name[0].toUpperCase() + name[1].toLowerCase();
			valid = validateName(lastName);
		} while (!valid);
		do {
			System.out.print("Enter Address1: ");
			address1 = keyboard.nextLine();
			address1 = address1.trim();
			String[] address = address1.split(" ");
			address1 = address[0] + " ";
			for (int i = 1; i < address.length; i++) {
				address[i] = address[i].substring(0, 1).toUpperCase() + address[i].substring(1).toLowerCase();
				address1 += address[i] + " ";
			}
			address1 = address1.trim();
			valid = validateAddress(address1);				
		} while (!valid);
		do {
			System.out.print("Enter Address2: ");
			address2 = keyboard.nextLine();
			address2 = address2.trim();
			valid = validateLettersAndNumbersOnly(address2);
			if (!valid)
				System.out.println("Only Letters and Numbers are allowed");
		} while (!valid);
		do {
			System.out.print("Enter City: ");
			city = keyboard.nextLine();
			city = city.trim();
			String[] input = { city.substring(0, 1), city.substring(1) };
			city = input[0].toUpperCase() + input[1].toLowerCase();
			valid = validateCity(city);
		} while (!valid);
		do {
			System.out.print("Enter State: ");
			state = keyboard.nextLine();
			state = state.toUpperCase().trim();
			valid = validateState(state);
		} while (!valid);
		do {
			System.out.print("Enter ZipCode: ");
			zip = keyboard.nextLine();
			zip = zip.trim();
			valid = validateZip(zip);
		} while (!valid);
		do {
			System.out.print("Enter Phone Number: ");
			phone = keyboard.nextLine();
			phone = modifyPhone(phone);
			valid = validatePhone(phone);
		} while (!valid);
		do {
			System.out.print("Enter Email Address: ");
			email = keyboard.nextLine();
			email = email.trim();
			valid = validateEmail(email);
		} while (!valid);
		do {
			System.out.print("Enter Social Security Number: ");
			ssNumber = keyboard.nextLine();
			ssNumber = ssNumber.trim();
			valid = true;
			if (ssNumber.length() < 8)
				valid = false;
			if (valid && !ssNumber.contains("-"))
				ssNumber = ssNumber.substring(0, 3) + "-" + ssNumber.substring(3, 5) + "-" + ssNumber.substring(5);
			if (valid)
				valid = validateSocialSecurity(ssNumber);
		} while (!valid);
		do {
			System.out.print("Enter Driver License State: ");
			dlState = keyboard.nextLine();
			dlState = dlState.toUpperCase().trim();
			valid = validateState(dlState);
		} while (!valid);
		do {
			System.out.print("Enter Driver License Number: ");
			dlNumber = keyboard.nextLine();
			dlNumber = dlNumber.toUpperCase().trim();
			valid = validateLicenseNumber(dlNumber);
			if (valid) {
				UserAccount temp = impl.getByDl(dlState, dlNumber);
				if (temp.getId() != null) {
					System.out.println("Driver License is already in use");
					valid = false;
				}
			}
		} while (!valid);
		do {
			System.out.print("Enter Driver License Expiration (##/##/####): ");
			dlExp = keyboard.nextLine();
			dlExp = dlExp.trim();
			if (dlExp.length() < 8)
				valid = false;
			if (valid && !dlExp.contains("/"))
				dlExp = dlExp.substring(0, 2) + "/" + dlExp.substring(2, 4) + "/" + dlExp.substring(4);
			valid = validateExpiration(dlExp);
		} while (!valid);
		account.setAccessLvl(accessLvl);
		login.setUsername(username);
		login.setPassword(password, accessLvl);
		account.setLogin(login);
		account.setFirstName(firstName);
		account.setLastName(lastName);
		account.setAddress1(address1);
		account.setAddress2(address2);
		account.setCity(city);
		account.setState(state);
		account.setZipCode(zip);
		account.setPhone(phone);
		account.setEmail(email);
		account.setDlState(dlState);
		account.setDlNumber(dlNumber);
		account.setDlExp(dlExp);
		account.setSocialSecurity(ssNumber);
		if (impl.add(account)) {
			logger.info("User Account Successfully Created");
			account = impl.getBySs(account.getSocialSecurity());
			Bank.getUserAccounts().add(account);
		}
		else {
			logger.info("User Account Did Not Save");
			System.exit(1);
		}
		return account;
	}

	public static boolean validateLicenseNumber(String dlNumber) {
		// TODO Auto-generated method stub
		boolean valid = true;
		if (dlNumber.length() < 4 || !validateLettersAndNumbersOnly(dlNumber)) {
			System.out.println("Invalid Driver License Number.");
			valid = false;
		}
		return valid;
	}

	public static boolean validatePhone(String phone) {
		// TODO Auto-generated method stub
		boolean valid = true;
		if (phone.length() == 12) {
			String[] digits = phone.split("-");
			if (!validateNumbersOnly(digits[0]))
				valid = false;
			if (!validateNumbersOnly(digits[1]))
				valid = false;
			if (!validateNumbersOnly(digits[2]))
				valid = false;
		} else {
			valid = false;
		}
		if (!valid) {
			System.out.println("Invalid Phone Number.");
		}
		return valid;
	}

	public static String modifyPhone(String phone) {
		// TODO Auto-generated method stub
		if (phone.length() == 10) {
			phone = phone.substring(0, 3) + "-" + phone.substring(3, 6) + "-" + phone.substring(6);
		}
		if (phone.length() == 13) {
			if (phone.charAt(0) == 40 && phone.charAt(4) == 41) {
				phone = phone.substring(1, 4) + "-" + phone.substring(5);
			}
		}
		return phone;
	}

	public static boolean validateZip(String zip) {
		// TODO Auto-generated method stub
		boolean valid = true;
		String[] zipCode = zip.split("-");
		if (zipCode.length >= 1 && zipCode.length <= 2) {
			if (!validateNumbersOnly(zipCode[0]) || zipCode[0].length() != 5)
				valid = false;
			if (zipCode.length == 2) {
				if (!validateNumbersOnly(zipCode[1]) || zipCode[1].length() != 4)
					valid = false;
			}
		} else
			valid = false;
		if (!valid)
			System.out.println("Invalid ZipCode.");
		return valid;
	}

	public static boolean validateState(String state) {
		// TODO Auto-generated method stub
		boolean valid = false;
		for (String s : states.getStateAbr()) {
			if (s.equals(state))
				valid = true;
		}
		if (!valid)
			System.out.println("Invalid State. Please Use State Abbreviation.");
		return valid;
	}

	public static boolean validateCity(String city) {
		// TODO Auto-generated method stub
		boolean valid = true;
		if (city.length() < 2 || !validateLettersOnly(city)) {
			System.out.println("Invalid City.");
			valid = false;
		}
		return valid;
	}

	public static boolean validateName(String name) {
		// TODO Auto-generated method stub
		boolean valid = true;
		if (name.length() < 2 || !validateLettersOnly(name)) {
			System.out.println("Invalid Name.");
			valid = false;
		}
		return valid;
	}

	public static boolean validateUsername(String username) {
		// TODO Auto-generated method stub
		UserAccountDAO impl = UserAccountDAO.getConnection();
		UserAccount temp = new UserAccount();
		boolean valid = true;
		if (username.length() < 5) {
			System.out.println("Username must be at least 5 characters.");
			valid = false;
		}
		if (!validateLettersAndNumbersOnly(username)) {
			System.out.println("Username may not contain special characters.");
			valid = false;
		}
		temp = impl.getByUsername(username);
		if (temp.getId() != null) {
			System.out.println("Username is already taken.");
			valid = false;
		}
		return valid;
	}

	public static boolean validateSocialSecurity(String ssNumber) {
		// TODO Auto-generated method stub
		UserAccountDAO impl = UserAccountDAO.getConnection();
		UserAccount temp = new UserAccount();
		boolean ssExists = false;
		boolean valid = true;
		String[] ss = ssNumber.split("-");
		if (ss.length != 3)
			valid = false;
		if (valid && (!validateNumbersOnly(ss[0]) || !validateNumbersOnly(ss[1]) || !validateNumbersOnly(ss[2])))
			valid = false;
		if (!valid)
			System.out.println("Invalid Social Security Number.");
		if (valid) {
			temp = impl.getBySs(ssNumber);
			if (temp.getId() != null) {
				ssExists = true;
				System.out.println("Social Security Number is in Use.");
			}
		}
		if (valid == false || ssExists == true)
			return false;
		else
			return true;
	}

	public static boolean validateExpiration(String dlExp) {
		// TODO Auto-generated method stub
		boolean valid = true;
		Date date = new Date();
		String[] dateString = date.toString().split(" ");
		int month = Integer.valueOf(Months.getMonths().get(dateString[1]));
		int day = Integer.valueOf(dateString[2]);
		int year = Integer.valueOf(dateString[5]);
		String[] expString = dlExp.split("/");
		if (expString.length != 3)
			return false;
		if (expString[0].length() != 2 || expString[1].length() != 2 || expString[2].length() != 4)
			valid = false;
		if (!validateNumbersOnly(expString[0]) || !validateNumbersOnly(expString[1])
				|| !validateNumbersOnly(expString[2]))
			valid = false;
		if (Integer.valueOf(expString[2]) < year)
			valid = false;
		if (Integer.valueOf(expString[2]) == year && Integer.valueOf(expString[0]) < month)
			valid = false;
		if (Integer.valueOf(expString[2]) == year && Integer.valueOf(expString[0]) == month
				&& Integer.valueOf(expString[1]) < day)
			valid = false;
		if (!valid) {
			System.out.println("Invalid Driver License Expiration.");
			valid = false;
		}
		return valid;
	}

	public static boolean validateAddress(String address1) {
		// TODO Auto-generated method stub
		String[] temp = address1.split(" ");
		if (temp.length < 2)
			return false;
		boolean valid = true;
		if (!validateNumbersOnly(temp[0]))
			return false;
		for (int i = 1; i < temp.length; i++) {
			valid = validateLettersOnly(temp[i]);
			if (!valid) {
				System.out.println("Invalid Address. Include Apt/Suite numbers in Address2.");
				return false;
			}
		}
		return valid;
	}

	public static boolean validateLettersOnly(String text) {
		boolean valid = true;
		if (text.contentEquals(""))
			valid = false;
		for (char c : text.toCharArray())
			if (c < 65 || c > 90 && c < 97 || c > 122)
				valid = false;
		return valid;
	}

	public static boolean validateNumbersOnly(String text) {
		boolean valid = true;
		if (text.contentEquals(""))
			valid = false;
		for (char c : text.toCharArray())
			if (c < 48 || c > 57)
				valid = false;
		return valid;
	}

	public static boolean validateLettersAndNumbersOnly(String text) {
		boolean valid = true;
		if (text.contentEquals(""))
			valid = false;
		for (char c : text.toCharArray())
			if (c < 48 || c > 57 && c < 65 || c > 90 && c < 97 || c > 122)
				valid = false;
		return valid;
	}

	public static boolean validatePassword(String password) {
		boolean check1 = true;
		boolean check2 = false;
		boolean check3 = false;
		if (password.length() < 8)
			check1 = false;
		String temp = password.toLowerCase();
		if (temp.equals(password))
			check1 = false;
		temp = password.toUpperCase();
		if (temp.equals(password))
			check1 = false;
		for (char c : password.toCharArray()) {
			if (c >= 48 && c <= 57)
				check2 = true;
		}
		for (char c : password.toCharArray()) {
			if (c >= 33 && c <= 47 || c >= 58 && c <= 64 || c >= 91 && c <= 96 || c >= 123 && c <= 126)
				check3 = true;
		}
		if (!check1 || !check2 || !check3) {
			System.out.println("Password must be at least 8 characters in length,");
			System.out.println("Contain at least 1 uppercase letter,");
			System.out.println("Contain at least 1 lowercase letter,");
			System.out.println("Contain at least 1 number, and");
			System.out.println("Contain at least 1 special character\n");
			return false;
		}
		return true;
	}

	public static boolean validateEmail(String email) {
		String [] emailArray = email.split("@");
		if (emailArray.length != 2)
			return false;
		if (!validateLettersAndNumbersOnly(emailArray[0]))
			return false;
		String [] emailArray2 = emailArray[1].split("\\.");
		if (!validateLettersAndNumbersOnly(emailArray2[0]))
			return false;
		if (emailArray2.length != 2)
			return false;
		if ((email.endsWith(".com") || email.endsWith(".edu") || email.endsWith(".net") || email.endsWith(".org")
						|| email.endsWith(".mil") || email.endsWith(".gov")))
			return true;
		else {
			System.out.println("Invalid Email Address.");
			return false;
		}
	}
}
