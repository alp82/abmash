//package com.abmash.TODO.browser.server;
//
//import org.openqa.selenium.server.SeleniumServer;
//
//@SuppressWarnings("serial")
//public class SeleniumRemoteBrowserServer implements BrowserServer {
//
//	SeleniumServer seleniumServer;
//	
//	public SeleniumRemoteBrowserServer() {
//		 try {
//			seleniumServer = new SeleniumServer();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//	
//	public void start() {
//		try {
//			seleniumServer.start();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//
//	public void stop() {
//		seleniumServer.stop();
//	}
//
//}
