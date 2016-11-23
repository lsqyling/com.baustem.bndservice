package com.baustem.bndservice.utils;

import com.baustem.bndservice.entity.Content;
import com.baustem.bndservice.entity.ErrorCodeMessage;
import com.baustem.bndservice.entity.ResponseError;
import com.google.gson.Gson;

public class ResponseUtil {
	
	
	
	
	public static String errRespJson(){
		String jsonStr = "";
		
		ErrorCodeMessage error = new ErrorCodeMessage();
		error.setErrorCode("1");
		error.setErrorMessage("没有找到bundle列表");
		
		Content cont = new Content();
		cont.setCode("1");
		cont.setMethod("getsglist");
		cont.setResult(error);
		
		ResponseError respE = new ResponseError();
		respE.setResponse(cont);
		
		Gson gson = new Gson();
		jsonStr = gson.toJson(respE);
		
		return jsonStr;
	}
	
	public static void main(String[] args) {
		String jsonStr = errRespJson();
		System.out.println(jsonStr);
		
	}
	
	

}
