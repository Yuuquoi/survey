package com.example.survey.vo;

import com.example.survey.constants.RtnCode;

public class BaseRes {
	
	private RtnCode rtnCode;

	public BaseRes() {
		super();
	}

	public BaseRes(RtnCode rtnCode) {
		super();
		this.rtnCode = rtnCode;
	}

	public RtnCode getRtnCode() {
		return rtnCode;
	}

	public void setRtnCode(RtnCode rtnCode) {
		this.rtnCode = rtnCode;
	}
	
}
