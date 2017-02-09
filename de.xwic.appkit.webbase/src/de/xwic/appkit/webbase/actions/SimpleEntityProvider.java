/*
 * Copyright (c) NetApp Inc. - All Rights Reserved
 * 
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 * Proprietary and confidential.
 * 
 * com.netapp.pulse.fsm.ui.fem.SimpleEntityProvider 
 */
package de.xwic.appkit.webbase.actions;

import de.xwic.appkit.core.dao.IEntity;


/**
 * @author Adrian Ionescu
 */
public class SimpleEntityProvider implements IEntityProvider {

	private final IEntity entity;
	private final IEntity baseEntity;

	/**
	 * @param entity
	 */
	public SimpleEntityProvider(IEntity entity) {
		this(entity, null);
	}
	
	/**
	 * @param entity
	 * @param baseEntity
	 */
	public SimpleEntityProvider(IEntity entity, IEntity baseEntity) {
		this.entity = entity;
		this.baseEntity = baseEntity;
	}
	
	/* (non-Javadoc)
	 * @see de.xwic.appkit.webbase.actions.IEntityProvider#getEntityKey()
	 */
	@Override
	public String getEntityKey() {
		return String.valueOf(entity.getId());
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.webbase.actions.IEntityProvider#getEntity()
	 */
	@Override
	public IEntity getEntity() {
		return this.entity;
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.webbase.actions.IEntityProvider#getBaseEntity()
	 */
	@Override
	public IEntity getBaseEntity() {
		return this.baseEntity;
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.webbase.actions.IEntityProvider#hasEntity()
	 */
	@Override
	public boolean hasEntity() {
		return true;
	}

}
