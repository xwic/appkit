/*
 * Copyright 2005 pol GmbH
 *
 * de.xwic.appkit.core.security.daos.impl.ScopeDAO
 * Created on 02.08.2005
 *
 */
package de.xwic.appkit.core.security.daos.impl;

import de.xwic.appkit.core.dao.AbstractDAO;
import de.xwic.appkit.core.security.IScope;
import de.xwic.appkit.core.security.daos.IScopeDAO;
import de.xwic.appkit.core.security.impl.Scope;

/**
 * @author Florian Lippisch
 */
public class ScopeDAO extends AbstractDAO<IScope, Scope> implements IScopeDAO {

	/**
	 *
	 */
	public ScopeDAO() {
		super(IScope.class, Scope.class);
	}

}
