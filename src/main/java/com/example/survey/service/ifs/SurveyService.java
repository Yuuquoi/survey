package com.example.survey.service.ifs;

import java.time.LocalDate;
import java.util.List;

import com.example.survey.vo.Answer;
import com.example.survey.vo.BaseRes;
import com.example.survey.vo.Question;
import com.example.survey.vo.SearchRes;
import com.example.survey.vo.StatisticsRes;

public interface SurveyService {
	// ��x
	public BaseRes create(String author, String name, String description, boolean published, LocalDate startDate, LocalDate endDate, List<Question> questions);

	public BaseRes update(int no, String author, String name, String description, boolean published, LocalDate startDate, LocalDate endDate, List<Question> questions);

	public BaseRes deleteSurvey(List<Integer> noList);

	public StatisticsRes statistics(int no);

	// �e�x
	public BaseRes reply(int surveyNo, String phone, String name, String email, int age, List<Answer> answerss);
	
	// �n�J
	public BaseRes signIn(String account, String pw);
	
	// ���U
	public BaseRes signUp(String account, String pw);
	
	// �e�ᤣ��
	public SearchRes search(String surveyName, LocalDate startDate, LocalDate endDate, boolean isFront, String author, int index);
	
	public SearchRes searchForHome(int type);

}
