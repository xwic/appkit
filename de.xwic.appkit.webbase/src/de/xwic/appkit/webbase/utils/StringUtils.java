/*******************************************************************************
 * Copyright 2015 xWic group (http://www.xwic.de)
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 		http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 *  
 *******************************************************************************/
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
	
	/**
	 * @param s
	 * @param length
	 * @return
	 */
	public static String trimToSize(String s, int length) {
		if (isEmpty(s)) {
			return s;
		}
		int trimTo = s.length() < length ? s.length() : length;
		return s.substring(0, trimTo);
	}

	/**
	 * @param s
	 * @return
	 */
	public static boolean isEmpty(String s) {
		return s == null || s.trim().isEmpty();
	}

}
