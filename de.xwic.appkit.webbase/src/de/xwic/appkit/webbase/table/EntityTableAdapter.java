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
 * Adapter class for IEntityTableListener.
 * @author lippisch
 */
public abstract class EntityTableAdapter implements IEntityTableListener {

	/* (non-Javadoc)
	 * @see de.xwic.appkit.webbase.table.IEntityTableListener#columnSorted(de.xwic.appkit.webbase.table.EntityTableEvent)
	 */
	@Override
	public void columnSorted(EntityTableEvent event) {
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.webbase.table.IEntityTableListener#columnFiltered(de.xwic.appkit.webbase.table.EntityTableEvent)
	 */
	@Override
	public void columnFiltered(EntityTableEvent event) {
	}
	
	/* (non-Javadoc)
	 * @see de.xwic.appkit.webbase.table.IEntityTableListener#columnsReordered(de.xwic.appkit.webbase.table.EntityTableEvent)
	 */
	@Override
	public void columnsReordered(EntityTableEvent event) {
	}
	
	/* (non-Javadoc)
	 * @see de.xwic.appkit.webbase.table.IEntityTableListener#userConfigurationChanged(de.xwic.appkit.webbase.table.EntityTableEvent)
	 */
	@Override
	public void userConfigurationChanged(EntityTableEvent event) {
	}
	
	/* (non-Javadoc)
	 * @see de.xwic.appkit.webbase.table.IEntityTableListener#userConfigurationDirty(de.xwic.appkit.webbase.table.EntityTableEvent)
	 */
	@Override
	public void userConfigurationDirtyChanged(EntityTableEvent event) {
	}
}
