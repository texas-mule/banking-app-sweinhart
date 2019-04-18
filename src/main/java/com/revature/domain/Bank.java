package com.revature.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import org.apache.log4j.Logger;

import com.revature.service.AccountRequestDbSvcImpl;
import com.revature.service.BankDbSvcImpl;
import com.revature.service.UserAccountDbSvcImpl;

public class Bank {
	static Logger logger = Logger.getLogger(Bank.class);
	private static Integer id;
	private static final String bankName = "Revature Bank";
	private static final Integer routingNumber = 123456789;
	private static Integer nextAccountNumber = 10000000;
	private static UserAccount admin = new UserAccount();
	private static List<UserAccount> employees = new ArrayList<UserAccount>();
	private static List<UserAccount> clients = new ArrayList<UserAccount>();
	private static List<AccountRequest.Request> accountRequests = new ArrayList<AccountRequest.Request>();
	private static List<BankAccount> accounts = new ArrayList<BankAccount>();

	public static void initializeBank() {
		// TODO Auto-generated constructor stub
		BankDbSvcImpl bankDb = BankDbSvcImpl.getInstance();
		bankDb.get(bankName);
	}

	public static List<AccountRequest.Request> getPendingClients() {
		AccountRequestDbSvcImpl impl = AccountRequestDbSvcImpl.getInstance();
		accountRequests = impl.getAll();
		return accountRequests;
	}
	
	public static void addAccountRequest(AccountRequest.Request request) {
		accountRequests.add(request);
		AccountRequestDbSvcImpl impl = AccountRequestDbSvcImpl.getInstance();
		if (!impl.add(request)) {
			System.out.println("Failed to Add Account Request. Exiting System.");
			logger.info("Failed to Add Account Request to DB");
			System.exit(1);
		}
	}

	public static Integer getId() {
		return id;
	}

	public static void setId(Integer id) {
		Bank.id = id;
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

	public static UserAccount getAdmin() {
		return admin;
	}

	public static void setAdmin(UserAccount admin) {
		Bank.admin = admin;
	}

	public static List<UserAccount> getEmployees() {
		return employees;
	}

	public static void setEmployees(List<UserAccount> employees) {
		Bank.employees = employees;
	}

	public static List<UserAccount> getClients() {
		return clients;
	}

	public static void setClients(List<UserAccount> clients) {
		Bank.clients = clients;
	}

	public static List<BankAccount> getAccounts() {
		return accounts;
	}

	public static void setAccounts(List<BankAccount> accounts) {
		Bank.accounts = accounts;
	}

	public static UserAccount createAccount(int accessLvl) {
		// TODO Auto-generated method stub
		logger.info("Creating New User Account");
		UserAccount account = new UserAccount();
		UserAccountDbSvcImpl impl = UserAccountDbSvcImpl.getInstance();
		UserAccount temp = new UserAccount();
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
		Scanner keyboard = new Scanner(System.in);
		System.out.println("\nAccount Setup:");
		boolean valid = true;
		do {
			System.out.print("Create Username: ");
			username = keyboard.nextLine();
			username = username.trim();
			if (username.length() < 6) {
				System.out.println("Username must be at least 6 characters.");
				valid = false;
			}
			temp = impl.get(username);
			if (temp.getId() != null) {
				System.out.println("Username is already taken.");
				valid = false;
			}
		} while (!valid);
		String confirm = "";
		do {
			do {
				System.out.print("Create Password: ");
				password = keyboard.nextLine();
				password = password.trim();
				valid = validatePassword(password);
				if (!valid) {
					System.out.println("Password must be at least 8 characters in length,");
					System.out.println("Contain at least 1 uppercase letter,");
					System.out.println("Contain at least 1 lowercase letter,");
					System.out.println("Contain at least 1 number, and");
					System.out.println("Contain at least 1 special character\n");
				}
			} while (!valid);
			System.out.print("Confirm Password: ");
			confirm = keyboard.nextLine();
			if (!confirm.equals(password)) {
				System.out.println("Passwords do not match. Please try again");
			}
		} while (!confirm.equals(password));
		do {
			valid = true;
			System.out.print("Enter First Name: ");
			firstName = keyboard.nextLine();
			firstName = firstName.trim();
			if (firstName.length() < 2 || !validateLettersOnly(firstName)) {
				System.out.println("Invalid First Name.");
				valid = false;
			}
		} while (!valid);
		do {
			valid = true;
			System.out.print("Enter Last Name: ");
			lastName = keyboard.nextLine();
			lastName = lastName.trim();
			if (lastName.length() < 2 || !validateLettersOnly(lastName)) {
				System.out.println("Invalid Last Name.");
				valid = false;
			}
		} while (!valid);
		do {
			System.out.print("Enter Address1: ");
			address1 = keyboard.nextLine();
			address1 = address1.trim();
			valid = validateAddress(address1);
			if (!valid)
				System.out.println("Invalid Address. Include Apt/Suite numbers in Address2.");
		} while (!valid);
		System.out.print("Enter Address2: ");
		address2 = keyboard.nextLine();
		address2 = address2.trim();
		do {
			valid = true;
			System.out.print("Enter City: ");
			city = keyboard.nextLine();
			city = city.trim();
			if (city.length() < 2 || !validateLettersOnly(city)) {
				System.out.println("Invalid City.");
				valid = false;
			}
		} while (!valid);
		do {
			valid = false;
			System.out.print("Enter State: ");
			state = keyboard.nextLine();
			state = state.toUpperCase().trim();
			States states = new States();
			for (String s : states.getStateAbr()) {
				if (s.equals(state))
					valid = true;
			}
			if (!valid)
				System.out.println("Invalid State. Please Use State Abbreviation.");
		} while (!valid);
		do {
			valid = true;
			System.out.print("Enter ZipCode: ");
			zip = keyboard.nextLine();
			zip = zip.trim();
			String[] zipCode = zip.split("-");
			if (zipCode.length >= 1) {
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
		} while (!valid);
		do {
			valid = true;
			System.out.print("Enter Phone Number: ");
			phone = keyboard.nextLine();
			if (phone.length() == 10) {
				phone = phone.substring(0, 3) + "-" + phone.substring(3, 6) + "-" + phone.substring(6);
			}
			if (phone.length() == 13) {
				if (phone.charAt(0) == 40 && phone.charAt(4) == 41) {
					phone = phone.substring(1, 4) + "-" + phone.substring(5);
				}
			}
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
		} while (!valid);
		do {
			System.out.print("Enter Email Address: ");
			email = keyboard.nextLine();
			email = email.trim();
			if (!validateEmail(email)) {
				System.out.println("Invalid Email Address.");
				valid = false;
			}
		} while (!valid);
		boolean ssExists = false;
		do {
			valid = true;
			System.out.print("Enter Social Security Number: ");
			ssNumber = keyboard.nextLine();
			ssNumber = ssNumber.trim();
			if (!ssNumber.contains("-"))
				ssNumber = ssNumber.substring(0, 3) + "-" + ssNumber.substring(3, 5) + "-" + ssNumber.substring(5);
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
		} while (!valid || ssExists);
		do {
			valid = false;
			System.out.print("Enter Driver License State: ");
			dlState = keyboard.nextLine();
			dlState = dlState.toUpperCase().trim();
			States states = new States();
			for (String s : states.getStateAbr()) {
				if (s.equals(dlState))
					valid = true;
			}
			if (!valid)
				System.out.println("Invalid State. Please Use State Abbreviation.");
		} while (!valid);
		do {
			valid = true;
			System.out.print("Enter Driver License Number: ");
			dlNumber = keyboard.nextLine();
			dlNumber = dlNumber.toUpperCase().trim();
			if (dlNumber.length() < 4 || !validateLettersandNumbersOnly(dlNumber)) {
				System.out.println("Invalid Driver License Number.");
				valid = false;
			}
		} while (!valid);
		Date date = new Date(System.currentTimeMillis());
		do {
			valid = true;
			System.out.print("Enter Driver License Expiration (##/##/####): ");
			dlExp = keyboard.nextLine();
			dlExp = dlExp.trim();
			if (!validateExpiration(dlExp, date)) {
				System.out.println("Invalid Driver License Expiration.");
				valid = false;
			}
		} while (!valid);
		login.setUsername(username);
		login.setPassword(password);
		account.setLogin(login);
		account.setFirstName(firstName);
		account.setLastName(lastName);
		account.setAddress1(address1);
		account.setAddress2(address2);
		account.setCity(city);
		account.setState(state);
		account.setZipcode(zip);
		account.setPhone(phone);
		account.setEmail(email);
		account.setDlState(dlState);
		account.setDlNumber(dlNumber);
		account.setDlExp(dlExp);
		account.setSocialSecurity(ssNumber);
		account.setAccessLvl(accessLvl);
		if (impl.add(account))
			logger.info("User Account Successfully Created");
		else {
			logger.info("User Account Did Not Save");
			System.exit(1);
		}
		keyboard.close();
		return account;
	}

	private static boolean validateExpiration(String dlExp, Date date) {
		// TODO Auto-generated method stub
		String[] dateString = date.toString().split(" ");
		int month = Integer.valueOf(Months.getMonths().get(dateString[1]));
		int day = Integer.valueOf(dateString[2]);
		int year = Integer.valueOf(dateString[5]);
		String[] expString = dlExp.split("/");
		if (expString.length != 3)
			return false;
		if (expString[0].length() != 2 || expString[1].length() != 2 || expString[2].length() != 4)
			return false;
		if (!validateNumbersOnly(expString[0]) || !validateNumbersOnly(expString[1])
				|| !validateNumbersOnly(expString[2]))
			return false;
		if (Integer.valueOf(expString[2]) < year)
			return false;
		if (Integer.valueOf(expString[2]) == year && Integer.valueOf(expString[0]) < month)
			return false;
		if (Integer.valueOf(expString[2]) == year && Integer.valueOf(expString[0]) == month
				&& Integer.valueOf(expString[1]) < day)
			return false;
		return true;
	}

	private static boolean validateAddress(String address1) {
		// TODO Auto-generated method stub
		String[] temp = address1.split(" ");
		if (temp.length < 2)
			return false;
		boolean valid = true;
		if (!validateNumbersOnly(temp[0]))
			return false;
		for (int i = 1; i < temp.length; i++) {
			valid = validateLettersOnly(temp[i]);
			if (!valid)
				return false;
		}
		return valid;
	}

	public static boolean validateLettersOnly(String text) {
		boolean valid = true;
		for (char c : text.toCharArray())
			if (c < 65 || c > 90 && c < 97 || c > 122)
				valid = false;
		return valid;
	}

	public static boolean validateNumbersOnly(String text) {
		boolean valid = true;
		for (char c : text.toCharArray())
			if (c < 48 || c > 57)
				valid = false;
		return valid;
	}

	private static boolean validateLettersandNumbersOnly(String text) {
		boolean valid = true;
		for (char c : text.toCharArray())
			if (c < 48 || c > 57 && c < 65 || c > 90 && c < 97 || c > 122)
				valid = false;
		return valid;
	}

	private static boolean validatePassword(String password) {
		if (password.length() < 8)
			return false;
		String temp = password.toLowerCase();
		if (temp.equals(password))
			return false;
		temp = password.toUpperCase();
		if (temp.equals(password))
			return false;
		boolean valid = false;
		for (char c : password.toCharArray()) {
			if (c >= 48 && c <= 57)
				valid = true;
		}
		if (valid)
			valid = false;
		else
			return false;
		for (char c : password.toCharArray()) {
			if (c >= 33 && c <= 47 || c >= 58 && c <= 64 || c >= 91 && c <= 96 || c >= 123 && c <= 126)
				valid = true;
		}
		return valid;
	}

	public static boolean validateEmail(String email) {
		if (email.contains("@") && email.length() > 6
				&& (email.endsWith(".com") || email.endsWith(".edu") || email.endsWith(".net") || email.endsWith(".org")
						|| email.endsWith(".mil") || email.endsWith(".gov")))
			return true;
		else
			return false;
	}
}
