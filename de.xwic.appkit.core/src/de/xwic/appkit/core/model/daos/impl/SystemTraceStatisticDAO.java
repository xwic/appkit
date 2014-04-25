/*
 *
 * de.xwic.appkit.core.model.daos.impl.SyncStateDAO
 *
 */
package de.xwic.appkit.core.model.daos.impl;

import de.xwic.appkit.core.dao.AbstractDAO;
import de.xwic.appkit.core.model.daos.ISystemTraceStatisticDAO;
import de.xwic.appkit.core.model.entities.ISystemTraceStatistic;
import de.xwic.appkit.core.model.entities.impl.SystemTraceStatistic;


/**
 * DAO implementation for SystemTraceStatistic.
 *
 * @author Florian Lippisch
 */
public class SystemTraceStatisticDAO extends AbstractDAO<ISystemTraceStatistic, SystemTraceStatistic> implements ISystemTraceStatisticDAO {

	/**
	 *
	 */
	public SystemTraceStatisticDAO() {
		super(ISystemTraceStatistic.class, SystemTraceStatistic.class);
	}

}
