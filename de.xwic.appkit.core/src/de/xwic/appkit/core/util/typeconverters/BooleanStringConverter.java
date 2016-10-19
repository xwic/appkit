/*
 * Copyright (c) NetApp Inc. - All Rights Reserved
 * 
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 * Proprietary and confidential.
 * 
 * de.xwic.appkit.core.util.typeconverters.StringBooleanConverter 
 */

package de.xwic.appkit.core.util.typeconverters;

/**
 * Converts a Boolean into a String and viceversa
 * 
 * @author Andrei Pat
 *
 */
public class BooleanStringConverter extends AbstractModelViewConverter<Boolean, String> {

	public static final BooleanStringConverter INSTANCE = new BooleanStringConverter();

	/**
	 * hide constructor
	 */
	private BooleanStringConverter() {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.xwic.appkit.core.util.ITypeConverter#convertLeft(java.lang.Object)
	 */
	@Override
	public String convertToViewType(Boolean t1) {
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
	public Boolean convertToModelType(String t2) {
		if (t2 == null) {
			return null;
		}
		return Boolean.valueOf(t2);
	}
}
