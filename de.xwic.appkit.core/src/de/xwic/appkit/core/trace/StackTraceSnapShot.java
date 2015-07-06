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
package de.xwic.appkit.core.trace;

/**
 * Stores the data of a taken thread snapshot.
 * @author lippisch
 */
public class StackTraceSnapShot {

	long snapShotTime;
	StackTraceElement[] stackTrace;

	public StackTraceSnapShot (StackTraceElement[] st) {
		stackTrace = st;
		snapShotTime = System.currentTimeMillis();
	}

	/**
	 * @return the snapShotTime
	 */
	public long getSnapShotTime() {
		return snapShotTime;
	}

	/**
	 * @return the stackTrace
	 */
	public StackTraceElement[] getStackTrace() {
		return stackTrace;
	}
	
}
