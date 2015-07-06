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
package de.xwic.appkit.webbase.history.wizard;

import java.util.Set;

import de.jwic.base.SessionContext;
import de.jwic.controls.wizard.Wizard;
import de.jwic.controls.wizard.WizardPage;
import de.xwic.appkit.core.dao.IEntity;
import de.xwic.appkit.webbase.history.HistorySelectionModel;


/**
 * 
 *
 * @author Aron Cotrau
 */
public class HistorySelectionWizard extends Wizard {

	private HistorySelectionModel model = null;

	private HistorySelWizPage page1 = null;
	private HistoryCompareWizPage page2 = null;
	
	/**
	 * constructor
	 */
	public HistorySelectionWizard(IEntity entity) {
		model = new HistorySelectionModel(entity);

		setTitle(model.getResourceString("historySelWiz.title"));
		
		setHeight(400);
		//setWidth(950);
	}
	
	@Override
	public WizardPage getNextPage(WizardPage page) {
		
		if (page == page1) {
			Set<?> tableSelection = (Set<?>) page1.getSelectionFromTable();
			
			if (tableSelection.size() < 2) {
				page1.showError();
				return page1;
			}
			
			model.setSelectedHisKeys(tableSelection);
			page2.updateInput(model.getSelectedHistoryEntities());
			
			return page2;
		}
		
		return super.getNextPage(page);
	}

	/* (non-Javadoc)
	 * @see de.jwic.ecolib.wizard.Wizard#performFinish()
	 */
	public boolean performFinish() {
		return true;
	}
	
	/* (non-Javadoc)
	 * @see de.jwic.ecolib.wizard.Wizard#createWizardPages(de.jwic.base.SessionContext)
	 */
	@Override
	public WizardPage createWizardPages(SessionContext sessionContext) {
		page1 = new HistorySelWizPage(model);
		addWizardPage(page1);
		
		page2 = new HistoryCompareWizPage(model);
		addWizardPage(page2);
		
		return page1;
	}

}
