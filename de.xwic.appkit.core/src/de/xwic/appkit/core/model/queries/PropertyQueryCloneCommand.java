/*
 * (c) Copyright 2005, 2006 by pol GmbH 
 * 
 * de.xwic.appkit.core.model.queries.PropertyQueryCloneCommand
 * Created on Jun 20, 2008 by Aron Cotrau
 *
 */
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
