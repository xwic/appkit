/**
 * 
 */
package de.xwic.appkit.webbase.entityviewer.config;

import java.io.StringReader;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

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

	private static Log log = LogFactory.getLog(ColumnsConfigurationSerializer.class);
	
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
	@SuppressWarnings("unchecked")
	private void deserializeFilters() {
		
		if (userConfiguration.getFiltersConfiguration() == null || userConfiguration.getFiltersConfiguration().trim().isEmpty()) {
			return;
		}
		
		SAXReader reader = new SAXReader();
		Document doc = null;
		try {
			doc = reader.read(new StringReader(userConfiguration.getFiltersConfiguration()));
		} catch (DocumentException e) {
			log.error(e.getMessage(), e);
			return;
		}
		Element root = doc.getRootElement();
		
		// first deserialize the custom quick filter
		
		Element cqf = root.element(ColumnsConfigurationSerializer.CUSTOM_QUICK_FILTER);
		Element pq = cqf.element(ColumnsConfigurationSerializer.PROPERTY_QUERY);
		customQuickFilter = deserializePropertyQuery(pq);
		
		// then deserlialize the column filters

		Element colsElement = root.element(ColumnsConfigurationSerializer.COLS);
		
		if (colsElement != null) {
			
			for (Iterator<Element> it = colsElement.elementIterator(ColumnsConfigurationSerializer.COL); it.hasNext();) {
				Element colElem = it.next();
				
				Element idElem = colElem.element(ColumnsConfigurationSerializer.ID);
				
				String id = idElem.getTextTrim();				
				ColumnConfigurationWrapper wrapper = columnConfigsMap.get(id);
				// we shouldn't have a filter on a column that doesn't exist, but an extra check never hurts
				if (wrapper != null) {
					wrapper.filter = deserializeQueryElement(colElem.element(ColumnsConfigurationSerializer.QUERY_ELEMENT));
				}
			}
		}
	}

	/**
	 * @param qeElement
	 * @return
	 */
	private QueryElement deserializeQueryElement(Element qeElement) {
		QueryElement result = new QueryElement();

		if (qeElement != null) {
		
			Element el = qeElement.element(ColumnsConfigurationSerializer.LINK_TYPE);
			result.setLinkType(Integer.parseInt(el.getTextTrim()));
			
			el = qeElement.element(ColumnsConfigurationSerializer.PROPERTY_QUERY);
			
			if (el != null) {
				result.setSubQuery(deserializePropertyQuery(el));
			} else {
				el = qeElement.element(ColumnsConfigurationSerializer.PROPERTY);
				result.setPropertyName(el.getTextTrim());
				
				el = qeElement.element(ColumnsConfigurationSerializer.OPERATION);
				result.setOperation(el.getTextTrim());
				
				el = qeElement.element(ColumnsConfigurationSerializer.VALUE);
				result.setValue(deserializeValue(el.getTextTrim()));
			}
			
		}
		
		return result;
	}

	/**
	 * @param cqf
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private PropertyQuery deserializePropertyQuery(Element pqElement) {
		PropertyQuery result = new PropertyQuery();

		if (pqElement != null) {
			for (Iterator<Element> it = pqElement.elementIterator(ColumnsConfigurationSerializer.QUERY_ELEMENT); it.hasNext();) {
				Element el = it.next();
				result.addQueryElement(deserializeQueryElement(el));
			}
			
			for (Iterator<Element> it = pqElement.elementIterator(ColumnsConfigurationSerializer.PROPERTY_QUERY); it.hasNext();) {
				Element el = it.next();
				result.addSubQuery(deserializePropertyQuery(el));
			}
		}
		
		return result;
	}
	
	/**
	 * @param str
	 * @return
	 */
	private Object deserializeValue(String str) {
		
		if (str == null || str.trim().isEmpty() || str.startsWith(ColumnsConfigurationSerializer.NULL)) {
			return null;
		} else if (str.startsWith(ColumnsConfigurationSerializer.STRING)) {
			return str.replace(ColumnsConfigurationSerializer.STRING, "");
		} else if (str.startsWith(ColumnsConfigurationSerializer.INT)) {
			return Integer.parseInt(str.replace(ColumnsConfigurationSerializer.INT, ""));
		} else if (str.startsWith(ColumnsConfigurationSerializer.LONG)) {
			return Long.parseLong(str.replace(ColumnsConfigurationSerializer.LONG, ""));
		} else if (str.startsWith(ColumnsConfigurationSerializer.DOUBLE)) {
			return Double.parseDouble(str.replace(ColumnsConfigurationSerializer.DOUBLE, ""));
		} else if (str.startsWith(ColumnsConfigurationSerializer.BOOLEAN)) {
			return Boolean.parseBoolean(str.replace(ColumnsConfigurationSerializer.BOOLEAN, ""));
		} else if (str.startsWith(ColumnsConfigurationSerializer.DATE_TIME)) {
			try {
				return ColumnsConfigurationSerializer.SDF_DATE_TIME.parse(str.replace(ColumnsConfigurationSerializer.DATE_TIME, ""));
			} catch (ParseException e) {
				log.error(e.getMessage(), e);
				return null;
			}
		} else if (str.startsWith(ColumnsConfigurationSerializer.DATE)) {
			try {
				return ColumnsConfigurationSerializer.SDF_DATE.parse(str.replace(ColumnsConfigurationSerializer.DATE, ""));
			} catch (ParseException e) {
				log.error(e.getMessage(), e);
				return null;
			}
		} else {
			throw new IllegalArgumentException("Invalid value: " + str);
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
	 * @return
	 */
	public PropertyQuery getCustomQuickFilter() {
		return customQuickFilter;
	}
}