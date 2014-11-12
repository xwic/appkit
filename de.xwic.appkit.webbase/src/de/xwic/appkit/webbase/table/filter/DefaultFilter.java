/*
 * Copyright (c) 2009 Network Appliance, Inc.
 * All rights reserved.
 */

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
 * @author lippisch
 */
public class DefaultFilter extends AbstractFilterControl {

	private DropDown ddLogic;
	private InputBox inpText;
	
	
	/**
	 * @param container
	 * @param name
	 */
	public DefaultFilter(IControlContainer container, String name) {
		super(container, name);
		
		ddLogic = new DropDown(this, "logic");
		ddLogic.addElement("Equals", "eq");
		ddLogic.addElement("Does Not Equal", "neq");
		ddLogic.addElement("Starts With", "sw");
		ddLogic.addElement("Ends With", "ew");
		ddLogic.addElement("Contains", "c");
		ddLogic.addElement("Does Not Contain","nc");
		ddLogic.setWidth(225);
		
		ddLogic.selectedByKey("sw");
		
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
	
	/* (non-Javadoc)
	 * @see de.xwic.appkit.webbase.table.filter.AbstractFilterControl#getPreferredHeight()
	 */
	@Override
	public int getPreferredHeight() {
		return 70;
	}
	
	/* (non-Javadoc)
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
					
					if(("eq".equals(logic) || "sw".equals(logic) || "c".equals(logic))){
						qe.setOperation(QueryElement.LIKE);
					}else if("neq".equals(logic) ||"nc".equals(logic)){
						qe.setOperation(QueryElement.NOT_LIKE);
					}
					if (("sw".equals(logic) || "c".equals(logic) || "nc".equals(logic)) && !search.endsWith("%")) {
						search = search + "%";
					} 
					if (("ew".equals(logic) || "c".equals(logic) || "nc".equals(logic)) && !search.startsWith("%")) {
						search = "%" + search;
					}
					
					// in case it's equal we should not trim the seaerch string
					if("eq".equals(logic) || "neq".equals(logic)){
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
	 * @see de.xwic.appkit.webbase.table.filter.AbstractFilterControl#loadFilter(de.xwic.appkit.webbase.table.Column, de.xwic.appkit.core.model.queries.QueryElement)
	 */
	@Override
	public void initialize(Column column, QueryElement queryElement) {
		this.column = column;
		
		if (queryElement == null) {
			ddLogic.selectedByKey("sw");
			inpText.setText("");
		} else {
			Object value = queryElement.getValue();
			String search = "";
			if (value instanceof String) {
				search = (String)value;
			} else if (value instanceof Number) {
				search = ((Number)value).toString();
			}
			if (QueryElement.EQUALS.equals(queryElement.getOperation())) {
				ddLogic.selectedByKey("eq");
			} else if (QueryElement.NOT_EQUALS.equals(queryElement.getOperation())) {
				ddLogic.selectedByKey("neq");
			} else if (QueryElement.LIKE.equals(queryElement.getOperation())) {
				if (search.startsWith("%")) {
					search = search.substring(1);
					if (search.endsWith("%")) {
						ddLogic.selectedByKey("c");
						search = search.substring(0, search.length() - 1);
					} else {
						ddLogic.selectedByKey("ew");
					}
				} else if (search.endsWith("%")) {
					ddLogic.selectedByKey("sw");
					search = search.substring(0, search.length() - 1);
				} else {
					ddLogic.selectedByKey("eq");
				}
			}else if(QueryElement.NOT_LIKE.equals(queryElement.getOperation())){
				boolean contains = false;
				if(search.startsWith("%")){
					search = search.substring(1);
					contains = true;
				}
				if(search.endsWith("%")){
					search = search.substring(0,search.length()-1);
					contains = true;
				}
				
				ddLogic.selectedByKey(contains ? "nc" : "neq");
			}
			inpText.setText(search);
		}
	}

}
