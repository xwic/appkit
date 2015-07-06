/*******************************************************************************
 * Copyright 2015 xWic group (http://www.xwic.de)
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 		http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 *  
 *******************************************************************************/
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
