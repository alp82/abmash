package com.abmash.extraction;


import com.abmash.api.Browser;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;


public class TestWithDatabase {
	
	protected static Browser browser;
	
	protected static Connection conn = null;
	
	protected static Properties properties = new Properties();
	
	public TestWithDatabase() {
		// read properties
		try {
			FileInputStream stream;
			stream = new FileInputStream("private.properties");
			properties.load(stream);
			stream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}

	protected static void createDBConnection(String host, String dbName, String user, String pass) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://" + host + "/" + dbName, user, pass);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	protected static void closeDBConnection() {
		try {
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
}
