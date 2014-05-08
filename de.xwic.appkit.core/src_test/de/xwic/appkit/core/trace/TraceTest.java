/*
 * Copyright (c) 2009 Network Appliance, Inc.
 * All rights reserved.
 */

package de.xwic.appkit.core.trace;

import java.io.File;

import junit.framework.TestCase;
import de.xwic.appkit.core.trace.ITraceDataManager.TraceLevel;
import de.xwic.appkit.core.trace.impl.TraceOperation;

/**
 * @author lippisch
 */
public class TraceTest extends TestCase {

	private void assertNear(long v1, long v2, long maxRange) {
		
		long dif = Math.abs(v1 - v2);
		if (dif > maxRange) {
			throw new RuntimeException("Values outside max. range (" + v1 + " != " + v2 + " / " + dif + ")");
		}
		
	}
	
	public void testTraceDuration() throws Exception {
		
		ITraceOperation td = new TraceOperation("test");
		
		Thread.sleep(200);
		
		td.finished();
		
		assertNear(td.getDuration(), 200, 15);
		System.out.println(td.getDuration());
		
		Thread.sleep(200);
		assertNear(td.getDuration(), 200, 15);
		
		
	}
	
	public void testCategories() throws InterruptedException {
		Trace.setEnabled(true);
		Trace.getDataManager().setTraceLogFile(new File("c:\\temp\\trace.log"));
		Trace.getDataManager().setLogTraceAboveThreshold(true);
		Trace.getDataManager().setLogThreshold(100);
		Trace.getDataManager().setTraceLevel(TraceLevel.BASIC);
		Trace.startTrace();
		
		ITraceOperation txOp1 = Trace.getTraceContext().startOperation("dao", "test-1");
		
		Thread.sleep(300);
		
		txOp1.finished();
//		assertNear(txOp1.getDuration(), 300, 15);
		
		ITraceOperation txOp2 = Trace.getTraceContext().startOperation("dao");
		Thread.sleep(200);
		txOp2.setInfo("This is a fancy op.");
		txOp2.finished();
//		assertNear(txOp2.getDuration(), 200, 5);
		
		ITraceContext tx = Trace.endTrace();
		System.out.println("Total Duration: " + tx.getDuration());
//		assertNear(tx.getDuration(), 500, 5);
		
		ITraceCategory category = tx.getTraceCategory("dao");
		System.out.println("Count: " + category.getCount());
		System.out.println("Cat-Duration: " + category.getTotalDuration());
		
		Trace.getDataManager().handleTraceResult(tx);
		
	}
	
}
