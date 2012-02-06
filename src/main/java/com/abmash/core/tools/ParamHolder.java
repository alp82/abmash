package com.abmash.core.tools;

import com.abmash.api.data.Date;

import java.util.ArrayList;


import java.util.HashMap;
import java.util.Iterator;


public class ParamHolder {

	protected HashMap<String, ArrayList<String>> keys= new HashMap<String, ArrayList<String>>();

	protected HashMap<String, Integer> ints = new HashMap<String, Integer>();
	protected HashMap<String, Double> doubles = new HashMap<String, Double>();
	protected HashMap<String, String> strings = new HashMap<String, String>();
	protected HashMap<String, Date> dates = new HashMap<String, Date>();
	
	public ParamHolder() {
		// TODO constructor params
	}
	
	// getters
	
	public Integer getInteger(String key) {
		return ints.get(key);
	}
	
	public Double getDouble(String key) {
		return doubles.get(key);
	}
	
	public String getString(String key) {
		return strings.get(key);
	}
	
	public Date getDate(String key) {
		return dates.get(key);
	}
	
	// adders
	
	public void setInteger(String key, Integer value) {
		if(addKeyIfUnique(key, Integer.class.getSimpleName())) {
			ints.put(key, value);
		}
	}
	
	public void setDouble(String key, Double value) {
		if(addKeyIfUnique(key, Double.class.getSimpleName())) {
			doubles.put(key, value);
		}
	}
	
	public void setString(String key, String value) {
		if(addKeyIfUnique(key, String.class.getSimpleName())) {
			strings.put(key, value);
		}
	}
	
	public void setDate(String key, Date value) {
		if(addKeyIfUnique(key, Date.class.getSimpleName())) {
			dates.put(key, value);
		}
	}
	
	private Boolean addKeyIfUnique(String key, String type) {
		for (String otherType: keys.keySet()) {
			if(!otherType.equals(type) && keys.get(otherType).contains(key)) return false;
		}

		// add the key
		if(!keys.keySet().contains(type)) {
			keys.put(type, new ArrayList<String>());
		}
		keys.get(type).add(key);
		
		return true;
	}
	

}
