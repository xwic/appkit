/*
 * Copyright 2005, 2006 pol GmbH
 *
 * de.xwic.appkit.core.config.editor.EField
 * Created on 02.11.2006
 *
 */
package de.xwic.appkit.core.config.editor;

import de.xwic.appkit.core.config.model.Property;

/**
 * @author Florian Lippisch
 */
public class EField extends UIElement {

	private Property property[] = null;
	/** if true, field is always readonly **/
	private boolean readonly = false;
	/**if true, the main background is set for the field*/
	private boolean transparency = false;
	
	private boolean triggerHideWhen = false;
	
	/**
	 * @return Returns the property.
	 */
	public Property[] getProperty() {
		return property;
	}
	/**
	 * The property handled by this field. The property is specified using
	 * the OGNL format:</p>
	 * <p><code>property="unternehmen.adresse.ort"</code></p>
	 * @param property The property to set.
	 * @mandatory
	 */
	public void setProperty(Property[] property) {
		this.property = property;
	}
	/**
	 * @return Returns the readonly.
	 */
	public boolean isReadonly() {
		return readonly;
	}
	/**
	 * @param readonly The readonly to set.
	 * @default false
	 */
	public void setReadonly(boolean readonly) {
		this.readonly = readonly;
	}
	/**
	 * Returns the property that is the leaf.
	 * @return
	 */
	public Property getFinalProperty() {
		return property != null ? property[property.length - 1] : null;
	}
	
	/**
	 * @return the transparency
	 */
	public boolean isTransparency() {
		return transparency;
	}
	/**
	 * @param transparency the transparency to set
	 */
	public void setTransparency(boolean transparency) {
		this.transparency = transparency;
	}
	/**
	 * @return the triggerHideWhen
	 */
	public boolean isTriggerHideWhen() {
		return triggerHideWhen;
	}
	/**
	 * Specifies if a modification of the value handled by this field should trigger
	 * the recalculation of all hideWhen conditions.
	 * 
	 * @param triggerHideWhen the triggerHideWhen to set
	 * @default false
	 */
	public void setTriggerHideWhen(boolean triggerHideWhen) {
		this.triggerHideWhen = triggerHideWhen;
	}
}
