package com.abmash.trial;

import com.abmash.TODO.browser.InteractionChain;
import com.abmash.api.Browser;
import com.abmash.webservice.ServiceMethod;
import com.abmash.webservice.WebService;

public class StoryWebService {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Browser browser = new Browser(null);
		
		// wrap functions as web service (internally described as WSDL 2.0)
		ServiceMethod serviceMethod = new ServiceMethod(
			new InteractionChain() {
				@Override
				public void perform(Browser browser) {
					browser.openUrl("http://example.com");
					browser.click("foo");
					browser.type("name", "bar");
				}
			}
		);
		WebService webService = new WebService();
		webService.addMethod("foobar", serviceMethod);
		
		webService.invoke("foobar", browser);
		
	}

}
