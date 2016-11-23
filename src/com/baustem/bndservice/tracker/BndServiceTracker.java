package com.baustem.bndservice.tracker;

import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.http.HttpService;
import org.osgi.service.http.NamespaceException;
import org.osgi.util.tracker.ServiceTracker;

import com.baustem.bndservice.servlet.ActivateSG;
import com.baustem.bndservice.servlet.ClearSGMgmtLog;
import com.baustem.bndservice.servlet.DeploySG;
import com.baustem.bndservice.servlet.InstalledSGList;
import com.baustem.bndservice.servlet.ObrService;
import com.baustem.bndservice.servlet.SGList;
import com.baustem.bndservice.servlet.SGMgmtLog;
import com.baustem.bndservice.servlet.SimpleServlet;
import com.baustem.bndservice.servlet.UninstallSG;

@SuppressWarnings("rawtypes")
public class BndServiceTracker extends ServiceTracker {
	
	private BundleContext context;
	
	private static Log log = LogFactory.getLog(BndServiceTracker.class);
	
	@SuppressWarnings("unchecked")
	public BndServiceTracker(BundleContext context){
		super(context,HttpService.class.getName(),null);
		setContext(context);
	}
	
	public void setContext(BundleContext context) {
		this.context = context;
	}
	
	@Override
	public Object addingService(ServiceReference reference) {
		@SuppressWarnings("unchecked")
		HttpService httpService = (HttpService) super.addingService(reference);
		if(httpService == null){
			log.info("get the httpService is failed:"+httpService);
			return null;
			
		}
		
		/**
		 * simple servlet
		 */
		SimpleServlet simple = new SimpleServlet();
		simple.setContext(context);
		register(httpService, "/simple", simple);
		
		/**
		 * register SGList 
		 */
		SGList sgList = new SGList();
		sgList.setContext(context);
		register(httpService, "/getsglist", sgList);
		
		/**
		 * register getInstalledSGList
		 */
		InstalledSGList insgList = new InstalledSGList();
		insgList.setContext(context);
		register(httpService,"/getinstalledsglist",insgList);
		
		/**
		 * register ActivateSG
		 */
		ActivateSG activateSg = new ActivateSG();
		activateSg.setContext(context);
		register(httpService,"/activateSG",activateSg);
		
		/**
		 * register DeploySG
		 */
		DeploySG deploySG = new DeploySG();
		deploySG.setContext(context);
		register(httpService,"/deploySG",deploySG);
		
		/**
		 * register UninstallSG
		 */
		UninstallSG uninstallSg = new UninstallSG();
		uninstallSg.setContext(context);
		register(httpService,"/uninstallSG",uninstallSg);
		
		/**
		 * register SGMgmtLog 
		 */
		SGMgmtLog sg = new SGMgmtLog();
		sg.setContext(context);
		register(httpService, "/getsgmgmtlog", sg);
		
		/**
		 * register ClearSGMgmtLog
		 */
		ClearSGMgmtLog clearLog = new ClearSGMgmtLog();
		clearLog.setContext(context);
		register(httpService,"/clearsgmgmtlog",clearLog);
		
		/**
		 * register ObrService
		 */
		ObrService os = new ObrService();
		os.setContext(context);
		register(httpService, "/osgimgmt", os);
		
		
		
		
		return httpService;
		
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public void removedService(ServiceReference reference, Object service) {
		HttpService httpService = (HttpService) service;
		
		/**
		 * unregister /simple
		 */
		unRegister(httpService,"/simple");
		/**
		 * unregister getInstalledSGList
		 */
		unRegister(httpService, "/getsglist");
		/**
		 * unregister simple servlet
		 */
		unRegister(httpService, "/getinstalledsglist");
		/**
		 * unregister  InstallSG
		 */
		unRegister(httpService,"/activateSG");
		/**
		 * unregister  ObrService
		 */
		unRegister(httpService, "/deploySG");
		/**
		 * unregister /uninstallSG
		 */
		unRegister(httpService,"/uninstallSG");
		/**
		 * unregister /getsgmgmtlog
		 */
		unRegister(httpService,"/getsgmgmtlog");
		/**
		 * unregister /clearsgmgmtlog
		 */
		unRegister(httpService,"/clearsgmgmtlog");
		/**
		 * unregister /osgimgmt
		 */
		unRegister(httpService,"/osgimgmt");
	}
	
	

	private void register(HttpService httpService,String baseName,Servlet servlet) {
		try {
			httpService.registerServlet(baseName, servlet,null, null);
		} catch (ServletException e) {
			log.info("ServletException "+e.getMessage());
			e.printStackTrace();
		} catch (NamespaceException e) {
			log.info("NameException "+e.getMessage());
			e.printStackTrace();
		}
	}
	
	private void unRegister(HttpService httpService,String baseName) {
		log.info("unregister the service of "+baseName);
		httpService.unregister(baseName);
	}
	
	
}
