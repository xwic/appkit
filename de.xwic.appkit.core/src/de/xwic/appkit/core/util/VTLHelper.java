/**
 * 
 */
package de.xwic.appkit.core.util;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.xwic.appkit.core.dao.DAO;
import de.xwic.appkit.core.dao.DAOSystem;
import de.xwic.appkit.core.dao.IEntity;
import de.xwic.appkit.core.model.daos.IPicklisteDAO;
import de.xwic.appkit.core.model.entities.IPicklistEntry;

/**
 * Class passed along in VTL engines to help with formatting.
 * 
 * This class is a result of merging several different classes used for the same purpose, therefore you might find similar methods with the
 * same name. We couldn't remove the duplicates because different vtl files can use different methods and it's very hard to look into all of
 * them.
 * 
 * @author Adrian Ionescu
 */
public class VTLHelper {

	private static final Log log = LogFactory.getLog(VTLHelper.class);
	private String lang;
	private Locale locale;
	private TimeZone timeZone;

	private DateFormat dfDateWithTimeZone;
	private DateFormat dfDateTimeWithTimeZone;
	private DateFormat dfDateTimeTz;

	private DateFormat dfDate;
	private DateFormat dfDateTime;

	private static IPicklisteDAO plDoa = (IPicklisteDAO) DAOSystem.getDAO(IPicklisteDAO.class);

	/**
	 * @param locale
	 */
	public VTLHelper(Locale locale) {
		this.lang = locale.getLanguage();
		this.locale = locale;

		dfDate = DateFormatter.getDateFormat();
		dfDateTime = DateFormatter.getDateTimeAmPm(null);
		dfDateTimeTz = DateFormatter.getDateTimeAmPmTz(TimeZone.getDefault());
	}

	/**
	 * @param locale
	 * @param timeZone
	 */
	public VTLHelper(Locale locale, TimeZone timeZone) {
		this(locale);

		this.timeZone = timeZone;

		dfDateWithTimeZone = DateFormatter.getDateFormat();
		if (timeZone != null) {
			dfDateWithTimeZone.setTimeZone(timeZone);
		}

		dfDateTimeWithTimeZone = DateFormatter.getDateTimeAmPmTz(timeZone);

		dfDateTimeTz = DateFormatter.getDateTimeAmPmTz(timeZone);
	}

	//
	// ************ DATE ************
	//

	/**
	 * @param date
	 * @return
	 */
	public String formatDate(Date date) {
		return date != null ? dfDate.format(date) : "";
	}

	/**
	 * @param date
	 * @return
	 */
	public String date(Date date) {
		if (date == null) {
			return "";
		}
		return dfDateWithTimeZone.format(date);
	}

	/**
	 * @param date
	 * @param timeZoneId
	 * @return
	 */
	public String date(Date date, String timeZoneId) {
		if (date == null) {
			return "";
		}

		DateFormat df = DateFormatter.getDateFormat();
		TimeZone usedTz = TimeZone.getDefault();

		if (timeZoneId != null && !timeZoneId.trim().isEmpty()) {
			usedTz = TimeZone.getTimeZone(timeZoneId);
		}

		if (usedTz != null) {
			df.setTimeZone(usedTz);
		}

		return df.format(date);
	}

	/**
	 * @param date
	 * @return
	 */
	public String dateWithTimeZone(Date date) {
		if (date == null) {
			return "";
		}

		return dfDateTimeTz.format(date);
	}

	//
	// ************ TIME ************
	//

	/**
	 * @param date
	 * @return
	 */
	public String formatDateTime(Date date) {
		return date != null ? dfDateTime.format(date) : "";
	}

	/**
	 * @param date
	 * @return
	 */
	public String dateTime(Date date) {
		if (date == null) {
			return "";
		}
		return dfDateTimeWithTimeZone.format(date);
	}

	/**
	 * @param date
	 * @param timeZoneId
	 * @return
	 */
	public String dateTime(Date date, String timeZoneId) {
		if (date == null) {
			return "";
		}

		TimeZone usedTz = TimeZone.getDefault();

		if (timeZoneId != null && !timeZoneId.trim().isEmpty()) {
			usedTz = TimeZone.getTimeZone(timeZoneId);
		}

		DateFormat df = DateFormatter.getDateTimeAmPm(usedTz);
		return df.format(date);
	}

	/**
	 * @param date
	 * @return
	 */
	public String time(Date date) {
		if (date == null) {
			return "";
		}
		DateFormat df = DateFormatter.getTime(timeZone);
		return df.format(date);
	}

	//
	// ************ ENTITIES ************
	//

	/**
	 * @param plEntry
	 * @return
	 */
	public String getPicklistText(IPicklistEntry plEntry) {
		if (plEntry == null) {
			return "";
		}
		return plEntry.getBezeichnung(lang);
	}

	/**
	 * originally especially for IPart.PL_FIN_SERVICE_TYPE
	 * 
	 * @param peKey
	 * @return
	 */
	public String getPicklistText(String plKey, String peKey) {
		if (StringUtil.isEmpty(peKey)) {
			return "";
		}

		IPicklistEntry pe = plDoa.getPickListEntryByKey(plKey, peKey);
		return pe.getBezeichnung(lang);
	}

	/**
	 * @param entity
	 * @return
	 */
	public String formatEntity(IEntity entity) {
		if (entity == null) {
			return "";
		}
		DAO<?> dao = DAOSystem.findDAOforEntity(entity.type());
		return dao.buildTitle(entity);
	}

	/**
	 * @param entity
	 * @return
	 */
	public String buildTitle(IEntity entity) {

		try {
			if (entity == null) {
				return "";
			} else if (entity instanceof IPicklistEntry) {
				IPicklistEntry entry = (IPicklistEntry) entity;
				return entry.getBezeichnung(locale.getLanguage());
			}

			return DAOSystem.findDAOforEntity(entity.getClass()).buildTitle(entity);
		} catch (Exception e) {
			log.error(e);
			return entity.toString();
		}
	}

	//
	// ************ MISC ************
	//

	/**
	 * @param bool
	 * @return
	 */
	public String formatBoolean(boolean bool) {
		return bool ? "Yes" : "No";
	}

	/**
	 * @param d
	 * @return
	 */
	public String formatDouble(Double d) {
		return d != null ? DecimalFormatter.formatDoubleValue(d) : "";
	}

	/**
	 * @param nb
	 * @return
	 */
	public String doubleValue(double nb) {
		return DecimalFormatter.formatDoubleValue(nb);
	}

	/**
	 * @param text
	 * @return
	 */
	public static String html(String text) {

		if (text == null) {
			return "";
		}

		StringBuffer sbHTML = new StringBuffer("");
		//boolean newline = true;
		boolean newline = true;
		boolean wascr = false;

		for (int i = 0; i < text.length(); i++) {
			char c = text.charAt(i);
			switch (c) {
			case 34:
				sbHTML.append("&quot;");
				break;
			case 60:
				sbHTML.append("&lt;");
				break;
			case 62:
				sbHTML.append("&gt;");
				break;
			case 38:
				sbHTML.append("&amp;");
				break;
			case 13:
				// do some
				sbHTML.append("<BR>");
				wascr = true;
				break;

			case 32:
				// space
				if (newline) {
					sbHTML.append("&nbsp;");
				} else {
					sbHTML.append(c);
				}
				wascr = false;
				break;

			case 10:
				if (!wascr) {
					sbHTML.append("<BR>");
				}
				wascr = false;
				break;

			default:
				sbHTML.append(c);
				wascr = false;
				newline = false;

			}

		}

		return sbHTML.toString();
	}

	/**
	 * @param text
	 * @return
	 */
	public static String xml(String text) {
		if (text == null) {
			return "";
		}

		String APOS = "&apos;";
		String AMP = "&amp;";
		String QUOT = "&quot;";
		String LT = "&lt;";
		String GT = "&gt;";

		text = text.replace("&nbsp;", " ");

		StringBuilder sb = new StringBuilder();

		for (char c : text.toCharArray()) {
			switch (c) {
			case '\'':
				sb.append(APOS);
				break;
			case '&':
				sb.append(AMP);
				break;
			case '"':
				sb.append(QUOT);
				break;
			case '<':
				sb.append(LT);
				break;
			case '>':
				sb.append(GT);
				break;
			default:
				sb.append(c);
				break;
			}
		}
		return sb.toString();
	}

	/**
	 * @return
	 */
	public String getTimeZoneAsString() {
		TimeZone tz = timeZone != null ? timeZone : TimeZone.getDefault();
		return tz.getDisplayName(true, TimeZone.SHORT);
	}

	/**
	 * @return
	 */
	public Locale getLocale() {
		return locale;
	}

	/**
	 * @param locale
	 */
	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	/**
	 * Get Or Else function.
	 * 
	 * @param value
	 * @param orElse
	 * @return the value or some specified default(orElse)
	 */
	public <T> T orElse(T value, T orElse) {
		return value != null ? value : orElse;
	}

	/**
	 * @param bool
	 * @return N/A if bool is null, Yes if true, No if false
	 */
	public String yesNo(Boolean bool) {
		if (bool == null) {
			return "N/A";
		}
		return bool ? "Yes" : "No";
	}

	/**
	 * @param s
	 * @return
	 */
	public String lineBreaks(String s) {
		return s.replace("\n", "<br>").replace("\r", "");
	}

}
