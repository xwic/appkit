/*
 * Copyright (c) 2009 Network Appliance, Inc.
 * All rights reserved.
 */

package de.xwic.appkit.webbase.table.filter;

import de.jwic.base.IControlContainer;
import de.jwic.base.ImageRef;
import de.jwic.controls.SelectableControl;

/**
 * Used as an action on a column.
 * @author lippisch
 */
public class ColumnAction extends SelectableControl {

	private String title = null;
	private ImageRef image = null;

	/**
	 * @param container
	 * @param name
	 */
	public ColumnAction(IControlContainer container, String name) {
		super(container, name);
	}

	/**
	 * Click action.
	 */
	public void actionClick() {
		click();
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
	 * @return the image
	 */
	public ImageRef getImage() {
		return image;
	}

	/**
	 * @param image the image to set
	 */
	public void setImage(ImageRef image) {
		this.image = image;
	}
	
	

}
