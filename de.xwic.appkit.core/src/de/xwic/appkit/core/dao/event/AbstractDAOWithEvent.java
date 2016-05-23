/*
 * Copyright (c) NetApp Inc. - All Rights Reserved
 * 
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 * Proprietary and confidential.
 * 
 * de.xwic.appkit.core.dao.AbstractEventListenerDAO 
 */
package de.xwic.appkit.core.dao.event;

import java.util.ArrayList;
import java.util.List;

import de.xwic.appkit.core.dao.AbstractDAO;
import de.xwic.appkit.core.dao.DataAccessException;
import de.xwic.appkit.core.dao.Entity;
import de.xwic.appkit.core.dao.IEntity;

/**
 * @author dotto
 *
 */
public class AbstractDAOWithEvent<I extends IEntity, E extends Entity> extends AbstractDAO<I, E>{

	protected List<IDaoEntityListener> listeners = new ArrayList<IDaoEntityListener>();
	
	/**
	 * @param iClass
	 * @param eClass
	 */
	public AbstractDAOWithEvent(Class<I> iClass, Class<E> eClass) {
		super(iClass, eClass);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see de.xwic.appkit.core.dao.AbstractDAO#update(de.xwic.appkit.core.dao.IEntity)
	 */
	@Override
	public void update(IEntity entity) throws DataAccessException {
		super.update(entity);
		fireEntityChangeEvent(new DaoEntityEvent(DaoEntityEvent.UPDATE, entity));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.xwic.appkit.core.dao.AbstractDAO#softDelete(de.xwic.appkit.core.dao.IEntity)
	 */
	@Override
	public void softDelete(IEntity entity) throws DataAccessException {
		super.softDelete(entity);
		fireEntityChangeEvent(new DaoEntityEvent(DaoEntityEvent.DELETE, entity));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.xwic.appkit.core.dao.AbstractDAO#delete(de.xwic.appkit.core.dao.IEntity)
	 */
	@Override
	public void delete(IEntity entity) throws DataAccessException {
		super.delete(entity);
		fireEntityChangeEvent(new DaoEntityEvent(DaoEntityEvent.DELETE, entity));
	}

	/**
     * Add model listener to the model changes.
     * @param listener
     */
    public void addDaoListener(IDaoEntityListener listener) {
    	listeners.add(listener);
    }
    
    /**
     * Removes the given listener.
     * 
     * @param listener
     */
    public void removeDaoListener(IDaoEntityListener listener) {
    	listeners.remove(listener);
    }
    
    /**
     * Fire event.
     * 
     * @param event
     */
    public void fireEntityChangeEvent(DaoEntityEvent event) {
    	Object[] lst = listeners.toArray();
    	for (int i = 0; i < lst.length; i++) {
    		IDaoEntityListener type = (IDaoEntityListener)lst[i];
			type.daoEntityChanged(event);
		}
    }
}
