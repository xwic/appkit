/*
 * (c) Copyright 2005, 2006 by pol GmbH 
 * 
 * de.xwic.appkit.core.config.Language
 * Created on 06.11.2006 by Florian Lippisch
 *
 */
package de.xwic.appkit.core.config;

/**
 * @author Florian Lippisch
 *
 */
public class Language {

	private String id = null;
	private String title = null;
	
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	
}
