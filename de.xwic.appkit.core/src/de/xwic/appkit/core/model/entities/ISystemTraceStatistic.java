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
	 * @param fromDate the fromDate to set
	 */
	public abstract void setFromDate(Date fromDate);

	/**
	 * @return the toDate
	 */
	public abstract Date getToDate();

	/**
	 * @param toDate the toDate to set
	 */
	public abstract void setToDate(Date toDate);

	/**
	 * @return the averageResponseTime
	 */
	public abstract Double getAverageResponseTime();

	/**
	 * @param averageResponseTime the averageResponseTime to set
	 */
	public abstract void setAverageResponseTime(Double averageResponseTime);

	/**
	 * @return the totalResponseTime
	 */
	public abstract Long getTotalResponseTime();

	/**
	 * @param totalResponseTime the totalResponseTime to set
	 */
	public abstract void setTotalResponseTime(Long totalResponseTime);

	/**
	 * @return the responseCount
	 */
	public abstract Integer getResponseCount();

	/**
	 * @param responseCount the responseCount to set
	 */
	public abstract void setResponseCount(Integer responseCount);

	/**
	 * @return the totalDAOops
	 */
	public abstract Integer getTotalDAOops();

	/**
	 * @param totalDAOops the totalDAOops to set
	 */
	public abstract void setTotalDAOops(Integer totalDAOops);

	/**
	 * @return the totalDAODuration
	 */
	public abstract Long getTotalDAODuration();

	/**
	 * @param totalDAODuration the totalDAODuration to set
	 */
	public abstract void setTotalDAODuration(Long totalDAODuration);

	/**
	 * @return the memoryUsed
	 */
	public abstract Long getMemoryUsed();

	/**
	 * @param memoryUsed the memoryUsed to set
	 */
	public abstract void setMemoryUsed(Long memoryUsed);

	/**
	 * @return the activeUsers
	 */
	public abstract Integer getActiveUsers();

	/**
	 * @param activeUsers the activeUsers to set
	 */
	public abstract void setActiveUsers(Integer activeUsers);

	/**
	 * @return the sessionCount
	 */
	public abstract Integer getSessionCount();

	/**
	 * @param sessionCount the sessionCount to set
	 */
	public abstract void setSessionCount(Integer sessionCount);

	/**
	 * @return the totalUsersOnline
	 */
	public abstract Integer getTotalUsersOnline();

	/**
	 * @param totalUsersOnline the totalUsersOnline to set
	 */
	public abstract void setTotalUsersOnline(Integer totalUsersOnline);

	/**
	 * @return the customCat1Ops
	 */
	public abstract Integer getCustomCat1Ops();

	/**
	 * @param customCat1Ops the customCat1Ops to set
	 */
	public abstract void setCustomCat1Ops(Integer customCat1Ops);

	/**
	 * @return the customCat1Duration
	 */
	public abstract Long getCustomCat1Duration();

	/**
	 * @param customCat1Duration the customCat1Duration to set
	 */
	public abstract void setCustomCat1Duration(Long customCat1Duration);

	/**
	 * @return the customCat2Ops
	 */
	public abstract Integer getCustomCat2Ops();

	/**
	 * @param customCat2Ops the customCat2Ops to set
	 */
	public abstract void setCustomCat2Ops(Integer customCat2Ops);

	/**
	 * @return the customCat2Duration
	 */
	public abstract Long getCustomCat2Duration();

	/**
	 * @param customCat2Duration the customCat2Duration to set
	 */
	public abstract void setCustomCat2Duration(Long customCat2Duration);

	/**
	 * @return the customCat3Ops
	 */
	public abstract Integer getCustomCat3Ops();

	/**
	 * @param customCat3Ops the customCat3Ops to set
	 */
	public abstract void setCustomCat3Ops(Integer customCat3Ops);

	/**
	 * @return the customCat3Duration
	 */
	public abstract Long getCustomCat3Duration();

	/**
	 * @param customCat3Duration the customCat3Duration to set
	 */
	public abstract void setCustomCat3Duration(Long customCat3Duration);

}