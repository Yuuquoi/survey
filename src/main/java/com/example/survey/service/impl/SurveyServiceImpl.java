package com.example.survey.service.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.example.survey.constants.RtnCode;
import com.example.survey.entity.Reply;
import com.example.survey.entity.ReplyId;
import com.example.survey.entity.Survey;
import com.example.survey.repository.ReplyDao;
import com.example.survey.repository.SurveyDao;
import com.example.survey.service.ifs.SurveyService;
import com.example.survey.vo.SurveyUseForm;
import com.example.survey.vo.ReplyUseForm;
import com.example.survey.vo.Answer;
import com.example.survey.vo.BaseRes;
import com.example.survey.vo.Question;
import com.example.survey.vo.SearchRes;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Transactional
@Service
public class SurveyServiceImpl implements SurveyService {

	@Autowired
	private SurveyDao surveyDao;
	
	private ReplyDao replyDao;

	private ObjectMapper mapper;

	private BaseRes res;

	/**
	 * 功能：新增傳入的表單<br>
	 * 防呆 (1)判斷輸入內容邏輯<br>
	 *     (2)確認表單不存在
	 */
	@Override
	public BaseRes create(SurveyUseForm req) {
		if (!checkInputData(req)) {
			return res;
		}
		if (surveyDao.existsById(req.getNo())) {
			return new BaseRes(RtnCode.SURVEY_EXISTS);
		}
		try {
			surveyDao.save(convertToSaveForm(req));
		} catch (JsonProcessingException e) {
			return new BaseRes(RtnCode.JSON_ERROR);
		}
		return new BaseRes(RtnCode.SUCCESS);
	}
	
	/**
	 * 功能：更新傳入的表單<br>
	 * 防呆 (1)判斷輸入內容邏輯<br>
	 *     (2)確認表單存在且可編輯<br>
	 */
	@Override
	public BaseRes update(SurveyUseForm req) {
		if (!checkInputData(req)) {
			return res;
		}
		if (!surveyDao.existsByNoAndPublishedFalseOrNoAndStartDateAfter(req.getNo(), req.getNo(), LocalDate.now())) {
			return new BaseRes(RtnCode.SURVEY_NOT_EXISTS);
		}
		try {
			surveyDao.save(convertToSaveForm(req));
		} catch (JsonProcessingException e) {
			return new BaseRes(RtnCode.JSON_ERROR);
		}
		return new BaseRes(RtnCode.SUCCESS);
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
	 * 功能：新增傳入的答案<br>
	 * 防呆 (1)判斷輸入內容邏輯<br>
	 *     (2)確認同一人的答案不存在
	 */
	@Override
	public BaseRes answer(ReplyUseForm req) {
		if(!checkInputReply(req)) {
			return res;
		}
		if(replyDao.existsById(new ReplyId(req.getSurveyNo(), req.getPhone()))) {
			return new BaseRes(RtnCode.DUPLICATED_REPLY);
		}
		try {
			replyDao.save(convertToReplySaveForm(req));
		} catch (JsonProcessingException e) {
			return new BaseRes(RtnCode.JSON_ERROR);
		}
		return new BaseRes(RtnCode.SUCCESS);
	}
	
	/**
	 * 功能：判斷是否為後台搜尋，搜尋符合傳入條件的表單
	 */
	@Override
	public SearchRes search(String author, String surveyName, LocalDate startDate, LocalDate endDate, boolean isFront) {
		if(!StringUtils.hasText(author)) {
			author="";
		}
		if (!StringUtils.hasText(surveyName)) {
			surveyName = "";
		}
		if (startDate == null) {
			startDate = LocalDate.of(1912, 1, 1);
		}
		if (endDate == null) {
			endDate = LocalDate.of(9999, 12, 31);
		}
		List<SurveyUseForm> listRes = new ArrayList<>();
		for (Survey item : surveyDao.findByAuthorContainingAndNameContainingAndStartDateGreaterThanEqualAndEndDateLessThanEqualAndPublished(author, surveyName,
				startDate, endDate, isFront)) {
			try {
				listRes.add(convertToUseForm(item));
			} catch (JsonProcessingException e) {
				return new SearchRes(RtnCode.JSON_ERROR, null);
			}
		}
		return new SearchRes(RtnCode.SUCCESS, listRes);
	}
	
	@Override
	public BaseRes statistics(int no) {

		return null;
	}
	
	//--------------------------------Survey Area-----------------------------------------------------//
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
		if (req.getNo() <= 0 || req.getAuthor() == null || req.getName() == null || req.getQuestionList() == null || req.getStartDate() == null
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
	
	//------------------------------------Reply Area-------------------------------------------------//
	/**
	 * 1. 確認參數是否有意義<br>
	 * 2. 確認區塊是否符合邏輯 ( Not Null、編號>=0、格式正確 )<br>
	 * result: 格式均正確 return True, 有錯誤 return False.
	 **/
	private boolean checkInputReply(ReplyUseForm req) {
		if(req == null || CollectionUtils.isEmpty(req.getAnswerList())) {
			res.setRtnCode(RtnCode.PARAM_NULL);
			return false;
		}
		if(req.getSurveyNo() < 0 || !req.getPhone().matches("09\\d{8}") || !StringUtils.hasText(req.getName()) || !req.getEmail().matches(".{1,}@.{1,}") || req.getAge() < 0) {
			res.setRtnCode(RtnCode.PARAM_ERROR);
			return false;
		}
		return true;
	}
	
	/**
	 * 1. 將參數強制轉型<br>
	 * 2. 將轉型後的 Reply 中存入 List 轉換成的 String<br>
	 * result: return Reply
	 **/
	private Reply convertToReplySaveForm(ReplyUseForm req) throws JsonProcessingException {
		Reply temp = (Reply) req;
		temp.setAnswers(mapper.writeValueAsString(req.getAnswerList()));
		return temp;
	}

	/**
	 * 1. 將參數強制轉型<br>
	 * 2. 將轉型後的 Reply 中存入 String 轉換成的 List<br>
	 * result: return ReplyUseForm
	 */
	private ReplyUseForm convertToReplyUseForm(Reply req) throws JsonMappingException, JsonProcessingException {
		ReplyUseForm temp = (ReplyUseForm) req;
		temp.setAnswerList(mapper.readValue(req.getAnswers(),
				new TypeReference<List<Answer>>() {
				}));
		return temp;
	}


}
