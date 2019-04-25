package com.revature.domain;

import java.util.ArrayList;
import java.util.List;

import com.revature.service.AccountRequestDbSvcImpl;

public class UserAccount implements Comparable<UserAccount>{
	private Integer id;
	private Integer accessLvl;
	private String firstName;
	private String lastName;
	private String address1;
	private String address2;
	private String city;
	private String state;
	private String zipcode;
	private String phone;
	private String email;
	private Login login;
	private String socialSecurity;
	private String dlState;
	private String dlNumber;
	private String dlExp;
	private List<BankAccount> accounts = new ArrayList<BankAccount>();
	private AccountRequest pendingRequests = new AccountRequest();

	public Integer getAccessLvl() {
		return accessLvl;
	}

	public void setAccessLvl(Integer accessLvl) {
		this.accessLvl = accessLvl;
	}

	public AccountRequest getPendingRequests() {
		if (this.getId() != null) {
			AccountRequestDbSvcImpl impl = AccountRequestDbSvcImpl.getInstance();
			List<AccountRequest.Request> requests = impl.getAll();
			AccountRequest userRequests = new AccountRequest();
			for (AccountRequest.Request request : requests) {
				for (String id : request.getUserSSNumbers()) {
					if (id.equals(this.getSocialSecurity())) {
						userRequests.addAccountRequest(request);
					}
				}
			}
			pendingRequests = userRequests;
		}
		return pendingRequests;

	}

	public void setPendingRequests(AccountRequest pendingRequests) {
		this.pendingRequests = pendingRequests;
	}

	public String getDlState() {
		return dlState;
	}

	public void setDlState(String dlState) {
		this.dlState = dlState;
	}

	public String getDlNumber() {
		return dlNumber;
	}

	public void setDlNumber(String dlNumber) {
		this.dlNumber = dlNumber;
	}

	public String getDlExp() {
		return dlExp;
	}

	public void setDlExp(String dlExp) {
		this.dlExp = dlExp;
	}

	public String getSocialSecurity() {
		return socialSecurity;
	}

	public void setSocialSecurity(String socialSecurity) {
		this.socialSecurity = socialSecurity;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getAddress1() {
		return address1;
	}

	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	public String getAddress2() {
		return address2;
	}

	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getZipcode() {
		return zipcode;
	}

	public void setZipCode(String zipcode) {
		this.zipcode = zipcode;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Login getLogin() {
		return login;
	}

	public void setLogin(Login login) {
		this.login = login;
	}

	public List<BankAccount> getAccounts() {
		return accounts;
	}

	public void setAccounts(List<BankAccount> accounts) {
		this.accounts = accounts;
	}

	public int compareTo(UserAccount o) {
		// TODO Auto-generated method stub
		String name1 = lastName + ", " + firstName;
		String name2 = o.getLastName() + ", " + o.getFirstName();
		return name1.compareTo(name2);
	}
}
