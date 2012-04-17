/*
 * Copyright 2005, 2006 pol GmbH
 *
 * de.xwic.appkit.core.config.editor.EGroup
 * Created on 02.11.2006
 *
 */
package de.xwic.appkit.core.config.editor;

/**
 * A group is a special composite which is bordered by a thin border and has a
 * title
 * 
 * @author Florian Lippisch
 * @editortag group
 */
public class EGroup extends EComposite {

	private String title = null;

	/**
	 * @return Returns the title.
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Defines the title of the group
	 * 
	 * @param title
	 *            The title to set.
	 */
	public void setTitle(String title) {
		this.title = title;
	}

}
