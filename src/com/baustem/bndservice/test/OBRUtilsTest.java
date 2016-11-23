package com.baustem.bndservice.test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.baustem.bndservice.utils.OBRConfigUtil;

public class OBRUtilsTest {
	private static Log log = LogFactory.getLog(OBRConfigUtil.class);
	
	public static void main(String[] args) {
		String url = OBRConfigUtil.getUrl();
		String bindIp = OBRConfigUtil.getBindIp();
		System.out.println(url);
		System.out.println(bindIp);
	}
	
	public static String getUrl(){
		String url = "";
		InputStream input = OBRConfigUtil.class.getResourceAsStream("/obr-config.properties");
		Properties prop = new Properties();
		try {
			prop.load(input);
		} catch (IOException e) {
			log.info("OBRConfigUtil.getUrl(): "+e.getMessage());
		} catch (NullPointerException e){
			log.info("OBRConfigUtil.getUrl(): "+e.getMessage());
		} finally{
			url = prop.getProperty("OSGIUrl");
			log.info("OBRConfigUtil.getUrl():OSGIUrl="+url);
		}
		return url;
	}
	
	public static String getBindIp(){
		String ip = "";
		InputStream input = OBRConfigUtil.class.getResourceAsStream("/obr-config.properties");
		Properties prop = new Properties();
		try {
			prop.load(input);
		} catch (IOException e) {
			log.info("OBRConfigUtil.getBindIp(): "+e.getMessage());
		}  catch (NullPointerException e){
			log.info("OBRConfigUtil.getBindIp(): "+e.getMessage());
		} finally{
			ip = prop.getProperty("bindIp");
			log.info("OBRConfigUtil.getUrl():bindIp="+ip);
		}
		return ip;
	}
	
	public static String getLogStoreFile(){
		String logfile = "";
		Properties prop = new Properties();
		try {
			prop.load(OBRConfigUtil.class.getResourceAsStream("/log4j.properties"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		logfile = prop.getProperty("log4j.appender.file.File");
		return logfile;
	}
	

}
