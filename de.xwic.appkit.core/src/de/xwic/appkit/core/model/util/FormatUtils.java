/**
 * 
 */
package de.xwic.appkit.core.model.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Utils class for formatting. <p>
 * 
 * @author Ronny Pfretzschner
 *
 */
public class FormatUtils {

	private final static Log log = LogFactory.getLog(FormatUtils.class);
	private final static Pattern pattern = Pattern.compile("\\+[0-9]{1,3}[ ]*\\([0-9]+\\)[ ]*[0-9]+[0-9, ,]+[ ]*/[ ]*[0-9]+[0-9, ,\\-,/]+[0-9]+");
	
	/**
	 * @param number
	 * @return if the string parameter matches the telephone number pattern
	 */
	public static boolean matchNumber(String number) {
		//ignore empty numbers and return true
		if (number == null || number.length() < 1) {
			return true;
		}
		
		Matcher matcher = pattern.matcher(number);
		return matcher.matches();
	}

	/**
	 * Formats the phonenumber. 
	 * The global access pattern "+49 (0) ..." will be returned.<p>
	 * 
	 * @param telNr
	 * @return a formatted tel number, if possible, not null or the given number, if any errors appear 
	 */
	public static String formatTelNr(String telNr) {
		if (telNr == null || telNr.length() < 1) {
			return "";
		}
		
//		telNr = telNr.replaceAll("[^\\+,^0-9,^ ,^/,^-]",  "");
//		telNr = telNr.replaceAll("[^\\(,^\\)]", "");
//		telNr = telNr.replaceAll("[(]", " ");
//		telNr = telNr.replaceAll("[)]", "/");
		telNr = telNr.trim();
		String tel = "";
		
		String countryCode = getCountryCode(telNr, false);
		
		try {
			if (!hasCountryCode(telNr)) {
				//cut off "0"
				tel = getFormatedNumberWithoutCountryCode(telNr);
			}
			else {
				tel = telNr.substring(getCountryCode(telNr, true).length(), telNr.length());
				if (tel.startsWith("-") || tel.startsWith("/")) {
					tel = tel.substring(1, tel.length());
				}
				tel = tel.trim();
				tel = getFormatedNumberWithoutCountryCode(tel);
			}

		} catch (RuntimeException e) {
			log.error("Error occurred while formating telefon-no. Using the origin String...");
			return telNr;
		}
		return countryCode + " " + tel;
	}
	
	private static String getFormatedNumberWithoutCountryCode(String newTel) {
		newTel = newTel.replaceAll("[(]", " ");
		newTel = newTel.replaceAll("[)]", "/");
		newTel = newTel.trim();
		
		if (newTel.startsWith("0")) {
			newTel = newTel.substring(1, newTel.length());
			newTel = newTel.trim();
		}
		
		//trying to get the first digits of the "vorwahl"
		//mostly "/" is used as delimiter
		int firstIdx = newTel.indexOf("/");
		
		//nothing found, maybe only " " is used as delimiter
		if (firstIdx == -1) {
			firstIdx = newTel.indexOf(" ");
		}

		//"/" not found -> "-" is no so often used, mostly at end, could be dangerous
		if (firstIdx == -1) {
			firstIdx = newTel.indexOf("-");
		}
		
		if (firstIdx == -1) {
			firstIdx = newTel.length();
		}
		String vorwahl = newTel.substring(0, firstIdx);
		vorwahl = vorwahl.trim();
		
		String lastNo = "";
		if (newTel.length() >= vorwahl.length() + 1) {
			lastNo = newTel.substring(vorwahl.length() + 1, newTel.length());
		}
		lastNo = lastNo.trim();
		lastNo = lastNo.replaceAll("[/, ]", "");
		
		
		return "(0)" + splashNumber(vorwahl) + " / " + splashNumber(lastNo);
	}
	
	/**
	 * Splits a number string into pairs seperated by a space. If the 
	 * number of numbers is not a multiple of 2, the first number stands 
	 * alone.
	 * @param num
	 * @return
	 */
	private static String splashNumber(String num) {
		
		// splash lastNo
		StringBuffer sb = new StringBuffer();
		int nr = 0;
		for (int i = num.length() - 1; i >= 0; i--) {
			char c = num.charAt(i);
			if (c >= '0' && c <= '9') {
				if (nr == 2) {
					sb.insert(0, ' ');
					nr = 0;
				}
				sb.insert(0, c);
				nr++;
			} else if (c == '-') {
				sb.insert(0, " - ");
				nr = 0;
			} else {
				sb.insert(0, c);
			}
		}
		return sb.toString();
		
	}
	
	private static String getCountryCode(String telNr, boolean rawFormat) {
		String countryCode = "49";
		
		//trying to get the first digits of the "countrycode"
		//mostly "/" is used as delimiter
		int firstIdx = telNr.indexOf("/");

		//"-" not found -> " " is no so often used
		if (firstIdx == -1) {
			firstIdx = telNr.indexOf(" ");
		}

		//nothing found, maybe only "-" is used as delimiter
		if (firstIdx == -1) {
			firstIdx = telNr.indexOf("-");
		}
		
		//avoid index out of bound
		if (firstIdx == -1) {
			firstIdx = telNr.length();
		}
		
		//just raw format -> return it
		if (rawFormat) {
			return telNr.substring(0, firstIdx);
		}
		
		if (telNr.startsWith("00")) {
			countryCode = telNr.substring(2, firstIdx);
		}
		else if (telNr.startsWith("+")) {
			countryCode = telNr.substring(1, firstIdx);
		}
		return "+" + countryCode;
	}

	
	private static boolean hasCountryCode(String telNr) {
		return telNr != null && (telNr.startsWith("+") || telNr.startsWith("00")); 
	}
	
	
}
