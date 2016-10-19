/*
 * Copyright (c) NetApp Inc. - All Rights Reserved
 * 
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 * Proprietary and confidential.
 * 
 * de.xwic.appkit.core.util.typeconverters.IntStringConverter 
 */

package de.xwic.appkit.core.util.typeconverters;

import de.xwic.appkit.core.util.IModelViewTypeConverter;

/**
 * Converts an Integer to a String and viceversa
 * 
 * @author Andrei Pat
 *
 */
public class IntegerStringConverter  extends AbstractModelViewConverter<Integer, String> {

	public static final IntegerStringConverter INSTANCE = new IntegerStringConverter();

	/**
	 * hide constructor
	 */
	private IntegerStringConverter() {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.xwic.appkit.core.util.ITypeConverter#convertLeft(java.lang.Object)
	 */
	@Override
	public String convertToViewType(Integer t1) {
		if (t1 == null) {
			return null;
		}
		return t1.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.xwic.appkit.core.util.ITypeConverter#convertRight(java.lang.Object)
	 */
	@Override
	public Integer convertToModelType(String t2) {
		if (t2 == null) {
			return null;
		}
		t2 = t2.trim();
		if (t2.isEmpty()) {
			return 0;
		}
		return Integer.valueOf(t2);
	}
}
