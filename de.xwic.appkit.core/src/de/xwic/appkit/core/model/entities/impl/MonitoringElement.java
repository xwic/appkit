/*
 * (c) Copyright 2005, 2006 by pol GmbH 
 * 
 * de.xwic.appkit.core.model.entities.impl.MonitoringElement
 * Created on 04.01.2008 by Florian Lippisch
 *
 */
package de.xwic.appkit.core.model.entities.impl;

import de.xwic.appkit.core.dao.Entity;
import de.xwic.appkit.core.model.entities.IMonitoringElement;

/**
 * Specifies entities and properties where the changes are monitored by the system.
 * 
 * @author Florian Lippisch
 */
public class MonitoringElement extends Entity implements IMonitoringElement {

	private String entityClassName = null;
	private String propertyName = null;
	private boolean monitored = false;
	
	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.model.entities.impl.IMonitoringElement#getEntityClassName()
	 */
	public String getEntityClassName() {
		return entityClassName;
	}
	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.model.entities.impl.IMonitoringElement#setEntityClassName(java.lang.String)
	 */
	public void setEntityClassName(String entityClassName) {
		this.entityClassName = entityClassName;
	}
	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.model.entities.impl.IMonitoringElement#isMonitored()
	 */
	public boolean isMonitored() {
		return monitored;
	}
	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.model.entities.impl.IMonitoringElement#setMonitored(boolean)
	 */
	public void setMonitored(boolean monitored) {
		this.monitored = monitored;
	}
	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.model.entities.impl.IMonitoringElement#getPropertyName()
	 */
	public String getPropertyName() {
		return propertyName;
	}
	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.model.entities.impl.IMonitoringElement#setPropertyName(java.lang.String)
	 */
	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}
	
}
