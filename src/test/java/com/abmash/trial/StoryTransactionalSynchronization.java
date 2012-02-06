package com.abmash.trial;

import com.abmash.api.Browser;
import com.abmash.core.browser.BrowserConfig;
import com.abmash.webservice.WebService;

public class StoryTransactionalSynchronization {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Browser browser = new Browser(null);
		
		// synchronize data with transactions
//		DataSource dbDataSource = new DBDataSource(new Database());
//		dbSource.setTable("tableName");
//		dbSource.setFields("id, key, value");
//		
//		DataSource webDataSource = new WebDataSource(new WebService());
//		webSource.setReadMethod("serviceMethodNameToGetData"); // using any fitting element
//		webSource.setWriteMethod("serviceMethodNameToWriteData"); // using input fields
//		webSource.setFields("ID, Name, Value");
//
//		Synchronization sync = new Synchronization(browser, dbDataSource, webDataSource);
//		boolean success = sync.execute(); // start transaction, then read and write data on both sources
//		sync.disconnect();
	}

}
