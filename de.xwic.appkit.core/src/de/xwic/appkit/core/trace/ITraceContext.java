/*
 * Copyright (c) 2009 Network Appliance, Inc.
 * All rights reserved.
 */

package de.xwic.appkit.core.trace;

import java.util.Map;

/**
 * @author lippisch
 */
public interface ITraceContext extends ITraceOperation {

	public final static String CATEGORY_DEFAULT = "default";

	/**
	 * Returns the category with the specified name of null if it does not exist.
	 * @param category
	 * @return
	 */
	public abstract ITraceCategory getTraceCategory(String category);

	/**
	 * Returns the map of all categories.
	 * @return
	 */
	public abstract Map<String, ITraceCategory> getTraceCategories();

	/**
	 * Trace a new operation in the default category.
	 * @return
	 */
	public abstract ITraceOperation startOperation();

	/**
	 * Trace a new operation.
	 * @param category
	 * @param name
	 * @return
	 */
	public abstract ITraceOperation startOperation(String category);

	/**
	 * Trace a new operation.
	 * @param category
	 * @param name
	 * @return
	 */
	public abstract ITraceOperation startOperation(String category, String name);
	
	/**
	 * Set an attribute value. Attributes may be used to store further information
	 * about the trace that help to identify the trace.
	 * @param key
	 * @param value
	 */
	public void setAttribute(String key, String value);
	
	/**
	 * Returns an attribute value.
	 * @param key
	 * @return
	 */
	public String getAttribute(String key);
	
	/**
	 * @return the attributes
	 */
	public Map<String, String> getAttributes();	
}