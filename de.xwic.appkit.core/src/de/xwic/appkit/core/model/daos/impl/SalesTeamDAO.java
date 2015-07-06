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
import de.xwic.appkit.core.dao.IEntity;
import de.xwic.appkit.core.dao.ValidationResult;
import de.xwic.appkit.core.model.daos.ISalesTeamDAO;
import de.xwic.appkit.core.model.entities.ISalesTeam;
import de.xwic.appkit.core.model.entities.impl.SalesTeam;

/**
 * DAO for the SalesTeam business object. <p>
 *
 * @author Ronny Pfretzschner
 */
public class SalesTeamDAO extends AbstractDAO<ISalesTeam, SalesTeam> implements ISalesTeamDAO {

	/**
	 *
	 */
	public SalesTeamDAO() {
		super(ISalesTeam.class, SalesTeam.class);
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.dao.AbstractDAO#validateEntity(de.xwic.appkit.core.dao.IEntity)
	 */
	@Override
	public ValidationResult validateEntity(IEntity entity) {
		ValidationResult result = new ValidationResult();
		ISalesTeam newST = (ISalesTeam) entity;

		if (newST.getBezeichnung() == null || newST.getBezeichnung().length() < 1) {
			result.addError("st.bezeichnung", ValidationResult.ENTITY_ARGUMENTS_ERROR_RESOURCE_KEY);
		}
		return result;
	}
}
