/*
 * (c) Copyright 2005, 2006 by pol GmbH 
 * 
 * de.xwic.appkit.core.config.editor.EIf
 * Created on 15.02.2007 by Florian Lippisch
 *
 */
package de.xwic.appkit.core.config.editor;

/**
 * Used to handle conditional elements. This version handles
 * hasAccess and hasNoAccess on a specified scope. The condition is
 * true, if the user has the action ACCESS on the specified scope
 * or not, if hasNoAccess is used.
 * 
 * @author Florian Lippisch
 * @editortag if
 */
public class EIf extends EComposite {

	private String hasAccess = null;
	private String hasNoAccess = null;
	/**
	 * @return the hasAccess
	 */
	public String getHasAccess() {
		return hasAccess;
	}
	/**
	 * @param hasAccess the hasAccess to set
	 */
	public void setHasAccess(String hasAccess) {
		this.hasAccess = hasAccess;
	}
	/**
	 * @return the hasNoAccess
	 */
	public String getHasNoAccess() {
		return hasNoAccess;
	}
	/**
	 * @param hasNoAccess the hasNoAccess to set
	 */
	public void setHasNoAccess(String hasNoAccess) {
		this.hasNoAccess = hasNoAccess;
	}
	
}
