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
import org.json.JSONObject;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

import com.baustem.bndservice.utils.OBRConfigUtil;
import com.baustem.bndservice.utils.StateUtil;
import com.baustem.bndservice.utils.TextFile;

public class SGMgmtLog extends HttpServlet {

	private static final long serialVersionUID = -4465989863328166912L;

	private static Log log = LogFactory.getLog(SGMgmtLog.class);
	
	private BundleContext context;
	
	public void setContext(BundleContext context) {
		this.context = context;
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
		
		String sgname = req.getParameter("sgname");
		
		Bundle[] bundles = context.getBundles();
		String location = "";
		for (Bundle bnd : bundles) {
			if(bnd.getSymbolicName().equalsIgnoreCase(sgname)){
				location = bnd.getLocation();
				break;
			}
		}
		
		String logfile = OBRConfigUtil.getLogStoreFile();
		String readStr = TextFile.read(logfile);
		Bundle bnd = context.getBundle(location);
		Map<String,String> mapInfo = new LinkedHashMap<>();
		Dictionary<String, String> dictionary = bnd.getHeaders();
		
		String displayname = dictionary.get("Bundle-Name");
		String sgicon = dictionary.get("Bundle-Icon");
		String category = dictionary.get("Bundle-Category");
		String desc = dictionary.get("Bundle-Description");
		String version = dictionary.get("Bundle-Version");
		
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
		
		JSONObject js = new JSONObject(mapInfo);
		String resultStr = js.toString();
		
		int ix = readStr.lastIndexOf("sgname:"+sgname);
		if(ix==-1){
			out.write("{\"result\":\"sgname:"+sgname+"暂无日志\"}");
			log.info("{\"result\":\"sgname:"+sgname+"暂无日志\"}");
		}else{
			out.write("the log content is:\n"+resultStr);
		}
		
		
		
		
	}
	
	
	
	
	
	
	
	
}
