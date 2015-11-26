/*
 * Copyright (c) NetApp Inc. - All Rights Reserved
 * 
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 * Proprietary and confidential.
 * 
 * de.xwic.appkit.core.util.typeconverters.LongStringConverter 
 */

package de.xwic.appkit.core.util.typeconverters;

import de.xwic.appkit.core.util.ITypeConverter;

/**
 * @author Andrei Pat
 *
 */
public class LongStringConverter implements ITypeConverter<Long, String> {

	public static final LongStringConverter INSTANCE = new LongStringConverter();

	/**
	 * hide constructor
	 */
	private LongStringConverter() {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.xwic.appkit.core.util.ITypeConverter#convertLeft(java.lang.Object)
	 */
	@Override
	public String convertLeft(Long t1) {
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
	public Long convertRight(String t2) {
		if (t2 == null) {
			return null;
		}
		t2 = t2.trim();
		if (t2.isEmpty()) {
			return 0L;
		}
		return Long.valueOf(t2);
	}
}
