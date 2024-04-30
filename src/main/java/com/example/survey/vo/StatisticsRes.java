package com.example.survey.vo;

import java.util.Map;

import com.example.survey.constants.RtnCode;

public class StatisticsRes extends BaseRes {
	
	private Map<Integer, Map<String, Integer>> result;
	
	public StatisticsRes() {
		super();
	}

	public StatisticsRes(RtnCode rtnCode) {
		super(rtnCode);
	}

	public StatisticsRes(RtnCode rtnCode, Map<Integer, Map<String, Integer>> result) {
		super(rtnCode);
		this.result = result;
	}

	public Map<Integer, Map<String, Integer>> getResult() {
		return result;
	}

	public void setResult(Map<Integer, Map<String, Integer>> result) {
		this.result = result;
	}
	
}
