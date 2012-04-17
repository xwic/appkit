/**
 * 
 */
package de.xwic.appkit.core.registry.impl;

import de.xwic.appkit.core.registry.IExtensionPoint;

/**
 * @author Adrian Ionescu
 */
public class DefaultExtensionPoint implements IExtensionPoint {

	private String id;
	
	/**
	 * 
	 */
	public DefaultExtensionPoint(String id) {
		this.id = id;
	}

	/*
	 * (non-Javadoc)
	 * @see de.xwic.appkit.core.registry.IExtensionPoint#getId()
	 */
	@Override
	public String getId() {
		return id;
	}
}
