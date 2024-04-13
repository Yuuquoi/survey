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
	 * �\��G�s�W�ǤJ�����<br>
	 * ���b (1)�P�_��J���e�޿�<br>
	 *     (2)�T�{��椣�s�b
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
	 * �\��G�j�M�ŦX�ǤJ���󪺪��
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
	 * �\��G�R���ǤJ�����s��<br>
	 * ���b (1)�P�_��J���e�޿�<br>
	 *     (2)�T�{���i�R��
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
	 * �\��G��s�ǤJ�����<br>
	 * ���b (1)�P�_��J���e�޿�<br>
	 *     (2)�T�{���s�b�B�i�s��<br>
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
	 * 1. �T�{�ѼƬO�_���N�q<br>
	 * 2. �T�{�ݨ��϶��O�_�ŦX�޿� ( Not Null�B�s��>=0�B�_�W�ɶ����` )<br>
	 * 3. �T�{�C�Ӱ��D�϶��O�_�ŦX�޿� ( Not Null )<br>
	 * result: �榡�����T return True, �����~ return False.
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
	 * 1. �N�ѼƱj���૬<br>
	 * 2. �N�૬�᪺ Questions ���s�J List �ഫ���� String<br>
	 * result: return Survey
	 **/
	private Survey convertToSaveForm(SurveyUseForm req) throws JsonProcessingException {
		Survey temp = (Survey) req;
		temp.setQuestions(mapper.writeValueAsString(req.getQuestionList()));
		return temp;
	}

	/**
	 * 1. �N�ѼƱj���૬<br>
	 * 2. �N�૬�᪺ Questions ���s�J String �ഫ���� List<br>
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
