/*
 * Copyright 2005 pol GmbH
 *
 * de.xwic.appkit.core.model.daos.IAnhangDAO
 * Created on 04.08.2005
 *
 */
package de.xwic.appkit.core.model.daos;

import de.xwic.appkit.core.dao.DAO;
import de.xwic.appkit.core.dao.EntityList;
import de.xwic.appkit.core.dao.IEntity;
import de.xwic.appkit.core.model.entities.IAnhang;

/**
 * DAO Interface for the Anhang entity. <p>
 *
 * @author Ronny Pfretzschner
 */
public interface IAnhangDAO extends DAO<IAnhang> {

	/**
	 * @param entity
	 * @return
	 */
	EntityList<IAnhang> getByEntity(IEntity entity);
}
