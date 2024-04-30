package com.example.survey.vo;

import java.util.List;

public class DeleteReq {
	
	private List<Integer> noList;

	public DeleteReq() {
		super();
	}

	public DeleteReq(List<Integer> noList) {
		super();
		this.noList = noList;
	}

	public List<Integer> getNoList() {
		return noList;
	}

	public void setNoList(List<Integer> noList) {
		this.noList = noList;
	}

}
