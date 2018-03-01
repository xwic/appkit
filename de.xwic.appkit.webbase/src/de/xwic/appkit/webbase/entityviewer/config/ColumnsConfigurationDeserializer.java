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

import java.io.StringReader;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
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
	//private PropertyQuery customQuickFilter;
	
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
		
		//Element cqf = root.element(ColumnsConfigurationSerializer.CUSTOM_QUICK_FILTER);
		//Element pq = cqf.element(ColumnsConfigurationSerializer.PROPERTY_QUERY);
		//PropertyQuery pq = deserializePropertyQuery(pq);
		//if (pq != null && pq.size() > 0) {
		//customQuickFilter = pq;
		//}
		
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
				PropertyQuery pq = deserializePropertyQuery(el);
				if (pq != null && pq.size() > 0) {
					result.setSubQuery(pq);
				} else {
					// if, from whatever reason, the subquery found is null or empty, the result must be null
					result = null;
				}
			} else {
				el = qeElement.element(ColumnsConfigurationSerializer.PROPERTY);
				result.setPropertyName(el.getTextTrim());
				
				// apparently we don't need to unescape the operations (<, >)
				el = qeElement.element(ColumnsConfigurationSerializer.OPERATION);
				result.setOperation(el.getTextTrim());
				if (QueryElement.IN.equals(result.getOperation()) || QueryElement.NOT_IN.equals(result.getOperation())) {
					result.setRewriteIn(true);
				}
				
				el = qeElement.element(ColumnsConfigurationSerializer.VALUE);
				result.setValue(deserializeValue(el.getTextTrim()));

				boolean isCollectionElement = false;
				el = qeElement.element(ColumnsConfigurationSerializer.COLLECTION_ELEM);
				if (el != null) {
					isCollectionElement = el.getTextTrim().equalsIgnoreCase("y");
				}
				
				result.setCollectionElement(isCollectionElement);
			}
			
		}
		
		return result;
	}

	/**
	 * @param pqElement
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
				
				PropertyQuery pq = deserializePropertyQuery(el);				
				if (pq != null && pq.size() > 0) {
					result.addSubQuery(pq);
				}
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
		} else if (str.startsWith(ColumnsConfigurationSerializer.COLLECTION)) {
			String val = str.replace(ColumnsConfigurationSerializer.COLLECTION, "");
			String[] values = val.split(ColumnsConfigurationSerializer.COLLECTION_ITEM);
			List result = new ArrayList<>();
			for (String v: values) {
				result.add(deserializeValue(v));
			}
			return result;			
		}
		else {
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
	//public PropertyQuery getCustomQuickFilter() {
	//	return customQuickFilter;
	//}
}