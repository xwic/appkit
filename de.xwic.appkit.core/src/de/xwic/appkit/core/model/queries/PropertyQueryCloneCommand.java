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
package de.xwic.appkit.core.model.queries;

import java.util.ArrayList;
import java.util.LinkedHashMap;



/**
 * Useful class for cloning a {@link PropertyQuery}.
 *
 * @author Aron Cotrau
 */
public class PropertyQueryCloneCommand {

	/**
	 * @param target
	 * @return a clone of the initial query
	 */
	public static final PropertyQuery cloneQuery(PropertyQuery target) {
		
		return cloneQuery(target, true);
		
	}
	
	/**
	 * @param target
	 * @param cloneElements
	 * @return a clone of the initial query
	 */
	public static final PropertyQuery cloneQuery(PropertyQuery target, boolean cloneElements) {
		
		if (null == target) {
			return null;
		}
		
		PropertyQuery propQuery = new PropertyQuery(target.getWildcardPreferenceSetting());
		
		propQuery.setElements(cloneElements ? new ArrayList<QueryElement>(target.getElements()) : new ArrayList<QueryElement>());
		propQuery.setColumns(null != target.getColumns() ? new ArrayList<String>(target.getColumns()) : null);
		//propQuery.setLeftOuterJoinProperties(new ArrayList<String>(target.getLeftOuterJoinProperties()));
		propQuery.setLeftOuterJoinPropertiesMap(new LinkedHashMap<String, String>(target.getLeftOuterJoinPropertiesMap()));
		
		propQuery.setHideDeleted(target.isHideDeleted());
		propQuery.setJoinPicklistEntries(target.isJoinPicklistEntries());
		
		propQuery.setLanguageId(target.getLanguageId());
		propQuery.setSortField(target.getSortField());
		
		propQuery.setSortDirection(target.getSortDirection());
		
		return propQuery;
		
		
	}
	
}
