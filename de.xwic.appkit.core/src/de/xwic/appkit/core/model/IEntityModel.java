/*
 * (c) Copyright 2005, 2006 by pol GmbH 
 * 
 * de.xwic.appkit.core.model.IEntityModel
 * Created on 20.11.2006 by Florian Lippisch
 *
 */
package de.xwic.appkit.core.model;

import java.beans.PropertyChangeListener;

import de.xwic.appkit.core.dao.IEntity;

/**
 * Adds propertyChange events to entities.
 * @author Florian Lippisch
 */
public interface IEntityModel {
	
	/**
	 * Add a listener to listen to the changes on the underlying entity.
	 * @param pListener
	 */
	public void addPropertyChangeListener(PropertyChangeListener pListener);
	
	/**
	 * Copies the changes on the temporary entity to the original instance(s). 
	 * @throws EntityModelException 
	 * @throws EntityModelException 
	 *
	 */
	public void commit() throws EntityModelException;
	
	/**
	 * Returns the original entity instance. To get the editable (copied) instance
	 * of the entity simply cast the model against the entity.</p>
	 * Sample:<br>
	 * IPerson copy = (IPerson)model; 
	 * @return
	 */
	public IEntity getOriginalEntity();
	
	/**
	 * Returns true when the model has been modified. This does not include child models.
	 * @return
	 */
	public boolean isModified();
	
	/**
	 * Remove the PropertyChangeListener.
	 * @param pListener
	 */
	public void removePropertyChangeListener(PropertyChangeListener pListener);

}
