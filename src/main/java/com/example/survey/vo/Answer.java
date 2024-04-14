package com.example.survey.vo;

public class Answer {
	// 題號
	private int questionNo;
	
	// 該題答案
	private String ans;

	public Answer() {
		super();
	}

	public Answer(int questionNo, String ans) {
		super();
		this.questionNo = questionNo;
		this.ans = ans;
	}

	public int getQuestionNo() {
		return questionNo;
	}

	public void setQuestionNo(int questionNo) {
		this.questionNo = questionNo;
	}

	public String getAns() {
		return ans;
	}

	public void setAns(String ans) {
		this.ans = ans;
	}
	
}
