package com.revature.domain;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class AccountRequest {
	public String checking = "Checking";
	public String saving = "Savings";
	private List<Request> accountRequests = new ArrayList<Request>();		

	public List<Request> getAccountRequests() {
		Collections.sort(accountRequests);
		return accountRequests;
	}

	public void addAccountRequest(Request accountRequest) {
		this.accountRequests.add(accountRequest);
	}

	public class Request implements Comparable<Request>{
		private Integer id;
		private String accountType;
		private Double deposit;
		private Date dateInstance = new Date();
		private String date;
		private String time;
		private List<Integer> userIds = new ArrayList<Integer>();
		
		public Request() {
			// TODO Auto-generated constructor stub
			setDateInstance();
		}
		
		public Integer getId() {
			return id;
		}

		public void setId(Integer id) {
			this.id = id;
		}

		public String getDate() {
			return date;
		}
		public void setDate(String date) {
			this.date = date;
		}
		public String getTime() {
			return time;
		}
		public void setTime(String time) {
			this.time = time;
		}
		
		public List<Integer> getUserIds() {
			return userIds;
		}
		public void addUserId(Integer id) {
			userIds.add(id);
		}		
		public void delUserId(Integer id) {
			userIds.remove(id);
		}
		public String getAccountType() {
			return accountType;
		}
		public void setAccountType(String accountType) {
			this.accountType = accountType;
		}
		public Double getDeposit() {
			return deposit;
		}
		public void setDeposit(Double deposit) {
			this.deposit = deposit;
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
		public int compareTo(Request o) {
			// TODO Auto-generated method stub
			Calendar cal1 = Calendar.getInstance();
			Calendar cal2 = Calendar.getInstance();
			String [] timeArray1 = time.split(":"); 
			String [] timeArray2 = o.getTime().split(":");
			String [] dateArray1 = date.split("/"); 
			String [] dateArray2 = o.getDate().split("/");
			cal1.set(Integer.valueOf(dateArray1[2]), Integer.valueOf(dateArray1[0]), Integer.valueOf(dateArray1[1]), 
					Integer.valueOf(timeArray1[0]), Integer.valueOf(timeArray1[1]), Integer.valueOf(timeArray1[2]));
			cal2.set(Integer.valueOf(dateArray2[2]), Integer.valueOf(dateArray2[0]), Integer.valueOf(dateArray2[1]), 
					Integer.valueOf(timeArray2[0]), Integer.valueOf(timeArray2[1]), Integer.valueOf(timeArray2[2]));
			Long time1 = cal1.getTimeInMillis();
			Long time2 = cal2.getTimeInMillis();
			return time1.compareTo(time2);
		}
		
	}
}
