package com.revature;

import com.revature.domain.Bank;
import com.revature.domain.UserAccount;

import java.io.IOException;
import java.util.Scanner;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.apache.log4j.SimpleLayout;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Layout;

/**
 * Banking App
 *
 */
public class BankingApp {
	private static Logger logger = Logger.getLogger(BankingApp.class);
	private static Scanner keyboard;

	public static void main(String[] args) {
		FileAppender fileAppender = new FileAppender();
		Layout layout = new SimpleLayout();
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
				System.out.print("Enter Admin Password: ");
				String password = keyboard.nextLine();
				if (!password.equals("BankAdmin1234!")) {
					System.out.println("Invalid Password. Exiting Application\n");
					logger.info("Invalid password. Exiting Application!");
					keyboard.close();
					System.exit(0);
				}
				boolean adminExists = false;
				for (UserAccount account : Bank.getUserAccounts()) {
					if (account.getAccessLvl() == 3)
						adminExists = true;
				}
				if (!adminExists) {
					System.out.println("Admin Setup");
					Bank.getUserAccounts().add(Bank.createUserAccount(3));
					System.out.println("Admin Setup Completed. Please Log In.\n");
				} else {
					System.out.print("Admin Account Already Assigned - Please Log In");
				}
			} else {
				System.out.println("Invalid arguments");
				logger.info("Invalid arguments. Exiting Application!");
				System.exit(0);
			}
		}
		//Login Menu
		LoginMenu menu = new LoginMenu();		
		menu.displayMenu();
		logger.info("Application completed successfully");
	}

	

}
