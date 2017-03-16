/**
 * 
 */
package de.xwic.appkit.webbase.actions;

import de.xwic.appkit.core.dao.IEntity;

/**
 * Creates a new entity instance and provides access to a base entity if there is any. Used within the UI 
 * to delegate the creation of the entity to an underlying component such as a list viewer or another editor.
 * 
 * @author lippisch
 */
public interface IEntityCreator {

	/**
	 * Create a new entity instance that is to be edited.
	 * @return
	 */
	public IEntity createEntity();
	

}
