package com.example.survey.entity;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "survey")
public class Survey {
	
	// 問卷編號
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	@Column(name = "no")
	private int no;
	
	// 作者
	@Column(name = "author")
	private String author;
	
	// 問卷名稱
	@Column(name = "name")
	private String name;
	
	// 問卷描述
	@Column(name = "description")
	private String description;
	
	// 是否發布
	@Column(name = "published")
	private boolean published;

	// 問卷開始時間
	@Column(name = "start_date")
	private LocalDate startDate;
	
	// 問卷結束時間
	@Column(name = "end_date")
	private LocalDate endDate;
	
	// 問題們
	@Column(name = "questions")
	private String questions;
	
	public Survey() {
		super();
	}

	public Survey(String author, String name, String description, boolean published, LocalDate startDate,
			LocalDate endDate, String questions) {
		super();
		this.author = author;
		this.name = name;
		this.description = description;
		this.published = published;
		this.startDate = startDate;
		this.endDate = endDate;
		this.questions = questions;
	}

	public Survey(int no, String author, String name, String description, boolean published, LocalDate startDate,
			LocalDate endDate, String questions) {
		super();
		this.no = no;
		this.author = author;
		this.name = name;
		this.description = description;
		this.published = published;
		this.startDate = startDate;
		this.endDate = endDate;
		this.questions = questions;
	}

	public int getNo() {
		return no;
	}

	public void setNo(int no) {
		this.no = no;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isPublished() {
		return published;
	}

	public void setPublished(boolean published) {
		this.published = published;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}

	public String getQuestions() {
		return questions;
	}

	public void setQuestions(String questions) {
		this.questions = questions;
	}

}
