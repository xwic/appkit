/**
 * 
 */
package de.xwic.appkit.webbase.table.filter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.jwic.base.IControlContainer;
import de.jwic.controls.InputBox;
import de.jwic.controls.Label;
import de.jwic.controls.combo.DropDown;
import de.jwic.events.ElementSelectedEvent;
import de.jwic.events.ElementSelectedListener;
import de.jwic.events.KeyListener;
import de.xwic.appkit.core.model.queries.PropertyQuery;
import de.xwic.appkit.core.model.queries.QueryElement;
import de.xwic.appkit.webbase.table.Column;

/**
 * @author Claudiu Mateias
 *
 */
public class NumberFilter extends AbstractFilterControl {

	private final DropDown ddLogic;
	private final InputBox inpNumberFrom;
	private final InputBox inpNumberTo;
	private final Label lblTo;
	private final Log log = LogFactory.getLog(getClass());
	
	
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

		inpNumberFrom = new InputBox(this, "inpNumberFrom");
		inpNumberFrom.setWidth(70);
		
		inpNumberTo = new InputBox(this, "inpNumberTo");
		inpNumberTo.setWidth(70);
		
		lblTo = new Label(this, "lblTo");
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
			inpNumberTo.setText("");
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
					if ((valFrom instanceof Integer || valFrom instanceof Double || valFrom instanceof Long) && 
							(valTo instanceof Integer || valTo instanceof Double || valTo instanceof Long)) {
						ddLogic.selectedByKey("in");
						inpNumberFrom.setText(String.valueOf(valFrom));
						inpNumberTo.setText(String.valueOf(valTo));
					}
				}
			} else {
				Object value = queryElement.getValue();
				if (value instanceof Integer || value instanceof Double || value instanceof Long) {
					inpNumberFrom.setText(String.valueOf(value));
					String operation = queryElement.getOperation();
					if (QueryElement.LOWER_THEN.equals(operation)) {
						ddLogic.selectedByKey("lt");
//						inpNumberTo.setText(String.valueOf(value));
					} else if (QueryElement.LOWER_EQUALS_THEN.equals(operation)) {
						ddLogic.selectedByKey("lte");
//						inpNumberTo.setText(String.valueOf(value));
					} else if (QueryElement.EQUALS.equals(operation)) {
						ddLogic.selectedByKey("eq");
//						inpNumberFrom.setText(String.valueOf(value));
					} else if (QueryElement.GREATER_THEN.equals(operation)) {
						ddLogic.selectedByKey("gt");
//						inpNumberFrom.setText(String.valueOf(value));
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
		String property = column.getListColumn().getPropertyId();
		String type = column.getListColumn().getFinalProperty().getEntityType();
		String fromText = inpNumberFrom.getText();
		String toText = inpNumberTo.getText();
		String logic = ddLogic.getSelectedKey();
		
		Object from = getNumber(type, fromText);
		Object to = getNumber(type, toText);
		if (null == from && null == to) {
			return null; // bail here, we don't want something like number is null
		}

		if ("in".equals(logic)) {
			if (to == null && from != null) {
				logic = "gte";
			} else if (to != null && from == null) {
				logic = "lte";
				from = to; // because we use below in the query <from>
			} else if (to != null && from != null) {
				PropertyQuery query = new PropertyQuery();
				query.addGreaterEqualsThen(property, from);
				query.addLowerEqualsThen(property, to);
				qe = new QueryElement(QueryElement.AND, query);
			} else { // both are null
				return qe;
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
		
		
		return qe;
	}

	/**
	 * @param type
	 * @param fromText
	 * @return
	 */
	private Object getNumber(String type, String fromText) {
		Object from = null;
		if (fromText != null && !(fromText = fromText.trim()).isEmpty()) {
			try {
				if ("java.lang.Integer".equals(type) || "int".equals(type)) {
					from = Integer.parseInt(fromText);
				} else if ("java.lang.Double".equals(type) || "double".equals(type)) {
					from = Double.parseDouble(fromText);
				} else if ("java.lang.Long".equals(type) || "long".equals(type)) {
					from = Long.parseLong(fromText);
				}
			} catch(NumberFormatException e) {
				log.error("Error while getting the number " + e.getMessage(), e);
			}
		}
		return from;
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.webbase.table.filter.AbstractFilterControl#getPreferredHeight()
	 */
	@Override
	public int getPreferredHeight() {
		return 64;
	}
	
	/**
	 * @param listener
	 */
	public void addKeyPressedListener(KeyListener listener) {
		inpNumberFrom.setListenKeyCode(13);
		inpNumberTo.setListenKeyCode(13);
		inpNumberFrom.addKeyListener(listener);
		inpNumberTo.addKeyListener(listener);
	}

}
