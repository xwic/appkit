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
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.xwic.appkit.core.dao.DAO;
import de.xwic.appkit.core.dao.DAOSystem;
import de.xwic.appkit.core.model.daos.ISystemTraceStatisticDAO;
import de.xwic.appkit.core.model.entities.ISystemTraceStatistic;
import de.xwic.appkit.core.model.entities.ISystemTraceStatistic.TraceStats;
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

	private String hostName;
	private final List<String> systemTraceLogCategories = new ArrayList<String>();
	private int[] traceIntervalBuckets = { 0 };

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

		ISystemTraceStatisticDAO stsDAO = DAOSystem.getDAO(ISystemTraceStatisticDAO.class);
		ISystemTraceStatistic sts = (ISystemTraceStatistic) stsDAO.createEntity();

		List<ITraceContext> history = getTraceHistory();
		populateSystemTraceStatistic(sts, interval, history);

		stsDAO.update(sts);

	}

	/**
	 * Add a trace category name to be saved in the system trace log
	 * 
	 * @param categoryName
	 */
	@Override
	public void addSystemTraceLogCategory(String categoryName) {
		systemTraceLogCategories.add(categoryName);
	}

	/**
	 * Set a list of intervals in milliseconds for grouping traces.
	 * 
	 * Example: setTraceIntervals(1000,5000,10000) will group the traces in 4 intervals: 0 to 1000ms, 1000 to 5000ms, 5000 to 10000ms, and
	 * over 10000ms
	 * 
	 * @param values
	 */
	@Override
	public void setTraceIntervals(int... values) {
		if (values.length == 0) {
			return;
		}
		//add a 0 to the values
		traceIntervalBuckets = Arrays.copyOf(values, values.length + 1);
		Arrays.sort(traceIntervalBuckets);

	}

	/**
	 * Store statistic data.
	 * 
	 * @param sts
	 * @param history
	 */
	protected void populateSystemTraceStatistic(ISystemTraceStatistic sts, long interval, List<ITraceContext> history) {

		Runtime rt = Runtime.getRuntime();
		long used = (rt.totalMemory() - rt.freeMemory()) >> 20; //show memory in MB

		sts.setMemoryUsed(used);
		sts.setHost(hostName);

		if (!history.isEmpty()) {

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

			List<TraceStats> traceStats = new ArrayList<ISystemTraceStatistic.TraceStats>();

			for (String catName : systemTraceLogCategories) {
				TraceStats ts = new TraceStats();
				ts.setName(catName);
				traceStats.add(ts);
			}

			LinkedHashMap<Integer, TraceStats> traceIntervals = new LinkedHashMap<Integer, ISystemTraceStatistic.TraceStats>(
					traceIntervalBuckets.length);
			for (int x : traceIntervalBuckets) {
				TraceStats ts = new TraceStats();
				ts.setName("Duration-" + x);
				traceIntervals.put(x, ts);
			}

			for (ITraceContext tx : history) {
				if (tx.getStartTime() >= from) { // skip older entries
					count++;
					total += tx.getDuration();
					putInIntervalBucket(tx, traceIntervals);
					ITraceCategory daoCategory = tx.getTraceCategory(DAO.TRACE_CAT);
					if (daoCategory != null) {
						daoOps += daoCategory.getCount();
						daoDuration += daoCategory.getTotalDuration();
					}

					for (TraceStats ts : traceStats) {
						countCat(ts, tx);
					}
				}
			}

			sts.setResponseCount(count);
			sts.setTotalResponseTime(total);
			sts.setAverageResponseTime(count != 0 ? total / count : 0d);

			sts.setTotalDAODuration(daoDuration);
			sts.setTotalDAOops(daoOps);

			traceStats.addAll(traceIntervals.values());
			sts.setTraceStats(traceStats);
		}
	}

	/**
	 * Determine in which of the predefined intervals the trace context duration falls
	 * 
	 * @param tx
	 * @param intervals
	 */
	private void putInIntervalBucket(ITraceContext tx, Map<Integer, TraceStats> intervals) {
		int duration = (int) tx.getDuration();
		int key = 0;
		for (int x : traceIntervalBuckets) {
			if (duration >= x) {
				key = x;
			} else {
				break;
			}
		}
		TraceStats bucket = intervals.get(key);
		if (bucket != null) {
			bucket.setCount(bucket.getCount() + 1);
			bucket.setDuration(bucket.getDuration() + duration);
		}
	}

	/**
	 * @param stat
	 * @param tx
	 */
	private void countCat(TraceStats stat, ITraceContext tx) {
		ITraceCategory tCat = tx.getTraceCategory(stat.getName());
		if (tCat != null) {
			stat.setCount(stat.getCount() + tCat.getCount());
			stat.setDuration(stat.getDuration() + tCat.getTotalDuration());
		}
	}
}
