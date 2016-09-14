/*
 * Copyright (c) NetApp Inc. - All Rights Reserved
 * 
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 * Proprietary and confidential.
 * 
 * de.xwic.appkit.core.dao.impl.hbn.TraceHibernateStatisticsFactory 
 */

package de.xwic.appkit.core.dao.impl.hbn;

import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.stat.internal.ConcurrentStatisticsImpl;
import org.hibernate.stat.spi.StatisticsFactory;
import org.hibernate.stat.spi.StatisticsImplementor;

import de.xwic.appkit.core.trace.TraceHibernateStatistics;

/**
 * @author Andrei Pat
 *
 */
public class TraceHibernateStatisticsFactory implements StatisticsFactory {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.hibernate.stat.spi.StatisticsFactory#buildStatistics(org.hibernate.engine.spi.SessionFactoryImplementor)
	 */
	@Override
	public StatisticsImplementor buildStatistics(SessionFactoryImplementor sessionFactory) {
		return new TraceHibernateStatistics(sessionFactory);
	}

}
