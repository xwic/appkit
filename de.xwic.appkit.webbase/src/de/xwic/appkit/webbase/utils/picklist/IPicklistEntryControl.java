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

import de.jwic.base.IControl;
import de.xwic.appkit.core.model.entities.IPicklistEntry;

/**
 * Created on Mar 9, 2008
 * @author Ronny Pfretzschner
 * @author lippisch
 */
public interface IPicklistEntryControl extends IControl {

	/**
	 * @return Returns the picklistKey.
	 */
	public String getPicklistKey();

	/**
	 * @param picklistKey The picklistKey to set.
	 */
	public void setPicklistKey(String picklistKey);

	
	/**
	 * Returns the selected IPicklistEntry.
	 * @return
	 */
	public IPicklistEntry getSelectedEntry();
	
	/**
	 * Select the picklistEntry.
	 * @param entry
	 */
	public void selectEntry(IPicklistEntry pEntry);
	
	/**
	 * Change enabled state.
	 * @param enabled
	 */
	public void setEnabled(boolean enabled);

	
	/**
	 * Returns true if the field is enabled (Editable).
	 * @param enabled
	 */
	public boolean isEnabled();

}
