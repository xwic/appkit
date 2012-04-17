/**
 * 
 */
package de.xwic.appkit.webbase.toolkit.model;

import de.xwic.appkit.core.dao.IEntity;

/**
 * @author Ronny Pfretzschner
 *
 */
public abstract class AbstractModel {

	
	public abstract IEntity getEntity(boolean reload);
	
}
