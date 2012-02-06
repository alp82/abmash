package com.abmash.trial;

import com.abmash.api.Browser;
import com.abmash.api.HtmlElement;


public class StoryMonitoring {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Browser browser = new Browser(null);
		
//		// monitor changes in realtime
//		Event dbEvent = new DatabaseEvent() {
//			public void fire(HtmlElement element, Object value) {
//				write("Element " + element + " value has risen to " + value);
//			}
//		};
//		
//		Monitoring exchangeRateMonitoring = browser.monitor("exchange rate"); // types available: RELOAD and NO_RELOAD
//		exchangeRateMonitoring.addEvent(dbEvent).greaterThan(1.5);
//
//	
//		Event webServiceEvent = new WebServiceEvent() {
//			public void fire(HtmlElement element, Object value) {
//				write("Temperature " + element + " exceeded limits to new value " + value);
//				invoke("alert");
//			}
//		};
//		
//		Monitoring tempMonitoring = browser.monitor("boiler temperature"); // types available: RELOAD and NO_RELOAD
//		tempMonitoring.addEvent(webServiceEvent).lessThan("10").greaterThan("40");
	}

}
