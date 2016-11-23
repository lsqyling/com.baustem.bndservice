    package com.baustem.bndservice.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Dictionary;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

import com.baustem.bndservice.utils.ResponseUtil;
import com.baustem.bndservice.utils.StateUtil;
/**
 * 获取已经安装的bundle 列表
 * 格式如下：{[
				{
					“sgname”:”smarthomeSG”,
					“displayname”:”精品智能家居”,
					“sgicon”:”…/icon.png”,
					“category”:”3rdasp”,
					“desc”:”any text”
				},
				…
			]} 

 * @author 10580
 *
 */
public class SGList extends HttpServlet {

	private static final long serialVersionUID = 7789534415648116255L;
	
	private static Log log = LogFactory.getLog(SGList.class );
	private BundleContext context;
	private Map<String,String> map;
	
	public void setContext(BundleContext context) {
		this.context = context;
	}
	
	public void setMap(Map<String, String> map) {
		this.map = map;
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}
	
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		resp.setCharacterEncoding("utf-8");
		resp.setContentType("application/json");
		PrintWriter out = resp.getWriter();
		
		JSONArray jsonArr = new JSONArray();
		Map<String,String> mapInfo = null;
		Bundle[] bndArr = context.getBundles();
		
		for (Bundle bnd : bndArr) {
			Dictionary<String, String> dictionary = bnd.getHeaders();
			
			String sgname = dictionary.get("Bundle-SymbolicName");
			String displayname = dictionary.get("Bundle-Name");
			String sgicon = dictionary.get("Bundle-Icon");
			String category = dictionary.get("Bundle-Category");
			String desc = dictionary.get("Bundle-Description");
			String version = dictionary.get("Bundle-Version");
			
			
			mapInfo = new LinkedHashMap<>();
			mapInfo.put("sgname",sgname);
			mapInfo.put("displayname", displayname);
			mapInfo.put("sgicon", sgicon);
			mapInfo.put("category", category);
			mapInfo.put("desc", desc);
			mapInfo.put("version", version);
			int state = 0;
			state = bnd.getState();
			String status = StateUtil.getBundleState(state);
			mapInfo.put("status", status);
			jsonArr.put(mapInfo);   
			
		}
		
		String jsonStr = jsonArr.toString();
		if(jsonStr !=null&&jsonStr!=""){
			out.write(jsonStr);
			log.info("result:\n"+jsonStr);
		} else {
			String erroResp = ResponseUtil.errRespJson();
			out.write(erroResp);
			log.info("result:\n"+erroResp);
		}
		
		
		
		
	}
	


}
