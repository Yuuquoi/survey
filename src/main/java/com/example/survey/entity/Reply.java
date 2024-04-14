package com.example.survey.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@Entity
@IdClass(value = ReplyId.class)
@Table(name = "reply")

public class Reply {
	
	@Id
	@Column(name = "survey_no")
	private int surveyNo;
	
	@Id
	@Column(name = "phone")
	private String phone;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "email")
	private String email;
	
	@Column(name = "age")
	private int age;
	
	@Column(name = "answers")
	private String answers;

	public Reply() {
		super();
	}

	public Reply(int surveyNo, String name, String phone, String email, int age, String answers) {
		super();
		this.surveyNo = surveyNo;
		this.name = name;
		this.phone = phone;
		this.email = email;
		this.age = age;
		this.answers = answers;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getAnswers() {
		return answers;
	}

	public void setAnswers(String answers) {
		this.answers = answers;
	}
	
}
