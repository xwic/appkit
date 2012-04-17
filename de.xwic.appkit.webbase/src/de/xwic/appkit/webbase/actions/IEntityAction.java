/**
 * 
 */
package de.xwic.appkit.webbase.actions;

import de.jwic.ecolib.actions.IAction;
import de.xwic.appkit.core.dao.DAO;
import de.xwic.appkit.core.dao.IEntity;
import de.xwic.appkit.webbase.toolkit.app.Site;

/**
 * @author Adrian Ionescu
 */
public interface IEntityAction extends IAction {

	/**
	 * @return
	 */
	public String getId();
	
	/**
	 * @param id
	 */
	public void setId(String id);
	
	/**
	 * @param entityDao
	 */
	public void setEntityDao(DAO entityDao);

	/**
	 * @param entityProvider
	 */
	public void setEntityProvider(IEntityProvider entityProvider);
	
	/**
	 * @param site
	 */
	public void setSite(Site site);
	
	/**
	 * @param entity
	 */
	public void updateState(IEntity entity);
}
