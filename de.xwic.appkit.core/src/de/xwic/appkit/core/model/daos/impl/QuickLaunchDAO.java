package de.xwic.appkit.core.model.daos.impl;

import de.xwic.appkit.core.dao.AbstractDAO;
import de.xwic.appkit.core.model.daos.IQuickLaunchDAO;
import de.xwic.appkit.core.model.entities.IQuickLaunch;
import de.xwic.appkit.core.model.entities.impl.QuickLaunch;

/**
 * DAO Implementation for the News entity.
 * @author lippisch
 */
public class QuickLaunchDAO extends AbstractDAO<IQuickLaunch, QuickLaunch> implements IQuickLaunchDAO {

	/**
	 *
	 */
	public QuickLaunchDAO() {
		super(IQuickLaunch.class, QuickLaunch.class);
	}

}
