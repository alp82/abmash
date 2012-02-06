package com.abmash.webservice;


import com.abmash.api.Browser;

import java.util.HashMap;


public class WebService {

	private HashMap<String, ServiceMethod> services = new HashMap<String, ServiceMethod>();
	
	public void addMethod(String name, ServiceMethod serviceMethod) {
		services.put(name, serviceMethod);
	}
	
	public void invoke(String name, Browser browser) {
		// TODO check if name exists
		services.get(name).invoke(browser);
	}

}
