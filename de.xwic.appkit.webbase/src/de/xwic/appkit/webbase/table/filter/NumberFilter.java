/**
 * 
 */
package de.xwic.appkit.webbase.table.filter;

import de.jwic.base.IControlContainer;
import de.jwic.controls.InputBoxControl;
import de.jwic.controls.LabelControl;
import de.jwic.controls.combo.DropDown;
import de.jwic.events.ElementSelectedEvent;
import de.jwic.events.ElementSelectedListener;
import de.xwic.appkit.core.model.queries.PropertyQuery;
import de.xwic.appkit.core.model.queries.QueryElement;
import de.xwic.appkit.webbase.table.Column;

/**
 * @author Claudiu Mateias
 *
 */
public class NumberFilter extends AbstractFilterControl {

	private final DropDown ddLogic;
	private final InputBoxControl inpNumberFrom;
	private final InputBoxControl inpNumberTo;
	private final LabelControl lblTo;
	
	/**
	 * @param container
	 * @param name
	 */
	public NumberFilter(IControlContainer container, String name) {
		super(container, name);

		ddLogic = new DropDown(this, "logic");
		ddLogic.addElement("<", "lt");
		ddLogic.addElement("<=", "lte");
		ddLogic.addElement("=", "eq");
		ddLogic.addElement(">", "gt");
		ddLogic.addElement(">=", "gte");
		ddLogic.addElement("Between", "in");
		ddLogic.setWidth(200);
		
		ddLogic.selectedByKey("in");
		ddLogic.addElementSelectedListener(new ElementSelectedListener() {
			@Override
			public void elementSelected(ElementSelectedEvent event) {
				onLogicUpdate();
			}
		});

		inpNumberFrom = new InputBoxControl(this, "inpNumberFrom");
		inpNumberFrom.setWidth(70);
		
		inpNumberTo = new InputBoxControl(this, "inpNumberTo");
		inpNumberTo.setWidth(70);
		
		lblTo = new LabelControl(this, "lblTo");
		lblTo.setText("and");
		
		onLogicUpdate();
	}

	/**
	 * 
	 */
	protected void onLogicUpdate() {
		
		String logic = ddLogic.getSelectedKey();
		if ("in".equals(logic)) {
			lblTo.setVisible(true);
			inpNumberTo.setVisible(true);
		} else {
			lblTo.setVisible(false);
			inpNumberTo.setVisible(false);
		}
		
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.webbase.table.filter.AbstractFilterControl#initialize(de.xwic.appkit.webbase.table.Column, de.xwic.appkit.core.model.queries.QueryElement)
	 */
	@Override
	public void initialize(Column column, QueryElement queryElement) {
		this.column = column;
		
		if (queryElement == null) {
			ddLogic.selectedByKey("eq");
			inpNumberFrom.setText("");
			inpNumberTo.setText("");
		} else {
			if (queryElement.getSubQuery() != null) {
				PropertyQuery subQuery = queryElement.getSubQuery();
				if (subQuery.size() == 2) {
					Object valFrom = subQuery.getElements().get(0).getValue();
					Object valTo = subQuery.getElements().get(1).getValue();
					if (valFrom instanceof Integer && valTo instanceof Integer) {
						ddLogic.selectedByKey("in");
						inpNumberFrom.setText(String.valueOf(valFrom));
						inpNumberTo.setText(String.valueOf(valTo));
					}
				}
			} else {
				Object value = queryElement.getValue();
				if (value instanceof Integer || value instanceof Double) {
					inpNumberFrom.setText(String.valueOf(value));
					String operation = queryElement.getOperation();
					if (QueryElement.LOWER_THEN.equals(operation)) {
						ddLogic.selectedByKey("lt");
					} else if (QueryElement.LOWER_EQUALS_THEN.equals(operation)) {
						ddLogic.selectedByKey("lte");
					} else if (QueryElement.EQUALS.equals(operation)) {
						ddLogic.selectedByKey("eq");
					} else if (QueryElement.GREATER_THEN.equals(operation)) {
						ddLogic.selectedByKey("gt");
					} else if (QueryElement.GREATER_EQUALS_THEN.equals(operation)) {
						ddLogic.selectedByKey("gte");
					}
				}
			}
		}
		onLogicUpdate();

	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.webbase.table.filter.AbstractFilterControl#getQueryElement()
	 */
	@Override
	public QueryElement getQueryElement() {
		QueryElement qe = null;
		Object from = null;
		Object to = null;
		String property = column.getListColumn().getPropertyId();
		String type = column.getListColumn().getFinalProperty().getEntityType();
		try {
			if ("java.lang.Integer".equals(type) || "int".equals(type)) {
				from = Integer.parseInt(inpNumberFrom.getText());
			} else if ("java.lang.Double".equals(type) || "double".equals(type)) {
				from = Double.parseDouble(inpNumberFrom.getText());
			} else if ("java.lang.Long".equals(type) || "long".equals(type)) {
				from = Long.parseLong(inpNumberFrom.getText());
			}
		} catch(NumberFormatException e) {
			
		}
		if (from != null && property != null) {
			String logic = ddLogic.getSelectedKey();
			try {
				if ("java.lang.Integer".equals(type) || "int".equals(type)) {
					to = Integer.parseInt(inpNumberTo.getText());
				} else if ("java.lang.Double".equals(type) || "double".equals(type)) {
					to = Double.parseDouble(inpNumberTo.getText());
				} else if ("java.lang.Long".equals(type) || "long".equals(type)) {
					to = Long.parseLong(inpNumberTo.getText());
				}
			} catch(NumberFormatException e) {
				
			}
			if ("in".equals(logic)) {
				if (to == null) {
					logic = "gt";
				} else {
					PropertyQuery query = new PropertyQuery();
					query.addGreaterEqualsThen(property, from);
					query.addLowerEqualsThen(property, to);
					qe = new QueryElement(QueryElement.AND, query);
				}
			}
			if ("gt".equals(logic)) {
				qe = new QueryElement(property, QueryElement.GREATER_THEN, from);
			} else if ("gte".equals(logic)) {
				qe = new QueryElement(property, QueryElement.GREATER_EQUALS_THEN, from);
			} else if ("lt".equals(logic)) {
				qe = new QueryElement(property, QueryElement.LOWER_THEN, from);
			} else if ("lte".equals(logic)) {
				qe = new QueryElement(property, QueryElement.LOWER_EQUALS_THEN, from);
			} else if ("eq".equals(logic)) {
				qe = new QueryElement(property, QueryElement.EQUALS, from);
			}
		}
		
		return qe;
	}
	
	/* (non-Javadoc)
	 * @see de.xwic.appkit.webbase.table.filter.AbstractFilterControl#getPreferredHeight()
	 */
	@Override
	public int getPreferredHeight() {
		return 64;
	}

}
