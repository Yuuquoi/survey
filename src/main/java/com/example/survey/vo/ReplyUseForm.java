package com.example.survey.vo;

import java.util.List;

import com.example.survey.entity.Reply;

public class ReplyUseForm extends Reply{

	private List<Answer> answerList;

	public ReplyUseForm() {
		super();
	}

	public ReplyUseForm(int surveyNo, String name, String phone, String email, int age, List<Answer> answerList) {
		super(surveyNo, name, phone, email, age, null);
		this.answerList = answerList;
	}

	public List<Answer> getAnswerList() {
		return answerList;
	}

	public void setAnswerList(List<Answer> answerList) {
		this.answerList = answerList;
	}
	
	
}
