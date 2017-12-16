package com.rabbithole.mongodb.utils;

import java.util.Calendar;
import java.util.Date;

public final class DateUtils {

	private DateUtils() {}
	
	/**
	 * Get Year from Java java.util.Date
	 *
	 * @param date
	 *
	 * @return year
	 */
	public static int getYearFromDate(final Date date) {

		final Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.YEAR);
	}

}
