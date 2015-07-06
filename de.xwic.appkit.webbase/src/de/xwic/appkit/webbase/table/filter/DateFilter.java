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

package de.xwic.appkit.webbase.table.filter;

import java.util.Date;

import de.jwic.base.IControlContainer;
import de.jwic.controls.DatePicker;
import de.jwic.controls.Label;
import de.jwic.controls.combo.DropDown;
import de.jwic.events.ElementSelectedEvent;
import de.jwic.events.ElementSelectedListener;
import de.xwic.appkit.core.model.queries.PropertyQuery;
import de.xwic.appkit.core.model.queries.QueryElement;
import de.xwic.appkit.webbase.table.Column;

/**
 * Implements the standard filter for text based search.
 * @author lippisch
 */
public class DateFilter extends AbstractFilterControl {

	private DropDown ddLogic;
	private DatePicker inpDateFrom;
	private DatePicker inpDateTo;
	private Label lblTo;
	
	/**
	 * @param container
	 * @param name
	 */
	public DateFilter(IControlContainer container, String name) {
		super(container, name);
		
		ddLogic = new DropDown(this, "logic");
		ddLogic.addElement("Equals", "eq");
		ddLogic.addElement("Before", "lt");
		ddLogic.addElement("After", "gt");
		ddLogic.addElement("Between", "in");
		ddLogic.setWidth(225);
		
		ddLogic.selectedByKey("in");
		ddLogic.addElementSelectedListener(new ElementSelectedListener() {
			@Override
			public void elementSelected(ElementSelectedEvent event) {
				onLogicUpdate();
			}
		});

		inpDateFrom = new DatePicker(this, "inpDateFrom");
		inpDateFrom.setWidth(120);
		inpDateTo = new DatePicker(this, "inpDateTo");
		inpDateTo.setWidth(120);
		
		lblTo = new Label(this, "lblTo");
		lblTo.setText("till");
		
		onLogicUpdate();
		
	}
	
	/**
	 * 
	 */
	protected void onLogicUpdate() {
		
		String logic = ddLogic.getSelectedKey();
		if ("in".equals(logic)) {
			lblTo.setVisible(true);
			inpDateTo.setVisible(true);
		} else {
			lblTo.setVisible(false);
			inpDateTo.setVisible(false);
		}
		
	}
	
	/* (non-Javadoc)
	 * @see de.xwic.appkit.webbase.table.filter.AbstractFilterControl#getPreferredWidth()
	 */
	@Override
	public int getPreferredWidth() {
		
		return 280;
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.webbase.table.filter.AbstractFilterControl#getPreferredHeight()
	 */
	@Override
	public int getPreferredHeight() {
		return 64;
	}
	
	/* (non-Javadoc)
	 * @see de.xwic.appkit.webbase.table.filter.AbstractFilterControl#getQueryElement()
	 */
	@Override
	public QueryElement getQueryElement() {
		QueryElement qe = null;
		Date from = inpDateFrom.getDate();
		if (from != null && column.getPropertyIds().length > 0) {
			String logic = ddLogic.getSelectedKey();
			Date to = inpDateTo.getDate();
			if ("in".equals(logic)) {
				if (to == null) {
					logic = "gt";
				} else {
					PropertyQuery subQuery = new PropertyQuery();
					subQuery.addGreaterEqualsThen(column.getPropertyIds()[0], from);
					subQuery.addLowerEqualsThen(column.getPropertyIds()[0], to);
					qe = new QueryElement(QueryElement.AND, subQuery);
				}
			}
			if ("gt".equals(logic)) {
				qe = new QueryElement(column.getPropertyIds()[0], QueryElement.GREATER_THEN, from);
			} else if ("lt".equals(logic)) {
				qe = new QueryElement(column.getPropertyIds()[0], QueryElement.LOWER_THEN, from);
			} else if ("eq".equals(logic)) {
				qe = new QueryElement(column.getPropertyIds()[0], QueryElement.EQUALS, from);
			}
				
		}
		return qe;
	}
	
	/*
	 * (non-Javadoc)
	 * @see de.xwic.appkit.webbase.table.filter.AbstractFilterControl#loadFilter(de.xwic.appkit.webbase.table.Column, de.xwic.appkit.core.model.queries.QueryElement)
	 */
	@Override
	public void initialize(Column column, QueryElement queryElement) {
		this.column = column;
		
		if (queryElement == null) {
			ddLogic.selectedByKey("in");
			inpDateFrom.setDate(null);
			inpDateTo.setDate(null);
		} else {
			if (queryElement.getSubQuery() != null) {
				PropertyQuery subQuery = queryElement.getSubQuery();
				if (subQuery.size() == 2) {
					Object valFrom = subQuery.getElements().get(0).getValue();
					Object valTo = subQuery.getElements().get(1).getValue();
					if (valFrom instanceof Date && valTo instanceof Date) {
						ddLogic.selectedByKey("in");
						inpDateFrom.setDate((Date)valFrom);
						inpDateTo.setDate((Date)valTo);
					}
					
				}
			} else {
				Object val = queryElement.getValue();
				if (val instanceof Date) {
					inpDateFrom.setDate((Date)val);
					if (QueryElement.EQUALS.equals(queryElement.getOperation())) {
						ddLogic.selectedByKey("eq");
					} else if (QueryElement.GREATER_THEN.equals(queryElement.getOperation())) {
						ddLogic.selectedByKey("gt");
					} else if (QueryElement.LOWER_THEN.equals(queryElement.getOperation())) {
						ddLogic.selectedByKey("lt");
					}
				}
			}
		}
		onLogicUpdate();
	}

}
