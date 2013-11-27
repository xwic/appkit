/*
 * Copyright 2005 pol GmbH
 *
 * de.xwic.appkit.core.model.daos.IMitarbeiterDAO
 * Created on 18.07.2005
 *
 */
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
