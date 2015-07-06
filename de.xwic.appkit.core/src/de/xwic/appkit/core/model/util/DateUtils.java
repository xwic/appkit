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
/**
 * 
 */
package de.xwic.appkit.core.model.util;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Mark Frewin
 *
 */
public class DateUtils {
	
	/** US Date Format */
	public final static int SQL_DATE_USA = 1;
	/** UK_FR Date Format */
	public final static int SQL_DATE_UK_FR = 3;
	/** German Date Format */
	public final static int SQL_DATE_DE = 4;
	
	private static DateUtils instance = null;	
	private Pattern p = null; 
	 
	/**
	 * Constructor for single instance pattern
	 *
	 */
	private DateUtils(){
		//date pattern 'dd.mm.yyyy'
		p = Pattern.compile("(')(\\d{1,2}?)(\\.|/|-)(\\d{1,2}?)(\\.|/|-)(\\d{2}|\\d{4})(')");
	}
	 
	/**
	 * 
	 * @return
	 */
	public static DateUtils getInstance(){
		if (instance == null) instance = new DateUtils();
		return instance;
	}
	
	/**
	 * 
	 * @param locale
	 * @param query
	 * @return
	 */
	public boolean hasMatches(Locale locale, String query) {
		Matcher m = p.matcher(query);
		return m.find();
	}
	
	/**
	 * Searches and replaces dates with SQL Convert function which are found in a query string. 
	 * 
	 * @param locale
	 * @param query
	 * @return
	 */
	public String addSQLConvert(Locale locale, String query) {
		Matcher m = p.matcher(query);
		StringBuffer sb = new StringBuffer();
		 while (m.find()) {
			 int sqlConstant = checkLocale(locale); 
			 if (m.group(6).length() == 4)
				 sqlConstant += 100;
			 
		     m.appendReplacement(sb, "CONVERT(datetime," + m.group(1)+ m.group(2)+ m.group(3)+ m.group(4)+ m.group(5)+ m.group(6)+ m.group(7) + ", " + sqlConstant + ")");
		 }
		 m.appendTail(sb);
		 
		return sb.toString();
	}
	
	/**
	 * Calculates the SQL Server datetime constant relevant for the users locale.
	 * Supports German, EN_US, EN_UK and French and defaults to DE when not supported. 
	 * 
	 * @param locale
	 * @return
	 */
	public int checkLocale(Locale locale){
		if (locale.getLanguage().equals(Locale.GERMAN.getLanguage()))
			return SQL_DATE_DE;
		
		if (locale.getLanguage().equals(Locale.ENGLISH.getLanguage()))
			return locale.getCountry().equals(Locale.US.getCountry()) ? SQL_DATE_USA : SQL_DATE_UK_FR;						
		
		if (locale.getLanguage().equals(Locale.FRENCH.getLanguage()))
			return SQL_DATE_UK_FR;
		
		return SQL_DATE_DE;
	}
	
}
