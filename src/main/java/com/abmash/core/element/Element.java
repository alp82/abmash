package com.abmash.core.element;

import com.abmash.api.data.Date;

import java.text.ParseException;
import java.text.SimpleDateFormat;


import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public abstract class Element {

	protected String id;
	
	public abstract String toString();
	
	public abstract String getText();
	
}