/*
 * Copyright (c) NetApp Inc. - All Rights Reserved
 * 
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 * Proprietary and confidential.
 * 
 * de.xwic.appkit.core.dao.event.DAOWithEvent 
 */
package de.xwic.appkit.core.dao.event;

import de.xwic.appkit.core.dao.DAO;
import de.xwic.appkit.core.dao.IEntity;

/**
 * @author dotto
 *
 */
public interface DAOWithEvent<I extends IEntity> extends DAO<I> {

	/**
     * Add model listener to the model changes.
     * @param listener
     */
    public void addDaoListener(IDaoEntityListener<I> listener);
    
    /**
     * Removes the given listener.
     * 
     * @param listener
     */
    public void removeDaoListener(IDaoEntityListener<I> listener);
}
