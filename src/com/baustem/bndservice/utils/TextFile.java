package com.baustem.bndservice.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;

public class TextFile extends ArrayList<String> {
	
	//Read a file as single string
	public static String read(String fileName){
		
		StringBuilder sb = new StringBuilder();
		try {
			BufferedReader in = new BufferedReader(new FileReader(
					new File(fileName).getAbsoluteFile()));
			try {
				String s ;
				while((s=in.readLine())!=null){
					sb.append(s);
					sb.append("\n");
				}
			} finally{
				in.close();
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sb.toString();
	}
	
	public static boolean clearFile(String filePath){
		
		File file = new File(filePath);
		try {
			
			if(!file.exists()){
				file.createNewFile();
			}
			FileWriter fw = new FileWriter(file);
			fw.append("");
		} catch (IOException e) {
			return false;
		}
		
		return true;
		
	}
	
	
	public static void main(String[] args) {
		
		String readStr = read("/logs/bndservice.log");
		boolean flag = clearFile("/logs/bndservice.log");
		System.out.println(flag);
		
		/*//^\\{[\"sgname\":\".*\",][\".*\":\".*\",]{6}+|$\\}
		String regex = "\\{(\"sgname\":\"com.baustem.bndservice\",)(\"\\w+\":\".*\",){5}(\"status\":\"\\w+\")|$\\}";
		
		int index = readStr.lastIndexOf("sgname:com.baustem.bndservice");
		
		
		
		
		Pattern p = Pattern.compile(regex);
		Matcher matcher = p.matcher(readStr);
		while (matcher.find()) {
			int end = matcher.end();
			
			int groupCount = matcher.groupCount();
			System.out.println("-------------------");
			
			System.out.println(matcher.group());
		}*/
		
		String str = "a ab abc abcd";
		
		char charAt = str.charAt(0);
		System.out.println(charAt);
		
		System.out.println(str.lastIndexOf("abc "));
		
		
		Map<String,String> map = new HashMap<String, String>();
		
		map.put("1", "metata");
		
		map.put("2", "jicjvo;djfij");
		System.out.println(map.get("1"));
		
		System.out.println(Integer.MAX_VALUE);
		
		
		
	}
	
	
	

}
