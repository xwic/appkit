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
package de.xwic.appkit.core.model.daos.impl.local;

import de.xwic.appkit.core.dao.DAOSystem;
import de.xwic.appkit.core.dao.DataAccessException;
import de.xwic.appkit.core.dao.IEntity;
import de.xwic.appkit.core.model.daos.impl.AnhangDAO;
import de.xwic.appkit.core.model.entities.IAnhang;

/**
 * Local DAO implementation for the Anhang entity. <p>
 * 
 * @author Ronny Pfretzschner
 *
 */
public class LocalAnhangDAO extends AnhangDAO {

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.dao.AbstractDAO#delete(de.xwic.appkit.core.dao.IEntity)
	 */
	public void delete(IEntity entity) throws DataAccessException {
		
		IAnhang anhang = (IAnhang)entity;
		
		//get fileID
		int tempFileID = anhang.getFileID(); 
		
		//delete object
		super.delete(anhang);
		
		//delete file if it's not an indirect reference
		if (! anhang.isIndirectReference()) {
			DAOSystem.getFileHandler().deleteFile(tempFileID);
		}
	}
}
