/*
 * (c) Copyright 2005, 2006 by pol GmbH 
 * 
 * de.xwic.appkit.webbase.editor.GenericEditorInput
 * Created on May 14, 2007 by Aron Cotrau
 *
 */
package de.xwic.appkit.webbase.editors;

import de.xwic.appkit.core.config.editor.EditorConfiguration;
import de.xwic.appkit.core.dao.DAO;
import de.xwic.appkit.core.dao.DAOSystem;
import de.xwic.appkit.core.dao.IEntity;

/**
 * Generic editor input that holds the Entity and the EditorConfiguration.
 * 
 * @author Florian Lippisch
 */
public class GenericEditorInput {
	
	private EditorConfiguration config;
	private IEntity entity;
	
	/**
	 * Constructor.
	 * @param entity
	 * @param config
	 */
	public GenericEditorInput(IEntity entity, EditorConfiguration config) {
		this.entity = entity;
		this.config = config;
	}
	
	/**
	 * @return a string representation of the entity
	 */
	public String getName() {
		return createName();
	}

	/**
	 * @return the DAO string representation of the entity
	 */
	private String createName() {
		DAO entityDao = DAOSystem.findDAOforEntity(config.getEntityType().getClassname());
		if (null == entityDao) {
			return entity.toString();
		}
		
		return entityDao.buildTitle(entity);
	}

	/**
	 * @return the config
	 */
	public EditorConfiguration getConfig() {
		return config;
	}

	/**
	 * @param config the config to set
	 */
	public void setConfig(EditorConfiguration config) {
		this.config = config;
	}

	/**
	 * @return the entity
	 */
	public IEntity getEntity() {
		return entity;
	}

	/**
	 * @param entity the entity to set
	 */
	public void setEntity(IEntity entity) {
		this.entity = entity;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((entity == null) ? 0 : entity.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final GenericEditorInput other = (GenericEditorInput) obj;
		if (entity == null) {
			if (other.entity != null)
				return false;
		} else if (entity.getId() != other.entity.getId()) {
			return false;
		} else if (! entity.type().getName().equals(other.entity.type().getName())) {
			return false;
		} 
		
		return true;
	}
}
