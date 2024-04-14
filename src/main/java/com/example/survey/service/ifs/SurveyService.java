package com.example.survey.service.ifs;

import java.time.LocalDate;
import java.util.List;

import com.example.survey.vo.ReplyUseForm;
import com.example.survey.vo.BaseRes;
import com.example.survey.vo.SearchRes;
import com.example.survey.vo.SurveyUseForm;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

public interface SurveyService {
	// ��x
	public BaseRes create(SurveyUseForm req);

	public BaseRes update(SurveyUseForm req);

	public BaseRes deleteSurvey(List<Integer> nos);

	// �e�x
	public BaseRes answer(ReplyUseForm req);
	
	// �e�ᤣ��
	public SearchRes search(String author, String surveyName, LocalDate startDate, LocalDate endDate, boolean isFront);
	
	public BaseRes statistics(int no);
	
}
