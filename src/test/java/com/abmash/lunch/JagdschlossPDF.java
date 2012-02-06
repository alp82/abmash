package com.abmash.lunch;

import com.abmash.api.Browser;
import com.abmash.core.pdf.PDFDocument;

public class JagdschlossPDF {

	private static Browser browser;

	public static void main(String[] args) {
		// read webdriver and profile stuff from property file and create remote browser instance
		browser = new Browser(null);
		
		browser.openUrl("http://www.hotel-jagdschloss-kranichstein.de/downloads/downloads.htm");
//		browser.getPDF("Sommerbuffet");
//		PDFDocument document = (PDFDocument) browser.getDocument("Sommerbuffet");
//		System.out.println(document.getHeaderNames());
//		document.getText();
		
		browser.close();
	}
}
