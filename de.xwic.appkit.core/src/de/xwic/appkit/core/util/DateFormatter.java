package de.xwic.appkit.core.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

/**
 * @author Adrian Ionescu
 */
public final class DateFormatter {

	// DATE
	public static final String formatDate = "dd-MMM-yyyy";
	private static final String formatDay = "EEE";
	private static final String formatMonthYear = "MMM yyyy";
	private static final String formatYMD = "yyyy-MM-dd";
	private static final String formatDM = "dd/MM";
	private static final String formatDDMMM = "dd MMM";
	private static final String formatDMY = "dd/MM/yyyy";
	private static final String formatDMYNoSeparator = "ddMMyyyy";
	private static final String formatMDY = "MM/dd/yyyy";
	private static final String formatYMMMD = "yyyy-MMM-dd";
	private static final String formatMMMYY = "MMM-yy";

	// TIME
	private static final String formatDateTime = "dd-MMM-yyyy HH:mm";
	private static final String formatDateTimeTz = "dd-MMM-yyyy HH:mm z";
	private static final String formatDateTimeSeconds = "dd-MMM-yyyy HH:mm:ss";
	private static final String formatDateTimeSecondsNoSeparator = "ddMMyyyy_HHmmss";
	private static final String formatDateTimeMillisecondsNoSeparator = "ddMMyyyy_HHmmssSSS";
	private static final String formatDateTimeSecondsNoSpaces = "yyyyMMddHHmmss";
	private static final String formatDateTimeAmPm = "dd-MMM-yyyy hh:mm aa";
	private static final String formatDateTimeAmPmTz = "dd-MMM-yyyy hh:mm aa z";
	private static final String formatDateTimeAmPmSeconds = "dd-MMM-yyyy hh:mm:ss aa";
	private static final String formatDateTimeAmPmSecondsTz = "dd-MMM-yyyy hh:mm:ss aa z";
	public static final String formatTime = "hh:mm aa";

	/**
	 * 
	 */
	private DateFormatter() {
	}

	//
	// DATE
	//

	/**
	 * @return
	 */
	public static DateFormat getDateFormat() {
		return new SimpleDateFormat(formatDate);
	}

	/**
	 * @return
	 */
	public static DateFormat getDay() {
		return new SimpleDateFormat(formatDay);
	}

	/**
	 * @return
	 */
	public static DateFormat getMonthYear() {
		return new SimpleDateFormat(formatMonthYear);
	}

	/**
	 * @return
	 */
	public static DateFormat getYMD() {
		return new SimpleDateFormat(formatYMD);
	}

	/**
	 * @return
	 */
	public static DateFormat getDM() {
		return new SimpleDateFormat(formatDM);
	}

	/**
	 * @return
	 */
	public static DateFormat getDDMMM() {
		return new SimpleDateFormat(formatDDMMM);
	}

	/**
	 * @return
	 */
	public static DateFormat getDMY() {
		return new SimpleDateFormat(formatDMY);
	}

	/**
	 * @return
	 */
	public static DateFormat getDMYNoSeparator() {
		return new SimpleDateFormat(formatDMYNoSeparator);
	}

	/**
	 * @return
	 */
	public static DateFormat getMDY() {
		return new SimpleDateFormat(formatMDY);
	}

	/**
	 * @return
	 */
	public static DateFormat getYMMMD() {
		return new SimpleDateFormat(formatYMMMD);
	}

	/**
	 * @return
	 */
	public static DateFormat getMMMYY() {
		return new SimpleDateFormat(formatMMMYY);
	}

	//
	// TIME
	//

	/**
	 * @return
	 */
	public static DateFormat getDateTime(TimeZone tz) {
		SimpleDateFormat df = new SimpleDateFormat(formatDateTime);

		if (tz != null) {
			df.setTimeZone(tz);
		}

		return df;
	}

	/**
	 * @return
	 */
	public static DateFormat getDateTimeTz(TimeZone tz) {
		SimpleDateFormat df = new SimpleDateFormat(formatDateTimeTz);

		if (tz != null) {
			df.setTimeZone(tz);
		}

		return df;
	}

	/**
	 * @return
	 */
	public static DateFormat getDateTimeSeconds(TimeZone tz) {
		SimpleDateFormat df = new SimpleDateFormat(formatDateTimeSeconds);

		if (tz != null) {
			df.setTimeZone(tz);
		}

		return df;
	}

	/**
	 * @return
	 */
	public static DateFormat getDateTimeSecondsNoSeparator(TimeZone tz) {
		SimpleDateFormat df = new SimpleDateFormat(formatDateTimeSecondsNoSeparator);

		if (tz != null) {
			df.setTimeZone(tz);
		}

		return df;
	}

	/**
	 * @return
	 */
	public static DateFormat getDateTimeMillisecondsNoSeparator(TimeZone tz) {
		SimpleDateFormat df = new SimpleDateFormat(formatDateTimeMillisecondsNoSeparator);

		if (tz != null) {
			df.setTimeZone(tz);
		}

		return df;
	}

	/**
	 * @return
	 */
	public static DateFormat getDateTimeSecondsNoSpaces(TimeZone tz) {
		SimpleDateFormat df = new SimpleDateFormat(formatDateTimeSecondsNoSpaces);

		if (tz != null) {
			df.setTimeZone(tz);
		}

		return df;
	}

	/**
	 * @return
	 */
	public static DateFormat getDateTimeAmPm(TimeZone tz) {
		SimpleDateFormat df = new SimpleDateFormat(formatDateTimeAmPm);

		if (tz != null) {
			df.setTimeZone(tz);
		}

		return df;
	}

	/**
	 * @param tz
	 * @return
	 */
	public static DateFormat getDateTimeAmPmTz(TimeZone tz) {
		SimpleDateFormat df = new SimpleDateFormat(formatDateTimeAmPmTz);

		if (tz != null) {
			df.setTimeZone(tz);
		}

		return df;
	}

	/**
	 * @return
	 */
	public static DateFormat getDateTimeAmPmSeconds(TimeZone tz) {
		SimpleDateFormat df = new SimpleDateFormat(formatDateTimeAmPmSeconds);

		if (tz != null) {
			df.setTimeZone(tz);
		}

		return df;
	}

	/**
	 * @return
	 */
	public static DateFormat getDateTimeAmPmSecondsTz(TimeZone tz) {
		SimpleDateFormat df = new SimpleDateFormat(formatDateTimeAmPmSecondsTz);

		if (tz != null) {
			df.setTimeZone(tz);
		}

		return df;
	}

	/**
	 * @param tz
	 * @return
	 */
	public static DateFormat getTime(TimeZone tz) {
		SimpleDateFormat df = new SimpleDateFormat(formatTime);

		if (tz != null) {
			df.setTimeZone(tz);
		}

		return df;
	}

}
