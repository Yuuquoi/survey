package com.example.survey.service.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.example.survey.constants.RtnCode;
import com.example.survey.entity.Survey;
import com.example.survey.repository.SurveyDao;
import com.example.survey.service.ifs.SurveyService;
import com.example.survey.vo.SurveyUseForm;
import com.example.survey.vo.AnswerUseForm;
import com.example.survey.vo.BaseRes;
import com.example.survey.vo.Question;
import com.example.survey.vo.SearchRes;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class SurveyServiceImpl implements SurveyService {

	@Autowired
	private SurveyDao surveyDao;

	private ObjectMapper mapper;

	private BaseRes res;

	/**
	 * 功能：新增傳入的表單<br>
	 * 防呆 (1)判斷輸入內容邏輯<br>
	 *     (2)確認表單不存在
	 */
	@Override
	public BaseRes create(SurveyUseForm req) throws JsonProcessingException {
		if (!checkInputData(req)) {
			return res;
		}
		if (surveyDao.existsById(req.getNo())) {
			return new BaseRes(RtnCode.SURVEY_EXISTS);
		}
		surveyDao.save(convertToSaveForm(req));
		return new BaseRes(RtnCode.SUCCESS);
	}
	
	/**
	 * 功能：搜尋符合傳入條件的表單
	 */
	@Override
	public SearchRes search(String name, LocalDate startDate, LocalDate endDate)
			throws JsonMappingException, JsonProcessingException {
		if (!StringUtils.hasText(name)) {
			name = "";
		}
		if (startDate == null) {
			startDate = LocalDate.of(1912, 1, 1);
		}
		if (endDate == null) {
			endDate = LocalDate.of(9999, 12, 31);
		}
		List<SurveyUseForm> listRes = new ArrayList<>();
		for (Survey item : surveyDao.findByNameContainingAndStartDateGreaterThanEqualAndEndDateLessThanEqual(name,
				startDate, endDate)) {
			listRes.add(convertToUseForm(item));
		}
		return new SearchRes(RtnCode.SUCCESS, listRes);
	}

	/**
	 * 功能：刪除傳入的表單編號<br>
	 * 防呆 (1)判斷輸入內容邏輯<br>
	 *     (2)確認表單可刪除
	 */
	@Override
	public BaseRes deleteSurvey(List<Integer> nos) {
		if (CollectionUtils.isEmpty(nos)) {
			return new BaseRes(RtnCode.PARAM_ERROR);
		}
		surveyDao.deleteAllByNoInAndPublishedFalseOrNoInAndStartDateAfter(nos, nos, LocalDate.now());
		return new BaseRes(RtnCode.SUCCESS);
	}

	/**
	 * 功能：更新傳入的表單<br>
	 * 防呆 (1)判斷輸入內容邏輯<br>
	 *     (2)確認表單存在且可編輯<br>
	 */
	@Override
	public BaseRes update(SurveyUseForm req) throws JsonProcessingException {
		if (!checkInputData(req)) {
			return res;
		}
		if (!surveyDao.existsByNoAndPublishedFalseOrNoAndStartDateAfter(req.getNo(), req.getNo(), LocalDate.now())) {
			return new BaseRes(RtnCode.SURVEY_NOT_EXISTS);
		}
		surveyDao.save(convertToSaveForm(req));
		return new BaseRes(RtnCode.SUCCESS);
	}


	/**
	 * 1. 確認參數是否有意義<br>
	 * 2. 確認問卷區塊是否符合邏輯 ( Not Null、編號>=0、起訖時間正常 )<br>
	 * 3. 確認每個問題區塊是否符合邏輯 ( Not Null )<br>
	 * result: 格式均正確 return True, 有錯誤 return False.
	 **/
	private boolean checkInputData(SurveyUseForm req) {
		if (req == null || CollectionUtils.isEmpty(req.getQuestionList())) {
			res.setRtnCode(RtnCode.PARAM_NULL);
			return false;
		}
		if (req.getNo() <= 0 || req.getName() == null || req.getQuestionList() == null || req.getStartDate() == null
				|| req.getEndDate() == null || req.getStartDate().isAfter(req.getEndDate())) {
			res.setRtnCode(RtnCode.PARAM_ERROR);
			return false;
		}
		for (Question item : req.getQuestionList()) {
			if (item.getQuestion() == null || item.getType() == null
					|| !item.getType().equals("TEXT") && item.getOption() == null) {
				res.setRtnCode(RtnCode.PARAM_ERROR);
				return false;
			}
		}
		return true;
	}

	/**
	 * 1. 將參數強制轉型<br>
	 * 2. 將轉型後的 Questions 中存入 List 轉換成的 String<br>
	 * result: return Survey
	 **/
	private Survey convertToSaveForm(SurveyUseForm req) throws JsonProcessingException {
		Survey temp = (Survey) req;
		temp.setQuestions(mapper.writeValueAsString(req.getQuestionList()));
		return temp;
	}

	/**
	 * 1. 將參數強制轉型<br>
	 * 2. 將轉型後的 Questions 中存入 String 轉換成的 List<br>
	 * result: return SurveyUseForm
	 */
	private SurveyUseForm convertToUseForm(Survey req) throws JsonMappingException, JsonProcessingException {
		SurveyUseForm temp = (SurveyUseForm) req;
		temp.setQuestionList(mapper.readValue(req.getQuestions(),
				new TypeReference<List<Question>>() {
				}));
		return temp;
	}

	@Override
	public BaseRes answer(AnswerUseForm req) {

		return null;
	}

	@Override
	public BaseRes statistics(int no) {

		return null;
	}

}
