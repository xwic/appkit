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
package de.xwic.appkit.core.security.impl;

import de.xwic.appkit.core.dao.Entity;
import de.xwic.appkit.core.security.IAction;

/**
 * @author Florian Lippisch
 */
public class Action extends Entity implements IAction {

	private String name = null;
	
	private int myHash = 0;
	private boolean hashCalculated = false;

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.security.IAction#getName()
	 */
	public String getName() {
		return name;
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.security.IAction#setName(java.lang.String)
	 */
	public void setName(String name) {
		this.name = name;
		hashCalculated = false;
	}
	
	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.dao.Entity#setId(int)
	 */
	public void setId(long newId) {
		super.setId(newId);
		hashCalculated = false;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		
		if (!obj.getClass().equals(getClass())) {
			return false;
		}
		
		Action action = (Action)obj;
		if (name == null && action.name != null) {
			return false;
		}
		return getId() == action.getId() && name.equals(action.name);
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		if (!hashCalculated) {
			myHash = 17;
			myHash = 37 * myHash + (name != null ? name.hashCode() : 0);
			myHash = 37 * myHash + (int) (getId() ^ (getId() >>> 32));
			hashCalculated = true;
		}
		return myHash;
	}

}
