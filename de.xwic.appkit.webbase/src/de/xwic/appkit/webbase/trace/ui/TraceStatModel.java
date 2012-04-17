/*
 * Copyright (c) 2009 Network Appliance, Inc.
 * All rights reserved.
 */

package de.xwic.appkit.webbase.trace.ui;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.jwic.util.SerObservable;
import de.xwic.appkit.core.trace.ITraceContext;
import de.xwic.appkit.core.trace.ITraceDataManager;
import de.xwic.appkit.core.trace.Trace;
import de.xwic.cube.DataPoolManagerFactory;
import de.xwic.cube.ICube;
import de.xwic.cube.IDataPool;
import de.xwic.cube.IDataPoolManager;
import de.xwic.cube.IDimension;
import de.xwic.cube.IDimensionElement;
import de.xwic.cube.IMeasure;
import de.xwic.cube.Key;
import de.xwic.cube.storage.impl.FileDataPoolStorageProvider;

/**
 * Keeps data and statics from the TraceData.
 * @author lippisch
 */
public class TraceStatModel extends SerObservable {

	private final Log log = LogFactory.getLog(getClass());
	
	private static final String DIM_TIME = "Time";
	private static final String MEASURE_COUNT = "Count";
	private static final String MEASURE_DURATION = "Duration";
	private static final String MEASURE_PEAK = "Peak";
	private static final String CUBE_STATS = "stats";
	
	private final static long MINUTE = 1000 * 60;
	private final static long HOUR = MINUTE * 60;
	private final static long DAY = HOUR * 24;
	
	private long maxSpan = DAY * 5;
	private IDataPoolManager dataPoolManager;
	private IDataPool dataPool = null;
	
	private double highestValue = 0d;
	private double highestPeak = 0d;
	private double highestCount = 0d;
	private double highestDuration = 0d;
	
	/**
	 * 
	 */
	public TraceStatModel() {
		super();
		dataPoolManager = DataPoolManagerFactory.createDataPoolManager(new FileDataPoolStorageProvider(new File("."))); 
		
	}
	
	/**
	 * Load the data.
	 */
	public void refresh() {
		
		ITraceDataManager dataManager = Trace.getDataManager();
		if (dataManager != null) {
			
			List<ITraceContext> history = dataManager.getTraceHistory();
			
			if (history.size() != 0) {
				// figure out span
				long oldest = history.get(0).getStartTime();
				long newest = history.get(history.size() - 1).getStartTime();
				long maxAge = newest - maxSpan; // max five days.
				
				long startTime = oldest < maxAge ? maxAge : oldest; 
				long span = newest - startTime;
				
				boolean useMinutes = span < HOUR * 2; // use minutes scale if span is < 2 hours 
				
				// create a "cube"
				// create a new temporary DataPoolManager.
				IDataPool dpTemp = dataPoolManager.createDataPool("temp-" + System.currentTimeMillis());
				IMeasure meDuration = dpTemp.createMeasure(MEASURE_DURATION);
				IMeasure meCount = dpTemp.createMeasure(MEASURE_COUNT);
				IMeasure mePeak = dpTemp.createMeasure(MEASURE_PEAK);
				
				// create time scale
				DateFormat df;
				if (useMinutes) {
					df = new SimpleDateFormat("HH:mm"); 
				} else {
					df = new SimpleDateFormat("dd-MMM HH");
				}
				
				IDimension dimTime = dpTemp.createDimension(DIM_TIME);
				Calendar cal = Calendar.getInstance();
				cal.setTimeInMillis(startTime);
				int field;
				if (useMinutes) {
					field = Calendar.MINUTE;
				} else {
					field = Calendar.HOUR_OF_DAY;
				}
				do {
					String timeKey = df.format(cal.getTime());
					if (!useMinutes) {
						timeKey = timeKey + ":00";
					}
					dimTime.createDimensionElement(timeKey);
					
					cal.add(field, 1);
					
				} while (cal.getTimeInMillis() <= newest);
				// add one more
				String timeKey = df.format(cal.getTime());
				if (!useMinutes) {
					timeKey = timeKey + ":00";
				}
				dimTime.createDimensionElement(timeKey);
				
				
				// create cube
				ICube cube = dpTemp.createCube(CUBE_STATS, new IDimension[] { dimTime }, new IMeasure[] { meDuration, meCount, mePeak });
				
				// load the data
				for (ITraceContext tcx : history) {
					if (tcx.getStartTime() >= startTime) {
						String key = df.format(new Date(tcx.getStartTime()));
						if (!useMinutes) {
							key = key + ":00";
						}
						if (dimTime.containsDimensionElement(key)) {
							IDimensionElement elm = dimTime.getDimensionElement(key);
							Key cKey = cube.createKey(elm);
							double value = tcx.getDuration();
							cube.addCellValue(cKey, meDuration, value);
							cube.addCellValue(cKey, meCount, 1);
							
							Double val = cube.getCellValue(cKey, mePeak);
							if (val == null || val < value) {
								cube.setCellValue(cKey, mePeak, value);
							}
						} else {
							log.warn("Can not find element with key " + key + " in time dimension??");
						}
					}
				}

				// scan for scale
				double h = 0;
				double hPeak = 0;
				double hCnt = 0;
				double hDuration = 0;
				for (IDimensionElement de : dimTime.getDimensionElements()) {
					Key cKey = cube.createKey(de);
					Double valDur = cube.getCellValue(cKey, meDuration);
					Double valPeak = cube.getCellValue(cKey, mePeak);
					Double valCnt = cube.getCellValue(cKey, meCount);
					if (valDur != null && valCnt != null) {
						double avr = valDur / valCnt;
						if (avr > h) {
							h = avr;
						}
					}
					if (valPeak != null && valPeak > hPeak) {
						hPeak = valPeak;
					}
					if (valCnt != null && valCnt > hCnt) {
						hCnt = valCnt;
					}
					if (valDur != null && valDur > hDuration) {
						hDuration = valDur;
					}
				}
				highestValue = h;
				highestPeak = hPeak;
				highestCount = hCnt;
				highestDuration = hDuration;
				
				// switch pools
				
				if (dataPool != null) {
					dataPoolManager.releaseDataPool(dataPool);
				}
				dataPool = dpTemp;
				
			}
			
			
		}
		
		setChanged();
		notifyObservers();
		
	}
	
	/**
	 * Returns the statistic cube.
	 * @return
	 */
	public ICube getCube() {
		return dataPool != null ? dataPool.getCube(CUBE_STATS) : null;
	}

	/**
	 * Returns the time dimension element.
	 * @return
	 */
	public IDimension getDimensionTime() {
		return dataPool != null ? dataPool.getDimension(DIM_TIME) : null;
	}

	/**
	 * Returns the duration measure element.
	 * @return
	 */
	public IMeasure getMeasureDuration() {
		return dataPool != null ? dataPool.getMeasure(MEASURE_DURATION) : null;
	}
	
	/**
	 * Returns the count measure element.
	 * @return
	 */
	public IMeasure getMeasureCount() {
		return dataPool != null ? dataPool.getMeasure(MEASURE_COUNT) : null;
	}
	
	/**
	 * Returns the count measure element.
	 * @return
	 */
	public IMeasure getMeasurePeak() {
		return dataPool != null ? dataPool.getMeasure(MEASURE_PEAK) : null;
	}
	

	/**
	 * @return the maxSpan
	 */
	public long getMaxSpan() {
		return maxSpan;
	}

	/**
	 * @param maxSpan the maxSpan to set
	 */
	public void setMaxSpan(long maxSpan) {
		this.maxSpan = maxSpan;
	}

	/**
	 * @return the highestValue
	 */
	public double getHighestAvr() {
		return highestValue;
	}

	/**
	 * @return the highestPeak
	 */
	public double getHighestPeak() {
		return highestPeak;
	}

	/**
	 * @return the highestPeak
	 */
	public double getHighestCount() {
		return highestCount;
	}
	/**
	 * @return the highestPeak
	 */
	public double getHighestDuration() {
		return highestDuration;
	}

}
