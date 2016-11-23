package com.baustem.bndservice.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.URL;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.felix.bundlerepository.Repository;
import org.apache.felix.bundlerepository.RepositoryAdmin;
import org.apache.felix.bundlerepository.Resolver;
import org.apache.felix.bundlerepository.Resource;
import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.util.tracker.ServiceTracker;

import com.baustem.bndservice.utils.OBRConfigUtil;

public class ObrService extends HttpServlet {

	
	private static final long serialVersionUID = -221233475868825375L;
	private static Log log = LogFactory.getLog(ObrService.class);
	
	
	private BundleContext context;
	private RepositoryAdmin repoAdmin;
	
	public void setContext(BundleContext context) {
		this.context = context;
	}
	
	public RepositoryAdmin getRepoAdmin(){
		ServiceTracker<?,?> serviceTracker = null;
		if(repoAdmin==null){
			try {
				serviceTracker = new ServiceTracker<>(context, context.createFilter("(objectClass=" + RepositoryAdmin.class.getName() + ")"), null);
				serviceTracker.open();
				repoAdmin = (RepositoryAdmin) serviceTracker.getService();
			} catch (InvalidSyntaxException e) {
				e.printStackTrace();
			}
		}
		return repoAdmin;
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
		
		RepositoryAdmin repoAdmin = getRepoAdmin();
		Resolver resolver = repoAdmin.resolver();
		
		String url = OBRConfigUtil.getUrl();
		boolean obrState = obrAddRepos(url);
		log.info("repositoryURL add state is "+obrState+"!");
		
		String symbolicName = req.getParameter("sgname");
		//格式为："(|(presentationname=" + symbolicName+")(symbolicname="+symbolicName+"))";
		String filterStr = "(|(symbolicname="+symbolicName+"))";
		Resource[] res = null;
		try {
			res = repoAdmin.discoverResources(filterStr);
		} catch (InvalidSyntaxException e) {
			log.info("doPost.repoAdmin.discoverResources()"+e.getMessage());
		}
		if(res!=null&&res.length!=0) {
			resolver.add(res[0]);
		}
		if((resolver.getAddedResources() != null) && (resolver.getAddedResources().length > 0)) {
			if (resolver.resolve()) {
				try {
					resolver.deploy(0);
					log.info("depoly is completed!");
				} catch (Exception ex) {
					log.info("depoly is failed!");
				}
			}
		}
		
		
		String method = req.getParameter("method");
		
		if("activateSG".equalsIgnoreCase(method)){
			req.getRequestDispatcher("/activateSG")
			   .forward(req, resp);
		}else if("getsglist".equalsIgnoreCase(method)){
			req.getRequestDispatcher("/getsglist").forward(req, resp);
		}else if("simple".equalsIgnoreCase(method)){
			req.getRequestDispatcher("/simple").forward(req, resp);
		}else if("deploySG".equalsIgnoreCase(method)){
			req.getRequestDispatcher("/deploySG")
			   .forward(req, resp);
		}else if("uninstallSG".equalsIgnoreCase(method)){
			req.getRequestDispatcher("/uninstallSG")
			   .forward(req, resp);
		}else if("getinstalledsglist".equalsIgnoreCase(method)){
			req.getRequestDispatcher("/getinstalledsglist").forward(req, resp);
		}else if("getsgmgmtlog".equalsIgnoreCase(method)){
			req.getRequestDispatcher("/getsgmgmtlog")
			   .forward(req, resp);
		}else if("clearsgmgmtlog".equalsIgnoreCase(method)){
			req.getRequestDispatcher("/clearsgmgmtlog")
			   .forward(req, resp);
		}
		
	
	}
	
	
	private boolean obrAddRepos(String url) {
		RepositoryAdmin repoAdmin=getRepoAdmin();
		if(url==null||url.trim()=="")
			return false;
		String[] urls = url.split(";");
		int flag = 0;
		for (String u : urls) {
			try {
				URL srcUrl = new URL(u);
				String protocol = srcUrl.getProtocol();
				String host = srcUrl.getHost();
				
				InetAddress address = InetAddress.getByName(host);
				String hostAddress = address.getHostAddress();
				
				int port = srcUrl.getPort();
				String path = srcUrl.getPath();
				u = protocol+"://"+hostAddress+":"+port+path;
				Repository repo = repoAdmin.addRepository(u);
				Resource[] res = repo.getResources();
				log.info("url: "+u+" repo.getResources().length is "+res.length);
			} catch (Exception e) {
				++flag;
				log.info("addRepository url: "+u+" is failed!");
			}
			
		}
		if(flag<urls.length)
			return true;
		else
			return false;
	}
	
	
	
	

}
