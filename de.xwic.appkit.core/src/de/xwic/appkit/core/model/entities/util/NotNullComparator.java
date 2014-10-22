/*
 * Copyright (c) NetApp Inc. - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 * Proprietary and confidential.
 *
 * de.xwic.appkit.core.model.entities.util.NotNullComparator
 */
package de.xwic.appkit.core.model.entities.util;

import java.util.Comparator;

import org.apache.commons.lang.Validate;

/**
 * @author Alexandru Bledea
 * @since Oct 21, 2014
 */
public abstract class NotNullComparator<T, V> implements Comparator<T> {

	protected final V whatIfNull;

	/**
	 * @param whatIfNull
	 */
	public NotNullComparator(final V whatIfNull) {
		Validate.notNull(whatIfNull, "Default not null value cannot be null!");
		this.whatIfNull = whatIfNull;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	@Override
	public final int compare(final T o1, final T o2) {
		return compareNotNull(getNotNullValue(o1), getNotNullValue(o2));
	}

	/**
	 * @return
	 *
	 */
	protected abstract int compareNotNull(final V o1, final V o2);

	/**
	 * @param possiblyNull
	 * @return
	 */
	protected abstract V extractNotNull(final T possiblyNull);

	/**
	 * @param possiblyNull
	 * @return
	 */
	private V getNotNullValue(final T possiblyNull) {
		if (null == possiblyNull) {
			return whatIfNull;
		}
		return extractNotNull(possiblyNull);
	}

}
