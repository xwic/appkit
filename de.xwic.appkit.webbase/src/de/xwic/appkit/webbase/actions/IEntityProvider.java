/**
 * 
 */
package de.xwic.appkit.webbase.actions;

import de.xwic.appkit.core.dao.IEntity;


/**
 * @author Adrian Ionescu
 */
public interface IEntityProvider {

	/**
	 * @return
	 */
	public String getEntityKey();
	
	/**
	 * @return
	 */
	public IEntity getEntity();
	
	/**
	 * @return
	 */
	public IEntity getBaseEntity();
	
	/**
	 * @return
	 */
	public boolean hasEntity();
}
