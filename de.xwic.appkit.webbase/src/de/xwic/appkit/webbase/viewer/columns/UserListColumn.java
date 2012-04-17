/*
 * (c) Copyright 2005, 2006 by pol GmbH 
 * 
 * de.xwic.appkit.webbase.utils.UserListColumn
 * Created on Mar 20, 2007 by Aron Cotrau
 *
 */
package de.xwic.appkit.webbase.viewer.columns;


/**
 * @author Aron Cotrau
 */
public class UserListColumn {
	
	private String propertyId = null;
	private int width = 200;
	
	/**
	 * @return the propertyId
	 */
	public String getPropertyId() {
		return propertyId;
	}

	/**
	 * @param propertyId
	 *            the propertyId to set
	 */
	public void setPropertyId(String propertyId) {
		this.propertyId = propertyId;
	}

	/**
	 * @return the width
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * @param width
	 *            the width to set
	 */
	public void setWidth(int width) {
		this.width = width;
	}
}
