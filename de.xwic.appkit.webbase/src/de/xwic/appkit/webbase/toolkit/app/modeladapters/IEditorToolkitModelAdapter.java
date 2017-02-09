/*
 * Copyright (c) NetApp Inc. - All Rights Reserved
 * 
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 * Proprietary and confidential.
 * 
 * de.xwic.appkit.webbase.toolkit.app.IEditorToolkitModelAdapter 
 */

package de.xwic.appkit.webbase.toolkit.app.modeladapters;

import de.xwic.appkit.core.util.IModelViewTypeConverter;

/**
 * Interface that allows the editor toolkit to read and write values to its backing model
 * 
 * @author Andrei Pat
 *
 */
public interface IEditorToolkitModelAdapter {

	/**
	 * Reads the value "propertyName" from the model. If a type converter is set, the value is converted first, otherwise is returned as is
	 * 
	 * @param propertyName
	 * @param converter
	 * @return
	 */
	public Object read(String propertyName, IModelViewTypeConverter converter);

	/**
	 * Writes the value from the control to the model's property specified by "propertyName". If a type converter is set, the value is
	 * converted. If no converter is set, the value might be autoconverted or not, depending on the implementation
	 * 
	 * @param propertyName
	 * @param controlValue
	 * @param converter
	 */
	public void write(String propertyName, Object controlValue, IModelViewTypeConverter converter);
}
