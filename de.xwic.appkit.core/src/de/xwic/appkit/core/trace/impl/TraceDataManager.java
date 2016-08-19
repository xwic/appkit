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

package de.xwic.appkit.core.trace.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.xwic.appkit.core.dao.DAO;
import de.xwic.appkit.core.dao.DAOSystem;
import de.xwic.appkit.core.model.daos.ISystemTraceStatisticDAO;
import de.xwic.appkit.core.model.entities.ISystemTraceStatistic;
import de.xwic.appkit.core.trace.ITraceCategory;
import de.xwic.appkit.core.trace.ITraceContext;
import de.xwic.appkit.core.trace.ITraceDataManager;
import de.xwic.appkit.core.trace.ITraceOperation;
import de.xwic.appkit.core.trace.StackTraceSnapShot;

/**
 * Manages the results of a trace. Ops may be stored if a certain threshold is exceeded
 * 
 * @author lippisch
 */
public class TraceDataManager implements ITraceDataManager {

	protected final Log log = LogFactory.getLog(getClass());

	private boolean logTraceAboveThreshold = false;
	private long logThreshold = 3000;
	private File traceLogFile = null;

	private TraceLevel traceLevel = TraceLevel.BASIC;

	private int maxHistory = 2000;
	private boolean keepHistory = false;
	private LinkedList<ITraceContext> traceHistory = new LinkedList<ITraceContext>();

	protected String customCat1Name = null;
	protected String customCat2Name = null;
	protected String customCat3Name = null;

	private String hostName;

	/**
	 * 
	 */
	public TraceDataManager() {
		try {
			hostName = java.net.InetAddress.getLocalHost().getHostName();
		} catch (Exception e) {
			log.error("Unable to get hostname", e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.xwic.appkit.core.trace.impl.ITraceDataManager#handleTraceResult(de.xwic.appkit.core.trace.ITraceContext)
	 */
	public void handleTraceResult(ITraceContext traceContext) {

		if (logTraceAboveThreshold && traceContext.getDuration() > logThreshold) {
			log.info("Trace cycle exceeded threshold: " + traceContext.getDuration());
			try {
				if (traceLogFile != null) {
					synchronized (traceLogFile) {
						FileOutputStream fos = new FileOutputStream(traceLogFile, true);
						try {
							logContext(traceContext, fos);
						} finally {
							fos.close();
						}
					}
				}
				logContext(traceContext, System.out); // log to console
			} catch (IOException e) {
				log.error("Error writing trace log file", e);

			}

		}

		if (keepHistory) {
			// Add an element to the end of the list to keep track of the history.
			synchronized (traceHistory) {
				if (traceHistory.size() >= maxHistory) {
					traceHistory.removeFirst();
				}
				traceHistory.add(traceContext);
			}

		}

	}

	/**
	 * @param traceContext
	 */
	private synchronized void logContext(ITraceContext tx, OutputStream out) {

		if (traceLogFile != null) {

			long start = tx.getStartTime();
			PrintWriter pw = new PrintWriter(out);
			pw.println("---------------------------------------------------------------------");
			pw.println("Start-Time: " + DateFormat.getDateTimeInstance().format(new Date(start)));
			pw.println("Total-Duration: " + tx.getDuration());
			if (tx.getName() != null) {
				pw.println("Name: " + tx.getName());
			}
			if (tx.getInfo() != null) {
				pw.println("Info: " + tx.getInfo());
			}
			pw.println("Attributes:");
			Map<String, String> attrs = tx.getAttributes();
			for (String key : attrs.keySet()) {
				pw.println("   " + key + "=" + attrs.get(key));
			}
			if (traceLevel != TraceLevel.BASIC) {
				pw.println();
				Map<String, ITraceCategory> catMap = tx.getTraceCategories();
				for (String cat : catMap.keySet()) {
					ITraceCategory category = catMap.get(cat);
					pw.println("[ " + cat + " (" + category.getCount() + " op(s) / " + category.getTotalDuration() + "ms)]");
					if (traceLevel == TraceLevel.DETAILED) {
						for (ITraceOperation op : category.getTraceOperations()) {
							pw.println(String.format("# %s [start: %d; end: %d; total: %d ms]", op.getName(), op.getStartTime() - start,
									op.getEndTime() - start, op.getDuration()));
							if (op.getInfo() != null) {
								pw.println(op.getInfo());
							}
						}
					}
				}

				List<StackTraceSnapShot> snapShots = tx.getStackTraceSnapShots();
				if (snapShots.size() > 0) {
					pw.println();
					pw.println("SnapShots taken:");
					StringBuilder sb = new StringBuilder();
					String previous = null;
					for (StackTraceSnapShot ss : snapShots) {
						sb.setLength(0);
						pw.println("StackTrace at " + (ss.getSnapShotTime() - start) + "ms");
						for (StackTraceElement elm : ss.getStackTrace()) {
							sb.append("  " + elm.getClassName() + "." + elm.getMethodName() + "(..):" + elm.getLineNumber()).append("\n");
						}
						if (sb.toString().equals(previous)) {
							pw.println("[ SAME AS PREVIOUS Stack Trace ]");
						} else {
							previous = sb.toString();
							pw.print(previous);
						}
						pw.println("----");
					}
				}

			}
			pw.flush();

		} else {
			log.warn("Can not log trace details as no traceLogFile is defined.");
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.xwic.appkit.core.trace.impl.ITraceDataManager#isLogTraceAboveThreshold()
	 */
	public boolean isLogTraceAboveThreshold() {
		return logTraceAboveThreshold;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.xwic.appkit.core.trace.impl.ITraceDataManager#setLogTraceAboveThreshold(boolean)
	 */
	public void setLogTraceAboveThreshold(boolean logTraceAboveThreshold) {
		this.logTraceAboveThreshold = logTraceAboveThreshold;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.xwic.appkit.core.trace.impl.ITraceDataManager#getLogThreshold()
	 */
	public long getLogThreshold() {
		return logThreshold;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.xwic.appkit.core.trace.impl.ITraceDataManager#setLogThreshold(long)
	 */
	public void setLogThreshold(long logThreshold) {
		this.logThreshold = logThreshold;
	}

	/**
	 * @return the traceLogFile
	 */
	public File getTraceLogFile() {
		return traceLogFile;
	}

	/**
	 * @param traceLogFile
	 *            the traceLogFile to set
	 */
	public void setTraceLogFile(File traceLogFile) {
		this.traceLogFile = traceLogFile;
	}

	/**
	 * @return the traceLevel
	 */
	public TraceLevel getTraceLevel() {
		return traceLevel;
	}

	/**
	 * @param traceLevel
	 *            the traceLevel to set
	 */
	public void setTraceLevel(TraceLevel traceLevel) {
		this.traceLevel = traceLevel;
	}

	/**
	 * @return the keepHistory
	 */
	@Override
	public boolean isKeepHistory() {
		return keepHistory;
	}

	/**
	 * @param keepHistory
	 *            the keepHistory to set
	 */
	@Override
	public void setKeepHistory(boolean keepHistory) {
		this.keepHistory = keepHistory;
	}

	/**
	 * @return the maxHistory
	 */
	@Override
	public int getMaxHistory() {
		return maxHistory;
	}

	/**
	 * @param maxHistory
	 *            the maxHistory to set
	 */
	@Override
	public void setMaxHistory(int maxHistory) {
		this.maxHistory = maxHistory;
	}

	/**
	 * @return the traceHistory
	 */
	@Override
	public List<ITraceContext> getTraceHistory() {
		List<ITraceContext> temp = new ArrayList<ITraceContext>();
		synchronized (traceHistory) {
			temp.addAll(traceHistory);
		}
		return temp;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.xwic.appkit.core.trace.ITraceDataManager#saveSystemTraceStatistic(long)
	 */
	@Override
	public void saveSystemTraceStatistic(long interval) {

		ISystemTraceStatisticDAO stsDAO = (ISystemTraceStatisticDAO) DAOSystem.getDAO(ISystemTraceStatisticDAO.class);
		ISystemTraceStatistic sts = (ISystemTraceStatistic) stsDAO.createEntity();

		List<ITraceContext> history = getTraceHistory();
		populateSystemTraceStatistic(sts, interval, history);

		stsDAO.update(sts);

	}

	/**
	 * Store statistic data.
	 * 
	 * @param sts
	 * @param history
	 */
	protected void populateSystemTraceStatistic(ISystemTraceStatistic sts, long interval, List<ITraceContext> history) {

		Runtime rt = Runtime.getRuntime();
		long used = rt.totalMemory() - rt.freeMemory();

		sts.setMemoryUsed(used);
		sts.setHost(hostName);

		if (history.size() != 0) {

			long max = System.currentTimeMillis() - (interval + 10); // make an "overlap of 10ms"
			long hisStart = history.get(0).getStartTime();
			long from = hisStart < max ? max : hisStart;
			long to = history.get(history.size() - 1).getStartTime();

			sts.setFromDate(new Date(from));
			if (to >= from) {
				sts.setToDate(new Date(to));
			} else {
				sts.setToDate(null);
			}

			int count = 0;
			long total = 0;

			int daoOps = 0;
			long daoDuration = 0;

			int[] customOps = { 0, 0, 0 };
			long[] customDur = { 0, 0, 0 };

			for (ITraceContext tx : history) {
				if (tx.getStartTime() >= from) { // skip older entries
					count++;
					total += tx.getDuration();

					ITraceCategory daoCategory = tx.getTraceCategory(DAO.TRACE_CAT);
					if (daoCategory != null) {
						daoOps += daoCategory.getCount();
						daoDuration += daoCategory.getTotalDuration();
					}

					if (customCat1Name != null) {
						countCat(customOps, customDur, 0, customCat1Name, tx);
					}
					if (customCat2Name != null) {
						countCat(customOps, customDur, 1, customCat2Name, tx);
					}
					if (customCat3Name != null) {
						countCat(customOps, customDur, 2, customCat3Name, tx);
					}

				}
			}

			sts.setResponseCount(count);
			sts.setTotalResponseTime(total);
			sts.setAverageResponseTime(count != 0 ? total / count : 0d);

			sts.setTotalDAODuration(daoDuration);
			sts.setTotalDAOops(daoOps);

			sts.setCustomCat1Duration(customDur[0]);
			sts.setCustomCat1Ops(customOps[0]);
			sts.setCustomCat2Duration(customDur[1]);
			sts.setCustomCat2Ops(customOps[1]);
			sts.setCustomCat3Duration(customDur[2]);
			sts.setCustomCat3Ops(customOps[2]);

		}

	}

	/**
	 * @param customOps
	 * @param customDur
	 * @param i
	 * @param tx
	 * @param customCat1Name2
	 */
	private void countCat(int[] customOps, long[] customDur, int i, String catName, ITraceContext tx) {
		ITraceCategory tCat = tx.getTraceCategory(catName);
		if (tCat != null) {
			customOps[i] += tCat.getCount();
			customDur[i] += tCat.getTotalDuration();
		}
	}

	/**
	 * @return the customCat1Name
	 */
	public String getCustomCat1Name() {
		return customCat1Name;
	}

	/**
	 * @param customCat1Name
	 *            the customCat1Name to set
	 */
	public void setCustomCat1Name(String customCat1Name) {
		this.customCat1Name = customCat1Name;
	}

	/**
	 * @return the customCat2Name
	 */
	public String getCustomCat2Name() {
		return customCat2Name;
	}

	/**
	 * @param customCat2Name
	 *            the customCat2Name to set
	 */
	public void setCustomCat2Name(String customCat2Name) {
		this.customCat2Name = customCat2Name;
	}

	/**
	 * @return the customCat3Name
	 */
	public String getCustomCat3Name() {
		return customCat3Name;
	}

	/**
	 * @param customCat3Name
	 *            the customCat3Name to set
	 */
	public void setCustomCat3Name(String customCat3Name) {
		this.customCat3Name = customCat3Name;
	}

}
