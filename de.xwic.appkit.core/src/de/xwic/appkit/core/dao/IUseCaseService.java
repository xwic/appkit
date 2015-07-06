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
