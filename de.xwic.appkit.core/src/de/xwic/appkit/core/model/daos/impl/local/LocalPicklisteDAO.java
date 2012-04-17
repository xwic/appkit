/*
 * Copyright 2005 pol GmbH
 *
 * de.xwic.appkit.core.model.daos.impl.local.LocalPicklisteDAO
 * Created on 09.01.2006
 *
 */
package de.xwic.appkit.core.model.daos.impl.local;

import java.util.Iterator;

import de.xwic.appkit.core.ApplicationData;
import de.xwic.appkit.core.dao.DAOCallback;
import de.xwic.appkit.core.dao.DAOProviderAPI;
import de.xwic.appkit.core.dao.DataAccessException;
import de.xwic.appkit.core.dao.IEntity;
import de.xwic.appkit.core.model.daos.impl.PicklisteDAO;
import de.xwic.appkit.core.model.entities.IPicklistEntry;
import de.xwic.appkit.core.model.entities.IPicklistText;

/**
 * @author Florian Lippisch
 */
public class LocalPicklisteDAO extends PicklisteDAO {

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.dao.AbstractDAO#delete(de.xwic.appkit.core.dao.IEntity)
	 */
	public void delete(final IEntity entity) throws DataAccessException {
		
		if (entity instanceof IPicklistEntry) {
	    	checkRights(ApplicationData.SECURITY_ACTION_DELETE);
	    	
	    	//remove entry from cache!
	    	//if an error occurs -> the entry will be reloaded correctly
	    	cache.removePicklistEntry((IPicklistEntry)entity);
	    	
	    	provider.execute(new DAOCallback() {
	       		public Object run(DAOProviderAPI api) {
	       			
	       			IPicklistEntry entry =(IPicklistEntry)entity;
	       			// remove texte first
	       			for (Iterator<IPicklistText> it = entry.getPickTextValues().iterator(); it.hasNext(); ) {
	       				IPicklistText text = it.next();
	       				api.delete(text);
	       				//it.remove();
	       			}
	       			//api.update(entry);
	       			entry.setPickTextValues(null);
	       			api.delete(entry);
	       			return null;
	       		}
	       	});
	
		} else {
			super.delete(entity);
		}
	}
	
}
