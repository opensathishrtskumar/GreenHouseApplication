package org.lemma.ems.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;

import org.joda.time.LocalDate;

/**
 * @author RTS Sathish Kumar
 *
 */
public abstract class DateUtil {

	public static final String hh_mma = "hh:mma";
	public static final String DASHBOARD_FMT = "dd-MMM,yyyy";
	public static final String DASHBOARD_POLLED_FMT = "dd-MMM,yy hh:mm a";
	public static final String REPORTNAME_FORMAT = "ddMMyyHHmm";

	public static final String SUMMARY_FMT1 = "dd-MM-yy : HH:mm";
	public static final String SUMMARY_FMT = "HH:mm";
	public static final String DD_MM_YY = "dd/MMM/yy";

	public static final String DD_MM_YYYY_HH_MM_S = "dd/MM/yyyy HH:mm:s";
	public static final String EXCEL_REPORTNAME_FORMAT = "ddMMMyyyyHHmm";

	/**
	 * @return
	 */
	public static String getHHmm() {
		return getFormattedDate("hh:mm a");
	}

	/**
	 * @param format
	 * @return
	 */
	public static String getFormattedDate(String format) {
		SimpleDateFormat formater = new SimpleDateFormat(format);
		return formater.format(new Date(System.currentTimeMillis()));
	}

	/**
	 * @param timeInMilli
	 * @param dateFormat
	 * @return
	 */
	public static String getFormattedTime(long timeInMilli, String dateFormat) {
		SimpleDateFormat format = new SimpleDateFormat(dateFormat);
		return format.format(new Date(timeInMilli));
	}

	/**
	 * @param memory
	 * @return
	 */
	public static Pattern getValueByAddressPattern(String memory) {
		return Pattern.compile(memory + "=[\\d.]+");
	}

	/**
	 * @param dateTime
	 * @param format
	 * @return
	 * @throws Exception
	 */
	public static long parseDateTime(String dateTime, String format) throws Exception {
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		return formatter.parse(dateTime).getTime();
	}

	/**
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static long findDateDiff(Date startDate, Date endDate) {
		Calendar calendar1 = Calendar.getInstance();
		Calendar calendar2 = Calendar.getInstance();
		calendar1.set(startDate.getYear(), startDate.getMonth(), startDate.getDate());
		calendar2.set(endDate.getYear(), endDate.getMonth(), endDate.getDate());

		long miliSecondForDate1 = calendar1.getTimeInMillis();
		long miliSecondForDate2 = calendar2.getTimeInMillis();
		long diffInMilis = miliSecondForDate2 - miliSecondForDate1;

		return (diffInMilis / (24 * 60 * 60 * 1000));
	}

	/**
	 * @param date
	 * @return
	 */
	public static long getEndOfDay(long date) {
		return getEndOfDay(new Date(date));
	}

	/**
	 * @param date
	 * @return
	 */
	public static long getEndOfDay(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		calendar.set(Calendar.MILLISECOND, 999);
		return calendar.getTime().getTime();
	}

	/**
	 * @param date
	 * @return
	 */
	public static long getStartOfDay(long date) {
		return getStartOfDay(new Date(date));
	}

	/**
	 * @param date
	 * @return
	 */
	public static long getStartOfDay(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime().getTime();
	}

	/**
	 * @param today
	 * @return
	 */
	public static long[] dateRange(LocalDate today) {

		int month = today.getMonthOfYear();
		LocalDate yesterday = today.minusDays(1);

		long[] timerange = new long[2];

		if (month != yesterday.getMonthOfYear()) {
			today = today.withMonthOfYear(yesterday.getMonthOfYear());
		}

		timerange[0] = getStartOfDay(today.withDayOfMonth(1).toDate().getTime());
		timerange[1] = getEndOfDay(today.withDayOfMonth(yesterday.getDayOfMonth()).toDate().getTime());

		return timerange;
	}
}
