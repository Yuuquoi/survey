package com.example.survey.vo;

import java.util.List;

import com.example.survey.constants.RtnCode;
import com.example.survey.entity.Reply;

public class ResultRes extends BaseRes {
	
	private List<Reply> replyList;

	public ResultRes() {
		super();
	}

	public ResultRes(RtnCode rtnCode) {
		super(rtnCode);
	}

	public ResultRes(RtnCode rtnCode, List<Reply> replyList) {
		super(rtnCode);
		this.replyList = replyList;
	}

	public List<Reply> getReplyList() {
		return replyList;
	}

	public void setReplyList(List<Reply> replyList) {
		this.replyList = replyList;
	}
	
}
