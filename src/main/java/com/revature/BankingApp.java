package com.revature;

import com.revature.domain.Bank;
import com.revature.domain.Login;
import com.revature.domain.UserAccount;
import com.revature.service.UserAccountDbSvcImpl;

import java.io.IOException;
import java.util.Scanner;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Layout;
import org.apache.log4j.PatternLayout;

/**
 * Banking App
 *
 */
public class BankingApp {
	private static Logger logger = Logger.getLogger(BankingApp.class);
	private static Scanner keyboard;

	public static void main(String[] args) {
		FileAppender fileAppender = new FileAppender();
		Layout layout = new PatternLayout("%-5p [%t]: %m%n");
		try {
			fileAppender.setFile("log.txt", false, false, 8);
			fileAppender.setLayout(layout);
			BasicConfigurator.configure(fileAppender);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error("Logger Output File Error: " + e.getMessage());
			e.printStackTrace();
		}
		logger.addAppender(fileAppender);
		Bank.initializeBank();
		keyboard = new Scanner(System.in);
		logger.info("Banking Application Started");
		if (args.length > 0) {
			if (args[0].equals("admin")) {
				UserAccountDbSvcImpl impl = UserAccountDbSvcImpl.getInstance();
				UserAccount admin = impl.getByUsername("BankAdmin");
				// TemporaryPassword = BankAdmin1234!
				if (admin.getId() == null) {
					Login login = new Login();
					login.setUsername("BankAdmin");
					login.setPassword("BankAdmin1234!", 3);
					admin.setLogin(login);
					admin.setFirstName("placeholder");
					admin.setLastName("placeholder");
					admin.setAddress1("Placeholder");
					admin.setAddress2("Placeholder");
					admin.setCity("placeholder");
					admin.setState("placeholder");
					admin.setZipCode("placeholder");
					admin.setPhone("placeholder");
					admin.setEmail("placeholder");
					admin.setSocialSecurity("placeholder");
					admin.setDlState("placeholder");
					admin.setDlNumber("placeholder");
					admin.setDlExp("placeholder");
					impl.add(admin);
					admin = impl.getByUsername("BankAdmin");
				}
				System.out.print("Enter Admin Password: ");
				String password = keyboard.nextLine();
				if (password.equals("BankAdmin1234!")
						&& Login.getPasswordAccessLvlMatch("BankAdmin1234!", admin.getLogin().getPassword()) == 3) {
					System.out.println("Admin Setup");
					UserAccount newAdmin = new UserAccount();
					newAdmin = Bank.createUserAccount(3);
					admin.getLogin().setPassword(newAdmin.getLogin().getPassword());
					impl.update(admin);
					System.out.println("Admin Setup Completed. Please Log In.\n");
				} else {
					if (Login.getPasswordAccessLvlMatch(password, admin.getLogin().getPassword()) == 3) {
						System.out.println("Admin Account Already Assigned - Please Log In");
					} else
						System.out.println("Incorrect Password");
				}
			} else {
				System.out.println("Invalid arguments");
				logger.info("Invalid arguments. Exiting Application!");
				System.exit(0);
			}
		}
		// Login Menu
		LoginMenu menu = new LoginMenu();
		menu.displayMenu();
		logger.info("Application completed successfully");
	}

}
