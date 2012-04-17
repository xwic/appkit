/**
 * 
 */
package de.xwic.appkit.core.registry.impl;

import java.util.HashMap;
import java.util.Map;

import de.xwic.appkit.core.registry.IExtension;
import de.xwic.appkit.core.registry.IExtensionFactory;

/**
 * @author Adrian Ionescu
 */
public class DefaultExtension implements IExtension {

	private IExtensionFactory extensionFactory;
	private Map<String, String> attributes;
	
	private String extensionPointId;
	private String id;
	private int priority;
	private String className;
	private int sortIndex = 0;
	
	/**
	 * 
	 */
	public DefaultExtension(String extensionPointId, String id, int priority, String className, int sortIndex) {
		extensionFactory = new DefaultExtensionFactory();
		attributes = new HashMap<String, String>();
		
		this.extensionPointId = extensionPointId;
		this.id = id;
		this.priority = priority;
		this.className = className;
		this.sortIndex = sortIndex;
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.registry.IExtension#getExtensionPointId()
	 */
	@Override
	public String getExtensionPointId() {
		return extensionPointId;
	}
	
	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.registry.IExtension#getId()
	 */
	@Override
	public String getId() {
		return id;
	}
	
	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.registry.IExtension#getPriority()
	 */
	@Override
	public int getPriority() {
		return priority;
	}
	
	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.registry.IExtension#getClassName()
	 */
	@Override
	public String getClassName() {
		return className;
	}
	
	/*
	 * (non-Javadoc)
	 * @see de.xwic.appkit.core.registry.IExtension#getExtensionFactory()
	 */
	@Override
	public IExtensionFactory getExtensionFactory() {
		return extensionFactory;
	}
	
	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.registry.IExtension#createExtensionObject()
	 */
	@Override
	public Object createExtensionObject() throws Exception {
		return getExtensionFactory().createExtension(this);
	}
	
	/*
	 * (non-Javadoc)
	 * @see de.xwic.appkit.core.registry.IExtension#addAttribute(java.lang.String, java.lang.String)
	 */
	@Override
	public void addAttribute(String key, String value) {
		attributes.put(key, value);
	}
	
	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.registry.IExtension#getAttribute(java.lang.String)
	 */
	@Override
	public String getAttribute(String key) {
		return attributes.get(key);
	}

	/*
	 * (non-Javadoc)
	 * @see de.xwic.appkit.core.registry.IExtension#getAllAttributes()
	 */
	@Override
	public Map<String, String> getAllAttributes() {
		return attributes;
	}

	/**
	 * @return the sortIndex
	 */
	public int getSortIndex() {
		return sortIndex;
	}

	/**
	 * @param sortIndex the sortIndex to set
	 */
	public void setSortIndex(int sortIndex) {
		this.sortIndex = sortIndex;
	}
}
