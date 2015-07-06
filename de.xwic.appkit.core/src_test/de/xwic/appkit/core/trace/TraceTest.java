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
