package com.example.survey.vo;

import java.util.List;

import com.example.survey.constants.RtnCode;

public class SearchRes extends BaseRes {

	private List<SurveyUseForm> surveyList;

	public SearchRes() {
		super();
	}
	
	public SearchRes(RtnCode rtnCode, List<SurveyUseForm> surveyList) {
		super(rtnCode);
		this.surveyList = surveyList;
	}

	public List<SurveyUseForm> getSurveyList() {
		return surveyList;
	}

	public void setSurveyList(List<SurveyUseForm> surveyList) {
		this.surveyList = surveyList;
	}

}
