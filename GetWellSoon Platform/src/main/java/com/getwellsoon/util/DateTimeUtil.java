package com.getwellsoon.util;

import java.util.Calendar;
import java.util.Date;

public class DateTimeUtil {
	private static Calendar calendar = Calendar.getInstance();
	
	public static Date getObsoleteMinute() {
		calendar.set(2010, 0, 1);
		// calendar.roll(Calendar.MINUTE, false);
		return calendar.getTime();
	}
}
