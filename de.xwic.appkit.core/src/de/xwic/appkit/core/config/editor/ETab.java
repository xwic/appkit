/*
 * Copyright 2005, 2006 pol GmbH
 *
 * de.xwic.appkit.core.config.editor.ETab
 * Created on 02.11.2006
 *
 */
package de.xwic.appkit.core.config.editor;

/**
 * A Tab element within an editor.
 * @author Florian Lippisch
 * @editortag tab
 */
public class ETab extends EComposite {

	private String title = null;

	/**
	 * @return Returns the title.
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * the title of the tab
	 * @param title The title to set.
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	
}
