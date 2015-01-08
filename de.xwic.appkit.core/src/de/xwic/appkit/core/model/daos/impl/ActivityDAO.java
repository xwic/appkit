package de.xwic.appkit.core.model.daos.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.SchedulerException;

import de.xwic.appkit.core.dao.AbstractDAO;
import de.xwic.appkit.core.dao.DAOSystem;
import de.xwic.appkit.core.dao.DataAccessException;
import de.xwic.appkit.core.dao.IEntity;
import de.xwic.appkit.core.model.daos.IActivityDAO;
import de.xwic.appkit.core.model.daos.IPicklisteDAO;
import de.xwic.appkit.core.model.entities.IActivity;
import de.xwic.appkit.core.model.entities.impl.Activity;
import de.xwic.appkit.core.scheduler.SchedulerManager;


/**
 * @author dotto
 * @date Aug 19, 2014
 *
 */
public class ActivityDAO extends AbstractDAO<IActivity, Activity> implements IActivityDAO {

	private static final Log log = LogFactory.getLog(ActivityDAO.class);

	public ActivityDAO() {
		super(IActivity.class, Activity.class);

	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.dao.AbstractDAO#createEntity()
	 */
	@Override
	public IActivity createEntity() throws DataAccessException {
		IPicklisteDAO plDao = (IPicklisteDAO) DAOSystem.getDAO(IPicklisteDAO.class);

		IActivity activity = super.createEntity();
		activity.setStatus(plDao.getPickListEntryByKey(IActivity.PL_ACTIVITY_STATUS, IActivity.PE_ACTIVITY_STATUS_PENDING));
		return activity;
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.dao.AbstractDAO#softDelete(de.xwic.appkit.core.dao.IEntity)
	 */
	@Override
	public void softDelete(IEntity entity) throws DataAccessException {
		super.softDelete(entity);
		try {
			SchedulerManager.getScheduler().changeCronJob((IActivity) entity);
		} catch (SchedulerException e) {
			log.error("Error while removing job.", e);
		} catch (ClassNotFoundException e) {
			log.error("Error while removing job.", e);
		}
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.dao.AbstractDAO#delete(de.xwic.appkit.core.dao.IEntity)
	 */
	@Override
	public void delete(IEntity entity) throws DataAccessException {
		super.delete(entity);
		try {
			SchedulerManager.getScheduler().changeCronJob((IActivity) entity);
		} catch (SchedulerException e) {
			log.error("Error while removing job.", e);
		} catch (ClassNotFoundException e) {
			log.error("Error while removing job.", e);
		}
	}
}
