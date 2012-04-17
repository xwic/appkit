/*
 * Copyright (c) 2009 Network Appliance, Inc.
 * All rights reserved.
 */

package de.xwic.appkit.core.util;

import java.util.Calendar;
import java.util.Date;

import de.xwic.appkit.core.model.entities.IPicklistEntry;

/**
 * @author lippisch
 */
public class Equals {

	/**
	 * @param pe1
	 * @param pe2
	 * @return
	 */
	public static boolean equal(IPicklistEntry pe1, IPicklistEntry pe2) {
		if (pe1 == null && pe2 == null) {
			return true;
		}
		if (pe1 == null || pe2 == null) {
			return false;
		}
		
		return pe1.getId() == pe2.getId();
	}
	
	/**
	 * Null save equal.
	 * @param o1
	 * @param o2
	 * @return
	 */
	public static boolean equal(Object o1, Object o2) {
		if (o1 == null && o2 == null) {
			return true;
		}
		if (o1 == null || o2 == null) {
			return false;
		}
		return o1.equals(o2);
	}

	/**
	 * Compares two strings. Ignores whitespaces and case.
	 * @param s1
	 * @param s2
	 * @return
	 */
	public static boolean equalText(String s1, String s2) {
		
		if (s1 == null && s2 == null) {
			return true;
		}
		if (s1 == null || s2 == null) {
			return false;
		}
		
		int i1max = s1.length();
		int i2max = s2.length();
		int i1 = 0;
		int i2 = 0;
		char c1;
		char c2;
		while (i1 < i1max) {
			c1 = s1.charAt(i1++);
			if (!(c1 == ' ' || c1 == '\t' || c1 == '\r' || c1 == '\n')) { // not a white space
				boolean match = false;
				while (i2 < i2max) {
					c2 = s2.charAt(i2++);
					if (!(c2 == ' ' || c2 == '\t' || c2 == '\r' || c2 == '\n')) { // not a white space
						// the ? character is treated as a wildcard, so we can have anything instead 
						// anything but white spaces, of course
						if (c1 == '?' || c2 == '?' || c1 == c2 || Character.toLowerCase(c1) == Character.toLowerCase(c2)) {
							match = true;
						}
						break;
					}
				}
				if (!match) {
					return false;
				}
			}
		}
		if (i2 < i2max) { // still chars left?
			while (i2 < i2max) {
				c2 = s2.charAt(i2++);
				if (!(c2 == ' ' || c2 == '\t' || c2 == '\r' || c2 == '\n')) { // not a white space
					return false; // a valid char is left
				}
			}
		}
		return true;
		
	}
	
	/**
	 * Null save equal day (ignores time). Compares year, month and day. Ignores the time
	 * @param o1
	 * @param o2
	 * @return
	 */
	public static boolean equalDay(Date o1, Date o2) {
		if (o1 == null && o2 == null) {
			return true;
		}
		if (o1 == null || o2 == null) {
			return false;
		}
		
		if (o1.equals(o2)) {
			return true;
		}
		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(o1);
		Calendar cal2 = Calendar.getInstance();
		cal2.setTime(o2);
		if (cal1.get(Calendar.YEAR) != cal2.get(Calendar.YEAR)) return false;
		if (cal1.get(Calendar.MONTH) != cal2.get(Calendar.MONTH)) return false;
		if (cal1.get(Calendar.DAY_OF_MONTH) != cal2.get(Calendar.DAY_OF_MONTH)) return false;
		
		return true;
	}

	/**
	 * Null save equal day and time (ignores time). Compares year, month, day, hour, minute and second.
	 * @param o1
	 * @param o2
	 * @return
	 */
	public static boolean equalDayTime(Date o1, Date o2) {
		if (o1 == null && o2 == null) {
			return true;
		}
		if (o1 == null || o2 == null) {
			return false;
		}
		
		if (o1.equals(o2)) {
			return true;
		}
		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(o1);
		Calendar cal2 = Calendar.getInstance();
		cal2.setTime(o2);
		if (cal1.get(Calendar.YEAR) != cal2.get(Calendar.YEAR)) return false;
		if (cal1.get(Calendar.MONTH) != cal2.get(Calendar.MONTH)) return false;
		if (cal1.get(Calendar.DAY_OF_MONTH) != cal2.get(Calendar.DAY_OF_MONTH)) return false;
		if (cal1.get(Calendar.HOUR_OF_DAY) != cal2.get(Calendar.HOUR_OF_DAY)) return false;
		if (cal1.get(Calendar.MINUTE) != cal2.get(Calendar.MINUTE)) return false;
		if (cal1.get(Calendar.SECOND) != cal2.get(Calendar.SECOND)) return false;
		
		return true;
	}

}
