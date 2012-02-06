package com.abmash.TODO.browser;

import com.abmash.api.Browser;
import com.abmash.core.tools.ParamHolder;

public abstract class InteractionChain {
	
	protected ParamHolder paramHolder = new ParamHolder();
	
	public abstract void perform(Browser browser);
	
	public ParamHolder inputParams() {
		return paramHolder;
	}

}
