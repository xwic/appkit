/*
 * (c) Copyright 2005, 2006 by pol GmbH 
 * 
 * de.xwic.appkit.core.ucexec.GetServerDateUseCase
 * Created on Mar 26, 2008 by Aron Cotrau
 *
 */
package de.xwic.appkit.core.ucexec;

import java.util.Date;

import de.xwic.appkit.core.dao.DAOProviderAPI;
import de.xwic.appkit.core.dao.UseCase;


/**
 * Use case for retreiving the date from the serverside.
 *
 * @author Aron Cotrau
 */
public class GetServerDateUseCase extends UseCase {

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.dao.UseCase#execute(de.xwic.appkit.core.dao.DAOProviderAPI)
	 */
	protected Object execute(DAOProviderAPI api) {
		Date date = new Date();
		
		// return the current date from the server
		return date;
	}

}
