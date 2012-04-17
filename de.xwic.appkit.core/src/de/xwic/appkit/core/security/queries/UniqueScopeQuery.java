/*
 * Copyright 2005 pol GmbH
 *
 * de.xwic.appkit.core.security.queries.UniqueActionQuery
 * Created on 08.08.2005
 *
 */
package de.xwic.appkit.core.security.queries;

import de.xwic.appkit.core.dao.EntityQuery;
import de.xwic.appkit.core.security.IScope;

/**
 * @author Florian Lippisch
 */
public class UniqueScopeQuery extends EntityQuery {

	private IScope scope = null;
	
	/**
	 * Default Constructor.
	 */
	public UniqueScopeQuery() {
		
	}

	/**
	 * 
	 */
	public UniqueScopeQuery(IScope scope ) {
		this.scope = scope;
	}

	/**
	 * @return Returns the action.
	 */
	public IScope getScope() {
		return scope;
	}

	/**
	 * @param action The action to set.
	 */
	public void setScope(IScope scope) {
		this.scope = scope;
	}

}
