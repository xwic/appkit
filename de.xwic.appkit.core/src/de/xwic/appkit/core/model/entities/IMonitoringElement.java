/*
 * (c) Copyright 2005, 2006 by pol GmbH 
 * 
 * de.xwic.appkit.core.model.entities.IMonitoringElement
 * Created on 04.01.2008 by Florian Lippisch
 *
 */
package de.xwic.appkit.core.model.entities;

import de.xwic.appkit.core.dao.IEntity;

/**
 * Specifies entities and properties where the changes are monitored by the system.
 * 
 * @author Florian Lippisch
 */
public interface IMonitoringElement extends IEntity {

	/**
	 * @return the entityClassName
	 */
	public abstract String getEntityClassName();

	/**
	 * @param entityClassName the entityClassName to set
	 */
	public abstract void setEntityClassName(String entityClassName);

	/**
	 * @return the monitored
	 */
	public abstract boolean isMonitored();

	/**
	 * @param monitored the monitored to set
	 */
	public abstract void setMonitored(boolean monitored);

	/**
	 * @return the propertyName
	 */
	public abstract String getPropertyName();

	/**
	 * @param propertyName the propertyName to set
	 */
	public abstract void setPropertyName(String propertyName);

}