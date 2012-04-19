package com.abmash.core.browser;

import java.util.List;
import java.util.Map;

import org.openqa.selenium.WebElement;

public class JavaScriptResult {
	
	private Object returnValue = null;
	
	public JavaScriptResult(Object returnValue) {
		this.returnValue = returnValue;
	}
	
	public Boolean isNull() {
		return returnValue == null;
	}
	
	public Object getReturnValue() {
		return returnValue;
	}
	
	public Class<? extends Object> getReturnValueClass() {
		return returnValue.getClass();
	}
	
	public Boolean hasAdequateReturnValue() {
		// TODO other return values
		if(returnValue instanceof WebElement) {
			return true;
		}
		if(returnValue instanceof Double) {
			return (Double) returnValue != 0.0;
		}
		if(returnValue instanceof Long) {
			return (Long) returnValue != 0;
		}
		if(returnValue instanceof Boolean) {
			return (Boolean) returnValue;
		}
		if(returnValue instanceof String) {
			return !((String) returnValue).equals("");
		}
		if(returnValue instanceof List<?>) {
			return !((List<?>) returnValue).isEmpty();
		}
		if(returnValue instanceof Map<?,?>) {
			return !((Map<?,?>) returnValue).isEmpty();
		}
		return false;
	}

}
