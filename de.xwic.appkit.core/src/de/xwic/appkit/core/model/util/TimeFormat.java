/*
 * (c) Copyright 2005, 2006 by pol GmbH 
 * 
 * de.xwic.appkit.core.client.dal.dialogs.TimeFormat
 * Created on 09.02.2007 by Ronny Pfretzschner
 *
 */
package de.xwic.appkit.core.model.util;


/**
 * Small helper for the time format. <p>
 * 
 * @author Ronny Pfretzschner
 *
 */
public class TimeFormat {

	/**
	 * Formats a given millisec as long into a proper String. <p>
	 * 
	 * Could be:<br> 
	 * 234 ms. <br>
	 * 234 sec. <br>
	 * 234 min., 33 sec. <br>
	 * 1 h, 234 min., 33 sec. <br>
	 * 
	 * @param millisec
	 * @return the formated String
	 */
	public static String getTimeFormat(long millisec) {
		String time = Long.toString(millisec);

		//below 1 sec -> show millisec 
		if (millisec <= 1000) {
			return time + " ms.";
		}
		
		//below 1 minute -> show seconds
		else if (millisec <= 60000) {
			long seconds = millisec / 1000;
			return seconds + " sec.";
		}
		
		//below 1 hour -> show minutes
		else if (millisec <= 3600000) {
			long sec = millisec / 1000;
			long minutes =  sec / 60;
			String secString = Long.toString(sec % 60);
			String minutesString = Long.toString(minutes);
			return minutesString + " min., " + secString + " sec.";
		}
		//more hours to go -> show hours
		else {
			long sec = millisec / 1000;
			long minutes =  sec / 60;
			long hours = minutes / 60;
			String secString = Long.toString(sec % 60);
			String minutesString = Long.toString(minutes % 60);
			String hourString = Long.toString(hours);
			return hourString + " h, " + minutesString + " min., " + secString + " sec.";
		}
	}
}
