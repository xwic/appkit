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
package de.xwic.appkit.core.security;


/**
 * @author Florian Lippisch
 */
public class ScopeActionKey {

	private String scopeName = null;
	private String actionName = null;

	private int myHash = 0;
	private boolean hashCalculated = false;

	/**
	 * Default Constructor.
	 *
	 */
	public ScopeActionKey() {
		
	}
	
	/**
	 * Default Constructor.
	 * @param scopeName
	 * @param actionName
	 */
	public ScopeActionKey(String scopeName, String actionName) {
		this.scopeName = scopeName;
		this.actionName = actionName;
		hashCalculated = false;
	}

	/**
	 * @param scope
	 * @param action
	 */
	public ScopeActionKey(IScope scope, IAction action) {
		this.scopeName = scope != null ? scope.getName() : null;
		this.actionName = action != null ? action.getName() : null;
		hashCalculated = false;
	}

	/**
	 * @return Returns the scopeName.
	 */
	public String getScopeName() {
		return scopeName;
	}

	/**
	 * @return Returns the actionName.
	 */
	public String getActionName() {
		return actionName;
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
		
		ScopeActionKey key = (ScopeActionKey)obj;
		if (actionName == null) {
			if (key.actionName != null) {
				return false;
			}
		} else { 
			if (!actionName.equals(key.actionName)) {
				return false;
			}
		}
		if (scopeName == null) {
			if (key.scopeName != null) {
				return false;
			}
		} else { 
			if (!scopeName.equals(key.scopeName)) {
				return false;
			}
		}
		return true;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {

		if (!hashCalculated) {
			myHash = 17;
			myHash = 37 * myHash + (scopeName != null ? scopeName.hashCode() : 0);
			myHash = 37 * myHash + (actionName != null ? actionName.hashCode() : 0);
			hashCalculated = true;
		}
		return myHash;
	}

	/**
	 * @param actionName The actionName to set.
	 */
	public void setActionName(String actionName) {
		this.actionName = actionName;
	}

	/**
	 * @param scopeName The scopeName to set.
	 */
	public void setScopeName(String scopeName) {
		this.scopeName = scopeName;
	}
	
	
}
