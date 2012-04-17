/*
 * Copyright (c) 2009 Network Appliance, Inc.
 * All rights reserved.
 */

package de.xwic.appkit.webbase.dialog;

/**
 * @author lippisch
 */
public class DialogEvent {

	private Object source;
	private Object args;
	
	public DialogEvent(Object source) {
		this.source = source;
	}
	
	/**
	 * @param source
	 * @param choice
	 */
	public DialogEvent(Object source, Object args) {
		super();
		this.source = source;
		this.args = args;
	}

	/**
	 * @return the choice
	 */
	public Object getArgs() {
		return args;
	}

	/**
	 * @return the source
	 */
	public Object getSource() {
		return source;
	}
	
	
}
