/*
 * Copyright 2005 pol GmbH
 *
 * de.xwic.appkit.core.dao.UseCase
 * Created on 20.09.2005
 *
 */
package de.xwic.appkit.core.dao;

/**
 * A UseCase represents the implementation of a process that
 * should be executed on the server side.
 * 
 * @author Florian Lippisch
 */
public abstract class UseCase {

	private boolean asynchronousExecution = false;
	private UseCaseMonitor monitor = null;
	private boolean abortable = false;
	
	/**
	 * Internal execute method. This method is called by the UseCaseExecuter. It is used
	 * to hide the internal methods from the users.
	 * @param api
	 */
	final Object internalExecuteUseCase(DAOProviderAPI api) {
		return execute(api);
	}
	
	/**
	 * A use case must implement this method to execute the use case. 
	 * @param api
	 */
	protected abstract Object execute(DAOProviderAPI api);
	
	/**
	 * Executes the UseCase. 
	 */
	public final Object execute() {
	
		return DAOSystem.getUseCaseService().execute(this);
		
	}

	/**
	 * @return the asynchronousExecution
	 */
	public boolean isAsynchronousExecution() {
		return asynchronousExecution;
	}

	/**
	 * Set to true, if the UseCase should be executed by an asynchronous
	 * backend job.
	 * @param asynchronousExecution the asynchronousExecution to set
	 */
	public void setAsynchronousExecution(boolean asynchronousExecution) {
		this.asynchronousExecution = asynchronousExecution;
	}

	/**
	 * @return the monitor
	 */
	public UseCaseMonitor getMonitor() {
		return monitor;
	}

	/**
	 * @param monitor the monitor to set
	 */
	public void setMonitor(UseCaseMonitor monitor) {
		this.monitor = monitor;
	}

	/**
	 * @return the abortable
	 */
	public boolean isAbortable() {
		return abortable;
	}

	/**
	 * @param abortable the abortable to set
	 */
	protected void setAbortable(boolean abortable) {
		this.abortable = abortable;
	}
	

}
