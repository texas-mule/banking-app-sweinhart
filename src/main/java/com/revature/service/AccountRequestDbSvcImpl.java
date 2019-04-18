package com.revature.service;

import java.util.List;

import com.revature.domain.AccountRequest.Request;

public class AccountRequestDbSvcImpl implements AccountRequestInterface{
	
	private static AccountRequestDbSvcImpl instance = new AccountRequestDbSvcImpl();

	public static AccountRequestDbSvcImpl getInstance() {
		return instance;
	}

	public boolean add(Request request) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean delete(Request request) {
		// TODO Auto-generated method stub
		return false;
	}

	public List<Request> getAll() {
		// TODO Auto-generated method stub
		return null;
	}

}
