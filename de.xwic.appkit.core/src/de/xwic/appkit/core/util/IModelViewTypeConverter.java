/*
 * Copyright (c) NetApp Inc. - All Rights Reserved
 * 
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 * Proprietary and confidential.
 * 
 * de.xwic.appkit.webbase.toolkit.app.ITypeConverter 
 */

package de.xwic.appkit.core.util;

/**
 * Interface for converting between two data types. The conversion should be bidirectional.
 * 
 * @author Andrei Pat
 *
 */
public interface IModelViewTypeConverter<M, V> {

	/**
	 * Converts the first generic type to the second generic type (left)
	 * 
	 * @param t1
	 * @return
	 */
	public V convertToViewType(M modelValue);

	/**
	 * Converts the second generic type to the first generic type (right)
	 * 
	 * @param t2
	 * @return
	 */
	public M convertToModelType(V viewValue);
}
