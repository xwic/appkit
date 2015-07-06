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

package de.xwic.appkit.core.model.entities.impl;

import java.util.Date;

import de.xwic.appkit.core.dao.Entity;
import de.xwic.appkit.core.model.entities.ISystemTraceStatistic;

/**
 * Used to store system trace/performance statistics with an agent every 5 minutes.
 * 
 * @author lippisch
 */
public class SystemTraceStatistic extends Entity implements ISystemTraceStatistic {

	private Date fromDate;
	private Date toDate;
	
	private Double averageResponseTime;
	private Long totalResponseTime;
	private Integer responseCount;
	
	private Integer totalDAOops;
	private Long totalDAODuration;
	
	private Long memoryUsed;
	
	private Integer activeUsers;
	private Integer sessionCount;
	private Integer totalUsersOnline;
	
	private Integer customCat1Ops;
	private Long customCat1Duration;

	private Integer customCat2Ops;
	private Long customCat2Duration;

	private Integer customCat3Ops;
	private Long customCat3Duration;
	
	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.model.entities.impl.ISystemTraceStatistic#getFromDate()
	 */
	public Date getFromDate() {
		return fromDate;
	}
	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.model.entities.impl.ISystemTraceStatistic#setFromDate(java.util.Date)
	 */
	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}
	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.model.entities.impl.ISystemTraceStatistic#getToDate()
	 */
	public Date getToDate() {
		return toDate;
	}
	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.model.entities.impl.ISystemTraceStatistic#setToDate(java.util.Date)
	 */
	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}
	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.model.entities.impl.ISystemTraceStatistic#getAverageResponseTime()
	 */
	public Double getAverageResponseTime() {
		return averageResponseTime;
	}
	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.model.entities.impl.ISystemTraceStatistic#setAverageResponseTime(java.lang.Double)
	 */
	public void setAverageResponseTime(Double averageResponseTime) {
		this.averageResponseTime = averageResponseTime;
	}
	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.model.entities.impl.ISystemTraceStatistic#getTotalResponseTime()
	 */
	public Long getTotalResponseTime() {
		return totalResponseTime;
	}
	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.model.entities.impl.ISystemTraceStatistic#setTotalResponseTime(java.lang.Long)
	 */
	public void setTotalResponseTime(Long totalResponseTime) {
		this.totalResponseTime = totalResponseTime;
	}
	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.model.entities.impl.ISystemTraceStatistic#getResponseCount()
	 */
	public Integer getResponseCount() {
		return responseCount;
	}
	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.model.entities.impl.ISystemTraceStatistic#setResponseCount(java.lang.Integer)
	 */
	public void setResponseCount(Integer responseCount) {
		this.responseCount = responseCount;
	}
	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.model.entities.impl.ISystemTraceStatistic#getTotalDAOops()
	 */
	public Integer getTotalDAOops() {
		return totalDAOops;
	}
	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.model.entities.impl.ISystemTraceStatistic#setTotalDAOops(java.lang.Integer)
	 */
	public void setTotalDAOops(Integer totalDAOops) {
		this.totalDAOops = totalDAOops;
	}
	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.model.entities.impl.ISystemTraceStatistic#getTotalDAODuration()
	 */
	public Long getTotalDAODuration() {
		return totalDAODuration;
	}
	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.model.entities.impl.ISystemTraceStatistic#setTotalDAODuration(java.lang.Long)
	 */
	public void setTotalDAODuration(Long totalDAODuration) {
		this.totalDAODuration = totalDAODuration;
	}
	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.model.entities.impl.ISystemTraceStatistic#getMemoryUsed()
	 */
	public Long getMemoryUsed() {
		return memoryUsed;
	}
	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.model.entities.impl.ISystemTraceStatistic#setMemoryUsed(java.lang.Long)
	 */
	public void setMemoryUsed(Long memoryUsed) {
		this.memoryUsed = memoryUsed;
	}
	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.model.entities.impl.ISystemTraceStatistic#getActiveUsers()
	 */
	public Integer getActiveUsers() {
		return activeUsers;
	}
	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.model.entities.impl.ISystemTraceStatistic#setActiveUsers(java.lang.Integer)
	 */
	public void setActiveUsers(Integer activeUsers) {
		this.activeUsers = activeUsers;
	}
	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.model.entities.impl.ISystemTraceStatistic#getSessionCount()
	 */
	public Integer getSessionCount() {
		return sessionCount;
	}
	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.model.entities.impl.ISystemTraceStatistic#setSessionCount(java.lang.Integer)
	 */
	public void setSessionCount(Integer sessionCount) {
		this.sessionCount = sessionCount;
	}
	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.model.entities.impl.ISystemTraceStatistic#getTotalUsersOnline()
	 */
	public Integer getTotalUsersOnline() {
		return totalUsersOnline;
	}
	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.model.entities.impl.ISystemTraceStatistic#setTotalUsersOnline(java.lang.Integer)
	 */
	public void setTotalUsersOnline(Integer totalUsersOnline) {
		this.totalUsersOnline = totalUsersOnline;
	}
	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.model.entities.impl.ISystemTraceStatistic#getCustomCat1Ops()
	 */
	public Integer getCustomCat1Ops() {
		return customCat1Ops;
	}
	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.model.entities.impl.ISystemTraceStatistic#setCustomCat1Ops(java.lang.Integer)
	 */
	public void setCustomCat1Ops(Integer customCat1Ops) {
		this.customCat1Ops = customCat1Ops;
	}
	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.model.entities.impl.ISystemTraceStatistic#getCustomCat1Duration()
	 */
	public Long getCustomCat1Duration() {
		return customCat1Duration;
	}
	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.model.entities.impl.ISystemTraceStatistic#setCustomCat1Duration(java.lang.Long)
	 */
	public void setCustomCat1Duration(Long customCat1Duration) {
		this.customCat1Duration = customCat1Duration;
	}
	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.model.entities.impl.ISystemTraceStatistic#getCustomCat2Ops()
	 */
	public Integer getCustomCat2Ops() {
		return customCat2Ops;
	}
	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.model.entities.impl.ISystemTraceStatistic#setCustomCat2Ops(java.lang.Integer)
	 */
	public void setCustomCat2Ops(Integer customCat2Ops) {
		this.customCat2Ops = customCat2Ops;
	}
	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.model.entities.impl.ISystemTraceStatistic#getCustomCat2Duration()
	 */
	public Long getCustomCat2Duration() {
		return customCat2Duration;
	}
	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.model.entities.impl.ISystemTraceStatistic#setCustomCat2Duration(java.lang.Long)
	 */
	public void setCustomCat2Duration(Long customCat2Duration) {
		this.customCat2Duration = customCat2Duration;
	}
	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.model.entities.impl.ISystemTraceStatistic#getCustomCat3Ops()
	 */
	public Integer getCustomCat3Ops() {
		return customCat3Ops;
	}
	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.model.entities.impl.ISystemTraceStatistic#setCustomCat3Ops(java.lang.Integer)
	 */
	public void setCustomCat3Ops(Integer customCat3Ops) {
		this.customCat3Ops = customCat3Ops;
	}
	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.model.entities.impl.ISystemTraceStatistic#getCustomCat3Duration()
	 */
	public Long getCustomCat3Duration() {
		return customCat3Duration;
	}
	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.model.entities.impl.ISystemTraceStatistic#setCustomCat3Duration(java.lang.Long)
	 */
	public void setCustomCat3Duration(Long customCat3Duration) {
		this.customCat3Duration = customCat3Duration;
	}
	
	
	
}
