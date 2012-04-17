/*
 * (c) Copyright 2005, 2006 by pol GmbH 
 * 
 * de.pol.isis.web.history.wizard.HistorySelectionWizard
 * Created on Dec 16, 2008 by Aron Cotrau
 *
 */
package de.xwic.appkit.webbase.history.wizard;

import java.util.Set;

import de.jwic.base.SessionContext;
import de.jwic.ecolib.wizard.Wizard;
import de.jwic.ecolib.wizard.WizardPage;
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
		
		setHeightHint("400");
		setWidthHint("950");
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
