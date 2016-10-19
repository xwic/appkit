/*
 * Copyright (c) NetApp Inc. - All Rights Reserved
 * 
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 * Proprietary and confidential.
 * 
 * de.xwic.appkit.core.util.typeconverters.FloatStringConverter 
 */

package de.xwic.appkit.core.util.typeconverters;

import de.xwic.appkit.core.util.IModelViewTypeConverter;

/**
 * Converts a Float to a String and viceversa
 * 
 * @author Andrei Pat
 *
 */
public class FloatStringConverter extends AbstractModelViewConverter<Float, String> {

	public static final FloatStringConverter INSTANCE = new FloatStringConverter();

	/**
	 * hide constructor
	 */
	private FloatStringConverter() {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.xwic.appkit.core.util.ITypeConverter#convertLeft(java.lang.Object)
	 */
	@Override
	public String convertToViewType(Float t1) {
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
	public Float convertToModelType(String t2) {
		if (t2 == null) {
			return null;
		}
		t2 = t2.trim();
		if (t2.isEmpty()) {
			return 0f;
		}
		return Float.valueOf(t2);
	}
}
