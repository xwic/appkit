/*
 * (c) Copyright 2005, 2006 by pol GmbH 
 * 
 * de.xwic.appkit.core.ucexec.TestUseCase
 * Created on 05.02.2007 by Florian Lippisch
 *
 */
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
