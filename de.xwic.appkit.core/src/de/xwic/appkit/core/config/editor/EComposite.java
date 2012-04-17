/*
 * Copyright 2005, 2006 pol GmbH
 *
 * de.xwic.appkit.core.config.editor.EComposite
 * Created on 02.11.2006
 *
 */
package de.xwic.appkit.core.config.editor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * A composite is a container for other UIElements.
 * 
 * @author Florian Lippisch
 * @editortag composite
 */
public class EComposite extends UIElement {

	private List<UIElement> childElements = new ArrayList<UIElement>();
	private String layout = null;
	private int cols = 0;
	
	/**
	 * Add a child element.
	 * @param element
	 */
	public void addElement(UIElement element) {
		childElements.add(element);
	}
	
	/**
	 * Returns the childs.
	 * @return
	 */
	public List<UIElement> getChilds() {
		return Collections.unmodifiableList(childElements);
	}
	
	/**
	 * @return Returns the layout.
	 */
	public String getLayout() {
		return layout;
	}
	/**
	 * @param layout The layout to set.
	 */
	public void setLayout(String layout) {
		this.layout = layout;
	}

	/**
	 * @return Returns the cols.
	 */
	public int getCols() {
		return cols;
	}

	/**
	 * @param cols The cols to set.
	 */
	public void setCols(int cols) {
		this.cols = cols;
	}
	
	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.config.editor.UIElement#toString()
	 */
	public String toString() {
		StringBuffer sb = new StringBuffer();
		String name = getClass().getName().substring(getClass().getName().lastIndexOf('.') + 1);
		sb.append("<")
		  .append(name);
		
		if (id != null) {
			sb.append(" id=\"")
			.append(id)
			.append("\"");
		}
		sb.append(" layout=\"")
		  .append(layout)
		  .append("\"");
		sb.append(" cols=\"")
		  .append(cols)
		  .append("\"");

		if (style != null) {
			sb.append(" style=\"")
			  .append(style)
			  .append("\"");
		}
		sb.append(">\n");
		for (Iterator<UIElement> it = childElements.iterator(); it.hasNext(); ) {
			sb.append("  ");
			sb.append(it.next())
			  .append("\n");
		}
		sb.append("<").append(name).append("/>");
		  
		return sb.toString();
	}
	
}
