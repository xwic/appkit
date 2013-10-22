/*
 * (c) Copyright 2005, 2006 by pol GmbH 
 * 
 * de.pol.isis.web.history.wizard.HistorySelWizPage
 * Created on Dec 16, 2008 by Aron Cotrau
 *
 */
package de.xwic.appkit.webbase.history.wizard;

import java.util.Collection;

import de.jwic.base.ControlContainer;
import de.jwic.base.IControlContainer;
import de.jwic.controls.ErrorWarning;
import de.jwic.controls.tableviewer.TableColumn;
import de.jwic.controls.tableviewer.TableModel;
import de.jwic.controls.tableviewer.TableViewer;
import de.jwic.controls.wizard.WizardPage;
import de.xwic.appkit.webbase.history.HistorySelectionModel;
import de.xwic.appkit.webbase.toolkit.app.PanelSectionContainer;


/**
 * 
 *
 * @author Aron Cotrau
 */
public class HistorySelWizPage extends WizardPage {

	private TableViewer tableViewer = null;
	private HistorySelectionModel model = null;
	
	private String[] colNames = null;
	
	private ErrorWarning errorControl = null;
	
	/**
	 * @param model
	 */
	protected HistorySelWizPage(HistorySelectionModel model) {
		setTitle(model.getResourceString("historySelWiz.title"));
		setSubTitle(model.getResourceString("historySelWiz.page.subTitle"));
		
		this.model = model;
		colNames = new String[] { model.getResourceString("historySelWiz.col1"),
				model.getResourceString("historySelWiz.col2"), model.getResourceString("historySelWiz.col3"),
				model.getResourceString("historySelWiz.col4") };
	}
	
	/* (non-Javadoc)
	 * @see de.jwic.ecolib.wizard.WizardPage#createControls(de.jwic.base.IControlContainer)
	 */
	@Override
	public void createControls(IControlContainer container) {
		PanelSectionContainer composite = new PanelSectionContainer(container, "parent");
		
		errorControl = new ErrorWarning(composite, "error");
		
		tableViewer = new TableViewer(composite, "hisTable");
		tableViewer.setScrollable(true);
		tableViewer.setResizeableColumns(true);
		tableViewer.setShowStatusBar(false);
		tableViewer.getModel().setSelectionMode(TableModel.SELECTION_MULTI);
		
		tableViewer.setHeight(350);
		//tableViewer.setWidth(850);
		
		createColumns();

		tableViewer.setContentProvider(new HistoryContentProvider(model));
		tableViewer.setTableLabelProvider(new HistoryLabelProvider(model, container.getSessionContext()));
	}

	private void createColumns() {
		TableModel tableModel = tableViewer.getModel();
		
		for (int i = 0; i < colNames.length; i++) {
			TableColumn col = new TableColumn();
			col.setTitle(colNames[i]);
			col.setWidth(150);
			
			tableModel.addColumn(col);
		}
	}

	/**
	 * @return The table selection in a list.
	 */
	public Collection<?> getSelectionFromTable() {
		return tableViewer.getModel().getSelection();
	}

	/**
	 * shows an error
	 */
	public void showError() {
		errorControl.showError("You must select at least 2 entities for comparision");
	}
	
}
