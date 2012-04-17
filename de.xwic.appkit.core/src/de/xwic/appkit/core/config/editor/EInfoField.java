/*
 * (c) Copyright 2005, 2006 by pol GmbH 
 * 
 * de.xwic.appkit.core.config.editor.EInfoField
 * Created on Feb 12, 2007 by Aron Cotrau
 *
 */
package de.xwic.appkit.core.config.editor;

/**
 * A special label which is dependant to entity fields, to display infos about
 * the selected entity in the field.
 * 
 * @author Aron Cotrau
 * @editortag info
 */
public class EInfoField extends EField {

	/** if the field depends on any other field, this is the attribute to be set */
	private String depends = null;

	/**
	 * @return the depends
	 */
	public String getDepends() {
		return depends;
	}

	/**
	 * if the field depends on any other field. set the id of the widget as
	 * defined in the editor configuration.
	 * 
	 * @param depends
	 *            the depends to set
	 */
	public void setDepends(String depends) {
		this.depends = depends;
	}
}
