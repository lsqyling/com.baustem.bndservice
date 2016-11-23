package com.baustem.bndservice.utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class OBRConfigUtil {
	
	private static Log log = LogFactory.getLog(OBRConfigUtil.class);
	
	public static void main(String[] args) {
		String url = getUrl();
		String bindIp = getBindIp();
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
	
	/*public static String getUrl(){
		
		String url = "";
		String srcUrl = "http://service.hmg.baustem.net:16121/SysConfig";
		String content = "ModelName=SYSTEM_WANService_CFG";
		
		try {
			byte [] data = HTTPRequest.post(srcUrl, content.getBytes(), null);
			
			String result = new String(data,"utf-8");
			JSONObject resJson = new JSONObject(result);
			JSONArray jsonArray = resJson.getJSONArray("SYSTEM_WANService_CFG");
			for (int i = 0; i < jsonArray.length(); i++) {
				
				JSONObject tmp = jsonArray.getJSONObject(i);
				
				Iterator iter = tmp.keys();
				
				while(iter.hasNext())
				{
					String key = (String) iter.next();
					if(key.equals("OSGIOBRService"))
					{
						url = tmp.getJSONObject(key).getString("URL");
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(url.equals("")||url==null){
			url = "http://osgiobr.baustem.net:9000/obr/repository.xml;http://192.168.3.184:9000/obr/repository.xml";
		}
		
		
		return url;
	}*/
	/*public static String getBindIp(){
		
		String ip = "";
		Properties prop = new Properties();
		String envObr = System.getenv("HGW_INTEGRATION_CONFIG_PATH_RW");
		if(envObr==null){
			envObr = "/hmt/data/cfg";
		}
		if(envObr.endsWith("/")){
			envObr = envObr.substring(0,envObr.lastIndexOf("/"));
		}
		InputStream fis = null;
		try {
			fis = new FileInputStream(envObr+"/obr-config.ini");
			prop.load(fis);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			if(fis!=null){
				try {
					fis.close();
				} catch (IOException e) {
				}
			}
		}
		ip = prop.getProperty("bindIp");
		if(ip==null||ip.equals("")){
			ip = "192.168.1.202";
			
		}
		return ip;
	}*/
	
	

}
