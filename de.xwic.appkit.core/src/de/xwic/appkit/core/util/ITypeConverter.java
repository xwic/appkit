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
public interface ITypeConverter<T1, T2> {

	/**
	 * Converts the first generic type to the second generic type (left)
	 * 
	 * @param t1
	 * @return
	 */
	public T2 convertLeft(T1 t1);

	/**
	 * Converts the second generic type to the first generic type (right)
	 * 
	 * @param t2
	 * @return
	 */
	public T1 convertRight(T2 t2);
}
