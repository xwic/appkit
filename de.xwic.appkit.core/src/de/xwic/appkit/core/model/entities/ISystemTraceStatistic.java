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

package de.xwic.appkit.core.model.entities;

import java.util.Date;
import java.util.List;

import de.xwic.appkit.core.dao.IEntity;

/**
 * @author lippisch
 */
public interface ISystemTraceStatistic extends IEntity {

	/**
	 * @return the fromDate
	 */
	public abstract Date getFromDate();

	/**
	 * @param fromDate
	 *            the fromDate to set
	 */
	public abstract void setFromDate(Date fromDate);

	/**
	 * @return the toDate
	 */
	public abstract Date getToDate();

	/**
	 * @param toDate
	 *            the toDate to set
	 */
	public abstract void setToDate(Date toDate);

	/**
	 * @return the averageResponseTime
	 */
	public abstract Double getAverageResponseTime();

	/**
	 * @param averageResponseTime
	 *            the averageResponseTime to set
	 */
	public abstract void setAverageResponseTime(Double averageResponseTime);

	/**
	 * @return the totalResponseTime
	 */
	public abstract Long getTotalResponseTime();

	/**
	 * @param totalResponseTime
	 *            the totalResponseTime to set
	 */
	public abstract void setTotalResponseTime(Long totalResponseTime);

	/**
	 * @return the responseCount
	 */
	public abstract Integer getResponseCount();

	/**
	 * @param responseCount
	 *            the responseCount to set
	 */
	public abstract void setResponseCount(Integer responseCount);

	/**
	 * @return the totalDAOops
	 */
	public abstract Integer getTotalDAOops();

	/**
	 * @param totalDAOops
	 *            the totalDAOops to set
	 */
	public abstract void setTotalDAOops(Integer totalDAOops);

	/**
	 * @return the totalDAODuration
	 */
	public abstract Long getTotalDAODuration();

	/**
	 * @param totalDAODuration
	 *            the totalDAODuration to set
	 */
	public abstract void setTotalDAODuration(Long totalDAODuration);

	/**
	 * @return the memoryUsed
	 */
	public abstract Long getMemoryUsed();

	/**
	 * @param memoryUsed
	 *            the memoryUsed to set
	 */
	public abstract void setMemoryUsed(Long memoryUsed);

	/**
	 * @return the activeUsers
	 */
	public abstract Integer getActiveUsers();

	/**
	 * @param activeUsers
	 *            the activeUsers to set
	 */
	public abstract void setActiveUsers(Integer activeUsers);

	/**
	 * @return the sessionCount
	 */
	public abstract Integer getSessionCount();

	/**
	 * @param sessionCount
	 *            the sessionCount to set
	 */
	public abstract void setSessionCount(Integer sessionCount);

	/**
	 * @return the totalUsersOnline
	 */
	public abstract Integer getTotalUsersOnline();

	/**
	 * @param totalUsersOnline
	 *            the totalUsersOnline to set
	 */
	public abstract void setTotalUsersOnline(Integer totalUsersOnline);

	/**
	 * @return the trace statistics in JSON format
	 */
	public String getJsonStats();

	/**
	 * @param jsonStats
	 */
	public void setJsonStats(String jsonStats);

	/**
	 * The host for which this statistic was collected
	 * 
	 * @param host
	 */
	public void setHost(String host);

	/**
	 * The host for which this statistic was collected
	 * 
	 * @return
	 */
	public String getHost();

	/**
	 * Not managed by hibernate
	 * 
	 * @return
	 */	
	public List<TraceStats> getTraceStats();

	/**
	 * Not managed by hibernate
	 * 
	 * @param stats
	 */
	public void setTraceStats(List<TraceStats> stats);

	/**
	 * Holds trace statistics
	 * 
	 * @author Andrei Pat
	 *
	 */
	public class TraceStats {

		private String name;
		private int count;
		private long duration;

		/**
		 * 
		 */
		public TraceStats() {
			//NOP
		}

		/**
		 * @param name
		 * @param count
		 * @param duration
		 */
		public TraceStats(String name, int count, long duration) {
			super();
			this.name = name;
			this.count = count;
			this.duration = duration;
		}

		/**
		 * @return the name
		 */
		public String getName() {
			return name;
		}

		/**
		 * @param name
		 *            the name to set
		 */
		public void setName(String name) {
			this.name = name;
		}

		/**
		 * @return the count
		 */
		public int getCount() {
			return count;
		}

		/**
		 * @param count
		 *            the count to set
		 */
		public void setCount(int count) {
			this.count = count;
		}

		/**
		 * @return the duration
		 */
		public long getDuration() {
			return duration;
		}

		/**
		 * @param duration
		 *            the duration to set
		 */
		public void setDuration(long duration) {
			this.duration = duration;
		}
	}
}
