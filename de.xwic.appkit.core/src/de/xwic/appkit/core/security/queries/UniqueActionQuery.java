/*
 * Copyright 2005 pol GmbH
 *
 * de.xwic.appkit.core.security.queries.UniqueActionQuery
 * Created on 08.08.2005
 *
 */
package de.xwic.appkit.core.security.queries;

import de.xwic.appkit.core.dao.EntityQuery;
import de.xwic.appkit.core.security.IAction;

/**
 * @author Florian Lippisch
 */
public class UniqueActionQuery extends EntityQuery {

	private IAction action = null;
	
	/**
	 * Default Constructor.
	 */
	public UniqueActionQuery() {
		
	}

	/**
	 * 
	 */
	public UniqueActionQuery(IAction action ) {
		this.action = action;
	}

	/**
	 * @return Returns the action.
	 */
	public IAction getAction() {
		return action;
	}

	/**
	 * @param action The action to set.
	 */
	public void setAction(IAction action) {
		this.action = action;
	}

}
