/**
 * 
 */
package de.xwic.appkit.webbase.actions;

import de.xwic.appkit.core.dao.DAO;
import de.xwic.appkit.core.dao.IEntity;
import de.xwic.appkit.webbase.actions.editors.EditorHelper;
import de.xwic.appkit.webbase.toolkit.app.Site;
import de.xwic.appkit.webbase.toolkit.util.ImageLibrary;

/**
 * @author Adrian Ionescu
 */
public class EntityActionNew extends AbstractEntityAction {

	/**
	 * @param site
	 * @param entityDao
	 * @param entityProvider 
	 */
	public EntityActionNew(Site site, DAO entityDao, IEntityProvider entityProvider) {
		setTitle("New");
		setIconEnabled(ImageLibrary.ICON_NEW_ACTIVE);
		setIconDisabled(ImageLibrary.ICON_NEW_INACTIVE);
		setVisible(true);
		setEnabled(true);
		
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
		try {
			EditorHelper.getInstance().openEditor(site, entityDao.createEntity(), entityProvider.getBaseEntity());
		} catch (Exception e) {
			log.error("Error while executing EntityActionNew", e);
			throw new RuntimeException(e);
		}
	}
	
	/* (non-Javadoc)
	 * @see de.xwic.appkit.webbase.actions.AbstractEntityAction#updateState(de.xwic.appkit.core.dao.IEntity)
	 */
	@Override
	public void updateState(IEntity entity) {
		// nothing, this one is always enabled
	}
}
