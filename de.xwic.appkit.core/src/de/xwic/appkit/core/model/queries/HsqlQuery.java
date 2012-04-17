/*
 * Copyright 2005 pol GmbH
 *
 * de.xwic.appkit.core.model.queries.HsqlQuery
 * Created on 12.09.2005
 *
 */
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
