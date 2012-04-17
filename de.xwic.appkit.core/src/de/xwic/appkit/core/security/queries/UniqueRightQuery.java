/*
 * Copyright 2005 pol GmbH
 *
 * de.xwic.appkit.core.security.queries.UniqueActionQuery
 * Created on 08.08.2005
 *
 */
package de.xwic.appkit.core.security.queries;

import de.xwic.appkit.core.dao.EntityQuery;
import de.xwic.appkit.core.security.IRight;

/**
 * @author Florian Lippisch
 */
public class UniqueRightQuery extends EntityQuery {

	private IRight right = null;
	
	/**
	 * Default Constructor.
	 */
	public UniqueRightQuery() {
		
	}

	/**
	 * 
	 */
	public UniqueRightQuery(IRight right ) {
		this.right = right;
	}

	/**
	 * @return Returns the action.
	 */
	public IRight getRight() {
		return right;
	}

	/**
	 * @param action The action to set.
	 */
	public void setRight(IRight right) {
		this.right = right;
	}

}
