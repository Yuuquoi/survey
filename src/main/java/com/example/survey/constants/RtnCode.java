package com.example.survey.constants;

public enum RtnCode {

	SUCCESS(200, "Sucess !!"), //
	DUPLICATED_REPLY(400, "Duplicated reply !!"), //
	SURVEY_EXISTS(400, "Survey exists !!"),//
	SURVEY_NOT_EXISTS(400, "Survey not exists !!"),//
	JSON_ERROR(400, "JSON error!!"), //
	PARAM_ERROR(400, "Param error !!"), //
	PARAM_NULL(400, "Param null !!"),;

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
