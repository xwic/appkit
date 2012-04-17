/*
 * Copyright (c) 2009 Network Appliance, Inc.
 * All rights reserved.
 */

package de.xwic.appkit.webbase.dialog;

import de.jwic.base.ControlContainer;
import de.jwic.base.IControlContainer;

/**
 * The container for dialog contents.
 * @author lippisch
 */
public class DialogContent extends ControlContainer {

	private int width = 0;
	private int height = 0;
	
	/**
	 * @param container
	 * @param name
	 */
	public DialogContent(IControlContainer container, String name) {
		super(container, name);
	}

	/**
	 * @return the width
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * @param width the width to set
	 */
	public void setWidth(int width) {
		this.width = width;
	}

	/**
	 * @return the height
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * @param height the height to set
	 */
	public void setHeight(int height) {
		this.height = height;
	}

	
	
}
