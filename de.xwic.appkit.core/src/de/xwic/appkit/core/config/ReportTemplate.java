/*
 * (c) Copyright 2005, 2006 by pol GmbH 
 * 
 * de.xwic.appkit.core.config.ReportTemplate
 * Created on 25.09.2007 by Florian Lippisch
 *
 */
package de.xwic.appkit.core.config;

import java.net.URL;

/**
 * Links to a file that contains a velocity based report. 
 * 
 * @author Florian Lippisch
 */
public class ReportTemplate {

	private URL location = null;
	private String title = "Untitled";
	private String right = null;
	private String filePath = null;
	
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
	/**
	 * @return the right
	 */
	public String getRight() {
		return right;
	}
	/**
	 * @param right the right to set
	 */
	public void setRight(String right) {
		this.right = right;
	}
	
	
}
