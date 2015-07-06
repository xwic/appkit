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
package de.xwic.appkit.core.security.queries;

import de.xwic.appkit.core.dao.EntityQuery;

/**
 * @author Florian Lippisch
 */
public class UserQuery extends EntityQuery {

	private String logonName = null;

	/**
	 * default constructor.
	 */
	public UserQuery() {
		
	}
	
	/**
	 * Search for the specified logonName.
	 * @param logonName
	 */
	public UserQuery(String logonName) {
		this.logonName = logonName;
	}
	
	/**
	 * @return Returns the logonName.
	 */
	public String getLogonName() {
		return logonName;
	}

	/**
	 * @param logonName The logonName to set.
	 */
	public void setLogonName(String logonName) {
		this.logonName = logonName;
	}
	
	
	
}
