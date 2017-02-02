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
	
	/**
	 * The base entity is a reference to the entity that is in context of the new entity. If, for example, a user
	 * edits a company record and wants to add a contact, the company would be the base entity and the contact 
	 * the new entity.
	 * 
	 * Legacy editors are using the base entity to create a connection between them. The new editor though does
	 * expect the created entity to be already properly connected.
	 * 	
	 * @return the base entity or <code>null</code>
	 */
	public IEntity getBaseEntity();
	
}
