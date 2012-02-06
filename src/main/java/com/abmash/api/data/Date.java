package com.abmash.api.data;


import com.abmash.api.HtmlElement;

import java.util.Calendar;
import java.util.GregorianCalendar;


/**
 * Date class to extract dates from web pages.
 * <p>
 * If an {@link HtmlElement} contains a date call {@link HtmlElement#extractDate()} to extract it. 
 * 
 * @author Alper Ortac
 * @see HtmlElement#extractDate()
 */
public class Date {

	GregorianCalendar calendar;
	private int day;
	private int month;
	private int year;
	private int hour;
	private int minute;
	private int second;
	private int ampm;
	
	/**
	 * Constructor which takes a Java Date.
	 * 
	 * @param date
	 */
	public Date(java.util.Date date) {
		calendar = new GregorianCalendar();
		calendar.setTime(date);
		day = calendar.get(Calendar.DAY_OF_MONTH);
		month = calendar.get(Calendar.MONTH) + 1;
		year = calendar.get(Calendar.YEAR);
		hour = calendar.get(Calendar.HOUR);
		if(hour == 0) hour = 12;
		minute = calendar.get(Calendar.MINUTE);
		second = calendar.get(Calendar.SECOND);
		ampm = calendar.get(Calendar.AM_PM);
	}

	/**
	 * Gets day of this date.
	 * 
	 * @return day
	 */
	public String getDay() {
		return String.valueOf(day);
	}

	/**
	 * Gets month of this date.
	 * 
	 * @return month
	 */
	public String getMonth() {
		return String.valueOf(month);
	}

	/**
	 * Gets year of this date.
	 * 
	 * @return year
	 */
	public String getYear() {
		return String.valueOf(year);
	}

	/**
	 * Gets hour of this date.
	 * 
	 * @return hour
	 */
	public String getHour() {
		return String.valueOf(hour);
	}

	/**
	 * Gets minute of this date.
	 * 
	 * @return minute
	 */
	public String getMinute() {
		return String.valueOf(minute);
	}

	/**
	 * Gets second of this date.
	 * 
	 * @return second
	 */
	public String getSecond() {
		return String.valueOf(minute);
	}
	
	/**
	 * Gets AM or PM of this date.
	 * 
	 * @return "am" or "pm"
	 */
	public String getAmpm() {
		return ampm == Calendar.AM ? "am" : "pm";
	}
	
	/**
	 * Gets {@link GregorianCalendar} instance.
	 * 
	 * @return calendar instance
	 */
	public GregorianCalendar getCalendar() {
		return calendar;
	}
}
