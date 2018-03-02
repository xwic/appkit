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
/**
 * 
 */
package de.xwic.appkit.webbase.entityviewer.config;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

import de.xwic.appkit.core.dao.IEntity;
import de.xwic.appkit.core.model.queries.PropertyQuery;
import de.xwic.appkit.core.model.queries.QueryElement;
import de.xwic.appkit.webbase.table.Column;
import de.xwic.appkit.webbase.table.Column.Sort;
import de.xwic.appkit.webbase.table.EntityTableModel;

/**
 * @author Adrian Ionescu
 */
public class ColumnsConfigurationSerializer {

	public static final String ITEM_SEPARATOR = ";";
	public static final String SUBITEM_SEPARATOR = ",";

	public static final String ROOT_START = "<filters>";
	public static final String ROOT_END = "</filters>";

	//public static final String CUSTOM_QUICK_FILTER = "cqf";
	public static final String COLS = "cols";
	public static final String COL = "col";
	public static final String ID = "id";
	public static final String PROPERTY_QUERY = "pq";
	public static final String QUERY_ELEMENT = "qe";
	public static final String LINK_TYPE = "l";
	public static final String COLLECTION_ELEM = "ce";
	public static final String PROPERTY = "p";
	public static final String OPERATION = "o";
	public static final String VALUE = "v";

	public static final String NULL = "{n}";
	public static final String STRING = "{s}";
	public static final String INT = "{i}";
	public static final String LONG = "{l}";
	public static final String DOUBLE = "{d}";
	public static final String BOOLEAN = "{b}";
	public static final String DATE_TIME = "{t}";
	public static final String COLLECTION = "{c}";
	public static final String COLLECTION_ITEM = "{ci}";
	public static final String DATE = "{da}";

	public static final SimpleDateFormat SDF_DATE = new SimpleDateFormat("dd-MM-yyyy");
	public static final SimpleDateFormat SDF_DATE_TIME = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

	private EntityTableModel model;

	private String columns = "";
	private String sortField = "";
	private String sortDirection = "";
	private String filters = "";

	/**
	 * @param model
	 */
	public ColumnsConfigurationSerializer(EntityTableModel model) {
		this.model = model;

		serializeColumns();

		serializeFilters();
	}

	/**
	 * 
	 */
	private void serializeColumns() {
		StringBuilder sbColumnsConfiguration = new StringBuilder();

		for (Column col : model.getColumns()) {
			if (sbColumnsConfiguration.length() > 0) {
				sbColumnsConfiguration.append(ITEM_SEPARATOR);
			}

			sbColumnsConfiguration.append(col.getId()).append(SUBITEM_SEPARATOR);
			sbColumnsConfiguration.append(col.getWidth()).append(SUBITEM_SEPARATOR);
			sbColumnsConfiguration.append(col.isVisible()).append(SUBITEM_SEPARATOR);
			sbColumnsConfiguration.append(col.getColumnOrder());

			if (col.getSortState() != Sort.NONE) {
				this.sortField = col.getId();
				this.sortDirection = col.getSortState().name();
			}
		}

		this.columns = sbColumnsConfiguration.toString();
	}

	/**
	 * The filters are serialized as XML
	 */
	private void serializeFilters() {
		StringBuilder sbFilters = new StringBuilder();

		sbFilters.append(ROOT_START);

		// first serialize the custom quick filter

		//sbFilters.append("<").append(CUSTOM_QUICK_FILTER).append(">");

		//PropertyQuery customQuickFilter = model.getCustomQuickFilter();

		//if (customQuickFilter != null) {
		//	serializePropertyQuery(customQuickFilter, sbFilters);
		//}

		//sbFilters.append("</").append(CUSTOM_QUICK_FILTER).append(">");

		// then serialize the column filters

		sbFilters.append("<").append(COLS).append(">");

		for (Column col : model.getColumns()) {

			if (col.getFilter() == null) {
				continue;
			}

			sbFilters.append("<").append(COL).append(">");

			sbFilters.append("<").append(ID).append(">").append(col.getId()).append("</").append(ID).append(">");

			serializeQueryElement(col.getFilter(), sbFilters);

			sbFilters.append("</").append(COL).append(">");
		}

		sbFilters.append("</").append(COLS).append(">");

		sbFilters.append(ROOT_END);

		this.filters = sbFilters.toString();
	}

	/**
	 * @return
	 */
	private void serializeQueryElement(QueryElement queryElement, StringBuilder sb) {
		// if the qe is null or has no elements, return 
		if (queryElement == null || ((queryElement.getPropertyName() == null || queryElement.getPropertyName().trim().isEmpty())
				&& (queryElement.getSubQuery() == null || queryElement.getSubQuery().size() == 0))) {
			return;
		}

		sb.append("<").append(QUERY_ELEMENT).append(">");

		sb.append("<").append(LINK_TYPE).append(">").append(queryElement.getLinkType()).append("</").append(LINK_TYPE).append(">");

		if (queryElement.getSubQuery() != null) {

			// if a sub query, the collection element flag is applied to the query elements
			sb.append("<").append(COLLECTION_ELEM).append(">").append("n").append("</").append(COLLECTION_ELEM).append(">");

			serializePropertyQuery(queryElement.getSubQuery(), sb);

		} else {

			if (queryElement.getPropertyName() != null && !queryElement.getPropertyName().trim().isEmpty()) {

				sb.append("<").append(COLLECTION_ELEM).append(">").append(queryElement.isCollectionElement() ? "y" : "n").append("</")
						.append(COLLECTION_ELEM).append(">");
				sb.append("<").append(PROPERTY).append(">").append(queryElement.getPropertyName()).append("</").append(PROPERTY)
						.append(">");
				sb.append("<").append(OPERATION).append(">").append(escapeMe(queryElement.getOperation())).append("</").append(OPERATION)
						.append(">");
				sb.append("<").append(VALUE).append(">").append(serializeQeValue(queryElement)).append("</").append(VALUE).append(">");

			}

		}

		sb.append("</").append(QUERY_ELEMENT).append(">");
	}

	/**
	 * @param pq
	 * @param sb
	 */
	private void serializePropertyQuery(PropertyQuery pq, StringBuilder sb) {

		if (pq.size() < 1) {
			return;
		}

		sb.append("<").append(PROPERTY_QUERY).append(">");

		for (QueryElement qe : pq.getElements()) {
			serializeQueryElement(qe, sb);
		}

		sb.append("</").append(PROPERTY_QUERY).append(">");
	}

	/**
	 * @param query
	 * @param q
	 * @param element
	 * @param index
	 * @return
	 */
	private String serializeQeValue(QueryElement qe) {

		Object value = qe.getValue();

		if (value == null) {
			return NULL;
		} else if (value instanceof Collection) {
			StringBuilder sb = new StringBuilder(COLLECTION);
			boolean first = true;
			for (Object val: (Collection) value) {
				if (!first) {
					sb.append(COLLECTION_ITEM);
				}
				first = false;
				sb.append(serializeValue(val, qe.isTimestamp()));
			}
			return sb.toString();
		} else {
			return serializeValue(value, qe.isTimestamp());
		}

	}

	private String serializeValue(Object value, boolean isTimestamp) {
		if (value == null) {
			return NULL;
		} else if (value instanceof String) {
			return STRING + escapeMe((String) value);
		} else if (value instanceof Integer) {
			return INT + String.valueOf(value);
		} else if (value instanceof Long) {
			return LONG + String.valueOf(value);
		} else if (value instanceof Double) {
			return DOUBLE + String.valueOf(value);
		} else if (value instanceof Boolean) {
			return BOOLEAN + String.valueOf(value);
		} else if (value instanceof Date) {
			if (isTimestamp) {
				return DATE_TIME + SDF_DATE_TIME.format((Date) value);
			} else {
				return DATE + SDF_DATE.format((Date) value);
			}
		} else if (value instanceof IEntity) {
			return LONG + String.valueOf(((IEntity) value).getId());
		} else {
			throw new IllegalArgumentException("Invalid value type: " + value.getClass());
		}
	}

	/**
	 * @param op
	 * @return
	 */
	private String escapeMe(String op) {
		return op.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
	}

	/**
	 * @return the columns
	 */
	public String getColumns() {
		return columns;
	}

	/**
	 * @return the sortField
	 */
	public String getSortField() {
		return sortField;
	}

	/**
	 * @return the sortDirection
	 */
	public String getSortDirection() {
		return sortDirection;
	}

	/**
	 * @return the filters
	 */
	public String getFilters() {
		return filters;
	}
}
