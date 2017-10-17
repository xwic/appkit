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

	/** Scope to be able to grant access on all roles */
	public final static String CAN_MANAGE_USER_ROLES = "CAN_MANAGE_USER_ROLES";

	/** Scope to be able to grant access on all roles */
	public final static String CAN_MANAGE_ALL_ROLES = "CAN_MANAGE_ALL_ROLES";

	/** Scope to access the Picklist-Administration */
	public final static String SCOPE_PICKLISTADMIN = "PL_ADMIN";

	/** Scope to access the Monitoring-Administration */
	public final static String SCOPE_MONITORINGADMIN = "MONITORING_ADMIN";
	
	/** Scope to access the Monitoring-Administration */
	public final static String SCOPE_OUTLOOKINTEGRATION = "OUTLOOK_INTEGRATION";
	
	/**
	 * scope for template editing 
	 */
	public final static String SCOPE_TEMPLATE_EDITING = "TEMPLATE_EDITING";
	
	/** The configuration property that contains the product ID. */
	public static final String CFG_DB_PRODUCT_ID = "PRODUCT_ID";
	/** The configuration property that contains the version number. */
	public static final String CFG_DB_PRODUCT_VERSION = "PRODUCT_VERSION";
	
	/** Default domain ID for administrative things */
	public static final String DOMAIN_ID_ADMIN = "admin";

	/** Default domain ID for core things */
	public static final String CORE_DOMAIN_ID = "core";

}
