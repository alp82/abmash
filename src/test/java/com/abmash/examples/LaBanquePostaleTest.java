package com.abmash.examples;

import com.abmash.api.Browser;

public class LaBanquePostaleTest {
	
	private static Browser browser;

	public static void main(String[] args) {
		// read webdriver and profile stuff from property file and create remote browser instance
		browser = new Browser(null);
		
		browser.openUrl("https://www.labanquepostale.fr/index.html");
		browser.click("Accès à vos comptes");
		browser.hover("div#clavier > ul > li > a");
		browser.debug().breakpoint();

		browser.close();
	}

}
