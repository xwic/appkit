/*
 * Copyright 2005 pol GmbH
 *
 * de.xwic.appkit.core.dao.IUseCaseService
 * Created on 20.09.2005
 *
 */
package de.xwic.appkit.core.dao;

/**
 * Executes use cases in the right environment. Makes sure that a use case
 * is executed on the server.
 * 
 * @author Florian Lippisch
 */
public interface IUseCaseService {

	/**
	 * Executes the specified useCase.
	 * @param useCase
	 */
	public abstract Object execute(UseCase useCase);
	
	/**
	 * Returns the monitor with that ticketNr.
	 * @param ticketNr
	 * @return
	 */
	public UseCaseMonitor getMonitor(long ticketNr);
	
	/**
	 * Releases the monitor with that ticket nr.
	 * @param ticketNr
	 */
	public void releaseMonitor(long ticketNr);
	
	/**
	 * Sets the specified monitor to aborted. The executed UseCase should
	 * check the aborted flag.
	 * @param ticketNr
	 */
	public void abortMonitor(long ticketNr);
}
