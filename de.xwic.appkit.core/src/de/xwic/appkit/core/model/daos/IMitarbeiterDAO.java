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
package de.xwic.appkit.core.model.daos;

import java.util.List;

import de.xwic.appkit.core.dao.DAO;
import de.xwic.appkit.core.model.entities.IMitarbeiter;

/**
 * @author Ronny Pfretzschner
 */
public interface IMitarbeiterDAO extends DAO<IMitarbeiter> {

	/**
	 * @return A list of IMitarbeiter, who are all Betreuer with group by clausel
	 */
	public List<?> getAllUNBetreuer();

	/**
	 * Returns the Mitarbeiter object linked to the current user.
	 * If no mitarbeiter was found, <code>null</code> is returned.
	 * @return
	 */
	public IMitarbeiter getByCurrentUser();

	/**
	 * @param username
	 * @return
	 */
	public IMitarbeiter getMittarbeiterByUsername(String username);
	
	/**
	 * @return
	 */
	public List<IMitarbeiter> getAllMyReports();
	
	/**
	 * @param leader
	 * @return
	 */
	public List<IMitarbeiter> getAllReportsByUser(IMitarbeiter leader);
}
