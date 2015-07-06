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
package de.xwic.appkit.core.ucexec;

import de.xwic.appkit.core.dao.DAOProviderAPI;
import de.xwic.appkit.core.dao.UseCase;
import de.xwic.appkit.core.dao.UseCaseMonitor;

/**
 * Does some dummy calculations.
 * @author Florian Lippisch
 */
public class TestUseCase extends UseCase {

	private int sleepTime = 200;
	private int countTo = 100;
	private boolean raiseException = false;
	
	/**
	 * Constructor.
	 *
	 */
	public TestUseCase() {
		setAbortable(true);
	}
	
	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.dao.UseCase#execute(de.xwic.appkit.core.dao.DAOProviderAPI)
	 */
	protected Object execute(DAOProviderAPI api) {
		
		UseCaseMonitor monitor = getMonitor();
		if (monitor == null) {
			monitor = new UseCaseMonitor();
		}
		monitor.setMax(countTo);
		monitor.setMin(0);
		for(int i = 0; i < countTo; i++) {
			monitor.setCounter(i);
			monitor.setInfo("Counting #" + i + " of " + countTo);
			//System.out.println("UC: "+ i);
			try {
				Thread.sleep(sleepTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (monitor.isAborted()) {
				return "ABORTED";
			}
		}
		
		if (raiseException) {
			throw new RuntimeException("Some Exception");
		}
		
		return "TEST";
	}

	/**
	 * @return the countTo
	 */
	public int getCountTo() {
		return countTo;
	}

	/**
	 * @param countTo the countTo to set
	 */
	public void setCountTo(int countTo) {
		this.countTo = countTo;
	}

	/**
	 * @return the raiseException
	 */
	public boolean isRaiseException() {
		return raiseException;
	}

	/**
	 * @param raiseException the raiseException to set
	 */
	public void setRaiseException(boolean raiseException) {
		this.raiseException = raiseException;
	}

	/**
	 * @return the sleepTime
	 */
	public int getSleepTime() {
		return sleepTime;
	}

	/**
	 * @param sleepTime the sleepTime to set
	 */
	public void setSleepTime(int sleepTime) {
		this.sleepTime = sleepTime;
	}

}
