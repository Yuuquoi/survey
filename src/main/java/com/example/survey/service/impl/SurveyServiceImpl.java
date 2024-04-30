package com.example.survey.service.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.example.survey.constants.RtnCode;
import com.example.survey.entity.Login;
import com.example.survey.entity.Reply;
import com.example.survey.entity.ReplyId;
import com.example.survey.entity.Survey;
import com.example.survey.repository.LoginDao;
import com.example.survey.repository.ReplyDao;
import com.example.survey.repository.SurveyDao;
import com.example.survey.service.ifs.SurveyService;
import com.example.survey.vo.BaseRes;
import com.example.survey.vo.Question;
import com.example.survey.vo.QuestionCreate;
import com.example.survey.vo.ResultRes;
import com.example.survey.vo.SearchRes;
import com.example.survey.vo.StatisticsRes;
import com.example.survey.vo.SurveyUseForm;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Transactional
@Service
public class SurveyServiceImpl implements SurveyService {

	@Autowired
	private SurveyDao surveyDao;

	@Autowired
	private ReplyDao replyDao;

	@Autowired
	private LoginDao loginDao;

	private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

	private ObjectMapper mapper = new ObjectMapper();

	/**
	 * 功能：新增傳入的表單<br>
	 * 防呆 (1)判斷輸入內容邏輯<br>
	 * (2)確認表單不存在
	 */
	@Override
	public BaseRes create(String author, String name, String description, boolean published, LocalDate startDate,
			LocalDate endDate, List<QuestionCreate> questions) {
		if (!checkInputSurvey(author, name, startDate, endDate, questions)) {
			return new BaseRes(RtnCode.PARAM_ERROR);
		}
		try {
			surveyDao.save(new Survey(author, name, description, published, startDate, endDate,
					mapper.writeValueAsString(questions)));
		} catch (JsonProcessingException e) {
			return new BaseRes(RtnCode.JSON_ERROR);
		}
		return new BaseRes(RtnCode.SUCCESS);
	}

	/**
	 * 功能：更新傳入的表單<br>
	 * 防呆 (1)判斷輸入內容邏輯<br>
	 * (2)確認表單存在且可編輯<br>
	 */
	@Override
	public BaseRes update(int no, String author, String name, String description, boolean published,
			LocalDate startDate, LocalDate endDate, List<QuestionCreate> questions) {
		if (!checkInputSurvey(author, name, startDate, endDate, questions)) {
			return new BaseRes(RtnCode.PARAM_ERROR);
		}
		try {
			if (surveyDao.update(no, name, description, published, startDate, endDate,
					mapper.writeValueAsString(questions)) == 0) {
				return new BaseRes(RtnCode.SURVEY_NOT_EXISTS);
			}
		} catch (JsonProcessingException e) {
			return new BaseRes(RtnCode.JSON_ERROR);
		}
		return new BaseRes(RtnCode.SUCCESS);
	}

	/**
	 * 功能：刪除傳入的表單編號<br>
	 * 防呆 (1)判斷輸入內容邏輯<br>
	 */
	@Override
	public BaseRes deleteSurvey(List<Integer> noList) {
		if (CollectionUtils.isEmpty(noList)) {
			return new BaseRes(RtnCode.PARAM_ERROR);
		}
		surveyDao.deleteAllByNoInAndPublishedFalseOrNoInAndStartDateAfter(noList, noList, LocalDate.now());
		return new BaseRes(RtnCode.SUCCESS);
	}

	/**
	 * 功能：根據需要的問卷編號，回傳統計結果
	 */
	@Override
	public StatisticsRes statistics(int no) {
		if (no < 1) {
			return new StatisticsRes(RtnCode.PARAM_ERROR, null);
		}
		// 抓取對應編號的答覆
		List<Reply> ans = replyDao.findBySurveyNo(no);
		if (CollectionUtils.isEmpty(ans)) {
			return new StatisticsRes(RtnCode.NO_REPLY, null);
		}
		// 判斷哪些題目需要統計，並將要統計的題目裝入陣列中
		Optional<Survey> quiz = surveyDao.findById(no);
		List<QuestionCreate> qu = new ArrayList<>();   // 裡面裝著題目的問題清單，選項格式為 String，需再用";"切開
		try {
			qu = mapper.readValue(quiz.get().getQuestions(), new TypeReference<List<QuestionCreate>>() {
			});
		} catch (JsonProcessingException e) {
			return new StatisticsRes(RtnCode.JSON_ERROR, null);
		}
		List<Integer> typeOption = new ArrayList<>();     // 裡面裝著題目的 index，加一即題號
		List<Integer> typeText = new ArrayList<>();     // 裡面裝著題目的 index，加一即題號
		for (QuestionCreate item : qu) {
			System.out.println("136 option: "+item.getOption());
			if (StringUtils.hasText(item.getOption())) {  // 如果問題的選項非空，則需要統計
				typeOption.add(qu.indexOf(item));
				System.out.println("138 add "+ qu.indexOf(item));
			} else {
				typeText.add(qu.indexOf(item));
				System.out.println("141 add "+ qu.indexOf(item));
			}
		}
		// 根據題號去計算每一題
		for (Integer id : typeOption) {
			String ansLongString = new String("");   // 每一題都要建一個字串
			for (Reply eachAns : ans) {   // 此時回答為 String
				try {
					Map<Integer, String> eachAnsInMapForm = mapper.readValue(eachAns.getAnswers(),
							new TypeReference<Map<Integer, String>>() {   // 將每一筆回答轉換成 Map，以題號為 key
							});
					ansLongString += eachAnsInMapForm.get(id);
					countOption(id, qu.get(id).getOption(), ansLongString); // 傳入題號、該題的選項、答案鏈
				} catch (JsonProcessingException e) {
					return new StatisticsRes(RtnCode.JSON_ERROR, null);
				}
			}
		}
		for(Integer id : typeText) {
			Map<String, Integer> ansCountMap = new HashMap<>();   // 每題都要建一個 map
			for(Reply eachAns : ans) {               
				try {
					Map<Integer, String> eachAnsInMapForm = mapper.readValue(eachAns.getAnswers(),
							new TypeReference<Map<Integer, String>>() {   // 將每一筆回答轉換成 Map，以題號為 key
							});
					if(!ansCountMap.containsKey(eachAnsInMapForm.get(id))) {   // 如果 ansMap 中沒有此 key 值，表示這是第一次出現的答覆
						ansCountMap.put(eachAnsInMapForm.get(id), 1);
					} else {
						ansCountMap.put(eachAnsInMapForm.get(id), ansCountMap.get(eachAnsInMapForm.get(id)) + 1);
					}
				} catch (JsonProcessingException e) {
					return new StatisticsRes(RtnCode.JSON_ERROR, null);
				}
			}
			answerCountMap.put(id, ansCountMap);
		}
		return new StatisticsRes(RtnCode.SUCCESS, answerCountMap);
	}
	
	private Map<Integer, Map<String, Integer>> answerCountMap = new HashMap<>();
	
	// 根據傳入的題目選項進行計算，並將統計結果記錄在 Map 中
	private void countOption(int index, String option, String ansString) {
		System.out.println("182: "+ option + " " + ansString);
		Map<String, Integer> ansCountMap = new HashMap<>(); // 每題進入都會重置一次
		String[] optionList = option.split(";");
		Arrays.sort(optionList, (a, b) -> Integer.compare(b.length(), a.length()));
		for (String op : optionList) {
			int lengthBefore = ansString.length();
			ansString = ansString.replace(op, "");
			int lengthAfter = ansString.length();
			int count = (lengthBefore - lengthAfter) / op.length();
			System.out.println("193"+" op:"+op+" lengthBefore:"+lengthBefore+" lengthAfter:"+lengthAfter+" count"+count);
			ansCountMap.put(op, count);
		}
		answerCountMap.put(index, ansCountMap);
	}

	@Override
	public ResultRes resultList(int no) {
		if (no < 1) {
			return new ResultRes(RtnCode.PARAM_ERROR, null);
		}
		return new ResultRes(RtnCode.SUCCESS, replyDao.findBySurveyNo(no));
	}

	/**
	 * 功能：新增傳入的答案<br>
	 * 防呆 (1)判斷輸入內容邏輯<br>
	 * (2)確認同一人的答案不存在
	 */
	@Override
	public BaseRes reply(int surveyNo, String name, String phone, String email, int age, String ans) {
		if (!checkInputReply(surveyNo, phone, name, email, age, ans)) {
			return new BaseRes(RtnCode.PARAM_ERROR);
		}
		if (replyDao.existsById(new ReplyId(surveyNo, phone))) {
			return new BaseRes(RtnCode.DUPLICATED_REPLY);
		}
		replyDao.save(new Reply(surveyNo, name, phone, email, age, ans));
		return new BaseRes(RtnCode.SUCCESS);
	}

	/**
	 * 1. 檢查 Account/Password 格式是否正確<br>
	 * 2. 檢查 Account 是否存在<br>
	 * 3. 檢查 Password 是否相同<br>
	 **/
	@Override
	public BaseRes signIn(String account, String pw) {
		if (checkACIsF(account) || checkPWIsF(pw)) {
			return new BaseRes(RtnCode.PARAM_ERROR);
		}
		Optional<Login> op = loginDao.findById(account);
		if (op.isEmpty()) {
			return new BaseRes(RtnCode.ACCOUNT_NOT_EXISTS);
		}
		if (!encoder.matches(pw, op.get().getPw())) {
			return new BaseRes(RtnCode.PASSWORD_INCORRECT);
		}
		return new BaseRes(RtnCode.SUCCESS);
	}

	@Override
	public BaseRes signUp(String account, String pw) {
		if (checkACIsF(account) || checkPWIsF(pw)) {
			return new BaseRes(RtnCode.PARAM_ERROR);
		}
		if (loginDao.existsById(account)) {
			return new BaseRes(RtnCode.ACCOUNT_EXISTS);
		}
		loginDao.save(new Login(account, encoder.encode(pw)));
		return new BaseRes(RtnCode.SUCCESS);
	}

	/**
	 * 功能：判斷是否為後台搜尋，搜尋符合傳入條件的表單
	 */
	@Override
	public SearchRes search(String surveyName, LocalDate startDate, LocalDate endDate, boolean front, String author) {
		if (!StringUtils.hasText(surveyName)) {
			surveyName = "";
		}
		if (startDate == null) {
			startDate = LocalDate.of(1912, 1, 1);
		}
		if (endDate == null) {
			endDate = LocalDate.of(9999, 12, 31);
		}
		List<Survey> temp;
		if (front) {
			temp = surveyDao.frontSearch(surveyName, startDate, endDate);
		} else {
			temp = surveyDao.backSearch(surveyName, startDate, endDate, author);
		}
		List<SurveyUseForm> resList = new ArrayList<SurveyUseForm>();
		for (Survey item : temp) {
			try {
				SurveyUseForm x = new SurveyUseForm(item.getNo(), item.getAuthor(), item.getName(),
						item.getDescription(), item.isPublished(), item.getStartDate(), item.getEndDate(), null,
						mapper.readValue(item.getQuestions(), new TypeReference<List<Question>>() {
						}));
				if (!item.isPublished()) {
					x.setState("尚未發布");
				}
				if (item.getEndDate().isBefore(LocalDate.now()) && item.isPublished()) {
					x.setState("已結束");
				}
				if (item.getStartDate().isAfter(LocalDate.now()) && item.isPublished()) {
					x.setState("尚未開始");
				}
				if (!item.getStartDate().isAfter(LocalDate.now()) && !item.getEndDate().isBefore(LocalDate.now())
						&& item.isPublished()) {
					x.setState("進行中");
				}
				resList.add(x);
			} catch (JsonProcessingException e) {
				return new SearchRes(RtnCode.JSON_ERROR, null);
			}
		}
		return new SearchRes(RtnCode.SUCCESS, resList);
	}

	// ------------------Survey Area------------------------//
	/**
	 * 1. 確認參數是否為 null<br>
	 * 2. 確認問卷區塊是否符合邏輯<br>
	 * result: 格式均正確 return True, 有錯誤 return False
	 **/
	private boolean checkInputSurvey(String author, String name, LocalDate startDate, LocalDate endDate,
			List<QuestionCreate> questions) {
		if (author == null || name == null || startDate == null || endDate == null
				|| CollectionUtils.isEmpty(questions)) {
			return false;
		}
		if (!author.matches("[\\w&&[^_]]{6,14}") || startDate.isAfter(endDate)) {
			return false;
		}
		for (QuestionCreate item : questions) {
			if (item.getQuestion() == null || item.getType() == null
					|| !item.getType().equals("text") && item.getOption() == null) {
				return false;
			}
		}
		return true;
	}

	// ------------------Reply Area------------------------//
	/**
	 * 1. 確認參數是否null<br>
	 * 2. 確認區塊是否符合邏輯<br>
	 * result: 格式均正確 return True, 有錯誤 return False.
	 **/
	private boolean checkInputReply(int surveyNo, String phone, String name, String email, int age, String answers) {
		if (!StringUtils.hasText(phone) || !StringUtils.hasText(name) || !StringUtils.hasText(email)
				|| !StringUtils.hasText(answers)) {
			return false;
		}
		if (surveyNo <= 0 || !phone.matches("09\\d{8}") || !email.matches(".{1,}@.{1,}") || age < 0) {
			return false;
		}
		return true;
	}

	/**
	 * 1. 將參數強制轉型<br>
	 * 2. 將轉型後的 Reply 中存入 String 轉換成的 List<br>
	 * result: return ReplyUseForm
	 */
//	private ReplyUseForm convertToReplyUseForm(Reply req) throws JsonMappingException, JsonProcessingException {
//		ReplyUseForm temp = (ReplyUseForm) req;
//		temp.setAnswerList(mapper.readValue(req.getAnswers(), new TypeReference<List<Answer>>() {
//		}));
//		return temp;
//	}
	// -----------Login Area------------//
	/**
	 * 1. 檢查 Account 內是否有有意義文字<br>
	 * 2. 檢查 Account 格式是否正確<br>
	 * 若均正確，return False<br>
	 * 若有錯誤，return True
	 **/
	private boolean checkACIsF(String account) {
		if (!StringUtils.hasText(account)) {
			return true;
		}
		String patternAC = "[\\w&&[^_]]{6,14}";
		return !(account.matches(patternAC));
	}

	/**
	 * 1. 檢查 Password 內是否有有意義文字<br>
	 * 2. 檢查 Password 格式是否正確<br>
	 * 若均正確，return False<br>
	 * 若有錯誤，return True
	 **/
	private boolean checkPWIsF(String password) {
		if (!StringUtils.hasText(password)) {
			return true;
		}
		String patternPW1 = "[\\W||\\w]{8,16}";
		String patternPW2 = ".{0,}[\\W_]+.{0,}";
		return !(password.matches(patternPW1) && password.matches(patternPW2));
	}

	@Override
	public SearchRes searchForHome(int type) {
		List<Survey> temp = new ArrayList<Survey>();
		switch (type) {
		case 1:
			// 即將結束
			temp = surveyDao.selectComingEnd();
			break;
		case 2:
			// 最近開始
			temp = surveyDao.selectRecentlyStart();
			break;
		case 3:
			// 即將開始
			temp = surveyDao.selectComingStart();
		}
		List<SurveyUseForm> resList = new ArrayList<SurveyUseForm>();
		for (Survey item : temp) {
			try {
				SurveyUseForm x = new SurveyUseForm(item.getNo(), item.getAuthor(), item.getName(),
						item.getDescription(), item.isPublished(), item.getStartDate(), item.getEndDate(), null,
						mapper.readValue(item.getQuestions(), new TypeReference<List<Question>>() {
						}));
				if (!item.isPublished()) {
					x.setState("尚未發布");
				}
				if (item.getEndDate().isBefore(LocalDate.now()) && item.isPublished()) {
					x.setState("已結束");
				}
				if (item.getStartDate().isAfter(LocalDate.now()) && item.isPublished()) {
					x.setState("尚未開始");
				}
				if (!item.getStartDate().isAfter(LocalDate.now()) && !item.getEndDate().isBefore(LocalDate.now())
						&& item.isPublished()) {
					x.setState("進行中");
				}
				resList.add(x);
			} catch (JsonProcessingException e) {
				return new SearchRes(RtnCode.JSON_ERROR, null);
			}
		}
		return new SearchRes(RtnCode.SUCCESS, resList);
	}

}
