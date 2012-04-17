/*
 * Copyright 2005, 2006 pol GmbH
 *
 * de.xwic.appkit.core.config.editor.UIElememt
 * Created on 02.11.2006
 *
 */
package de.xwic.appkit.core.config.editor;


/**
 * Superclass for all UI Element nodes.
 * 
 * @author Florian Lippisch
 */
public class UIElement {

	/** the id of the element */
	protected String id = null;
	/** the template */
	protected Style template = null;
	/** style settings */
	protected Style style = new Style();
	/**this field can be used if the custom control builders handle a special treatment
	 * of the setting color/fonts for the UIElement*/
	private boolean updatedFontColor = false;
	
	private String hideWhen = null;
	private String hideMode = "exclude";
	
	/**
	 * Returns the id of the element.
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * The id of the element. If an id is specified, the created widget can be accessed at runtime by
	 * listeners or other controls.
	 * 
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the style
	 */
	public Style getStyle() {
		return style;
	}

	/**
	 * Custom style settings. 
	 * 
	 * <p>Styles define the position and layout of the field. 
	 * Styles defined here override settings made in a template.</p>
	 * 
	 * @param style the style to set
	 */
	public void setStyle(Style style) {
		this.style = style;
		if (template != null) {
			style.setParentStyle(template);
		}
	}

	/**
	 * @return the template
	 */
	public Style getTemplate() {
		return template;
	}

	/**
	 * The name of a style template.
	 * 
	 * <p>Specifies the name of a style template. Sample:<br>
	 * <br>
	 * <code> &lt;text property="name" template="basicField"&gt;</code></p>
	 * 
	 * @param template the template to set
	 */
	public void setTemplate(Style template) {
		this.template = template;
		if (style != null) {
			style.setParentStyle(template);
		}
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("<")
		  .append(getClass().getName().substring(getClass().getName().lastIndexOf('.') + 1));
		
		if (id != null) {
			sb.append(" id=\"")
			.append(id)
			.append("\"");
		}
		if (style != null) {
			sb.append(" style=\"")
			  .append(style)
			  .append("\"");
		}
		
		sb.append("/>");
		  
		return sb.toString();
	}

	/**
	 * @return the updatedFontColor
	 */
	public boolean isUpdatedFontColor() {
		return updatedFontColor;
	}

	/**
	 * @param updatedFontColor the updatedFontColor to set
	 */
	public void setUpdatedFontColor(boolean updatedFontColor) {
		this.updatedFontColor = updatedFontColor;
	}

	/**
	 * @return the hideWhen
	 */
	public String getHideWhen() {
		return hideWhen;
	}

	/**
	 * <p>A javascript formula that returns true when the element should be hidden.</p>
	 * 
	 * <p>The formula can access the values of the current edited entity using the <code>entity.</code> prefix:<br>
	 * <br>
	 * <code> &lt;text ... hideWhen="entity.status == null || entity.status.key == 'new'"&gt;</code>
	 * 
	 * @param hideWhen the hideWhen to set
	 */
	public void setHideWhen(String hideWhen) {
		this.hideWhen = hideWhen;
	}

	/**
	 * @return the hideMode
	 */
	public String getHideMode() {
		return hideMode;
	}

	/**
	 * <p>Specifies how the control is hidden.</p>
	 * <p>The following options are available:
	 * <ul>
	 *  <li>exclude   - The control is removed from its container.</li>
	 *  <li>hide	  - The control is marked invisible. The space is still allocated, but the control is not visible.</li>
	 * </ul>
	 * @param hideMode the hideMode to set
	 */
	public void setHideMode(String hideMode) {
		this.hideMode = hideMode;
	}

}
