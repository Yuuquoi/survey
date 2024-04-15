package com.example.survey.constants;

public enum RtnCode {

	SUCCESS(200, "Sucess !!"), //
	ACCOUNT_NOT_EXISTS(400, "Account not exists !!"), //
	ACCOUNT_EXISTS(400, "Account exists !!"), //
	DUPLICATED_REPLY(400, "Duplicated reply !!"), //
	SURVEY_EXISTS(400, "Survey exists !!"),//
	SURVEY_NOT_EXISTS(400, "Survey not exists !!"),//
	JSON_ERROR(400, "JSON error!!"), //
	PARAM_ERROR(400, "Param error !!"), //
	PASSWORD_INCORRECT(400, "Password incorrect !!");

	private int code;

	private String message;

	private RtnCode(int code, String message) {
		this.code = code;
		this.message = message;
	}

	public int getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}

}
