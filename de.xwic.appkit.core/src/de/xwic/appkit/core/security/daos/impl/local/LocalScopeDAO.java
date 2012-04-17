/*
 * Copyright 2005 pol GmbH
 *
 * de.xwic.appkit.core.model.daos.impl.local.LocalUnternehmenDAO
 * Created on 03.08.2005
 *
 */
package de.xwic.appkit.core.security.daos.impl.local;

import de.xwic.appkit.core.dao.DataAccessException;
import de.xwic.appkit.core.dao.EntityList;
import de.xwic.appkit.core.dao.IEntity;
import de.xwic.appkit.core.security.IScope;
import de.xwic.appkit.core.security.daos.impl.ScopeDAO;
import de.xwic.appkit.core.security.queries.UniqueScopeQuery;

/**
 * Server DAO for the Scope. <p>
 * 
 * @author Florian Lippisch
 */
public class LocalScopeDAO extends ScopeDAO {
	
	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.dao.AbstractDAO#update(de.xwic.appkit.core.dao.IEntity)
	 */
	public void update(IEntity entity) throws DataAccessException {
		
		IScope newScope = (IScope) entity;
		EntityList erg = getEntities(null, new UniqueScopeQuery(newScope));
		
		if (erg.size() == 0) {
			super.update(entity);
		} else {
			throw new DataAccessException("Scope mit dem Namen: \"" + newScope.getName() + "\" bereits vorhanden!");
		}
	}

}
