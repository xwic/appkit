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
import de.xwic.appkit.core.security.IRight;
import de.xwic.appkit.core.security.daos.impl.RightDAO;
import de.xwic.appkit.core.security.queries.UniqueRightQuery;

/**
 * Server DAO for the Right. <p>
 * 
 * @author Florian Lippisch
 */
public class LocalRightDAO extends RightDAO {
	
	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.dao.AbstractDAO#update(de.xwic.appkit.core.dao.IEntity)
	 */
	public void update(IEntity entity) throws DataAccessException {
		
		IRight newRight = (IRight) entity;
		EntityList erg = getEntities(null, new UniqueRightQuery(newRight));
		
		if (erg.size() == 0) {
			super.update(entity);
		} else {
			throw new DataAccessException("Right bereits vorhanden!");
		}
	}

}
