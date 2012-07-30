/**
 * 
 */
package de.xwic.appkit.webbase.entityviewer.config;

import de.xwic.appkit.core.model.queries.PropertyQuery;
import de.xwic.appkit.core.model.queries.QueryElement;
import de.xwic.appkit.webbase.table.Column;
import de.xwic.appkit.webbase.table.Column.Sort;
import de.xwic.appkit.webbase.table.EntityTableModel;

/**
 * @author Adrian Ionescu
 */
public class ColumnsConfigurationSerializer {

	public static final String CATEGORY_SEPARATOR = "<||>";
	public static final String ITEM_SEPARATOR = ";";
	public static final String SUBITEM_SEPARATOR = ",";
	
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
	 * 
	 */
	private void serializeFilters() {
		StringBuilder sbFilters = new StringBuilder();
		
		// first serialize the custom quick filter
		
		PropertyQuery pq = model.getCustomQuickFilter();

		for (QueryElement qe : pq.getElements()) {
			if (sbFilters.length() != 0) {
				sbFilters.append(ITEM_SEPARATOR);
			}
			sbFilters.append(serializeQueryElement(qe));
		}
		
		// separate the two filters with a custom separator
		sbFilters.append(CATEGORY_SEPARATOR);
		
		// then serialize the column filters
		
		boolean first = true;
		for (Column col : model.getColumns()) {
			if (!first) {
				sbFilters.append(ITEM_SEPARATOR);
			}
			sbFilters.append(col.getId()).append(SUBITEM_SEPARATOR).append(serializeQueryElement(col.getFilter()));
			first = false;
		}
		
		this.filters = sbFilters.toString();
	}

	/**
	 * @return
	 */
	private String serializeQueryElement(QueryElement qe) {
		if (qe == null) {
			return "";
		}
		
		return null;
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
