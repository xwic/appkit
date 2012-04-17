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
import de.xwic.appkit.core.security.IActionSet;
import de.xwic.appkit.core.security.daos.impl.ActionSetDAO;
import de.xwic.appkit.core.security.queries.UniqueActionSetQuery;

/**
 * Server DAO for the ActionSet. <p>
 * 
 * @author Florian Lippisch
 */
public class LocalActionSetDAO extends ActionSetDAO {
	
	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.dao.AbstractDAO#update(de.xwic.appkit.core.dao.IEntity)
	 */
	public void update(IEntity entity) throws DataAccessException {
		
		IActionSet newActionSet = (IActionSet) entity;
		EntityList erg = getEntities(null, new UniqueActionSetQuery(newActionSet));
		
		if (erg.size() == 0) {
			super.update(entity);
		} else {
			throw new DataAccessException("ActionSet mit dem Namen: \"" + newActionSet.getName() + "\" bereits vorhanden!");
		}
	}

}
