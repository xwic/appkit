/*
 * (c) Copyright 2005, 2006 by pol GmbH 
 * 
 * de.pol.isis.web.history.wizard.HistoryCompareWizPage
 * Created on Dec 16, 2008 by Aron Cotrau
 *
 */
package de.xwic.appkit.webbase.history.wizard;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import de.jwic.base.ControlContainer;
import de.jwic.base.IControlContainer;
import de.jwic.controls.tableviewer.TableColumn;
import de.jwic.controls.tableviewer.TableModel;
import de.jwic.controls.tableviewer.TableViewer;
import de.jwic.controls.wizard.WizardPage;
import de.xwic.appkit.core.dao.IEntity;
import de.xwic.appkit.core.dao.IHistory;
import de.xwic.appkit.webbase.history.HistorySelectionModel;
import de.xwic.appkit.webbase.toolkit.app.PanelSectionContainer;

/**
 * 
 * 
 * @author Aron Cotrau
 */
public class HistoryCompareWizPage extends WizardPage {

	private HistorySelectionModel model = null;
	private TableViewer tableViewer = null;
	private String[] colNames = null;

	private final static String PATTERN = "dd-MMM-yyyy hh:mm aa";
	private final static SimpleDateFormat format = new SimpleDateFormat(PATTERN);

	/**
	 * constructor.
	 * <p>
	 * 
	 * @param shell
	 *            the parent shell
	 * @param model
	 *            the HistorySelectionModel
	 * @param selection
	 *            The previous selection
	 */
	public HistoryCompareWizPage(HistorySelectionModel model) {
		this.model = model;
		
		setTitle(model.getResourceString("historySelWiz.compare.title"));
		setSubTitle(model.getResourceString("historySelWiz.compare.subtitle"));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.window.Window#createContents(org.eclipse.swt.widgets.Composite)
	 */
	public void createControls(IControlContainer container) {

		PanelSectionContainer composite = new PanelSectionContainer(container, "parent");

		tableViewer = new TableViewer(composite, "tableViewer");
		tableViewer.setScrollable(true);
		tableViewer.setResizeableColumns(true);
		tableViewer.setShowStatusBar(false);

		tableViewer.setHeight(350);
		tableViewer.setWidth(850);

		generateColumns();
		
		tableViewer.setContentProvider(new HistoryCompareTableContentProvider(model));
		tableViewer.setTableLabelProvider(new HistoryCompareTableLabelProvider(model, container.getSessionContext().getTimeZone()));
		format.setTimeZone(container.getSessionContext().getTimeZone());
	}

	@SuppressWarnings("unchecked")
	private void generateColumns() {
		
		TableModel tableModel = tableViewer.getModel();
		
		if (tableModel.getColumnsCount() > 0) {
			// remove old columns
			Iterator<TableColumn> iter = tableModel.getColumnIterator();
			while(iter.hasNext()) {
				iter.next();
				iter.remove();
			}
		}
		
		TableColumn col1 = new TableColumn();
		col1.setTitle(colNames[0]);
		col1.setWidth(150);

		tableModel.addColumn(col1);
		
		for (int i = 1; i < colNames.length; i++) {
			TableColumn col = new TableColumn();
			col.setTitle(colNames[i]);
			col.setWidth(250);

			tableModel.addColumn(col);
			
		}
		
	}

	/**
	 * Generates and displays a comparison page.
	 * <p>
	 * 
	 * Rows are the properties, which changed in the past, the columns are the
	 * selected versions.
	 * 
	 * @param historySelection
	 *            A List containing PropertyHelperVersions for comparison
	 */
	public void updateInput(List<?> historySelection) {

		if (null != historySelection) {
			// First column is Attribut...
			colNames = new String[historySelection.size() + 1];
			colNames[0] = model.getResourceString("historycompare.app.col1.name");

			for (int i = 0; i < historySelection.size(); i++) {
				IEntity entity = (IEntity) historySelection.get(i);
				IHistory hisObj = (IHistory) historySelection.get(i);

				Date date = entity.getLastModifiedAt();
				String dateString = "<unknown>";

				if (date != null) {
					dateString = format.format(date);
				}

				colNames[i + 1] = dateString + " Ver: " + hisObj.getEntityVersion();
			}

			if (null != tableViewer) {
				generateColumns();
			}
			
		}
	}

}
