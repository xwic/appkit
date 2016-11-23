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
package de.xwic.appkit.webbase.toolkit.components;

import de.jwic.base.IControlContainer;
import de.jwic.controls.ListBox;
import de.xwic.appkit.core.dao.DAO;
import de.xwic.appkit.core.dao.IEntity;

/**
 * Created on 01.04.2008
 * @author Ronny Pfretzschner
 */
public abstract class AbstractEntityComboControl<E extends IEntity> extends ListBox implements IEntityListBoxControl<E> {

	protected boolean allowEmptySelection = true;


	public AbstractEntityComboControl(IControlContainer container, String name) {
		super(container, name);
		setTemplateName(ListBox.class.getName());
	}


	public abstract DAO<E> getEntityDao();

	protected abstract void setupEntries();

	/* (non-Javadoc)
	 * @see de.xwic.appkit.webbase.toolkit.components.IEntityListBoxControl#selectEntry(de.xwic.appkit.core.dao.IEntity)
	 */
	@Override
	public void selectEntry(E entity){
		if (entity != null) {
			setSelectedKey(Long.toString(entity.getId()));
		}
		else {
			setSelectedKey("0");
		}
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.webbase.toolkit.components.IEntityListBoxControl#getSelectedEntry()
	 */
	@Override
	public E getSelectedEntry(){
		String selectedKey = getSelectedKey();
		if (selectedKey == null || selectedKey.isEmpty()) {
			return null;
		}

		long l = Long.parseLong(selectedKey);

		if (l > 0){
			return getEntityDao().getEntity(l);
		}
		return null;
	}


	/**
	 * If true, use can select "empty" value.
	 * @param allowEmptySelection
	 */
	public void setAllowEmptySelection(boolean allowEmptySelection) {
		this.allowEmptySelection = allowEmptySelection;
	}

}
