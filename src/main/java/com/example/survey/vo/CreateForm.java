package com.example.survey.vo;

import java.time.LocalDate;
import java.util.List;

import com.example.survey.entity.Survey;

public class CreateForm extends Survey {

	private List<QuestionCreate> questionListForCreate;

	public CreateForm() {
		super();
	}

	public CreateForm(int no, String author, String name, String description, boolean published, LocalDate startDate,
			LocalDate endDate, List<QuestionCreate> questionList) {
		super(no, author, name, description, published, startDate, endDate, null);
		this.questionListForCreate = questionList;
	}

	public List<QuestionCreate> getQuestionListForCreate() {
		return questionListForCreate;
	}

	public void setQuestionListForCreate(List<QuestionCreate> questionListForCreate) {
		this.questionListForCreate = questionListForCreate;
	}

}
