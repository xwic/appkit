/**
 * 
 */
package de.xwic.appkit.webbase.table;

import java.util.HashMap;
import java.util.Map;

import de.xwic.appkit.core.model.entities.IUserViewConfiguration;
import de.xwic.appkit.webbase.table.Column.Sort;

/**
 * Util class to help with parsing the column setting from a UserViewConfiguration
 * 
 * @author Adrian Ionescu
 */
public class ColumnsConfigurationUtil {
	
	private IUserViewConfiguration userConfiguration;
	private Map<String, ColumnConfigurationWrapper> columnConfigsMap;

	/**
	 * @param userConfiguration
	 */
	public ColumnsConfigurationUtil(IUserViewConfiguration userConfiguration) {
		this.userConfiguration = userConfiguration;
		
		columnConfigsMap = new HashMap<String, ColumnConfigurationWrapper>();
		
		if (userConfiguration.getColumnsConfiguration() != null) {
			String[] columnConfigs = userConfiguration.getColumnsConfiguration().split(";");
			for (String columnConfig : columnConfigs) {
				String[] items = columnConfig.split(",");

				ColumnConfigurationWrapper wrapper = new ColumnConfigurationWrapper();
				wrapper.name = items[0];
				wrapper.size = Integer.parseInt(items[1]);
				wrapper.visible = "true".equalsIgnoreCase(items[2]);
				wrapper.index = Integer.parseInt(items[3]);

				columnConfigsMap.put(wrapper.name, wrapper);
			}
		}
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
	 * @author Adrian Ionescu
	 */
	public class ColumnConfigurationWrapper {
		public String name;
		public int size;
		public boolean visible;
		public int index;
	}
}
