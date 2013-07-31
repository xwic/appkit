/*
 * (c) Copyright 2005, 2006 by pol GmbH 
 * 
 * de.pol.isis.web.history.wizard.HistoryLabelProvider
 * Created on Dec 16, 2008 by Aron Cotrau
 *
 */
package de.xwic.appkit.webbase.history.wizard;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import de.jwic.base.SessionContext;
import de.jwic.controls.tableviewer.CellLabel;
import de.jwic.controls.tableviewer.ITableLabelProvider;
import de.jwic.controls.tableviewer.RowContext;
import de.jwic.controls.tableviewer.TableColumn;
import de.xwic.appkit.core.dao.IEntity;
import de.xwic.appkit.core.dao.IHistory;
import de.xwic.appkit.webbase.history.HistorySelectionModel;

/**
 * 
 * 
 * @author Aron Cotrau
 */
public class HistoryLabelProvider implements ITableLabelProvider {

	private HistorySelectionModel model;
	private SessionContext sessionContext;

	/**
	 * @param bundle
	 */
	public HistoryLabelProvider(HistorySelectionModel bundle, SessionContext sessionContext) {
		this.model = bundle;
		this.sessionContext = sessionContext;
	}

	public CellLabel getCellLabel(Object row, TableColumn column, RowContext rowContext) {
		int index = column.getIndex();
		IEntity entity = (IEntity) row;
		IHistory hisObj = (IHistory) row;

		switch (index) {
		case 0: {
			
			Date date = entity.getLastModifiedAt();
			DateFormat df = new SimpleDateFormat(sessionContext.getDateFormat()+" "+sessionContext.getTimeFormat());
			df.setTimeZone(sessionContext.getTimeZone());
			return new CellLabel(df.format(date));
		}
		case 1: {
			return new CellLabel(entity.getLastModifiedFrom());
		}
		case 2: {
			return new CellLabel(Long.toString(hisObj.getEntityVersion()));
		}
		case 3: {
			int reason = hisObj.getHistoryReason();
			return new CellLabel(getHistoryReasonString(reason));
		}
		}

		return new CellLabel("");
	}

	private String getHistoryReasonString(int reason) {
		switch (reason) {
		case 0: {
			return model.getResourceString("historySelWiz.reason.created");
		}
		case 1: {
			return model.getResourceString("historySelWiz.reason.updated");
		}
		case 2: {
			return model.getResourceString("historySelWiz.reason.deleted");
		}
		default:
			return "";
		}
	}

}
