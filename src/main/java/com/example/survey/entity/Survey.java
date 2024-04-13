package com.example.survey.entity;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "survey")
public class Survey {
	
	// �ݨ��s��
	@Id
	@Column(name = "no")
	private int no;
	
	// �ݨ��W��
	@Column(name = "name")
	private String name;
	
	// �ݨ��y�z
	@Column(name = "description")
	private String description;
	
	// �O�_�o��
	@Column(name = "published")
	private boolean published;

	// �ݨ��}�l�ɶ�
	@Column(name = "start_date")
	private LocalDate startDate;
	
	// �ݨ������ɶ�
	@Column(name = "end_date")
	private LocalDate endDate;
	
	// ���D��
	@Column(name = "questions")
	private String questions;
	
	public Survey() {
		super();
	}

	public Survey(int no, String name, String description, boolean published, LocalDate startDate, LocalDate endDate,
			String questions) {
		super();
		this.no = no;
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
