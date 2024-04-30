package com.example.survey.vo;

import java.time.LocalDate;
import java.util.List;

import com.example.survey.entity.Survey;

public class SurveyUseForm extends Survey{
	
	private String state;

	private List<Question> questionList;
	

	public SurveyUseForm() {
		super();
	}
		
	public SurveyUseForm(int no, String author,String name, String description, boolean published, LocalDate startDate, LocalDate endDate,
			String state, List<Question> questionList) {
		super(no, author, name, description, published, startDate, endDate, null);
		this.state = state;
		this.questionList = questionList;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public List<Question> getQuestionList() {
		return questionList;
	}

	public void setQuestionList(List<Question> questionList) {
		this.questionList = questionList;
	}

}
