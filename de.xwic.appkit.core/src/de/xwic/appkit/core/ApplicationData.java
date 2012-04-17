/*
 * Copyright 2005 pol GmbH
 *
 * de.xwic.appkit.core.testapp.ApplicationData
 * Created on 13.07.2005
 *
 */
package de.xwic.appkit.core;

/**
 * All application relevant static information are provided as static public
 * variables in this class.
 * <p>
 * These are for example actual amount of Languages in form of a String[]
 * containing the LanguageID. <br>
 * 
 * @author Ronny Pfretzschner
 */
public class ApplicationData {

	/** READ action */
	public final static String SECURITY_ACTION_READ = "READ";

	/** UPDATE action */
	public final static String SECURITY_ACTION_UPDATE = "UPDATE";

	/** DELETE action */
	public final static String SECURITY_ACTION_DELETE = "DELETE";

	/** CREATE action */
	public final static String SECURITY_ACTION_CREATE = "CREATE";

	/** APPROVE action */
	public final static String SECURITY_ACTION_APPROVE = "APPROVE";

	/** ACCESS action */
	public final static String SECURITY_ACTION_ACCESS = "ACCESS";

	/** Scope to access the User Administration */
	public final static String SCOPE_USERADMIN = "USER_ADMIN";

	/** Scope to access the System-User Administration */
	public final static String SCOPE_SYSADMIN = "SYS_ADMIN";

	/** Scope to access the Picklist-Administration */
	public final static String SCOPE_PICKLISTADMIN = "PL_ADMIN";

	/** Scope to access the Monitoring-Administration */
	public final static String SCOPE_MONITORINGADMIN = "MONITORING_ADMIN";
	
	/** Scope to access the Monitoring-Administration */
	public final static String SCOPE_OUTLOOKINTEGRATION = "OUTLOOK_INTEGRATION";
	
	/**
	 * Scope to change the betreuer of an unternehmen
	 */
	public final static String SCOPE_UN_MODIFY_BETREUER = "UN_MODIFY_BETREUER";
	/**
	 * Scope to query a list of all GV of all owners.
	 */
	public final static String SCOPE_GV_QUERY_ALL_OWNER = "GV_QUERY_ALL_OWNER";
	/**
	 * Scope to query a list of all SV of all owners.
	 */
	public final static String SCOPE_SV_QUERY_ALL_OWNER = "SV_QUERY_ALL_OWNER";
	/**
	 * Scope to query a list of all CR of all owners.
	 */
	public final static String SCOPE_CR_QUERY_ALL_OWNER = "CR_QUERY_ALL_OWNER";

	/**
	 * scope for mass betreuer transfer for betreute unternehmen view under mitarbeiter editor 
	 */
	public final static String SCOPE_MASS_BETREUER_TRANSFER = "MASS_BETREUER_TRANSFER";
	
	/**
	 * scope for template editing 
	 */
	public final static String SCOPE_TEMPLATE_EDITING = "TEMPLATE_EDITING";
	
	/***/
	public final static String SCOPE_ACT_REP_UNTERNEHMEN = "ACTIVITY_UNTERNEHMEN";
	/***/
	public final static String SCOPE_ACT_REP_KONTAKT = "ACTIVITY_KONTAKT";
	/***/
	public final static String SCOPE_ACT_REP_MITARBEITER = "ACTIVITY_MITARBEITER";
	/***/
	public final static String SCOPE_ACT_REP_MANDATE = "ACTIVITY_MANDATE";

	/***/
	public final static String SCOPE_ACT_REPORT = "ACTIVITY_REPORT";
	/***/
	public final static String SCOPE_DEAL_REPORT = "DEALPIPELINE_REPORT";

	/** The configuration property that contains the product ID. */
	public static final String CFG_DB_PRODUCT_ID = "PRODUCT_ID";
	/** The configuration property that contains the version number. */
	public static final String CFG_DB_PRODUCT_VERSION = "PRODUCT_VERSION";
	
	/** Default domain ID for administrative things */
	public static final String DOMAIN_ID_ADMIN = "admin";

	/** Default domain ID for core things */
	public static final String CORE_DOMAIN_ID = "core";

}
