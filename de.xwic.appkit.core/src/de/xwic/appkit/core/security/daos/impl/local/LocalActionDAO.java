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
import de.xwic.appkit.core.security.IAction;
import de.xwic.appkit.core.security.daos.impl.ActionDAO;
import de.xwic.appkit.core.security.queries.UniqueActionQuery;

/**
 * Server DAO for the Action. <p>
 * 
 * @author Florian Lippisch
 */
public class LocalActionDAO extends ActionDAO {
	
	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.dao.AbstractDAO#update(de.xwic.appkit.core.dao.IEntity)
	 */
	public void update(IEntity entity) throws DataAccessException {
		
		IAction newAction = (IAction) entity;
		EntityList erg = getEntities(null, new UniqueActionQuery(newAction));
		
		if (erg.size() == 0) {
			super.update(entity);
		} else {
			throw new DataAccessException("Action mit dem Namen: \"" + newAction.getName() + "\" bereits vorhanden!");
		}
	}

}
