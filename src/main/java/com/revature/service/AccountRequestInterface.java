package com.revature.bankingApp.service;

import java.util.List;

import com.revature.bankingApp.domain.AccountRequest.Request;

public interface AccountRequestInterface {
	boolean add(Request request);
	boolean delete(Request request);
	List<Request> getAll();
}
