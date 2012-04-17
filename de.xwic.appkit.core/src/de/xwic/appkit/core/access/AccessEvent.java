/*
 * (c) Copyright 2005, 2006 by pol GmbH 
 * 
 * de.xwic.appkit.core.access.AccessEvent
 * Created on 04.01.2008 by Florian Lippisch
 *
 */
package de.xwic.appkit.core.access;

import de.xwic.appkit.core.dao.IEntity;

/**
 * @author Florian Lippisch
 */
public class AccessEvent {

	private Object source = null;
	private IEntity entity = null;
	
	/**
	 * @param source
	 * @param entity
	 */
	public AccessEvent(Object source, IEntity entity) {
		super();
		this.source = source;
		this.entity = entity;
	}

	/**
	 * Constructor.
	 * @param source
	 */
	public AccessEvent(Object source) {
		this.source = source;
	}

	/**
	 * @return the source
	 */
	public Object getSource() {
		return source;
	}

	/**
	 * @return the entity
	 */
	public IEntity getEntity() {
		return entity;
	}
	
}
