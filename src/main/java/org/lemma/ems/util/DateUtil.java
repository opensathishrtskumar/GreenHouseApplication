package org.lemma.ems.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

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

	public static String getHHmm() {
		return getFormattedDate("hh:mm a");
	}

	public static String getFormattedDate(String format) {
		SimpleDateFormat formater = new SimpleDateFormat(format);
		return formater.format(new Date(System.currentTimeMillis()));
	}

	public static String getFormattedTime(long timeInMilli, String dateFormat) {
		SimpleDateFormat format = new SimpleDateFormat(dateFormat);
		return format.format(new Date(timeInMilli));
	}

	public static Pattern getValueByAddressPattern(String memory) {
		return Pattern.compile(memory + "=[\\d.]+");
	}

	public static long parseDateTime(String dateTime, String format) throws Exception {
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		return formatter.parse(dateTime).getTime();
	}
}
