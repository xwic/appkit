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
package de.xwic.appkit.webbase.utils.picklist;

import java.util.Comparator;

import de.xwic.appkit.core.model.entities.IPicklistEntry;

/**
 * Comparator for picklistentries.
 * 
 * Created on 19.02.2008
 * 
 * @author Ronny Pfretzschner
 */
public class PicklistEntryComparator implements Comparator<IPicklistEntry> {

	/**
	 * 
	 */
	private final String lang;

	/**
	 * @param combo
	 */
	public PicklistEntryComparator(String lang) {
		this.lang = lang;
	}

	/* (non-Javadoc)
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	@Override
	public int compare(IPicklistEntry o1, IPicklistEntry o2) {
		if (o1 == null && o2 == null) {
			return 0;
		}
		if (o1 == null) {
			return -1;
		}
		if (o2 == null) {
			return 1;
		}
		if (o1.getSortIndex() == o2.getSortIndex()) {
			if (lang == null) {
				if (o1.getKey() != null && o2.getKey() != null) {
					return o1.getKey().compareTo(o2.getKey());
				}
			} else {
				return o1.getBezeichnung(lang).compareTo(o2.getBezeichnung(lang));
			}
		} else {
			if (o1.getSortIndex() < o2.getSortIndex()) {
				return -1;
			}
			return 1;
		}
		return 0;
	}

}
