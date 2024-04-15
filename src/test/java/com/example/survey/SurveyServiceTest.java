package com.example.survey;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import com.example.survey.repository.SurveyDao;
import com.example.survey.service.ifs.SurveyService;
import com.example.survey.vo.Answer;
import com.example.survey.vo.BaseRes;
import com.example.survey.vo.Question;
import com.example.survey.vo.SearchRes;
import com.example.survey.vo.SurveyUseForm;

@SpringBootTest
public class SurveyServiceTest {

	@Autowired
	private SurveyService surveyService;

	@Autowired
	private SurveyDao surveyDao;

	@Test
	public void createTest() {
		/*********** 正確數值 ***********/
		String author = "test001";
		String name = "我是問卷";
		String description = "這是問卷名稱的描述";
		boolean published = true;
		LocalDate startDate = LocalDate.now().plusDays(2);
		LocalDate endDate = LocalDate.now().plusDays(9);
		Question q1 = new Question("我是誰", "text", true, "A;B;C;D");
		Question q2 = new Question("你是誰", "text", true, "A;B;C;D");
		Question q3 = new Question("他是誰", "text", true, "A;B;C;D");
		Question q4 = new Question();
		List<Question> questionList = new ArrayList<>(List.of(q1, q2, q3));

		/*********** 測試 null ***********/
		BaseRes res = surveyService.create(null, name, description, published, startDate, endDate, questionList);
		Assert.isTrue(res.getRtnCode().getCode() == 400, "Create Test Fail: null author");
		res = surveyService.create(author, null, description, published, startDate, endDate, questionList);
		Assert.isTrue(res.getRtnCode().getCode() == 400, "Create Test Fail: null name");
		res = surveyService.create(author, name, description, published, null, endDate, questionList);
		Assert.isTrue(res.getRtnCode().getCode() == 400, "Create Test Fail: null startDate");
		res = surveyService.create(author, name, description, published, startDate, null, questionList);
		Assert.isTrue(res.getRtnCode().getCode() == 400, "Create Test Fail: null endDate");
		res = surveyService.create(author, name, description, published, startDate, endDate, null);
		Assert.isTrue(res.getRtnCode().getCode() == 400, "Create Test Fail: null questionList");

		/*********** 測試 logic ***********/
		res = surveyService.create("123", name, description, published, startDate, endDate, questionList);
		Assert.isTrue(res.getRtnCode().getCode() == 400, "Create Test Fail: logic author");
		res = surveyService.create(author, name, description, published, LocalDate.now().plusDays(7), LocalDate.now(),
				questionList);
		Assert.isTrue(res.getRtnCode().getCode() == 400, "Create Test Fail: logic startDate&endDate");
		questionList.add(q4);
		res = surveyService.create(author, name, description, published, startDate, endDate, questionList);
		Assert.isTrue(res.getRtnCode().getCode() == 400, "Create Test Fail: null questionList");
		questionList.remove(3);

		/*********** 測試 Success ***************/
		res = surveyService.create(author, name, description, published, startDate, endDate, questionList);
		Assert.isTrue(res.getRtnCode().getCode() == 200, "Create Test Fail: Success");

		// delete
	}

	@Test
	public void updateTest() {
		/*********** 測試數值 ***********/
		String author = "test001";
		String name = "我是問卷";
		String description = "這是問卷名稱的描述";
		boolean published = true;
		LocalDate startDate = LocalDate.now().plusDays(2);
		LocalDate endDate = LocalDate.now().plusDays(9);
		Question q1 = new Question("我是誰", "text", true, "A;B;C;D");
		Question q2 = new Question("你是誰", "text", true, "A;B;C;D");
		Question q3 = new Question("他是誰", "text", true, "A;B;C;D");
		Question q4 = new Question();
		List<Question> questionList = new ArrayList<>(List.of(q1, q2, q3));
		// 尚未發布
		surveyService.create(author, name, description, false, startDate, endDate, questionList);
		// 尚未開始
		surveyService.create(author, name, description, published, startDate, endDate, questionList);
		// 已開始
		surveyService.create(author, name, description, published, LocalDate.now().plusDays(-5),
				LocalDate.now().plusDays(3), questionList);
		// 已結束
		surveyService.create(author, name, description, published, LocalDate.now().plusDays(-5),
				LocalDate.now().plusDays(-2), questionList);

		/*********** 測試 logic ***********/
		BaseRes res = surveyService.update(2, author, name, description, published, startDate, endDate, questionList);
		Assert.isTrue(res.getRtnCode().getCode() == 200, "Update Test Fail: 尚未發布");
		res = surveyService.update(3, author, name, description, published, startDate, endDate, questionList);
		Assert.isTrue(res.getRtnCode().getCode() == 200, "Update Test Fail: 尚未開始");
		res = surveyService.update(4, author, name, description, published, startDate, endDate, questionList);
		Assert.isTrue(res.getRtnCode().getCode() == 400, "Update Test Fail: 已開始");
		res = surveyService.update(5, author, name, description, published, startDate, endDate, questionList);
		Assert.isTrue(res.getRtnCode().getCode() == 400, "Update Test Fail: 已結束");

		// delete
	}

	@Test
	public void deleteTest() {
		/*********** 測試 null ***********/
		BaseRes res = surveyService.deleteSurvey(null);
		Assert.isTrue(res.getRtnCode().getCode() == 400, "Delete Test Fail: null");
		/*********** 測試 logic ***********/
		res = surveyService.deleteSurvey(List.of(2, 3, 4, 5));
		Assert.isTrue(res.getRtnCode().getCode() == 200, "Update Test Fail: logic");
	}

	@Test
	public void replyTest() {
		/*********** 正確數值 ***********/
		int surveyNo = 1;
		String phone = "0912345678";
		String name = "AAA";
		String email = "no@thankyou.com";
		int age = 17;
		Answer a1 = new Answer(1, "B");
		Answer a2 = new Answer(2, "C");
		Answer a3 = new Answer(3, "ACD");
		List<Answer> answers = new ArrayList<>(List.of(a1, a2, a3));

		/*********** 測試 null ***********/
		BaseRes res = surveyService.reply(surveyNo, null, name, email, age, answers);
		Assert.isTrue(res.getRtnCode().getCode() == 400, "Reply Test Fail: phone null");
		res = surveyService.reply(surveyNo, phone, null, email, age, answers);
		Assert.isTrue(res.getRtnCode().getCode() == 400, "Reply Test Fail: name null");
		res = surveyService.reply(surveyNo, phone, name, null, age, answers);
		Assert.isTrue(res.getRtnCode().getCode() == 400, "Reply Test Fail: email null");
		res = surveyService.reply(surveyNo, phone, name, email, age, null);
		Assert.isTrue(res.getRtnCode().getCode() == 400, "Reply Test Fail: answers null");

		/*********** 測試 logic ***********/
		res = surveyService.reply(-1, phone, name, email, age, answers);
		Assert.isTrue(res.getRtnCode().getCode() == 400, "Reply Test Fail: surveyNo logic");
		res = surveyService.reply(surveyNo, "091234", name, email, age, answers);
		Assert.isTrue(res.getRtnCode().getCode() == 400, "Reply Test Fail: phone logic");
		res = surveyService.reply(surveyNo, phone, name, "123456", age, answers);
		Assert.isTrue(res.getRtnCode().getCode() == 400, "Reply Test Fail: email logic");
		res = surveyService.reply(surveyNo, phone, name, email, -1, answers);
		Assert.isTrue(res.getRtnCode().getCode() == 400, "Reply Test Fail: age logic");

		/*********** 測試 Success ***************/
		res = surveyService.reply(surveyNo, phone, name, email, age, answers);
		Assert.isTrue(res.getRtnCode().getCode() == 200, "Reply Test Fail: Success");
	}

	@Test
	public void searchTest() {
		SearchRes y = surveyService.search("1", null, null, true, "test01", 2);
		for(SurveyUseForm item : y.getSurveyList()) {
			System.out.println(item.getNo()+" "+item.getAuthor()+" "+item.getName());
		}
		System.out.println("***************************************");
		y = surveyService.search("", null, null, false, "test01", 2);
		for(SurveyUseForm item : y.getSurveyList()) {
			System.out.println(item.getNo()+" "+item.getAuthor()+" "+item.getName());
		}

	}

}
