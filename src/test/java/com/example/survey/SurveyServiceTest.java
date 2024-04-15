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
		/*********** ���T�ƭ� ***********/
		String author = "test001";
		String name = "�ڬO�ݨ�";
		String description = "�o�O�ݨ��W�٪��y�z";
		boolean published = true;
		LocalDate startDate = LocalDate.now().plusDays(2);
		LocalDate endDate = LocalDate.now().plusDays(9);
		Question q1 = new Question("�ڬO��", "text", true, "A;B;C;D");
		Question q2 = new Question("�A�O��", "text", true, "A;B;C;D");
		Question q3 = new Question("�L�O��", "text", true, "A;B;C;D");
		Question q4 = new Question();
		List<Question> questionList = new ArrayList<>(List.of(q1, q2, q3));

		/*********** ���� null ***********/
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

		/*********** ���� logic ***********/
		res = surveyService.create("123", name, description, published, startDate, endDate, questionList);
		Assert.isTrue(res.getRtnCode().getCode() == 400, "Create Test Fail: logic author");
		res = surveyService.create(author, name, description, published, LocalDate.now().plusDays(7), LocalDate.now(),
				questionList);
		Assert.isTrue(res.getRtnCode().getCode() == 400, "Create Test Fail: logic startDate&endDate");
		questionList.add(q4);
		res = surveyService.create(author, name, description, published, startDate, endDate, questionList);
		Assert.isTrue(res.getRtnCode().getCode() == 400, "Create Test Fail: null questionList");
		questionList.remove(3);

		/*********** ���� Success ***************/
		res = surveyService.create(author, name, description, published, startDate, endDate, questionList);
		Assert.isTrue(res.getRtnCode().getCode() == 200, "Create Test Fail: Success");

		// delete
	}

	@Test
	public void updateTest() {
		/*********** ���ռƭ� ***********/
		String author = "test001";
		String name = "�ڬO�ݨ�";
		String description = "�o�O�ݨ��W�٪��y�z";
		boolean published = true;
		LocalDate startDate = LocalDate.now().plusDays(2);
		LocalDate endDate = LocalDate.now().plusDays(9);
		Question q1 = new Question("�ڬO��", "text", true, "A;B;C;D");
		Question q2 = new Question("�A�O��", "text", true, "A;B;C;D");
		Question q3 = new Question("�L�O��", "text", true, "A;B;C;D");
		Question q4 = new Question();
		List<Question> questionList = new ArrayList<>(List.of(q1, q2, q3));
		// �|���o��
		surveyService.create(author, name, description, false, startDate, endDate, questionList);
		// �|���}�l
		surveyService.create(author, name, description, published, startDate, endDate, questionList);
		// �w�}�l
		surveyService.create(author, name, description, published, LocalDate.now().plusDays(-5),
				LocalDate.now().plusDays(3), questionList);
		// �w����
		surveyService.create(author, name, description, published, LocalDate.now().plusDays(-5),
				LocalDate.now().plusDays(-2), questionList);

		/*********** ���� logic ***********/
		BaseRes res = surveyService.update(2, author, name, description, published, startDate, endDate, questionList);
		Assert.isTrue(res.getRtnCode().getCode() == 200, "Update Test Fail: �|���o��");
		res = surveyService.update(3, author, name, description, published, startDate, endDate, questionList);
		Assert.isTrue(res.getRtnCode().getCode() == 200, "Update Test Fail: �|���}�l");
		res = surveyService.update(4, author, name, description, published, startDate, endDate, questionList);
		Assert.isTrue(res.getRtnCode().getCode() == 400, "Update Test Fail: �w�}�l");
		res = surveyService.update(5, author, name, description, published, startDate, endDate, questionList);
		Assert.isTrue(res.getRtnCode().getCode() == 400, "Update Test Fail: �w����");

		// delete
	}

	@Test
	public void deleteTest() {
		/*********** ���� null ***********/
		BaseRes res = surveyService.deleteSurvey(null);
		Assert.isTrue(res.getRtnCode().getCode() == 400, "Delete Test Fail: null");
		/*********** ���� logic ***********/
		res = surveyService.deleteSurvey(List.of(2, 3, 4, 5));
		Assert.isTrue(res.getRtnCode().getCode() == 200, "Update Test Fail: logic");
	}

	@Test
	public void replyTest() {
		/*********** ���T�ƭ� ***********/
		int surveyNo = 1;
		String phone = "0912345678";
		String name = "AAA";
		String email = "no@thankyou.com";
		int age = 17;
		Answer a1 = new Answer(1, "B");
		Answer a2 = new Answer(2, "C");
		Answer a3 = new Answer(3, "ACD");
		List<Answer> answers = new ArrayList<>(List.of(a1, a2, a3));

		/*********** ���� null ***********/
		BaseRes res = surveyService.reply(surveyNo, null, name, email, age, answers);
		Assert.isTrue(res.getRtnCode().getCode() == 400, "Reply Test Fail: phone null");
		res = surveyService.reply(surveyNo, phone, null, email, age, answers);
		Assert.isTrue(res.getRtnCode().getCode() == 400, "Reply Test Fail: name null");
		res = surveyService.reply(surveyNo, phone, name, null, age, answers);
		Assert.isTrue(res.getRtnCode().getCode() == 400, "Reply Test Fail: email null");
		res = surveyService.reply(surveyNo, phone, name, email, age, null);
		Assert.isTrue(res.getRtnCode().getCode() == 400, "Reply Test Fail: answers null");

		/*********** ���� logic ***********/
		res = surveyService.reply(-1, phone, name, email, age, answers);
		Assert.isTrue(res.getRtnCode().getCode() == 400, "Reply Test Fail: surveyNo logic");
		res = surveyService.reply(surveyNo, "091234", name, email, age, answers);
		Assert.isTrue(res.getRtnCode().getCode() == 400, "Reply Test Fail: phone logic");
		res = surveyService.reply(surveyNo, phone, name, "123456", age, answers);
		Assert.isTrue(res.getRtnCode().getCode() == 400, "Reply Test Fail: email logic");
		res = surveyService.reply(surveyNo, phone, name, email, -1, answers);
		Assert.isTrue(res.getRtnCode().getCode() == 400, "Reply Test Fail: age logic");

		/*********** ���� Success ***************/
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
