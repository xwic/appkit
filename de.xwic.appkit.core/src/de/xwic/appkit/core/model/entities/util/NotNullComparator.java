/*******************************************************************************
 * Copyright 2015 xWic group (http://www.xwic.de)
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 		http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 *  
 *******************************************************************************/
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
