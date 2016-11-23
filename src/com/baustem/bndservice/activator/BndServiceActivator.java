package com.baustem.bndservice.activator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import com.baustem.bndservice.tracker.BndServiceTracker;

public class BndServiceActivator implements BundleActivator {

	private static BundleContext context;
	private static Log log = LogFactory.getLog(BndServiceActivator.class);
	
	private BndServiceTracker bndServiceTracker;
	
	
	static BundleContext getContext() {
		return context;
	}
	
	
	/**
	 * bundle start 
	 */
	public void start(BundleContext bundleContext) throws Exception {
		BndServiceActivator.context = bundleContext;
		log.info("hello context "+context);
		bndServiceTracker = new BndServiceTracker(context);
		bndServiceTracker.open();
		log.info("BndServiceTracker.open()");
		
	}
	
	
	/**
	 * bundle stop when the bundle is stopped
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		BndServiceActivator.context = null;
		bndServiceTracker.close();
		bndServiceTracker = null;
		log.info("BndServiceTracker.close()");
	}

}
