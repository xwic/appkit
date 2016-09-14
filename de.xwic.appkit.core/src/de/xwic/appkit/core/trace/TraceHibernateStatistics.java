/*
 * Copyright (c) NetApp Inc. - All Rights Reserved
 * 
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 * Proprietary and confidential.
 * 
 * de.xwic.appkit.core.trace.TraceHibernateStatistics 
 */

package de.xwic.appkit.core.trace;

import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.stat.internal.ConcurrentStatisticsImpl;

/**
 * @author Andrei Pat
 *
 */
public class TraceHibernateStatistics extends ConcurrentStatisticsImpl {

	public TraceHibernateStatistics() {
		super();
	}

	public TraceHibernateStatistics(SessionFactoryImplementor sessionFactory) {
		super(sessionFactory);
	}
	
	/* (non-Javadoc)
	 * @see org.hibernate.stat.internal.ConcurrentStatisticsImpl#queryExecuted(java.lang.String, int, long)
	 */
	@Override
	public void queryExecuted(String hql, int rows, long time) {		
		super.queryExecuted(hql, rows, time);
	}	
	
	/* (non-Javadoc)
	 * @see org.hibernate.stat.internal.ConcurrentStatisticsImpl#naturalIdQueryExecuted(java.lang.String, long)
	 */
	@Override
	public void naturalIdQueryExecuted(String regionName, long time) {
		super.naturalIdQueryExecuted(regionName, time);
	}
	
	/* (non-Javadoc)
	 * @see org.hibernate.stat.internal.ConcurrentStatisticsImpl#fetchEntity(java.lang.String)
	 */
	@Override
	public void fetchEntity(String entityName) {
		// TODO Auto-generated method stub
		super.fetchEntity(entityName);
	}
	
	
	
}
