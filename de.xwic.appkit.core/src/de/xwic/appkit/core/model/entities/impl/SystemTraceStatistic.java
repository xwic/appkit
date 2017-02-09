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

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import de.xwic.appkit.core.dao.Entity;
import de.xwic.appkit.core.model.entities.ISystemTraceStatistic;

/**
 * Used to store system trace/performance statistics with an agent every 5 minutes.
 * 
 * @author lippisch
 */
public class SystemTraceStatistic extends Entity implements ISystemTraceStatistic {

	private String instanceId;
	private String host;
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

	//trace stats held in JSON format:
	//{
	//  {name:whateverNameYouWant - the name of the category you are tracing
	//   ops:5  - the number of operations
	//   duration: 1000 - total duration of the operations
	//  },
	//  {name:
	//   ops:
	//   duration:
	//  }
	//}
	//
	private String jsonStats;

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.xwic.appkit.core.model.entities.impl.ISystemTraceStatistic#getFromDate()
	 */
	public Date getFromDate() {
		return fromDate;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.xwic.appkit.core.model.entities.impl.ISystemTraceStatistic#setFromDate(java.util.Date)
	 */
	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.xwic.appkit.core.model.entities.impl.ISystemTraceStatistic#getToDate()
	 */
	public Date getToDate() {
		return toDate;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.xwic.appkit.core.model.entities.impl.ISystemTraceStatistic#setToDate(java.util.Date)
	 */
	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.xwic.appkit.core.model.entities.impl.ISystemTraceStatistic#getAverageResponseTime()
	 */
	public Double getAverageResponseTime() {
		return averageResponseTime;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.xwic.appkit.core.model.entities.impl.ISystemTraceStatistic#setAverageResponseTime(java.lang.Double)
	 */
	public void setAverageResponseTime(Double averageResponseTime) {
		this.averageResponseTime = averageResponseTime;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.xwic.appkit.core.model.entities.impl.ISystemTraceStatistic#getTotalResponseTime()
	 */
	public Long getTotalResponseTime() {
		return totalResponseTime;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.xwic.appkit.core.model.entities.impl.ISystemTraceStatistic#setTotalResponseTime(java.lang.Long)
	 */
	public void setTotalResponseTime(Long totalResponseTime) {
		this.totalResponseTime = totalResponseTime;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.xwic.appkit.core.model.entities.impl.ISystemTraceStatistic#getResponseCount()
	 */
	public Integer getResponseCount() {
		return responseCount;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.xwic.appkit.core.model.entities.impl.ISystemTraceStatistic#setResponseCount(java.lang.Integer)
	 */
	public void setResponseCount(Integer responseCount) {
		this.responseCount = responseCount;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.xwic.appkit.core.model.entities.impl.ISystemTraceStatistic#getTotalDAOops()
	 */
	public Integer getTotalDAOops() {
		return totalDAOops;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.xwic.appkit.core.model.entities.impl.ISystemTraceStatistic#setTotalDAOops(java.lang.Integer)
	 */
	public void setTotalDAOops(Integer totalDAOops) {
		this.totalDAOops = totalDAOops;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.xwic.appkit.core.model.entities.impl.ISystemTraceStatistic#getTotalDAODuration()
	 */
	public Long getTotalDAODuration() {
		return totalDAODuration;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.xwic.appkit.core.model.entities.impl.ISystemTraceStatistic#setTotalDAODuration(java.lang.Long)
	 */
	public void setTotalDAODuration(Long totalDAODuration) {
		this.totalDAODuration = totalDAODuration;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.xwic.appkit.core.model.entities.impl.ISystemTraceStatistic#getMemoryUsed()
	 */
	public Long getMemoryUsed() {
		return memoryUsed;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.xwic.appkit.core.model.entities.impl.ISystemTraceStatistic#setMemoryUsed(java.lang.Long)
	 */
	public void setMemoryUsed(Long memoryUsed) {
		this.memoryUsed = memoryUsed;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.xwic.appkit.core.model.entities.impl.ISystemTraceStatistic#getActiveUsers()
	 */
	public Integer getActiveUsers() {
		return activeUsers;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.xwic.appkit.core.model.entities.impl.ISystemTraceStatistic#setActiveUsers(java.lang.Integer)
	 */
	public void setActiveUsers(Integer activeUsers) {
		this.activeUsers = activeUsers;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.xwic.appkit.core.model.entities.impl.ISystemTraceStatistic#getSessionCount()
	 */
	public Integer getSessionCount() {
		return sessionCount;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.xwic.appkit.core.model.entities.impl.ISystemTraceStatistic#setSessionCount(java.lang.Integer)
	 */
	public void setSessionCount(Integer sessionCount) {
		this.sessionCount = sessionCount;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.xwic.appkit.core.model.entities.impl.ISystemTraceStatistic#getTotalUsersOnline()
	 */
	public Integer getTotalUsersOnline() {
		return totalUsersOnline;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.xwic.appkit.core.model.entities.impl.ISystemTraceStatistic#setTotalUsersOnline(java.lang.Integer)
	 */
	public void setTotalUsersOnline(Integer totalUsersOnline) {
		this.totalUsersOnline = totalUsersOnline;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.xwic.appkit.core.model.entities.ISystemTraceStatistic#getHost()
	 */
	@Override
	public String getHost() {
		return host;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.xwic.appkit.core.model.entities.ISystemTraceStatistic#setHost(java.lang.String)
	 */
	@Override
	public void setHost(String host) {
		this.host = host;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.xwic.appkit.core.model.entities.ISystemTraceStatistic#getInstanceId()
	 */
	@Override
	public String getInstanceId() {
		return instanceId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.xwic.appkit.core.model.entities.ISystemTraceStatistic#setInstanceId(java.lang.String)
	 */
	@Override
	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.xwic.appkit.core.model.entities.ISystemTraceStatistic#getJsonStats()
	 */
	@Override
	public String getJsonStats() {
		return jsonStats;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.xwic.appkit.core.model.entities.ISystemTraceStatistic#setJsonStats(java.lang.String)
	 */
	@Override
	public void setJsonStats(String jsonStats) {
		this.jsonStats = jsonStats;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.xwic.appkit.core.model.entities.ISystemTraceStatistic#setTraceStats(java.util.List)
	 */
	@Override
	public void setTraceStats(List<TraceStats> stats) {
		Gson gson = new Gson();
		jsonStats = gson.toJson(stats);
	}

}
