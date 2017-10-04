/*
 * Copyright (c) 2009 Network Appliance, Inc.
 * All rights reserved.
 */

package de.xwic.appkit.webbase.controls.comment;

/**
 * @author lippisch
 */
public class EntityShoutBoxEvent {

	private Object source;
	private String comment;
	
	/**
	 * @param source
	 * @param comment
	 */
	public EntityShoutBoxEvent(Object source, String comment) {
		super();
		this.source = source;
		this.comment = comment;
	}

	/**
	 * @param source
	 */
	public EntityShoutBoxEvent(Object source) {
		super();
		this.source = source;
	}

	/**
	 * @return the source
	 */
	public Object getSource() {
		return source;
	}

	/**
	 * @return the comment
	 */
	public String getComment() {
		return comment;
	}

	/**
	 * @param comment the comment to set
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}
	
}
