package com.example.survey.entity;

import javax.persistence.Entity;

@Entity
public class Login {

	private String account;
	
	private String pw;

	public Login() {
		super();
	}

	public Login(String account, String pw) {
		super();
		this.account = account;
		this.pw = pw;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getPw() {
		return pw;
	}

	public void setPw(String pw) {
		this.pw = pw;
	}
	
	
}
