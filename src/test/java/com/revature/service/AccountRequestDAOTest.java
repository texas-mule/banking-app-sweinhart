package com.revature.service;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.revature.domain.AccountRequests;
import com.revature.domain.AccountRequests.Request;

public class AccountRequestDAOTest {
	AccountRequests accountRequests = new AccountRequests();
	Request newRequest;
	String ss1 = "123-45-6789";
	String ss2 = "234-56-7890";
	String ss3 = "345-67-8901";
	
	@Before
	public void init() {
		newRequest = accountRequests.new Request();
		newRequest.addUserSSNumber(ss1);
		newRequest.addUserSSNumber(ss2);
		newRequest.setAccountType(accountRequests.checking);
		newRequest.setDeposit(200.0);
	}
	
	@Test
	public void testAccountRequests() {
		AccountRequestDAO impl = AccountRequestDAO.getConnection();
		impl.add(newRequest);
		Request request = accountRequests.new Request();
		List<AccountRequests.Request> list = new ArrayList<AccountRequests.Request>();
		list = impl.getAll();
		boolean test = true;
		if (list.size() > 0)
			test = false;
		assertFalse(test);
		boolean addRequest;
		for (Request r : list) {
			addRequest = false;
			for (String ss : r.getUserSSNumbers()) {
				if (ss.contentEquals(ss1) || ss.contentEquals(ss2))
					addRequest = true;
			}
			if (addRequest)
				request = r;
		}
		if (request.getId() == null)
			test = false;
		else
			test = true;
		assertTrue(test);
		request.getUserSSNumbers().clear();
		request.getUserSSNumbers().add(ss3);
		test = impl.update(request);
		assertTrue(test);
		list = impl.getAll();
		for (Request r : list) {
			addRequest = false;
			for (String ss : r.getUserSSNumbers()) {
				if (ss.contentEquals(ss3))
					addRequest = true;
			}
			if (addRequest)
				request = r;
		}
		if (request.getId() == null)
			test = false;
		else
			test = true;
		assertTrue(test);
		test = impl.delete(request);
		assertTrue(test);
	}
}
