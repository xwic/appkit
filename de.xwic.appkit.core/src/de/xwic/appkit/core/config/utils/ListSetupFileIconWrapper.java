/*
 * (c) Copyright 2005, 2006 by pol GmbH 
 * 
 * de.xwic.appkit.core.config.utils.ListSetupFileIconWrapper
 * Created on Dec 11, 2007 by Aron Cotrau
 *
 */
package de.xwic.appkit.core.config.utils;

import java.net.URL;

/**
 * 
 *
 * @author Aron Cotrau
 */
public class ListSetupFileIconWrapper {

	/** the icon key */
	private String iconKey = null;
	/** the url */
	private URL url = null;
	
	/**
	 * @return the iconKey
	 */
	public String getIconKey() {
		return iconKey;
	}
	
	/**
	 * @param iconKey the iconKey to set
	 */
	public void setIconKey(String iconKey) {
		this.iconKey = iconKey;
	}
	
	/**
	 * @return the url
	 */
	public URL getUrl() {
		return url;
	}
	
	/**
	 * @param url the url to set
	 */
	public void setUrl(URL url) {
		this.url = url;
	}
	
}
