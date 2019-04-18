package com.revature.bankingApp.domain;

import java.util.Calendar;
import java.util.Date;

public class Transaction implements Comparable<Transaction>{
	public static final String transfer = "Funds Transfer";
	public static final String withdrawl = "Withdrawl";
	public static final String deposit = "Deposit";
	private Integer id;
	private Integer accountNumber;
	private Date dateInstance;
	private String date;
	private String time;
	private String transactionType;
	private Double transactionAmount;
	
	
	public Transaction() {
		super();
		// TODO Auto-generated constructor stub
		setDateInstance();
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(Integer accountNumber) {
		this.accountNumber = accountNumber;
	}
	
	public String getTransactionType() {
		return transactionType;
	}
	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

	public Double getTransactionAmount() {
		return transactionAmount;
	}
	public void setTransactionAmount(Double transactionAmount) {
		this.transactionAmount = transactionAmount;
	}
	public String getDate() {
		return date;
	}
	
	public String getTime() {
		return time;
	}

	public void setDateInstance() {
		dateInstance = new Date();
		String dateString = dateInstance.toString();
		String[] dateStringArray = dateString.toString().split(" ");
		int month = Integer.valueOf(Months.getMonths().get(dateStringArray[1]));
		int day = Integer.valueOf(dateStringArray[2]);
		int year = Integer.valueOf(dateStringArray[5]);
		date = month + "/" + day + "/" + year;
		time = dateStringArray[3];
	}
	
	public int compareTo(Transaction t) {
		// TODO Auto-generated method stub
		Calendar cal1 = Calendar.getInstance();
		Calendar cal2 = Calendar.getInstance();
		String [] timeArray1 = time.split(":"); 
		String [] timeArray2 = t.getTime().split(":");
		String [] dateArray1 = date.split("/"); 
		String [] dateArray2 = t.getDate().split("/");
		cal1.set(Integer.valueOf(dateArray1[2]), Integer.valueOf(dateArray1[0]), Integer.valueOf(dateArray1[1]), 
				Integer.valueOf(timeArray1[0]), Integer.valueOf(timeArray1[1]), Integer.valueOf(timeArray1[2]));
		cal2.set(Integer.valueOf(dateArray2[2]), Integer.valueOf(dateArray2[0]), Integer.valueOf(dateArray2[1]), 
				Integer.valueOf(timeArray2[0]), Integer.valueOf(timeArray2[1]), Integer.valueOf(timeArray2[2]));
		Long time1 = cal1.getTimeInMillis();
		Long time2 = cal2.getTimeInMillis();
		return time1.compareTo(time2);
	}	
	
}
