package com.revature.domain;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import org.junit.Test;

import com.revature.domain.AccountRequest.Request;

public class AccountRequestTest {
	@Test
	public void testAccountRequest() {
		Date dateInstance = new Date();
		String dateString = dateInstance.toString();
		String[] dateStringArray = dateString.toString().split(" ");
		int month = Integer.valueOf(Months.getMonths().get(dateStringArray[1]));
		int day = Integer.valueOf(dateStringArray[2]);
		int year = Integer.valueOf(dateStringArray[5]);
		String date = month + "/" + day + "/" + year;
		String time = dateStringArray[3];
		AccountRequest accountRequest = new AccountRequest();
		Request testRequest = accountRequest.new Request();
		String ss = "123-45-6789";
		String type = "checking";
		testRequest.addUserSSNumber(ss);
		testRequest.setAccountType(type);
		testRequest.setDeposit(1.0);
		Request newRequest = testRequest;
		assertEquals(testRequest, newRequest);
		assertEquals((Double) 1.0, newRequest.getDeposit());
		assertEquals(ss, newRequest.getUserSSNumbers().get(0));
		assertEquals(type, newRequest.getAccountType());
		assertEquals(date, newRequest.getDate());
		assertEquals(time, newRequest.getTime());
	}
}
