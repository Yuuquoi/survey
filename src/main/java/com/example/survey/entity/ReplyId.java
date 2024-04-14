package com.example.survey.entity;

import java.io.Serializable;

@SuppressWarnings("serial")
public class ReplyId implements Serializable{
	
	private int surveyNo;

	private String phone;

	public ReplyId() {
		super();
	}

	public ReplyId(int surveyNo, String phone) {
		super();
		this.surveyNo = surveyNo;
		this.phone = phone;
	}

	public int getSurveyNo() {
		return surveyNo;
	}

	public void setSurveyNo(int surveyNo) {
		this.surveyNo = surveyNo;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
}
