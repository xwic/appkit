/**
 * 
 */
package de.xwic.appkit.webbase.entityviewer.config;

import java.util.HashMap;
import java.util.Map;

import de.xwic.appkit.core.model.entities.IUserViewConfiguration;
import de.xwic.appkit.core.model.queries.PropertyQuery;
import de.xwic.appkit.core.model.queries.QueryElement;
import de.xwic.appkit.webbase.table.Column.Sort;

/**
 * Util class to help with parsing the column setting from a UserViewConfiguration
 * 
 * @author Adrian Ionescu
 */
public class ColumnsConfigurationDeserializer {

	public class ColumnConfigurationWrapper {
		public String name;
		public int size;
		public boolean visible;
		public int index;
		public QueryElement filter;
	}
	
	private IUserViewConfiguration userConfiguration;
	private Map<String, ColumnConfigurationWrapper> columnConfigsMap;
	private PropertyQuery customQuickFilter;
	
	/**
	 * @param userConfiguration
	 */
	public ColumnsConfigurationDeserializer(IUserViewConfiguration userConfiguration) {
		this.userConfiguration = userConfiguration;

		deserializeColumns();
		
		deserializeFilters();
	}

	/**
	 * 
	 */
	private void deserializeColumns() {
		columnConfigsMap = new HashMap<String, ColumnConfigurationWrapper>();
		
		if (userConfiguration.getColumnsConfiguration() == null || userConfiguration.getColumnsConfiguration().trim().isEmpty()) {
			return;
		}
		
		String[] columnConfigs = userConfiguration.getColumnsConfiguration().split(ColumnsConfigurationSerializer.ITEM_SEPARATOR);
		for (String columnConfig : columnConfigs) {
			String[] items = columnConfig.split(ColumnsConfigurationSerializer.SUBITEM_SEPARATOR);

			ColumnConfigurationWrapper wrapper = new ColumnConfigurationWrapper();
			wrapper.name = items[0];
			wrapper.size = Integer.parseInt(items[1]);
			wrapper.visible = "true".equalsIgnoreCase(items[2]);
			wrapper.index = Integer.parseInt(items[3]);

			columnConfigsMap.put(wrapper.name, wrapper);
		}
	}
	
	/**
	 * 
	 */
	private void deserializeFilters() {
		
		if (userConfiguration.getFiltersConfiguration() == null || userConfiguration.getFiltersConfiguration().trim().isEmpty()) {
			return;
		}
		
		// customQuickFilter and column filters are separated by a custom separator
		int separatorIndex = userConfiguration.getFiltersConfiguration().indexOf(ColumnsConfigurationSerializer.CATEGORY_SEPARATOR);
		
		// first deserialize the custom quick filter
		
		String strCustomQuickFilter = userConfiguration.getFiltersConfiguration().substring(0, separatorIndex);
		
		if (!strCustomQuickFilter.trim().isEmpty()) {
			
			customQuickFilter = new PropertyQuery();
			
			String[] filterConfigs = userConfiguration.getFiltersConfiguration().split(ColumnsConfigurationSerializer.ITEM_SEPARATOR);
			for (String filterConfig : filterConfigs) {
				customQuickFilter.addQueryElement(deserializeQueryElement(filterConfig));
			}
			
		}
		
		// then deserlialize the column filters

		String strColumnFilters = userConfiguration.getFiltersConfiguration().substring(separatorIndex + ColumnsConfigurationSerializer.CATEGORY_SEPARATOR.length());
		
		if (!strColumnFilters.trim().isEmpty()) {
			
			String[] filterConfigs = userConfiguration.getFiltersConfiguration().split(ColumnsConfigurationSerializer.ITEM_SEPARATOR);
			for (String filterConfig : filterConfigs) {
				// <columnName, filter>
				String[] items = filterConfig.split(ColumnsConfigurationSerializer.SUBITEM_SEPARATOR);
				
				ColumnConfigurationWrapper wrapper = columnConfigsMap.get(items[0]);
				// we shouldn't have a filter on a column that doesn't exist, but an extra check never hurts
				if (wrapper != null) {
					wrapper.filter = deserializeQueryElement(items[1]);
				}
			}
			
		}
	}

	/**
	 * @param strQE
	 * @return
	 */
	private QueryElement deserializeQueryElement(String strQE) {
		if (strQE.trim().isEmpty()) {
			return null;
		}
		
		return null;
	}

	/**
	 * @param name
	 * @return
	 */
	public ColumnConfigurationWrapper getColumnConfiguration(String name){
		return columnConfigsMap.get(name);
	}
	
	/**
	 * @return
	 */
	public String getSortField() {
		return userConfiguration.getSortField();
	}
	
	/**
	 * @return
	 */
	public Sort getSortDirection() {
		return Sort.valueOf(userConfiguration.getSortDirection());
	}
	
	/**
	 * @return
	 */
	public PropertyQuery getCustomQuickFilter() {
		return customQuickFilter;
	}
}