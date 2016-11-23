package com.baustem.bndservice.socket;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;
import org.json.JSONObject;
public class ConfigInfo 
{
	private static Log log=LogFactory.getLog(ConfigInfo.class);
	public static String EROUTERNetwork="HSB1";
	public static String Status_on= "1";
	public static String Status_off = "0";
	
	public static Map<String,String>  mapFCCInstance = new HashMap<String, String>();
	
	public static String OSGIWEBURL = "http://192.168.1.202:8090";
	public static String ParentCtrlURL = "";
	public static String ThumbnailHttpServer="http://192.168.1.202:20011" ;
	public static String HttpResourceURL = "http://pro.baustem.net:8080/profile/h5configdata.json";
	
	public static String tag="标签";
	public static String country = "地区";
	public static String released = "时间";
	
	private static Properties pro;
	private static final String SYSTEM_BUSINESS_CFG = "SYSTEM_BUSINESS_CFG";
	private static final String SYSTEM_LANService_CFG = "SYSTEM_LANService_CFG";
	private static final String SYSTEM_WANService_CFG = "SYSTEM_WANService_CFG";
	private static final String SYSTEM_CONFIG_URL ="http://service.hmg.baustem.net:16121/SysConfig";
	public static String DefaultChannelNr = "";
	
	public static JSONArray packageNames = new JSONArray();
	
	public static  void init()
	{
		sysLanCfg();
		sysBussinessCFG();
		try
		{
			String env=System.getenv("HGW_INTEGRATION_CONFIG_PATH");
			String f="";
			if(!env.endsWith("/"))f="/";
			pro=new Properties();
			FileInputStream fis = new FileInputStream(env+f+"serviceurl.ini");
			pro.load(fis);
			fis.close();
			log.info(" HGW_INTEGRATION_CONFIG_PATH :"+env+f+"serviceurl.ini" );
			
			OSGIWEBURL = getValue("OSGIWEBURL", "http://192.168.1.202:8090");
			
			ThumbnailHttpServer  = getValue("ThumbnailHttpServer" , "http://192.168.1.202:20011");
			
			HttpResourceURL = getValue("HttpResourceURL","http://pro.baustem.net:8080/profile/h5configdata.json");
		}
		catch(Exception e)
		{
			log.error("Config:", e);
		}
		try {
		} catch (Exception e) {
			log.error("Config init error:", e);
		}
	}
	
	
	
	public static String getHistoryURL()
	{
		return OSGIWEBURL+"/HistoryService";
	}
	
	public static String getParentCtrlURL()
	{
		if(ParentCtrlURL.equals(""))
		{
			sysLanCfg();
		}
		return ParentCtrlURL;
	}
	
	public static String getFavorURL()
	{
		return OSGIWEBURL+"/FavorService";
	}
	public static String getTagURL()
	{
		return OSGIWEBURL+"/HUserTagService";
	}
	
	public static String getRADeviceManage()
	{
		return OSGIWEBURL+"/radevice";
	}
	public static String getRASessionURL()
	{
		return OSGIWEBURL+"/rasession";
	}
	
	
	public static String getHUserManageURL()
	{
		return OSGIWEBURL+"/husermanage";
	}
	public static String getHUserManageURL_V2()
	{
		return OSGIWEBURL+"/SUserManage";
	}
	
	public static String getCloudURL()
	{
		return OSGIWEBURL+"/cloudServices";
	}
	
	
	public static String getSmartEPGURL()
	{
		return OSGIWEBURL+"/SmartEPGService";
	}
	
	public static String getSmartEPGPoster()
	{
		return OSGIWEBURL+"/SmartEPGService/poster";
	}
	
	public static String getSessionHoldURL()
	{
		return OSGIWEBURL+"/SessionHold";
	}
	
	public static String getMiniatureURL()
	{
		return OSGIWEBURL+"/miniature";
	}
	
	
	public static String recommendURL()
	{
		Map<String,String> map = getNTypeURL("RecommendService");
		
		if("EROUTER".equals(map.get("nType")))
		{
			return map.get("url");
		}
		else
		{
			return OSGIWEBURL+"/recommend";
		}
	}
	public static String getMetadata()
	{
		Map<String,String> map = getNTypeURL("ColumnService");
		
		if("EROUTER".equals(map.get("nType")))
		{
			return map.get("url");
		}
		else
		{
			return OSGIWEBURL+"/metadata";
		}
	}
	
	public static String advertise()
	{
		Map<String,String> map = getNTypeURL("AdService");
		
		if("EROUTER".equals(map.get("nType")))
		{
			return map.get("url");
		}
		else
		{
			return OSGIWEBURL+"/advertise";
		}
	}
	
	public static String getAccountManage()
	{
		return OSGIWEBURL+"/AccountManageService";
	}
	
	public static String getValue(String key)
	{
		String val = pro.getProperty(key);
		
		try {
			val =  new String(val.getBytes("iso8859-1"),"utf-8").trim();
		} catch (Exception e) {
		}
		return val;
	}
	
	public static String getValue(String key,String defaultValue)
	{
		String val = pro.getProperty(key);
		
		try {
			val =  new String(val.getBytes("iso8859-1"),"utf-8").trim();
		} catch (Exception e) {
		}
		if(val == null || val.equals(""))
		{
			val =defaultValue;
		}
		return val;
	}
	
	public static  Map<String,String> getNTypeURL(String keyInput)
	{
		Map<String,String> map = new HashMap<String, String>();
		
		HTTPRequest http = new HTTPRequest();
		try {
			String content = "ModelName="+SYSTEM_WANService_CFG;
			
			byte [] data = http.post(SYSTEM_CONFIG_URL, content.getBytes(), null);
			
			String jsonStr = new String(data,"utf-8");
			JSONObject o = new JSONObject(jsonStr);
			
			JSONArray array = o.getJSONArray(SYSTEM_WANService_CFG);
			
			for(int i =0 ;i<array.length();i++)
			{
				JSONObject tmp = array.getJSONObject(i);
				
				Iterator iter = tmp.keys();
				
				while(iter.hasNext())
				{
					String key = (String) iter.next();
					if(key.equals(keyInput))
					{
						JSONObject  jo = tmp.getJSONObject(key);
						
						map.put("nType", jo.getString("nType"));
						map.put("url", jo.getString("URL"));
						break;
					}
				}
				
			}
			
		} catch (Exception e) {
		}
		
		return map;
	}
	
	
	private static void sysLanCfg()
	{
		HTTPRequest http = new HTTPRequest();
		try {
			String content = "ModelName="+SYSTEM_LANService_CFG;
			
			byte [] data = http.post(SYSTEM_CONFIG_URL, content.getBytes(), null);
			
			String jsonStr = new String(data,"utf-8");
			JSONObject o = new JSONObject(jsonStr);
			
			JSONArray array = o.getJSONArray(SYSTEM_LANService_CFG);
			
			for(int i =0 ;i<array.length();i++)
			{
				JSONObject tmp = array.getJSONObject(i);
				
				Iterator iter = tmp.keys();
				
				while(iter.hasNext())
				{
					String key = (String) iter.next();
					if(key.equals("OsgiWebURL"))
					{
						OSGIWEBURL = tmp.getJSONObject(key).getString("URL");
					}
					if(key.equals("ParentControlURL"))
					{
						ParentCtrlURL = tmp.getJSONObject(key).getString("URL");
					}
				}
				
			}
			
		} catch (Exception e) {
		}
	}
	
	public static void sysBussinessCFG()
	{
		HTTPRequest http = new HTTPRequest();
		try {
			String content = "ModelName="+ConfigInfo.SYSTEM_BUSINESS_CFG;
			
			byte [] data = http.post(ConfigInfo.SYSTEM_CONFIG_URL, content.getBytes(), null);
			
			String jsonStr = new String(data,"utf-8");
			JSONObject o = new JSONObject(jsonStr);
			
			JSONArray array = o.getJSONArray(ConfigInfo.SYSTEM_BUSINESS_CFG);
			
			for(int i =0 ;i<array.length();i++)
			{
				JSONObject tmp = array.getJSONObject(i);
				
				Iterator iter = tmp.keys();
				
				while(iter.hasNext())
				{
					String key = (String) iter.next();
					if(key.equals("ChannelNr"))
					{
						DefaultChannelNr = tmp.getJSONObject(key).getString("ChannelNr");
					}
					if(key.equals("DefaultApps"))
					{
						packageNames = tmp.getJSONObject(key).getJSONArray("PackageName");
					}
				}
			}
		} catch (Exception e) {
		}
	}
	
}
