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

package de.xwic.appkit.core.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helps to count multiple events.
 * 
 * @author lippisch
 */
public class Counter {

	private Map<String, CounterData> values = new  HashMap<String, CounterData>();
	private boolean statusUpdate = false;
	private long statusSteps = 100;
	
	private class CounterData {
		String key;
		long value = 0;
		String description = null;
		CounterData(String key) {
			this.key = key;
		}
		void count() {
			value++;
		}
		@Override
		public String toString() {
			StringBuilder sbSpaces = new StringBuilder("                      ");
			sbSpaces.setLength(key.length() < 20 ? 20 - key.length() : 0);
			return key + ":" + sbSpaces + value + "    " + (description != null ? "(" + description + ")" : "");
		}
	}
	
	/**
	 * Constructor.
	 */
	public Counter() {
		
	}

	/**
	 * Constructor.
	 */
	public Counter(boolean statusUpdate) {
		this.statusUpdate = statusUpdate;
	}
	
	/**
	 * Count with statusUpdate set to true.
	 * @param statusSteps
	 */
	public Counter(long statusSteps) {
		super();
		this.statusSteps = statusSteps;
		this.statusUpdate = true;
	}

	/**
	 * Count the specified event type.
	 * @param eventKey
	 */
	public void count(String eventKey) {
		CounterData cd = values.get(eventKey);
		if (cd == null) {
			cd = new CounterData(eventKey);
			values.put(eventKey, cd);
		}
		cd.count();
		if (statusUpdate && cd.value % statusSteps == 0) {
			System.out.println("Status: " + cd);
		}
	}

	/**
	 * Returns the counter value for the specified event type. Returns 0
	 * if no events of this type has been counted yet.
	 * @param eventKey
	 * @return
	 */
	public long getCount(String eventKey) {
		CounterData cd = values.get(eventKey);
		if (cd == null) {
			return 0;
		}
		return cd.value;
	}
	
	/**
	 * Reset a counter value.
	 * @param eventKey
	 */
	public void resetCount(String eventKey) {
		CounterData cd = values.get(eventKey);
		if (cd != null) {
			cd.value = 0;
		}
	}
	
	/**
	 * Set the description of an event. This info is used in the result string (toResultString()).
	 * @param eventKey
	 * @param description
	 */
	public void setDescription(String eventKey, String description) {
		CounterData cd = values.get(eventKey);
		if (cd == null) {
			cd = new CounterData(eventKey);
			values.put(eventKey, cd);
		}
		cd.description = description;
	}
	
	/**
	 * Returns a string with a list of all events and their counts.
	 * @return
	 */
	public String toResultString() {
		StringBuilder sb = new StringBuilder();
		List<String> keys = new ArrayList<String>();
		keys.addAll(values.keySet());
		Collections.sort(keys);
		for (String key : keys) {
			if (sb.length() != 0) {
				sb.append("\n");
			}
			CounterData cd = values.get(key);
			sb.append(cd.toString());
		}
		return sb.toString();
	}

	/**
	 * @return the statusUpdate
	 */
	protected boolean isStatusUpdate() {
		return statusUpdate;
	}

	/**
	 * Set to true to print a status message every [statusSteps] counts to the console (System.out)
	 * @param statusUpdate the statusUpdate to set
	 */
	protected void setStatusUpdate(boolean statusUpdate) {
		this.statusUpdate = statusUpdate;
	}

	/**
	 * @return the statusSteps
	 */
	protected long getStatusSteps() {
		return statusSteps;
	}

	/**
	 * @param statusSteps the statusSteps to set
	 */
	protected void setStatusSteps(long statusSteps) {
		this.statusSteps = statusSteps;
	}
	
}
