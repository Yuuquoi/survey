package com.example.survey.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.survey.entity.Login;
import com.example.survey.entity.Reply;
import com.example.survey.service.ifs.SurveyService;
import com.example.survey.vo.BaseRes;
import com.example.survey.vo.CreateForm;
import com.example.survey.vo.DeleteReq;
import com.example.survey.vo.ResultRes;
import com.example.survey.vo.SearchReq;
import com.example.survey.vo.SearchRes;
import com.example.survey.vo.StatisticsRes;

@CrossOrigin
@RestController
public class SurveyServiceController {

	@Autowired
	private SurveyService surveyService;

	@PostMapping(value = "survey/create")
	public BaseRes create(@RequestBody CreateForm survey) {
		return surveyService.create(survey.getAuthor(), survey.getName(), survey.getDescription(), survey.isPublished(),
				survey.getStartDate(), survey.getEndDate(), survey.getQuestionListForCreate());
	}

	@PostMapping(value = "survey/edit")
	public BaseRes update(@RequestBody CreateForm survey) {
		return surveyService.update(survey.getNo(), survey.getAuthor(), survey.getName(), survey.getDescription(),
				survey.isPublished(), survey.getStartDate(), survey.getEndDate(), survey.getQuestionListForCreate());
	}

	@PostMapping(value = "survey/delete")
	public BaseRes deleteSurvey(@RequestBody DeleteReq noList) {
		return surveyService.deleteSurvey(noList.getNoList());
	}
	
	@PostMapping(value="survey/statistics")
	public StatisticsRes statistics(@RequestParam int no) {
		return surveyService.statistics(no);
	}

	@PostMapping(value="survey/result_list")
	public ResultRes resultList(@RequestParam int no) {
		return surveyService.resultList(no);
	}	

	@PostMapping(value = "survey/login")
	public BaseRes signIn(@RequestBody Login data) {
		return surveyService.signIn(data.getAccount(), data.getPw());
	}

	@PostMapping(value = "survey/reply")
	public BaseRes reply(@RequestBody Reply reply) {
		return surveyService.reply(reply.getSurveyNo(), reply.getName(), reply.getPhone(), reply.getEmail(),
				reply.getAge(), reply.getAnswers());
	}

	@PostMapping(value = "survey/search")
	public SearchRes search(@RequestBody SearchReq req) {
		return surveyService.search(req.getSurveyName(), req.getStartDate(), req.getEndDate(), req.isFront(),
				req.getAuthor());
	};

	@GetMapping(value = "survey/search_for_home")
	public SearchRes searchForHome(@RequestParam int type) {
		return surveyService.searchForHome(type);
	}

}
