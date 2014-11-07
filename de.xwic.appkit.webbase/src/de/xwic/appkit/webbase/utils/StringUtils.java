/*
 * (c) Copyright 2005, 2006 by pol GmbH 
 * 
 * de.xwic.appkit.webbase.utils.StringUtils
 * Created on Mar 22, 2007 by Aron Cotrau
 *
 */
package de.xwic.appkit.webbase.utils;

/**
 * @author Aron Cotrau
 */
public class StringUtils {

	public static final String[] EMPTY_STRING_ARRAY = new String[0];

	/**
	 * @param all
	 * @param what
	 * @return if 'what' is equal to any element of 'all'
	 */
	public static boolean contains(String[] all, String what) {
		for (int i = 0; i < all.length; i++) {
			if (what.equals(all[i])) {
				return true;
			}
		}

		return false;
	}
}
