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
