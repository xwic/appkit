/*
 * Copyright 2005 pol GmbH
 *
 * de.xwic.appkit.core.model.daos.impl.local.LocalAnhangDAO
 * Created on 09.09.2005
 *
 */
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
