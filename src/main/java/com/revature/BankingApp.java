package com.revature.bankingApp;

import com.revature.bankingApp.domain.Bank;
import java.util.Scanner;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

/**
 * Banking App
 *
 */
public class BankingApp {
	static Logger logger = Logger.getLogger(BankingApp.class);

	public static void main(String[] args) {
		Bank.initializeBank();
		BasicConfigurator.configure();
		Scanner keyboard = new Scanner(System.in);		
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
				if (Bank.getId() == null) {
					System.out.println("Admin Setup");
					Bank.setAdmin(Bank.createAccount(3));
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
