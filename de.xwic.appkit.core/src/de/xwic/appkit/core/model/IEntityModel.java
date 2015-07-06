/*******************************************************************************
 * Copyright 2015 xWic group (http://www.xwic.de)
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 		http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 *  
 *******************************************************************************/
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
