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
import org.osgi.framework.BundleException;

import com.baustem.bndservice.entity.CodeAndMessage;
import com.baustem.bndservice.entity.InstallRespContent;
import com.google.gson.Gson;

public class UninstallSG extends HttpServlet {

	private static final long serialVersionUID = 6407918996143072408L;
	private static Log log = LogFactory.getLog(UninstallSG.class);

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
		String sgname = req.getParameter("sgname");
		
		Bundle[] bundles = context.getBundles();
		String location = "";
		for (Bundle bnd : bundles) {
			if(bnd.getSymbolicName().equalsIgnoreCase(sgname)){
				location = bnd.getLocation();
				break;
			}
		}
		
		int state = -1;
		try {
			
			if(location.length()>0){
				
				Bundle bundle = context.getBundle(location);
				bundle.uninstall();
				state = bundle.getState();
			}
		} catch (BundleException e) {
			log.info("BundleExecption " + e.getMessage());
		}

		PrintWriter out = resp.getWriter();
		String result = "";
		switch (state) {

		case 32:
			result = getResult("32", "symbolicName:" + sgname + "的bundle 正在运行");
			out.write(result);
			log.info(result);
			break;
		case 2:
			result = getResult("2", "symbolicName:" + sgname
					+ "的Bundle 已经成功部署，可以启动");
			out.write(result);
			log.info(result);
			break;
		case 4:
			result = getResult("4", "symbolicName:" + sgname
					+ "的Bundle 已经被解析而且可以被启动");
			out.write(result);
			log.info(result);
			break;
		case 8:
			result = getResult("8", "symbolicName:" + sgname
					+ "的当前Bundle 正在启动的过程");
			out.write(result);
			log.info(result);
			break;
		case 1:
			result = getResult("1", "symbolicName: " + sgname 
					+ " uninstall ok!");
			out.write(result);
			log.info(result);
			break;
		case -1:
			result = getResult("-1", "symbolicName:" + sgname + "uninstall failed!");
			out.write(result);
			log.info(result);
		}

	}

	private String getResult(String code, String message) {
		String result = "";
		Gson gson = new Gson();
		CodeAndMessage cm = new CodeAndMessage();
		cm.setCode(code);
		cm.setMessage(message);
		InstallRespContent irc = new InstallRespContent();
		irc.setResult(cm);
		result = gson.toJson(irc);
		return result;
	}

}
