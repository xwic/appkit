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
package de.xwic.appkit.core.registry;

import java.util.Comparator;

/**
 * @author Adrian Ionescu
 */
public class ExtensionSortIndexComparator implements Comparator<IExtension> {

	/* (non-Javadoc)
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	@Override
	public int compare(IExtension o1, IExtension o2) {
		int si1 = o1.getSortIndex();
		int si2 = o2.getSortIndex();
		
		// sorting ASC
		return si1 < si2 ? -1 : si1 == si2 ? 0 : 1;
	}

}
