package com.abc;

import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.time.DateUtils;

public class DateProvider {
    
	private static DateProvider instance = null;
	
	public static final double MONTHS = 12.0;
	public static final double WEEKS = 4.0;
	public static final double DAYS = 7.0;

	public static DateProvider getInstance() {
		if (instance == null)
			instance = new DateProvider();
		return instance;
	}

	public Date now() {	
		return Calendar.getInstance().getTime();
	}
    
	public static Date roundDate(Date date) {
		return DateUtils.setHours(date,0);
	}
    
	public static Date getDatePast(Date date, int days) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.DATE, -days);
		return c.getTime();
	}
    
	public static Date getDateFuture(Date date, int days) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.DATE, days);
		return c.getTime();
	}
}
