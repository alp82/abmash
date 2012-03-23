package com.abmash.REMOVE.api.data;


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
//public class Date {
//
//	GregorianCalendar calendar;
//	private int day;
//	private int month;
//	private int year;
//	private int hour;
//	private int minute;
//	private int second;
//	private int ampm;
//	
//	/**
//	 * Constructor which takes a Java Date.
//	 * 
//	 * @param date
//	 */
//	public Date(java.util.Date date) {
//		calendar = new GregorianCalendar();
//		calendar.setTime(date);
//		year = calendar.get(Calendar.YEAR);
//		month = calendar.get(Calendar.MONTH) + 1;
//		day = calendar.get(Calendar.DAY_OF_MONTH);
//		hour = calendar.get(Calendar.HOUR);
//		if(hour == 0) hour = 12;
//		minute = calendar.get(Calendar.MINUTE);
//		second = calendar.get(Calendar.SECOND);
//		ampm = calendar.get(Calendar.AM_PM);
//	}
//	
//	/**
//	 * Constructor which takes a specific date.
//	 * 
//	 * TODO
//	 */
//	public Date(int year, int month, int day, int hour, int minute, int second) {
//		calendar = new GregorianCalendar();
//		calendar.set(year, month, day, hour, minute, second);
//		this.year = calendar.get(Calendar.YEAR);
//		this.month = calendar.get(Calendar.MONTH) - 1;
//		this.day = calendar.get(Calendar.DAY_OF_MONTH);
//		this.hour = calendar.get(Calendar.HOUR);
//		if(this.hour == 0) this.hour = 12;
//		this.minute = calendar.get(Calendar.MINUTE);
//		this.second = calendar.get(Calendar.SECOND);
//		this.ampm = calendar.get(Calendar.AM_PM);
//	}
//
//	/**
//	 * Gets day of this date.
//	 * 
//	 * @return day
//	 */
//	public String getDay() {
//		return String.valueOf(day);
//	}
//
//	/**
//	 * Gets month of this date.
//	 * 
//	 * @return month
//	 */
//	public String getMonth() {
//		return String.valueOf(month);
//	}
//
//	/**
//	 * Gets year of this date.
//	 * 
//	 * @return year
//	 */
//	public String getYear() {
//		return String.valueOf(year);
//	}
//
//	/**
//	 * Gets hour of this date.
//	 * 
//	 * @return hour
//	 */
//	public String getHour() {
//		return String.valueOf(hour);
//	}
//
//	/**
//	 * Gets minute of this date.
//	 * 
//	 * @return minute
//	 */
//	public String getMinute() {
//		return String.valueOf(minute);
//	}
//
//	/**
//	 * Gets second of this date.
//	 * 
//	 * @return second
//	 */
//	public String getSecond() {
//		return String.valueOf(minute);
//	}
//	
//	/**
//	 * Gets AM or PM of this date.
//	 * 
//	 * @return "am" or "pm"
//	 */
//	public String getAmpm() {
//		return ampm == Calendar.AM ? "am" : "pm";
//	}
//	
//	/**
//	 * Gets {@link GregorianCalendar} instance.
//	 * 
//	 * @return calendar instance
//	 */
//	public GregorianCalendar getCalendar() {
//		return calendar;
//	}
//}
