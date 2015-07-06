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
import de.xwic.appkit.core.model.daos.IMitarbeiterDAO;
import de.xwic.appkit.core.model.entities.IMitarbeiter;
import de.xwic.appkit.core.model.queries.PropertyQuery;

/**
 * Selection Control for employees.
 *
 * @author Ronny Pfretzschner
 *
 */
public class EmployeeSelectionCombo extends AbstractEntityComboControl<IMitarbeiter> {

	private IMitarbeiterDAO emplDao = null;
	private String emptySelectionText = "- All -";

	/**
	 * Creates a combo box control for the employees.
	 *
	 * @param container
	 * @param name
	 */
	public EmployeeSelectionCombo(IControlContainer container, String name, boolean allowEmptySelection) {
		super(container, name);
		this.allowEmptySelection = allowEmptySelection;

		emplDao = DAOSystem.getDAO(IMitarbeiterDAO.class);
		setupEntries();
		if (allowEmptySelection) {
			setSelectedKey("0");
		}
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.webbase.toolkit.components.AbstractEntityComboControl#setupEntries()
	 */
	@Override
	protected void setupEntries(){

		PropertyQuery query = new PropertyQuery();
		query.setSortDirection(PropertyQuery.SORT_DIRECTION_UP);
		query.setSortField("nachname");

		EntityList<IMitarbeiter> entryList = emplDao.getEntities(null, query);

		if (null != entryList){
			//add empty selection
			if (allowEmptySelection){
				addElement(emptySelectionText, "0");
			}

			String defSel = null;

			for (int i = 0; i < entryList.size(); i++) {
				IMitarbeiter employee = entryList.get(i);
				String title = employee.getNachname() + ", " + employee.getVorname();

				String key = String.valueOf(employee.getId());

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

	/*
	 *
	 */
	@Override
	public IMitarbeiterDAO getEntityDao() {
		return emplDao;
	}

	/**
	 * @return the emptySelectionText
	 */
	public String getEmptySelectionText() {
		return emptySelectionText;
	}

	/**
	 * @param emptySelectionText the emptySelectionText to set
	 */
	public void setEmptySelectionText(String emptySelectionText) {
		this.emptySelectionText = emptySelectionText;
		setupEntries();
	}

}
