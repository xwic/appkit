/*
 * Copyright (c) NetApp Inc. - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 * Proprietary and confidential.
 *
 * de.xwic.appkit.core.model.entities.util.NotNullStringComparator
 */
package de.xwic.appkit.core.model.entities.util;



/**
 * @author Alexandru Bledea
 * @since Oct 21, 2014
 */
public abstract class NotNullStringComparator<T> extends NotNullComparator<T, String> {

	/**
	 *
	 */
	public NotNullStringComparator() {
		this("");
	}

	/**
	 * @param whatIfNull
	 */
	public NotNullStringComparator(final String whatIfNull) {
		super(whatIfNull);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see de.xwic.appkit.core.model.entities.util.NotNullComparator#compareNotNull(java.lang.Object, java.lang.Object)
	 */
	@Override
	protected final int compareNotNull(final String o1, final String o2) {
		return o1.compareTo(o2);
	}

}
