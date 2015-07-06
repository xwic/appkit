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
package de.xwic.appkit.webbase.toolkit.components;

import de.jwic.base.IControlContainer;
import de.xwic.appkit.core.dao.DAOSystem;
import de.xwic.appkit.core.dao.EntityList;
import de.xwic.appkit.core.model.daos.ISalesTeamDAO;
import de.xwic.appkit.core.model.entities.ISalesTeam;
import de.xwic.appkit.core.model.queries.PropertyQuery;

/**
 * @author Ronny Pfretzschner
 *
 */
public class SalesTeamSelectionCombo extends AbstractEntityComboControl<ISalesTeam> {

	private ISalesTeamDAO elaDao = null;
	
	/**
	 * Creates a combo box control for the employees.
	 * 
	 * @param container
	 * @param name
	 */
	public SalesTeamSelectionCombo(IControlContainer container, String name, boolean allowEmptySelection) {
		super(container, name);
		this.allowEmptySelection = allowEmptySelection;
		
		elaDao = DAOSystem.getDAO(ISalesTeamDAO.class);
		setupEntries();
		if (allowEmptySelection) {
			setSelectedKey("0");
		}
	}

	
	
	/**
	 * set an internal array for the given list of entries
	 * @param entryList
	 */
	protected void setupEntries(){
		
		PropertyQuery query = new PropertyQuery();
		
		EntityList<ISalesTeam> entryList = elaDao.getEntities(null, query);
		
		if (null != entryList){
			//add empty selection
			if (allowEmptySelection){
				addElement("", "0");
			}
			
			String defSel = null;
			
			long actualTime = System.currentTimeMillis();
			
			for (int i = 0; i < entryList.size(); i++) {
				ISalesTeam budget = entryList.get(i);
				String title = budget.getBezeichnung();
				
				String key = String.valueOf(budget.getId());

				if (i == 0) {
					defSel = key;
				}
				
				addElement(title, key);
			}
			
			if (defSel != null && !allowEmptySelection) {
				setSelectedKey(defSel);
			}
		}
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.webbase.toolkit.components.AbstractEntityComboControl#getEntityDao()
	 */
	@Override
	public ISalesTeamDAO getEntityDao() {
		return elaDao;
	}

}
