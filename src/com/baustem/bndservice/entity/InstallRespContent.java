package com.baustem.bndservice.entity;

public class InstallRespContent {

	private CodeAndMessage result;

	public InstallRespContent() {
		super();
	}

	public InstallRespContent(CodeAndMessage result) {
		super();
		this.result = result;
	}

	public CodeAndMessage getResult() {
		return result;
	}

	public void setResult(CodeAndMessage result) {
		this.result = result;
	}

	@Override
	public String toString() {
		return "InstallRespConent [result=" + result + "]";
	}

}
