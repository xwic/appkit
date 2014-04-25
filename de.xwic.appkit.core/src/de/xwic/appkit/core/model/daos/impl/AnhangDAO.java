/*
 * Copyright 2005 pol GmbH
 *
 * de.xwic.appkit.core.model.daos.impl.AnhangDAO
 * Created on 04.08.2005
 *
 */
package de.xwic.appkit.core.model.daos.impl;

import de.xwic.appkit.core.dao.AbstractDAO;
import de.xwic.appkit.core.dao.EntityList;
import de.xwic.appkit.core.dao.IEntity;
import de.xwic.appkit.core.dao.ValidationResult;
import de.xwic.appkit.core.model.daos.IAnhangDAO;
import de.xwic.appkit.core.model.entities.IAnhang;
import de.xwic.appkit.core.model.entities.impl.Anhang;
import de.xwic.appkit.core.model.queries.PropertyQuery;
import de.xwic.appkit.core.model.util.EntityUtil;

/**
 * DAO implementation for the Anhang object. <p>
 *
 * @author Ronny Pfretzschner
 */
public class AnhangDAO extends AbstractDAO<IAnhang, Anhang> implements IAnhangDAO {

	/**
	 *
	 */
	public AnhangDAO() {
		super(IAnhang.class, Anhang.class);
	}

	/*
	 *  (non-Javadoc)
	 * @see de.xwic.appkit.core.dao.DAO#validateEntity(de.xwic.appkit.core.dao.IEntity)
	 */
	@Override
	public ValidationResult validateEntity(IEntity entity) {
		ValidationResult result = new ValidationResult();
		IAnhang newAH = (IAnhang) entity;

		if (newAH.getDateiName() == null || newAH.getDateiName().length() < 1) {
			result.addError("ah.dateiName", ValidationResult.ENTITY_ARGUMENTS_ERROR_RESOURCE_KEY);
		}
		if (newAH.getEntityType() == null || newAH.getEntityType().length() < 1) {
			result.addWarning("ah.entityType", ValidationResult.ENTITY_ARGUMENTS_WARNING_RESOURCE_KEY);
		}
		if (newAH.getEntityID() < 0) {
			result.addWarning("ah.entityID", ValidationResult.ENTITY_ARGUMENTS_WARNING_RESOURCE_KEY);
		}
		if (newAH.getDateiGroesse() < 0) {
			result.addError("ah.dateiGroesse", ValidationResult.ENTITY_ARGUMENTS_ERROR_RESOURCE_KEY);
		}
		if (newAH.getFileID() < 1) {
			result.addError("ah.fileID", ValidationResult.ENTITY_ARGUMENTS_ERROR_RESOURCE_KEY);
		}

		return result;
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.model.daos.IAnhangDAO#getByEntity(de.xwic.appkit.core.dao.IEntity)
	 */
	@Override
	public EntityList<IAnhang> getByEntity(final IEntity entity) {
		EntityUtil.checkEntityExists(entity);
		PropertyQuery pq = new PropertyQuery();
		pq.addEquals("entityType", entity.type().getName());
		pq.addEquals("entityID", entity.getId());
		return getEntities(null, pq);
	}

}
