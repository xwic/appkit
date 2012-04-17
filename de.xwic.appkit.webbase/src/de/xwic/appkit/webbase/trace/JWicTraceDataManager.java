/*
 * Copyright (c) 2009 Network Appliance, Inc.
 * All rights reserved.
 */

package de.xwic.appkit.webbase.trace;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.jwic.base.JWicRuntime;
import de.jwic.base.SessionContainer;
import de.jwic.base.SessionContext;
import de.jwic.base.SessionManager;
import de.xwic.appkit.core.model.entities.ISystemTraceStatistic;
import de.xwic.appkit.core.trace.ITraceContext;
import de.xwic.appkit.core.trace.impl.TraceDataManager;
import de.xwic.appkit.webbase.toolkit.app.ExtendedApplication;

/**
 * This implementation does also log JWic related log information in the
 * SystemTraceStatistic.
 * @author lippisch
 */
public class JWicTraceDataManager extends TraceDataManager {

	public final static long ACTIVE_TIME = 1000 * 60 * 10; // Users active within the last 10 minutes count as active.
	
	/**
	 * 
	 */
	public JWicTraceDataManager() {
		this.customCat1Name = "entity-table-viewer";
	}
	
	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.trace.impl.TraceDataManager#populateSystemTraceStatistic(de.xwic.appkit.core.model.entities.ISystemTraceStatistic, long, java.util.List)
	 */
	@Override
	protected void populateSystemTraceStatistic(ISystemTraceStatistic sts, long interval, List<ITraceContext> history) {
		super.populateSystemTraceStatistic(sts, interval, history);
		
		JWicRuntime jWicRuntime = JWicRuntime.getJWicRuntime();
		SessionManager sessionManager = jWicRuntime.getSessionManager();
		sts.setSessionCount(sessionManager.getClientIDs().size());
		
		Set<String> userTotals = new HashSet<String>();
		Set<String> userActive = new HashSet<String>();
		
		for (String cId : sessionManager.getClientIDs()) {
			for(SessionContainer cont : sessionManager.getSessions(cId)) {
				long lastAccess = System.currentTimeMillis() - cont.getLastAccess();
				
				SessionContext ctx = cont.getSessionContext();
				if (ctx.getApplication() instanceof ExtendedApplication) {
					ExtendedApplication eApp = (ExtendedApplication)ctx.getApplication();
					String userName = eApp.getStartedByUser();
					if (userName != null) {
						userTotals.add(userName);
						if (lastAccess <= ACTIVE_TIME) {
							userActive.add(userName);
						}
					}
				}
				
			}
		}
		
		sts.setActiveUsers(userActive.size());
		sts.setTotalUsersOnline(userTotals.size());
		
	}
	
}
