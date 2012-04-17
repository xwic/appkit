/**
 * 
 */
package de.xwic.appkit.webbase.actions;

import de.xwic.appkit.core.dao.DAO;
import de.xwic.appkit.webbase.actions.editors.EditorHelper;
import de.xwic.appkit.webbase.toolkit.app.Site;
import de.xwic.appkit.webbase.toolkit.util.ImageLibrary;

/**
 * @author Adrian Ionescu
 */
public class EntityActionEdit extends AbstractEntityAction {

	/**
	 * @param site
	 * @param entityDao
	 * @param entityProvider
	 */
	public EntityActionEdit(Site site, DAO entityDao, IEntityProvider entityProvider) {
		setTitle("Edit");
		setIconEnabled(ImageLibrary.ICON_EDIT_ACTIVE);
		setIconDisabled(ImageLibrary.ICON_EDIT_INACTIVE);
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
		try {
			EditorHelper.getInstance().openEditor(site, entityProvider.getEntity(), entityProvider.getBaseEntity());
		} catch (Exception e) {
			log.error("Error while executing EntityActionEdit", e);
			throw new RuntimeException(e);
		}
	}
}