/**
 * 
 */
package de.xwic.appkit.webbase.actions;

import de.xwic.appkit.core.dao.DAO;
import de.xwic.appkit.webbase.toolkit.app.Site;
import de.xwic.appkit.webbase.toolkit.util.ImageLibrary;

/**
 * @author Adrian Ionescu
 */
public class EntityActionDelete extends AbstractEntityAction {

	/**
	 * @param site
	 * @param entityDao
	 * @param entityProvider
	 */
	public EntityActionDelete(Site site, DAO entityDao, IEntityProvider entityProvider) {
		setTitle("Delete");
		setIconEnabled(ImageLibrary.ICON_DELETE_ACTIVE);
		setIconDisabled(ImageLibrary.ICON_DELETE_INACTIVE);
		setVisible(true);
		setEnabled(false);
		
		this.site = site;
		this.entityDao = entityDao;
		this.entityProvider = entityProvider;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		entityDao.softDelete(entityProvider.getEntity()); // the exception is caught where this is called
	}
}
