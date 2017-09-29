/**
 * 
 */
package de.xwic.appkit.webbase.editors;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * @author lippisch
 *
 */
public class EditorScriptUtil {

	protected Locale locale = null;
	protected TimeZone timeZone = TimeZone.getDefault();
	/**
	 * JWicTools.
	 */
	public EditorScriptUtil(Locale locale) {
		super();
		this.locale = locale;
	}

	/**
	 * Instantiate with TimeZone informations.
	 * @param locale
	 * @param timeZone
	 */
	public EditorScriptUtil(Locale locale, TimeZone timeZone) {
		super();
		this.locale = locale;
		this.timeZone = timeZone;
	}


	
	/**
	 * Returns the time in short format. If time is 0:0:0.0 an empty String is returned.
	 * @param time
	 * @return
	 */
	public String formatDate(Date date) {
		if (date == null) {
			return ""; 
		}
		DateFormat format = SimpleDateFormat.getDateInstance(SimpleDateFormat.SHORT, locale);
		format.setTimeZone(timeZone);
		return format.format(date);
	}
	
}
