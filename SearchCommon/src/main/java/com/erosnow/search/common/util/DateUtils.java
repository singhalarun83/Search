package com.erosnow.search.common.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

public class DateUtils {

	private static final Logger LOG = LoggerFactory.getLogger(DateUtils.class);

	public static Date stringToDate(String date, String pattern) {
		DateFormat format = new SimpleDateFormat(pattern);
		try {
			return format.parse(date);
		} catch (ParseException e) {
			LOG.debug(e.getMessage());
		}
		return null;
	}

	public static long getNextInterval(Date current, Date reference, long interval) {
		return current.getTime() + (interval - (current.getTime() - reference.getTime()) % interval);
	}

	public static Date getCurrentDate() {
		Calendar now = Calendar.getInstance();
		now.set(now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
		now.set(Calendar.MILLISECOND, 0);
		return now.getTime();
	}
	
	public static String dateToString(Date date, String pattern) {
        if (date != null && StringUtils.hasText(pattern)) {
            DateFormat format = new SimpleDateFormat(pattern);
            return format.format(date);
        } else {
            return null;
        }
    }
}
