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
/**
 *
 */
package de.xwic.appkit.core.util;

/**
 * By default it returns all elements that are not null, you can override the keepNotNull method for more filtering
 * @author Alexandru Bledea
 * @since Jan 6, 2014
 * @deprecated - use de.xwic.appkit.core.util.Filter#notNullFilter() - instead
 */
@Deprecated
public class NotNullFilter<E> implements IFilter<E> {

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.util.IFilter#keep(java.lang.Object)
	 */
	@Override
	public final boolean keep(E element) {
		if (element == null) {
			return false;
		}
		return keepNotNull(element);
	}

	/**
	 * @param element
	 * @return
	 */
	protected boolean keepNotNull(E element) {
		return true;
	}

}
