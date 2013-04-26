/*
 * Copyright (c) 2009 Network Appliance, Inc.
 * All rights reserved.
 */

package de.xwic.appkit.webbase.table.filter;

import java.util.Collections;
import java.util.List;

import de.jwic.base.IControlContainer;
import de.jwic.controls.Button;
import de.jwic.controls.CheckBoxGroup;
import de.jwic.controls.ListEntry;
import de.jwic.events.SelectionEvent;
import de.jwic.events.SelectionListener;
import de.xwic.appkit.core.config.model.Property;
import de.xwic.appkit.core.dao.DAOSystem;
import de.xwic.appkit.core.model.daos.IPicklisteDAO;
import de.xwic.appkit.core.model.entities.IPicklistEntry;
import de.xwic.appkit.core.model.queries.PropertyQuery;
import de.xwic.appkit.core.model.queries.QueryElement;
import de.xwic.appkit.webbase.table.Column;
import de.xwic.appkit.webbase.utils.picklist.PicklistEntryComparator;

/**
 * Filter for properties that are a picklist.
 * @author lippisch
 */
public class PicklistFilter extends AbstractFilterControl {

	private CheckBoxGroup chkGroup;
	
	/**
	 * @param container
	 * @param name
	 */
	public PicklistFilter(IControlContainer container, String name) {
		super(container, name);

		chkGroup = new CheckBoxGroup(this, "chkGroup");
		chkGroup.setColumns(1);
		
		Button btAll = new Button(this, "btAll");
		btAll.setTitle("All");
		btAll.setCssClass("j-button j-btn-small");
		btAll.addSelectionListener(new SelectionListener() {
			/* (non-Javadoc)
			 * @see de.jwic.events.SelectionListener#objectSelected(de.jwic.events.SelectionEvent)
			 */
			@Override
			public void objectSelected(SelectionEvent event) {
				touchAll(true);
			}
		});

		Button btNone = new Button(this, "btNone");
		btNone.setTitle("None");
		btNone.setCssClass("j-button j-btn-small");
		btNone.addSelectionListener(new SelectionListener() {
			/* (non-Javadoc)
			 * @see de.jwic.events.SelectionListener#objectSelected(de.jwic.events.SelectionEvent)
			 */
			@Override
			public void objectSelected(SelectionEvent event) {
				touchAll(false);
			}
		});
		
	}
	
	/**
	 * @param b
	 */
	protected void touchAll(boolean selAll) {
		StringBuilder sbKey = new StringBuilder();
		if (selAll) {
			for (ListEntry le : chkGroup.buildEntryList()) {
				sbKey.append(le.key).append(";");
			}
		}
		chkGroup.setSelectedKey(sbKey.toString());
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.webbase.table.filter.AbstractFilterControl#getQueryElement()
	 */
	@Override
	public QueryElement getQueryElement() {
		QueryElement qe = null;
		
		String propId = null;
		boolean isCol = false;
		if (column.getListColumn().getFinalProperty().isCollection()) {
			propId = column.getListColumn().getPropertyId();
			isCol = true;
		} else if (column.getPropertyIds().length > 0) { 
			propId = column.getPropertyIds()[0];
		}
		
		if (propId != null) {
			String[] keys = chkGroup.getSelectedKeys();
			if (keys.length == 1) {
				Integer val = null;
				if (!"null".equals(keys[0])) {
					val = Integer.parseInt(keys[0]);
				}
				qe = new QueryElement(propId, QueryElement.EQUALS, val);
				qe.setCollectionElement(isCol);
				
			} else if (keys.length > 1) {
				PropertyQuery q = new PropertyQuery();
				for (String k : keys) {
					Integer val = null;
					if (!"null".equals(k)) {
						val = Integer.parseInt(k);
					}
					QueryElement qx = q.addOrEquals(propId, val);
					qx.setCollectionElement(isCol);
				}
				qe = new QueryElement(QueryElement.AND, q);
			}
		
		}
		return qe;
	}
	
	/* (non-Javadoc)
	 * @see de.xwic.appkit.webbase.table.filter.AbstractFilterControl#getPreferredHeight()
	 */
	@Override
	public int getPreferredHeight() {
					// base height + button heigh + (20 per line)
		int height = 4 + 24 + (chkGroup.buildEntryList().size() * 20);
		if (height > 200) {
			height = 200;
		} else if (height < 30) {
			height = 30;
		}
		return height;
	}
	
	/*
	 * (non-Javadoc)
	 * @see de.xwic.appkit.webbase.table.filter.AbstractFilterControl#loadFilter(de.xwic.appkit.webbase.table.Column, de.xwic.appkit.core.model.queries.QueryElement)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void initialize(Column column, QueryElement queryElement) {
		this.column = column;
		
		Property prop = column.getListColumn().getFinalProperty();

		chkGroup.clear();
		if (!prop.isCollection()) {
			chkGroup.addElement("<i>No Value</i>", "null");
		}
		
		String langId = getSessionContext().getLocale().getLanguage();
		if (prop.getPicklistId() != null && !prop.getPicklistId().isEmpty()) {
			IPicklisteDAO plDAO = (IPicklisteDAO)DAOSystem.getDAO(IPicklisteDAO.class);
			List<IPicklistEntry> allEntriesToList = plDAO.getAllEntriesToList(prop.getPicklistId());

			Collections.sort(allEntriesToList, new PicklistEntryComparator(langId));
			
			for (IPicklistEntry pe : allEntriesToList) {
				chkGroup.addElement(pe.getBezeichnung(langId), Integer.toString(pe.getId()));
			}
		}
		
		// select elements
		if (queryElement != null) {
			StringBuilder sbKeys = new StringBuilder();
			if (queryElement.getSubQuery() != null) {
				PropertyQuery sq = queryElement.getSubQuery();
				for (QueryElement qe : sq.getElements()) {
					checkElement(qe, sbKeys);
				}
			} else {
				checkElement(queryElement, sbKeys);
			}
			chkGroup.setSelectedKey(sbKeys.toString());
		}
		
	}

	/**
	 * @param queryElement
	 * @param sbKeys 
	 */
	private void checkElement(QueryElement queryElement, StringBuilder sbKeys) {
		
		if (QueryElement.EQUALS.equals(queryElement.getOperation())) {
			String key = null;
			if (queryElement.getValue() == null) {
				key = "null";
			} else if (queryElement.getValue() instanceof String) {
				key = (String)queryElement.getValue();
			} else if (queryElement.getValue() instanceof Integer) {
				key = ((Integer)queryElement.getValue()).toString();
			}
			
			if (key != null && chkGroup.getContentProvider().getObjectFromKey(key) != null) {
				if (sbKeys.length() != 0) {
					sbKeys.append(";");
				}
				sbKeys.append(key);
			}
		}
		
	}

}
