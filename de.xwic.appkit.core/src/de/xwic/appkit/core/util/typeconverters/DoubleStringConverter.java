/*
 * Copyright (c) NetApp Inc. - All Rights Reserved
 * 
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 * Proprietary and confidential.
 * 
 * de.xwic.appkit.core.util.typeconverters.DoubleStringConverter 
 */

package de.xwic.appkit.core.util.typeconverters;

/**
 * Converts an Double to a String and viceversa
 * 
 * @author Andrei Pat
 *
 */
public class DoubleStringConverter extends AbstractModelViewConverter<Double, String> {

	public static final DoubleStringConverter INSTANCE = new DoubleStringConverter();

	/**
	 * hide constructor
	 */
	private DoubleStringConverter() {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.xwic.appkit.core.util.ITypeConverter#convertLeft(java.lang.Object)
	 */
	@Override
	public String convertToViewType(Double t1) {
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
	public Double convertToModelType(String t2) {
		if (t2 == null) {
			return null;
		}
		t2 = t2.trim();
		if (t2.isEmpty()) {
			return 0d;
		}
		return Double.valueOf(t2);
	}
}
