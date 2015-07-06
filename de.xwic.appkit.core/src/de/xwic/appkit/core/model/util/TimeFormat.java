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
