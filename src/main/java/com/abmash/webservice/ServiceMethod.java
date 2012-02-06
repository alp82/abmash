package com.abmash.webservice;

import com.abmash.TODO.browser.InteractionChain;
import com.abmash.api.Browser;

public class ServiceMethod {

	private InteractionChain interactionChain;

	public ServiceMethod(InteractionChain interactionChain) {
		this.interactionChain = interactionChain;
	}

	public void invoke(Browser browser) {
		interactionChain.perform(browser);
	}

}
