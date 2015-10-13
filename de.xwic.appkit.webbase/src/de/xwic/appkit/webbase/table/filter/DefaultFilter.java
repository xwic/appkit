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

import de.jwic.base.IControlContainer;
import de.jwic.controls.InputBox;
import de.jwic.controls.combo.DropDown;
import de.jwic.events.KeyEvent;
import de.jwic.events.KeyListener;
import de.xwic.appkit.core.model.queries.QueryElement;
import de.xwic.appkit.webbase.table.Column;

/**
 * Implements the standard filter for text based search.
 * 
 * @author lippisch
 */
public class DefaultFilter extends AbstractFilterControl {

	private static final String LOGIC_EQUALS = "eq";
	private static final String LOGIC_NOT_EQUALS = "neq";
	private static final String LOGIC_STARTS_WITH = "sq";
	private static final String LOGIC_ENDS_WITH = "ew";
	private static final String LOGIC_CONTAINS = "c";
	private static final String LOGIC_NOT_CONTAINS = "nc";

	private DropDown ddLogic;
	private InputBox inpText;

	/**
	 * @param container
	 * @param name
	 */
	public DefaultFilter(IControlContainer container, String name) {
		super(container, name);

		ddLogic = new DropDown(this, "logic");
		ddLogic.addElement("Equals", LOGIC_EQUALS);
		ddLogic.addElement("Does Not Equal", LOGIC_NOT_EQUALS);
		ddLogic.addElement("Starts With", LOGIC_STARTS_WITH);
		ddLogic.addElement("Ends With", LOGIC_ENDS_WITH);
		ddLogic.addElement("Contains", LOGIC_CONTAINS);
		ddLogic.addElement("Does Not Contain", LOGIC_NOT_CONTAINS);
		ddLogic.setWidth(225);

		ddLogic.selectedByKey(LOGIC_STARTS_WITH);

		inpText = new InputBox(this, "inpText");
		inpText.setEmptyInfoText("Enter Search Term");
		inpText.setWidth(225);

		inpText.setListenKeyCode(13);
		inpText.addKeyListener(new KeyListener() {

			@Override
			public void keyPressed(KeyEvent event) {
				notifyListeners();
			}
		});

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.xwic.appkit.webbase.table.filter.AbstractFilterControl#getPreferredHeight()
	 */
	@Override
	public int getPreferredHeight() {
		return 70;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.xwic.appkit.webbase.table.filter.AbstractFilterControl#getQueryElement()
	 */
	@Override
	public QueryElement getQueryElement() {
		QueryElement qe = null;
		String search = inpText.getText().trim();
		if (!search.isEmpty()) {
			String[] propIds = column.getPropertyIds();
			if (propIds.length > 0 && !propIds[0].startsWith("#")) {
				String logic = ddLogic.getSelectedKey();
				if (logic != null) {
					qe = new QueryElement();
					qe.setLinkType(QueryElement.AND);
					qe.setPropertyName(propIds[0]);

					if ((LOGIC_EQUALS.equals(logic) || LOGIC_STARTS_WITH.equals(logic) || LOGIC_CONTAINS.equals(logic))
							|| LOGIC_ENDS_WITH.equals(logic)) {
						qe.setOperation(QueryElement.LIKE);
					} else if (LOGIC_NOT_EQUALS.equals(logic) || LOGIC_NOT_CONTAINS.equals(logic)) {
						qe.setOperation(QueryElement.NOT_LIKE);
					}
					if ((LOGIC_STARTS_WITH.equals(logic) || LOGIC_CONTAINS.equals(logic) || LOGIC_NOT_CONTAINS.equals(logic))
							&& !search.endsWith("%")) {
						search = search + "%";
					}
					if ((LOGIC_ENDS_WITH.equals(logic) || LOGIC_CONTAINS.equals(logic) || LOGIC_NOT_CONTAINS.equals(logic))
							&& !search.startsWith("%")) {
						search = "%" + search;
					}

					// in case it's equal we should not trim the search string
					if (LOGIC_EQUALS.equals(logic) || LOGIC_NOT_EQUALS.equals(logic)) {
						search = inpText.getText();
					}

					qe.setValue(search);
				}
			}
		}
		return qe;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.xwic.appkit.webbase.table.filter.AbstractFilterControl#loadFilter(de.xwic.appkit.webbase.table.Column,
	 * de.xwic.appkit.core.model.queries.QueryElement)
	 */
	@Override
	public void initialize(Column column, QueryElement queryElement) {
		this.column = column;

		if (queryElement == null) {
			ddLogic.selectedByKey(LOGIC_STARTS_WITH);
			inpText.setText("");
		} else {
			Object value = queryElement.getValue();
			String search = "";
			if (value instanceof String) {
				search = (String) value;
			} else if (value instanceof Number) {
				search = ((Number) value).toString();
			}
			if (QueryElement.EQUALS.equals(queryElement.getOperation())) {
				ddLogic.selectedByKey(LOGIC_EQUALS);
			} else if (QueryElement.NOT_EQUALS.equals(queryElement.getOperation())) {
				ddLogic.selectedByKey(LOGIC_NOT_EQUALS);
			} else if (QueryElement.LIKE.equals(queryElement.getOperation())) {
				if (search.startsWith("%")) {
					search = search.substring(1);
					if (search.endsWith("%")) {
						ddLogic.selectedByKey(LOGIC_CONTAINS);
						search = search.substring(0, search.length() - 1);
					} else {
						ddLogic.selectedByKey(LOGIC_ENDS_WITH);
					}
				} else if (search.endsWith("%")) {
					ddLogic.selectedByKey(LOGIC_STARTS_WITH);
					search = search.substring(0, search.length() - 1);
				} else {
					ddLogic.selectedByKey(LOGIC_EQUALS);
				}
			} else if (QueryElement.NOT_LIKE.equals(queryElement.getOperation())) {
				boolean contains = false;
				if (search.startsWith("%")) {
					search = search.substring(1);
					contains = true;
				}
				if (search.endsWith("%")) {
					search = search.substring(0, search.length() - 1);
					contains = true;
				}

				ddLogic.selectedByKey(contains ? LOGIC_NOT_CONTAINS : LOGIC_NOT_EQUALS);
			}
			inpText.setText(search);
		}
	}

}
