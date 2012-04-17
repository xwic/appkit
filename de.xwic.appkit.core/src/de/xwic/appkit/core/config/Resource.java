/*
 * (c) Copyright 2005, 2006 by pol GmbH 
 * 
 * de.xwic.appkit.core.config.Resource
 * Created on 07.11.2006 by Florian Lippisch
 *
 */
package de.xwic.appkit.core.config;

import java.net.URL;

/**
 * Represents a link to a resource. 
 * @author Florian Lippisch
 */
public class Resource {

	private String id = null;
	private URL location = null;
	private String filePath = null;
	
	/**
	 * Constructor.
	 */
	public Resource() {
		
	}
	/**
	 * Constructor.
	 */
	public Resource(String id, URL location) {
		this.id = id;
		this.location = location;
	}

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
	 * @return the location
	 */
	public URL getLocation() {
		return location;
	}
	/**
	 * @param location the location to set
	 */
	public void setLocation(URL location) {
		this.location = location;
	}
	/**
	 * @return the filePath
	 */
	public String getFilePath() {
		return filePath;
	}
	/**
	 * @param filePath the filePath to set
	 */
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	
}
