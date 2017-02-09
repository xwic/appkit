/*
 * Copyright (c) NetApp Inc. - All Rights Reserved
 * 
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 * Proprietary and confidential.
 * 
 * de.xwic.appkit.webbase.toolkit.app.modeladapters.EditorToolkitMapModelAdapter 
 */

package de.xwic.appkit.webbase.toolkit.app.modeladapters;

import java.util.Map;

import de.xwic.appkit.core.util.IModelViewTypeConverter;

/**
 * Model adapter for Editor Toolkit that supports reading/writing properties from a Map
 * 
 * @author Andrei Pat
 *
 */
public class EditorToolkitMapModelAdapter implements IEditorToolkitModelAdapter {

	private Map<String, Object> model;

	public EditorToolkitMapModelAdapter(Map<String, Object> map) {
		this.model = map;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.xwic.appkit.webbase.toolkit.app.modeladapters.IEditorToolkitModelAdapter#read(java.lang.String,
	 * de.xwic.appkit.core.util.ITypeConverter)
	 */
	@Override
	public Object read(String propertyName, IModelViewTypeConverter converter) {
		Object value = model.get(propertyName);
		if (converter != null) {
			return converter.convertToViewType(value);
		} else {
			return value;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.xwic.appkit.webbase.toolkit.app.modeladapters.IEditorToolkitModelAdapter#write(java.lang.String, java.lang.Object,
	 * de.xwic.appkit.core.util.ITypeConverter)
	 */
	@Override
	public void write(String propertyName, Object controlValue, IModelViewTypeConverter converter) {
		Object value = controlValue;
		if (converter != null) {
			value = converter.convertToModelType(controlValue);
		}
		model.put(propertyName, value);
	}
}
