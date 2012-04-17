/*
 * Copyright (c) 2009 Network Appliance, Inc.
 * All rights reserved.
 */

package de.xwic.appkit.webbase.trace.ui;

import de.jwic.base.ControlContainer;
import de.jwic.base.IControlContainer;
import de.jwic.controls.Button;
import de.jwic.controls.combo.DropDown;
import de.jwic.ecolib.tableviewer.TableColumn;
import de.jwic.ecolib.tableviewer.TableModel;
import de.jwic.ecolib.tableviewer.TableViewer;
import de.jwic.events.ElementSelectedEvent;
import de.jwic.events.ElementSelectedListener;
import de.jwic.events.SelectionEvent;
import de.jwic.events.SelectionListener;
import de.xwic.appkit.webbase.trace.HttpTraceFilter;

/**
 * Displays list of most recent trace history entries.
 * @author lippisch
 */
public class TraceHistoryView extends ControlContainer {

	private TableViewer tblViewer;
	private DropDown ddMinDuration;
	private TraceHistoryContentProvider contentProvider;

	/**
	 * @param container
	 * @param name
	 */
	public TraceHistoryView(IControlContainer container, String name) {
		super(container, name);
		
		Button btRefresh = new Button(this, "btRefresh");
		btRefresh.setTitle("Refresh");
		btRefresh.addSelectionListener(new SelectionListener() {
			@Override
			public void objectSelected(SelectionEvent event) {
				tblViewer.requireRedraw();
			}
		});
		
		ddMinDuration = new DropDown(this, "ddMinDuration");
		ddMinDuration.addElement("- All -", "0");
		ddMinDuration.addElement("50ms", "50");
		ddMinDuration.addElement("100ms", "100");
		ddMinDuration.addElement("200ms", "200");
		ddMinDuration.addElement("500ms", "500");
		ddMinDuration.addElement("1000ms", "1000");
		ddMinDuration.addElement("2000ms", "2000");
		ddMinDuration.addElement("5000ms", "5000");
		ddMinDuration.addElement("10000ms", "10000");
		
		ddMinDuration.setChangeNotification(true);
		ddMinDuration.setText("- All -");
		ddMinDuration.setSelectedKey("0");
		ddMinDuration.addElementSelectedListener(new ElementSelectedListener() {
			/* (non-Javadoc)
			 * @see de.jwic.events.ElementSelectedListener#elementSelected(de.jwic.events.ElementSelectedEvent)
			 */
			@Override
			public void elementSelected(ElementSelectedEvent event) {
				onMinDurationChange();
			}
		});
		// add table viewer
		
		int clientWidth = getSessionContext().getUserAgent().getClientWidth();
		int clientHeight = getSessionContext().getUserAgent().getClientHeight();
		int width = clientWidth > 660 ? clientWidth - 60 : 600;
		int height = clientHeight > 600 ? clientHeight - 300 : 300;
		
		contentProvider = new TraceHistoryContentProvider();

		tblViewer = new TableViewer(this, "tblHistory");
		tblViewer.setWidth(width);
		tblViewer.setHeight(height);
		tblViewer.setScrollable(true);
		tblViewer.setContentProvider(contentProvider);
		tblViewer.setTableLabelProvider(new TraceHistoryLabelProvider());
		tblViewer.setShowStatusBar(true);
		tblViewer.setResizeableColumns(true);
		
		TableModel model = tblViewer.getModel();
		model.setMaxLines(100);
		model.addColumn(new TableColumn("Start Time", 140, "startTime"));
		model.addColumn(new TableColumn("User", 100, "user"));
		model.addColumn(new TableColumn("Duration", 90, "duration"));
		model.addColumn(new TableColumn("DAO Time", 60, "dao-time"));
		model.addColumn(new TableColumn("DAO Count", 60, "dao-count"));
		model.addColumn(new TableColumn("Method", 60, "method"));
		model.addColumn(new TableColumn("Remote IP", 90, "ip"));
		model.addColumn(new TableColumn("URI", 200, "uri"));
		model.addColumn(new TableColumn("JWic Control", 240, "jwic-control"));
		model.addColumn(new TableColumn("JWic Action", 100, "jwic-action"));
		model.addColumn(new TableColumn("Module", 100, HttpTraceFilter.ATTR_SITE_MODULE));
		model.addColumn(new TableColumn("Sub Module", 100, HttpTraceFilter.ATTR_SITE_SUBMODULE));
		model.addColumn(new TableColumn("Path", 300, HttpTraceFilter.ATTR_USER_PATH));
		model.addColumn(new TableColumn("Remote User", 100, "remote-user"));
		model.addColumn(new TableColumn("Info", 300, "info"));
		model.addColumn(new TableColumn("Trace Name", 120, "name"));
		
		
	}

	/**
	 * 
	 */
	protected void onMinDurationChange() {
		
		contentProvider.setMinDuration(Integer.parseInt(ddMinDuration.getSelectedKey()));
		tblViewer.requireRedraw();
		
	}

	
	
}
