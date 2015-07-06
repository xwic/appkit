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

package de.xwic.appkit.webbase.table;

/**
 * Listener to the entity table model.
 * @author lippisch
 */
public interface IEntityTableListener {
	
	/**
	 * A column sort setting was changed.
	 * @param event
	 */
	public void columnSorted(EntityTableEvent event);

	/**
	 * A columns filter options have been changed.
	 * @param event
	 */
	public void columnFiltered(EntityTableEvent event);

	/**
	 * The columns have been reordered or the visibility changed.
	 * @param event
	 */
	public void columnsReordered(EntityTableEvent event);
	
	/**
	 * The user configuration has been changed
	 * @param event
	 */
	public void userConfigurationChanged(EntityTableEvent event);
	
	/**
	 * Called when the current User Configuration has been modified
	 * @param event
	 */
	public void userConfigurationDirtyChanged(EntityTableEvent event);
}
