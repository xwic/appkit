/*
 * (c) Copyright 2005, 2006 by pol GmbH 
 * 
 * de.xwic.appkit.core.config.editor.EEntityField
 * Created on Nov 22, 2006 by Administrator
 *
 */
package de.xwic.appkit.core.config.editor;

/**
 * 
 * A field that handles entity references
 * 
 * @author Andra Iacovici
 * @editortag entity
 */
public class EEntityField extends EField {

	private String titlePattern = null;
	private String selectMessage = null;
	private String parameters = null;

	/**
	 * @return the titlePattern
	 */
	public String getTitlePattern() {
		return titlePattern;
	}

	/**
	 * Title pattern is used while displaying the entity title
	 * @param titlePattern the titlePattern to set
	 */
	public void setTitlePattern(String titlePattern) {
		this.titlePattern = titlePattern;
	}

	/**
	 * @return the selectMessage
	 */
	public String getSelectMessage() {
		return selectMessage;
	}

	/**
	 * @param selectMessage the selectMessage to set
	 */
	public void setSelectMessage(String selectMessage) {
		this.selectMessage = selectMessage;
	}

	
	/**
	 * @return the parameters
	 */
	public String getParameters() {
		return parameters;
	}

	
	/**
	 * Defines parameters for the search fields
	 * @param parameters the parameters to set
	 */
	public void setParameters(String parameters) {
		this.parameters = parameters;
	}
	
}
