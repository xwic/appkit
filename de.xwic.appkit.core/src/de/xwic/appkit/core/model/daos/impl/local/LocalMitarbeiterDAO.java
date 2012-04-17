/*
 * (c) Copyright 2005, 2006 by pol GmbH 
 * 
 * de.xwic.appkit.core.model.daos.impl.local.LocalMitarbeiterDAO
 * Created on 19.12.2006 by Florian Lippisch
 *
 */
package de.xwic.appkit.core.model.daos.impl.local;

import de.xwic.appkit.core.ApplicationData;
import de.xwic.appkit.core.dao.DAOCallback;
import de.xwic.appkit.core.dao.DAOProviderAPI;
import de.xwic.appkit.core.dao.DataAccessException;
import de.xwic.appkit.core.dao.HistoryTool;
import de.xwic.appkit.core.dao.IEntity;
import de.xwic.appkit.core.dao.IHistory;
import de.xwic.appkit.core.model.daos.impl.MitarbeiterDAO;
import de.xwic.appkit.core.model.entities.impl.history.MitarbeiterHistory;

/**
 * @author Florian Lippisch
 */
public class LocalMitarbeiterDAO extends MitarbeiterDAO {
	
	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.dao.AbstractDAO#update(de.xwic.appkit.core.dao.IEntity)
	 */
	public void update(final IEntity entity) throws DataAccessException {

    	checkRights(ApplicationData.SECURITY_ACTION_UPDATE);

		
        provider.execute(new DAOCallback() {
       		public Object run(DAOProviderAPI api) {

       			int reason = entity.getId() == 0 ? IHistory.REASON_CREATED : IHistory.REASON_UPDATED;
       			api.update(entity);
       			
       			if (getEntityDescriptor().isHistory()) {
	       			MitarbeiterHistory his = new MitarbeiterHistory();
	
	       			HistoryTool.createHistoryEntity(entity, his);
	       			his.setEntityID(entity.getId());
	       			his.setEntityVersion(entity.getVersion() + (reason == IHistory.REASON_CREATED ? 0 : 1));
	       			his.setHistoryReason(reason);
	       			api.update(his);
      			}
       			return null;
       		       			
       		}
       	});
	}

}
