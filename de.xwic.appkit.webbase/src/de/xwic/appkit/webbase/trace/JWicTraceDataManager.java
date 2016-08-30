/*******************************************************************************
 * Copyright 2015 xWic group (http://www.xwic.de)
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 		http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 *  
 *******************************************************************************/

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
		super();
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
