/**
 * 
 */
package de.xwic.appkit.dev.engine.model.impl;

import org.dom4j.Node;

import de.xwic.appkit.dev.engine.model.EntityPicklistEntry;

/**
 * @author lippisch
 */
public class XMLEntityPicklistEntry implements EntityPicklistEntry {

	private String key;
	private String defaultTitle;
	private String order;
	
	public XMLEntityPicklistEntry(Node node) {
		this.key = node.valueOf("@key");
		this.defaultTitle = node.valueOf("@defaultTitle");
		this.order = node.valueOf("@order");
		
		if (this.defaultTitle == null || this.defaultTitle.isEmpty()) {
			this.defaultTitle = key;
		}
		
	}
	
	/* (non-Javadoc)
	 * @see de.xwic.appkit.dev.engine.model.EntityPicklistEntry#getKey()
	 */
	@Override
	public String getKey() {
		return key;
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.dev.engine.model.EntityPicklistEntry#getDefaultTitle()
	 */
	@Override
	public String getDefaultTitle() {
		return defaultTitle;
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.dev.engine.model.EntityPicklistEntry#getOrder()
	 */
	@Override
	public String getOrder() {
		return order;
	}

}
