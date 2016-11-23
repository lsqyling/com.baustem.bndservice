package com.baustem.bndservice.entity;

public class ErrorCodeMessage {

	private String errorCode;
	private String errorMessage;

	public ErrorCodeMessage() {
		super();
	}

	public ErrorCodeMessage(String errorCode, String errorMessage) {
		super();
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

}
