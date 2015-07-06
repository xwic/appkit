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

import de.xwic.appkit.core.model.entities.IPicklistEntry;

/**
 * Index key comparator for picklistentries. Revert is possible, means from bit
 * to small values.
 * 
 * Created on 26.02.2008
 * 
 * @author Ronny Pfretzschner
 */
public class PicklistEntryByIndexComparator implements Comparator<IPicklistEntry> {

	private boolean revert = false;

	public PicklistEntryByIndexComparator(boolean revert) {
		this.revert = revert;
	}

	public PicklistEntryByIndexComparator() {
		this(false);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	public int compare(IPicklistEntry o1, IPicklistEntry o2) {

		IPicklistEntry pick1 = (IPicklistEntry) o1;
		IPicklistEntry pick2 = (IPicklistEntry) o2;

		if (!revert) {
			return new Integer(pick1.getSortIndex()).compareTo(new Integer(pick2.getSortIndex()));
		} else {
			return new Integer(pick2.getSortIndex()).compareTo(new Integer(pick1.getSortIndex()));
		}
	}
}
