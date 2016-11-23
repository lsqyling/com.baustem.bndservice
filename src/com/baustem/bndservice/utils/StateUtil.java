package com.baustem.bndservice.utils;

import java.util.HashMap;
import java.util.Map;

public class StateUtil {
	
	public static String getBundleState(int state){
		String result = "error";
		Map<Integer,String> map = new HashMap<>();
		
		map.put(0, "Error");
		map.put(1, "Uninstalled");
		map.put(2, "Installed");
		map.put(4, "Resolved");
		map.put(8, "Starting");
		map.put(16, "Stopping");
		map.put(32, "Active");
		result = map.get(state);
		
		return result;
	}
	
	

}
