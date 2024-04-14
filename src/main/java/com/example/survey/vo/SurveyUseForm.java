package com.example.survey.vo;

import java.time.LocalDate;
import java.util.List;

import com.example.survey.entity.Survey;

public class SurveyUseForm extends Survey{

	private List<Question> questionList;

	public SurveyUseForm() {
		super();
	}

	public SurveyUseForm(String author,String name, String description, boolean published, LocalDate startDate, LocalDate endDate,
			List<Question> questionList) {
		super(author, name, description, published, startDate, endDate, null);
		this.questionList = questionList;
	}

	public List<Question> getQuestionList() {
		return questionList;
	}

	public void setQuestionList(List<Question> questionList) {
		this.questionList = questionList;
	}



}
