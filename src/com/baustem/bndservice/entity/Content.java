package com.baustem.bndservice.entity;

public class Content {

	private String method;
	private String code;
	private ErrorCodeMessage result;

	public Content() {
		super();
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public ErrorCodeMessage getResult() {
		return result;
	}

	public void setResult(ErrorCodeMessage result) {
		this.result = result;
	}

	
	
}
