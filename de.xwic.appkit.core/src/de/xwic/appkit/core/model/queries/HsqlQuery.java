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
package de.xwic.appkit.core.model.queries;

import java.util.Locale;

import de.xwic.appkit.core.dao.EntityQuery;

/**
 * Holds a implementation specific HSQL query. This query type is used
 * by the advanced query in the table viewer. Other java code should 
 * use the specific, implementation-independent query objects instead.
 * 
 * @author Florian Lippisch
 */
public class HsqlQuery extends EntityQuery {

	private String userLanguage = null;
	private String hsqlQuery = null;
	private String userCountry = null;
	
	/**
	 * Default Constructor.
	 */
	public HsqlQuery() {
		this.userLanguage = Locale.getDefault().getLanguage();
		this.userCountry = Locale.getDefault().getCountry();
	}
	/**
	 * Construct a new HsqlQuery with all required properties.
	 * @param hsqlQuery
	 * @param userLanguage
	 */
	public HsqlQuery(String hsqlQuery, String userLanguage) {
		this.hsqlQuery = hsqlQuery;
		this.userLanguage = userLanguage;
		this.userCountry = Locale.getDefault().getCountry();
	}
	/**
	 * Construct a new HsqlQuery with all required properties.
	 * @param hsqlQuery
	 * @param userLanguage
	 */
	public HsqlQuery(String hsqlQuery, String userLanguage, String userCountry) {
		this.hsqlQuery = hsqlQuery;
		this.userLanguage = userLanguage;
		this.userCountry = userCountry;
	}
	

	/**
	 * @return Returns the hsqlQuery.
	 */
	public String getHsqlQuery() {
		return hsqlQuery;
	}

	/**
	 * @param hsqlQuery The hsqlQuery to set.
	 */
	public void setHsqlQuery(String hsqlQuery) {
		this.hsqlQuery = hsqlQuery;
	}

	/**
	 * @return Returns the userLanguage.
	 */
	public String getUserLanguage() {
		return userLanguage;
	}

	/**
	 * @param userLanguage The userLanguage to set.
	 */
	public void setUserLanguage(String userLanguage) {
		this.userLanguage = userLanguage;
	}
	/**
	 * Returns the language of the client.
	 * @return
	 */
	public String getUserCountry() {
		return userCountry;
	}
	/**
	 * Set the client language.
	 * @param userCountry
	 */
	public void setUserCountry(String userCountry) {
		this.userCountry = userCountry;
	}
	
}
