/*
 * Copyright 2005 pol GmbH
 *
 * de.xwic.appkit.core.security.queries.UniqueActionQuery
 * Created on 08.08.2005
 *
 */
package de.xwic.appkit.core.security.queries;

import de.xwic.appkit.core.dao.EntityQuery;
import de.xwic.appkit.core.security.IActionSet;

/**
 * @author Florian Lippisch
 */
public class UniqueActionSetQuery extends EntityQuery {

	private IActionSet actionSet = null;
	
	/**
	 * Default Constructor.
	 */
	public UniqueActionSetQuery() {
		
	}

	/**
	 * 
	 */
	public UniqueActionSetQuery(IActionSet actionSet ) {
		this.actionSet = actionSet;
	}

	/**
	 * @return Returns the action.
	 */
	public IActionSet getActionSet() {
		return actionSet;
	}

	/**
	 * @param action The action to set.
	 */
	public void setActionSet(IActionSet actionSet) {
		this.actionSet = actionSet;
	}

}
