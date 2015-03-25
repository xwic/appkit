/*
 * Copyright 2005 pol GmbH
 *
 * de.xwic.appkit.core.dao.HistoryTool
 * Created on 17.08.2005
 *
 */
package de.xwic.appkit.core.dao;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * @author Florian Lippisch
 */
public class HistoryTool {

	/**
	 * Copies the properties from the original entity to the historyEntity
	 * @param original
	 * @param historyEntity
	 * @throws DataAccessException 
	 */
	public static void createHistoryEntity(IEntity original, IHistory historyEntity) throws DataAccessException {
		
		// copy all

		try {
	        BeanInfo info = Introspector.getBeanInfo(original.getClass());
	        Class<? extends IHistory> targetClass = historyEntity.getClass();
	        Object[] noArgs = new Object[0];
	        PropertyDescriptor[] descs = info.getPropertyDescriptors();
	        
	        // loop through all properties
	        for (int i = 0; i < descs.length; i++) {
	        	if (!descs[i].getName().equals("id") &&
	        		!descs[i].getName().equals("version") &&
	        		!descs[i].getName().equals("callbacks")) {
		            Method mRead = descs[i].getReadMethod();
		            // this is the proper way to check if a method is PUBLIC
		            // because a method can also be final (the proxy entities used by Hibernate change the public into public final)
		            if (mRead != null && (mRead.getModifiers() & Modifier.PUBLIC) > 0) {
			            //Method mWrite = descs[i].getWriteMethod();
			            // create writer from 'target'
		            	Method mWrite = null;
			            try {
			            	PropertyDescriptor descr = new PropertyDescriptor(descs[i].getName(), targetClass);
				            mWrite = descr.getWriteMethod();
			            } catch (Throwable t) {}
			            	            
			            
			            // has a writer method?
			            if (mWrite != null) {
			            	// get the value from the source
				            Object value = mRead.invoke(original, noArgs);
				            
				    		// copy map
				    		if (value instanceof Map) {
				    			Map<Object, Object> newMap = new HashMap<Object, Object>();
				    			newMap.putAll((Map<?, ?>)value);
				    			value = newMap;
				    		// copy collection
				    		} else if (value instanceof Collection) {
				    			Collection<Object> newSet;
				    			if (value instanceof List) {
				    				newSet = new ArrayList<Object>();
				    			} else {
				    				newSet = new HashSet<Object>(); //(Collection)value.getClass().newInstance();
				    			}
				    			newSet.addAll((Collection<?>)value);
				    			value = newSet;
				    		}
				            
			            	mWrite.invoke(historyEntity, new Object[] {value});
			            } else {
			            	if (!mRead.getName().equals("getClass")) {
			            		System.out.println("WARNING: No setter method for property " + descs[i].getName());
			            	}
			            }
		            }
	        	}
	        }
		} catch (Exception e) {
			throw new DataAccessException("Cant create History object: " + e, e);
		}
		
	}
	
}
