package com.example.survey.vo;

public class Question {
	
	// 題目
	private String question;

	// 題型
	private String type;

	// 是否必填
	private boolean required;

	// 選項
	private String[] option;

	public Question() {
		super();
	}

	public Question(String question, String type, boolean required, String option) {
		super();
		this.question = question;
		this.type = type;
		this.required = required;
		this.option = option.split(";");
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public boolean isRequired() {
		return required;
	}

	public void setRequired(boolean required) {
		this.required = required;
	}

	public String[] getOption() {
		return option;
	}

	public void setOption(String option) {
		this.option = option.split(";");
	}

	
}
