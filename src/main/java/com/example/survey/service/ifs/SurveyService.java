package com.example.survey.service.ifs;

import java.time.LocalDate;
import java.util.List;

import com.example.survey.vo.AnswerUseForm;
import com.example.survey.vo.BaseRes;
import com.example.survey.vo.SearchRes;
import com.example.survey.vo.SurveyUseForm;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

public interface SurveyService {
	
	public BaseRes create(SurveyUseForm req) throws JsonProcessingException;
	
	public SearchRes search(String surveyName, LocalDate startDate, LocalDate endDate) throws JsonMappingException, JsonProcessingException;
	
	public BaseRes deleteSurvey(List<Integer> nos);

	public BaseRes update(SurveyUseForm req) throws JsonProcessingException;
	
	public BaseRes answer(AnswerUseForm req);
	
	public BaseRes statistics(int no);
	
}
