/*
 * Copyright 2005, 2006 pol GmbH
 *
 * de.xwic.appkit.core.config.editor.ELabel
 * Created on 02.11.2006
 *
 */
package de.xwic.appkit.core.config.editor;

import de.xwic.appkit.core.config.model.Property;

/**
 * A label is used to display plain text in an editor
 * 
 * @author Florian Lippisch
 * @editortag label
 */
public class ELabel extends UIElement {

	private String title = null;
	private Property[] property = null;
	private boolean wrap = false;

	/**
	 * @return Returns the title.
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * The title of the label.
	 * @param title The title to set.
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return Returns the property.
	 */
	public Property[] getProperty() {
		return property;
	}

	/**
	 * The properties mapped by this widget
	 * @param property The property to set.
	 */
	public void setProperty(Property[] property) {
		this.property = property;
	}
	
	/**
	 * Returns the property that is the leaf.
	 * @return
	 */
	public Property getFinalProperty() {
		return property != null ? property[property.length - 1] : null;
	}

	
	/**
	 * @return the wrap
	 */
	public boolean isWrap() {
		return wrap;
	}

	
	/**
	 * Set whether or not this label should wrap it's text.
	 * @param wrap the wrap to set
	 */
	public void setWrap(boolean wrap) {
		this.wrap = wrap;
	}
}
