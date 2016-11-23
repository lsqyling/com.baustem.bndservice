package com.baustem.bndservice.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

import com.baustem.bndservice.utils.OBRConfigUtil;
import com.baustem.bndservice.utils.TextFile;

public class ClearSGMgmtLog extends HttpServlet {

	private static final long serialVersionUID = -7257465253901917593L;
	private static Log log = LogFactory.getLog(ClearSGMgmtLog.class);
	
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
		
		boolean flag ;
		if("all".equalsIgnoreCase(sgname)){
			String logStoreFile = OBRConfigUtil.getLogStoreFile();
			flag = TextFile.clearFile(logStoreFile);
			out.write("{\"result\":\"clear "+flag+"\"}");
			
		}else{
			out.write("{\"result\":\"clear ok\"}");
		}
		
		
		
		
	}
	
	
	
	
	
	

}
