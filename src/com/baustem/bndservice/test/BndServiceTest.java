package com.baustem.bndservice.test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import com.baustem.bndservice.utils.OBRConfigUtil;

public class BndServiceTest {
	
	private Hello hello = new Hello();
	
	public BndServiceTest() {
		System.out.println("Hello BndSerivcetest");
	}
	
	
	public static void main(String[] args) {
		new BndServiceTest();
		String str = "abc;d;e;f;j;hello;world;";
		String[] split = str.split(";");
		System.out.println(split);
		
		String s = "abcdefgjhjjdinvjikadnfnh;";
		String[] split2 = s.split(";");
		System.out.println(split2);
		
		String url = "";
		Properties prop = new Properties();
		String envObr = System.getenv("HGW_INTEGRATION_CONFIG_PATH_RW");
		String getenv = System.getenv("JAVA_HOME");
		if(envObr.endsWith("/")){
			envObr = envObr.substring(0,envObr.lastIndexOf("/"));
		}
		try {
			prop.load(OBRConfigUtil.class.getResourceAsStream(envObr+"/obr-config.properties"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		url = prop.getProperty("obrUrl");
		
	}
	
	
	

}
