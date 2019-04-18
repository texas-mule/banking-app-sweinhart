package com.revature.service;

import java.util.List;

import com.revature.domain.AccountRequest.Request;

public interface AccountRequestInterface {
	boolean add(Request request);
	boolean delete(Request request);
	List<Request> getAll();
}
