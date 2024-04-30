package com.example.survey.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "login")
public class Login {

	@Id
	@Column(name = "account")
	private String account;
	
	@Column(name = "password")
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
