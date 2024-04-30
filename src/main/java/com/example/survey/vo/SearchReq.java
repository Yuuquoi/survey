package com.example.survey.vo;

import java.time.LocalDate;

public class SearchReq {
	
	private String surveyName;
	
	private LocalDate startDate;

	private LocalDate endDate;

	private boolean front;

	private String author;

	public SearchReq() {
		super();
	}

	public SearchReq(String surveyName, LocalDate startDate, LocalDate endDate, boolean front, String author) {
		super();
		this.surveyName = surveyName;
		this.startDate = startDate;
		this.endDate = endDate;
		this.front = front;
		this.author = author;
	}

	public String getSurveyName() {
		return surveyName;
	}

	public void setSurveyName(String surveyName) {
		this.surveyName = surveyName;
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

	public boolean isFront() {
		return front;
	}

	public void setFront(boolean front) {
		this.front = front;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}
	
}
