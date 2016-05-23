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
/*
 * de.xwic.appkit.core.model.daos.impl.PicklisteDAO
 * Created on 07.07.2005
 *
 */
package de.xwic.appkit.core.model.daos.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import de.xwic.appkit.core.config.ConfigurationException;
import de.xwic.appkit.core.config.model.EntityDescriptor;
import de.xwic.appkit.core.dao.DAOCallback;
import de.xwic.appkit.core.dao.DAOProviderAPI;
import de.xwic.appkit.core.dao.DAOSystem;
import de.xwic.appkit.core.dao.DataAccessException;
import de.xwic.appkit.core.dao.EntityList;
import de.xwic.appkit.core.dao.IEntity;
import de.xwic.appkit.core.dao.ValidationResult;
import de.xwic.appkit.core.dao.event.AbstractDAOWithEvent;
import de.xwic.appkit.core.dao.event.DaoEntityEvent;
import de.xwic.appkit.core.model.daos.IPicklisteDAO;
import de.xwic.appkit.core.model.entities.IPicklistEntry;
import de.xwic.appkit.core.model.entities.IPicklistText;
import de.xwic.appkit.core.model.entities.IPickliste;
import de.xwic.appkit.core.model.entities.impl.PicklistEntry;
import de.xwic.appkit.core.model.entities.impl.PicklistText;
import de.xwic.appkit.core.model.entities.impl.Pickliste;
import de.xwic.appkit.core.model.queries.PicklistEntryQuery;
import de.xwic.appkit.core.model.queries.PicklistQuery;
import de.xwic.appkit.core.model.queries.PicklistTextQuery;
import de.xwic.appkit.core.util.CollectionUtil;

/**
 * DAO implementation for the Pickliste object.
 * @author Florian Lippisch
 */
public class PicklisteDAO extends AbstractDAOWithEvent<IPickliste, Pickliste> implements IPicklisteDAO {

	/** container for the picklist cache. */
	protected PicklistCache cache = new PicklistCache();

	/**
	 *
	 */
	public PicklisteDAO() {
		super(IPickliste.class, Pickliste.class);
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.model.daos.IPicklisteDAO#create()
	 */
	@Override
	public IPickliste create(final String key, final String title, final String beschreibung) {

	    IPickliste pl = getPicklisteByKey(key);

	    if (pl != null) {
			throw new RuntimeException("Pickliste mit key: \"" + key + "\" existiert bereits!");
		}

	    pl = (IPickliste)provider.execute(new DAOCallback() {
			@Override
			public Object run(DAOProviderAPI api) {
				IPickliste picklist = new Pickliste(key, title, beschreibung);
				api.update(picklist);
				return picklist;
			}
		});
	    fireEntityChangeEvent(new DaoEntityEvent<IPickliste>(DaoEntityEvent.UPDATE, pl));
	    return pl; 
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.dao.AbstractDAO#createEntity(java.lang.String)
	 */
	@Override
	public IEntity createEntity(String subtype) {
		if (subtype.endsWith("Pickliste")) {
			return createEntity();
		} else if (subtype.endsWith("PicklistEntry")) {
			return createPicklistEntry();
		} else if (subtype.endsWith("PicklistText")) {
			return new PicklistText();
		}
		throw new IllegalArgumentException("Unknown subtype specified: " + subtype);
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.dao.AbstractDAO#handlesEntity(java.lang.String)
	 */
	@Override
	public boolean handlesEntity(String entityClass) {
    	if (getEntityClass().getName().equals(entityClass) ||
    		IPicklistEntry.class.getName().equals(entityClass) ||
    		IPicklistText.class.getName().equals(entityClass)) {
    		return true; // no need to instanciate if its the same classname.
    	}
    	try {
    		Class<?> clazz = Class.forName(entityClass);
			return IPickliste.class.isAssignableFrom(clazz) ||
			   IPicklistEntry.class.isAssignableFrom(clazz) ||
			   IPicklistText.class.isAssignableFrom(clazz);
		} catch (ClassNotFoundException e) {
			return false;
		}
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.dao.AbstractDAO#getEntityDescriptor(java.lang.String)
	 */
	@Override
	public EntityDescriptor getEntityDescriptor(String subtype) {
		try {
			if (subtype.equals(IPickliste.class.getName()) || subtype.equals(Pickliste.class.getName())) {
				return DAOSystem.getEntityDescriptor(IPickliste.class.getName());
			}
			if (subtype.equals(IPicklistEntry.class.getName()) || subtype.equals(PicklistEntry.class.getName())) {
				return DAOSystem.getEntityDescriptor(IPicklistEntry.class.getName());
			}
			if (subtype.equals(IPicklistText.class.getName()) || subtype.equals(PicklistText.class.getName())) {
				return DAOSystem.getEntityDescriptor(IPicklistText.class.getName());
			}
		} catch (ConfigurationException e) {
			throw new DataAccessException("Can not load EntityDescriptor for " + subtype, e);
		}
		throw new DataAccessException("Unknown subtype specified: " + subtype);
	}

    /* (non-Javadoc)
     * @see de.xwic.appkit.core.model.daos.IPicklisteDAO#registerPickListEntry(de.xwic.appkit.core.model.entities.IPickliste)
     */
    @Override
	public IPicklistEntry createPickListEntry(final IPickliste list) {
		IPicklistEntry pe = (IPicklistEntry)provider.execute(new DAOCallback() {
			@Override
			public Object run(DAOProviderAPI api) {
		        IPicklistEntry newEntry = new PicklistEntry(list);
		        api.update(newEntry);
		        return newEntry;
			}
		});
		fireEntityChangeEvent(new DaoEntityEvent<IPicklistEntry>(DaoEntityEvent.UPDATE, pe));
		return pe;
    }

    /*
     *  (non-Javadoc)
     * @see de.xwic.appkit.core.model.daos.IPicklisteDAO#createBezeichnung(de.xwic.appkit.core.model.entities.IPicklistEntry, java.lang.String, java.lang.String)
     */
    @Override
	public IPicklistText createBezeichnung(final IPicklistEntry entry, final String langID, final String bezeichnung) {
    	IPicklistText peText =
    	(IPicklistText)provider.execute(new DAOCallback() {
			@Override
			public Object run(DAOProviderAPI api) {
				PicklistText newText = new PicklistText(entry, langID, bezeichnung);
		        api.update(newText);
		        return newText;
			}
		});
    	fireEntityChangeEvent(new DaoEntityEvent<IPicklistText>(DaoEntityEvent.UPDATE, peText));
    	return peText;
    }

    /* (non-Javadoc)
     * @see de.xwic.appkit.core.model.daos.IPicklisteDAO#getPickListTextByID(int)
     */
    @Override
	public IPicklistText getPickListTextByID(final int id) {
        //look in cache first...
    	// must scan all entries
    	IPicklistText pt = cache.getPicklistTextById(id);
    	if (pt != null) {
    		return pt;
    	}

		//not in cache, get from db...
    	return (IPicklistText)provider.execute(new DAOCallback() {
    		@Override
			public Object run(DAOProviderAPI api) {
    			IPicklistText e = (IPicklistText)api.getEntity(PicklistText.class, id);
    			if (e != null) {
    				cache.putPicklistText(e);
    			}
		    	return e;
    		}
        });

    }

    /*
     *  (non-Javadoc)
     * @see de.xwic.appkit.core.model.daos.IPicklisteDAO#getPicklistText(de.xwic.appkit.core.model.entities.IPicklistEntry, java.lang.String)
     */
    @Override
	public IPicklistText getPicklistText(final IPicklistEntry entry, final String langID) {

    	//look in cache first...
    	IPicklistText text = cache.getPicklistText(entry.getId(), langID);
    	if (text != null) {
    		return text;
    	}

		//not in cache, get from db...
        Object o = provider.execute(new DAOCallback() {
			@Override
			public Object run(DAOProviderAPI api) {
		    	EntityList list = api.getEntities(PicklistText.class, null, new PicklistTextQuery(entry, langID));
		    	if (list.size() == 0) {		    		
		    		// AI 02-Mar-2016: do not automatically create an empty PicklistText. This will cause wrong entries to be created in the DB.
		    		// Make sure the getPicklistText method is always null checked
		    		
//		    	    IPicklistText newTextWithNewID = createBezeichnung(entry, langID, "<nicht gepflegt>");
//		    	    cache.putPicklistText(newTextWithNewID);
//		    	    return newTextWithNewID;
		    	    return null;
		    	} else {
		    		IPicklistText t = (IPicklistText)list.get(0);
		    		cache.putPicklistText(t);
		    		return t;
		    	}
			}
		});

        return (IPicklistText)o;
	}


    /* (non-Javadoc)
     * @see de.xwic.appkit.core.model.daos.IPicklisteDAO#getAllEntriesToList(de.xwic.appkit.core.model.entities.IPickliste)
     */
    @Override
	public EntityList<IPicklistEntry> getAllEntriesToList(final IPickliste list) {
    	if (list == null) {
    		throw new IllegalArgumentException("Picklist is null!");
    	}
		EntityList erg = cache.getEntryList(list.getKey());

		if (erg == null) {

	    	erg = (EntityList) provider.execute(new DAOCallback() {
		           /* (non-Javadoc)
		         * @see de.xwic.appkit.core.dao.DAOCallback#run(de.xwic.appkit.core.dao.DAOProviderAPI)
		         */
		        @Override
				public Object run(DAOProviderAPI api) {
		            return api.getEntities(PicklistEntry.class, null, new PicklistEntryQuery(list));
		        }
	        });

	    	cache.putEntryList(list.getKey(), erg);
	    	for (int i = 0; i < erg.size(); i++) {
	    		IPicklistEntry e = (IPicklistEntry)erg.get(i);

	    		//add only, if entry is not deleted
	    		if (!e.isDeleted()) {
	        		cache.putPicklistEntry(e);
	    		}
	    	}

		}

		EntityList<IPicklistEntry> copy = new EntityList<IPicklistEntry>(new ArrayList<IPicklistEntry>(), erg.getLimit(), erg.getTotalSize());
		copy.addAll(erg);
		return copy;
    }

    /*
     *  (non-Javadoc)
     * @see de.xwic.appkit.core.model.daos.IPicklisteDAO#getAllEntriesToList(java.lang.String)
     */
    @Override
	public EntityList<IPicklistEntry> getAllEntriesToList(String pickListKey) {
    	if (pickListKey == null || pickListKey.length() < 1) {
    		throw new IllegalArgumentException("Picklist-Key is null or not set correctly!");
    	}

    	IPickliste pl = getPicklisteByKey(pickListKey);
    	if (pl == null) {
    		return new EntityList<IPicklistEntry>(new ArrayList<IPicklistEntry>(), null, 0);

    		//throw new IllegalArgumentException("Picklist with given key: " + pickListKey + " not found in system!");
    	}

    	return getAllEntriesToList(pl);
    }


    /*
     *  (non-Javadoc)
     * @see de.xwic.appkit.core.model.daos.IPicklisteDAO#getPickListEntryByID(int)
     */
    @Override
	public IPicklistEntry getPickListEntryByID(final int id) {
        //look in cache first...
		IPicklistEntry entry = cache.getPicklistEntry(id);
		if (entry != null) {
			return entry;
		}

		//not in cache, get from db...
    	return (IPicklistEntry)provider.execute(new DAOCallback() {
    		@Override
			public Object run(DAOProviderAPI api) {
    			EntityList list = api.getEntities(IPicklistEntry.class, null, new PicklistEntryQuery(id));
		    	if (list.size() != 0) {
		    		IPicklistEntry e = (IPicklistEntry)list.get(0);
		    		//register list in cache...
		    		cache.putPicklistEntry(e);
		    		return e;
		    	}
		    	return null;
    		}
        });
    }

    /* (non-Javadoc)
     * @see de.xwic.appkit.core.model.daos.IPicklisteDAO#getPicklistEntriesByID(java.util.Collection)
     */
	@Override
	public Set<IPicklistEntry> getPicklistEntriesByID(final Collection<Integer> id) {
		final Set<IPicklistEntry> entries = new LinkedHashSet<IPicklistEntry>();
		for (final Integer integer : id) {
			if (null == integer) {
				continue;
			}
			CollectionUtil.addIfNotNull(getPickListEntryByID(integer), entries);
		}
		return entries;
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.model.daos.IPicklisteDAO#getPicklisteByKey(java.lang.String)
	 */
	@Override
	public IPickliste getPicklisteByKey(final String key) {
        //look in cache first...
		IPickliste pickList = cache.getPicklisteByKey(key);
		if (pickList != null) {
			return pickList;
		}
		//not in cache, get from db...
		return (IPickliste)provider.execute(new DAOCallback() {
    		@Override
			public Object run(DAOProviderAPI api) {
    			EntityList list = api.getEntities(Pickliste.class, null, new PicklistQuery(key));
		    	if (list.size() != 0) {
		    		IPickliste p = (IPickliste)list.get(0);
		    		//register list in cache...
		    		getAllEntriesToList(p);
		    		cache.putPickliste(p);
		    		return p;
		    	}
		    	return null;
    		}
        });
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.dao.AbstractDAO#validateEntity(de.xwic.appkit.core.dao.IEntity)
	 */
	@Override
	public ValidationResult validateEntity(IEntity entity) {
		ValidationResult result = new ValidationResult();
		IPickliste newPL = (IPickliste) entity;

		if (newPL.getBeschreibung() == null || newPL.getBeschreibung().length() < 1) {
			result.addError("pl.beschreibung", ValidationResult.ENTITY_ARGUMENTS_ERROR_RESOURCE_KEY);
		}
		if (newPL.getKey() == null || newPL.getKey().length() < 1) {
			result.addError("pl.key", ValidationResult.ENTITY_ARGUMENTS_ERROR_RESOURCE_KEY);
		}
		if (newPL.getTitle() == null || newPL.getTitle().length() < 1) {
			result.addError("pl.title", ValidationResult.ENTITY_ARGUMENTS_ERROR_RESOURCE_KEY);
		}
		return result;
	}

	/*
	 *  (non-Javadoc)
	 * @see de.xwic.appkit.core.model.daos.IPicklisteDAO#dropCache()
	 */
	@Override
	public void dropCache() {
		cache.clear();
		fireEntityChangeEvent(new DaoEntityEvent<IEntity>(DaoEntityEvent.CACHE_CHANGE, null));
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.dao.AbstractDAO#update(de.xwic.appkit.core.dao.IEntity)
	 */
	@Override
	public void update(IEntity entity) throws DataAccessException {
		if (entity instanceof IPicklistEntry) {
			IPicklistEntry entry = (IPicklistEntry)entity;
			if (entry.getPickliste() == null) {
				throw new DataAccessException("PicklistEntry.pickliste must not be NULL");
			}
		}
		super.update(entity);
		dropCache();
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.dao.AbstractDAO#delete(de.xwic.appkit.core.dao.IEntity)
	 */
	@Override
	public void delete(IEntity entity) throws DataAccessException {
		super.delete(entity);
		if (entity instanceof IPicklistEntry) {
			IPicklistEntry entry = (IPicklistEntry)entity;
			cache.removePicklistEntry(entry);
		}
		fireEntityChangeEvent(new DaoEntityEvent<IEntity>(DaoEntityEvent.DELETE, entity));
	}

	/**
	 * Set a new picklist cache.
	 * @param cache
	 */
	public void setCache(PicklistCache cache) {
		this.cache = cache;
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.model.daos.IPicklisteDAO#cacheAll()
	 */
	@Override
	public void cacheAll() {

		provider.execute(new DAOCallback() {
    		@Override
			public Object run(DAOProviderAPI api) {
    			// clear cache
    			cache.clear();

//    			2013-10-04:
//    			if a picklist entry doesn't have a picklist text we assume that this is
//    			because it's a new language so we create a new text for it
//    			when we left outer join, we also fetch the deleted picklist entry and pickliste
//    			which causes them to get cached and show up in selectors

    			// use the PicklistTextQuery because, since we don't have dedicated DAOs for PicklistTexts
    			// a normal query won't work when called from a remote client
    			PicklistTextQuery query = new PicklistTextQuery();
    			query.setExcludeDeletedParents(true);
    			query.setLanguageId(null);
//				query.addEquals("picklistEntry.deleted", false);
//				query.addEquals("picklistEntry.pickliste.deleted", false);

				List<IPicklistText> list = api.getEntities(PicklistText.class, null, query);
				for (IPicklistText text : list) {
		    		IPicklistEntry entry = text.getPicklistEntry();
		    		IPickliste p = entry.getPickliste();
		    		cache.putPickliste(p);
		    		// ensure list exists for auto add
		    		if (cache.getEntryList(p.getKey()) == null) {
		    			cache.putEntryList(p.getKey(), new EntityList(new ArrayList<Object>(), null, 0));
		    		}
		    		cache.putPicklistEntry(entry);
	    			cache.putPicklistText(text);
		    	}

				// Load Picklisten
    			/*
				EntityList list = api.getEntities(Pickliste.class, null, new PicklistQuery());
		    	for (Iterator<?> it = list.iterator(); it.hasNext(); ) {
		    		IPickliste p = (IPickliste)it.next();
		    		cache.putPickliste(p);

					EntityList entrylist = api.getEntities(PicklistEntry.class, null, new PicklistEntryQuery(p));
			    	for (Iterator<?> itE = entrylist.iterator(); itE.hasNext(); ) {
			    		PicklistEntry entry = (PicklistEntry)itE.next();
			    		cache.putPicklistEntry(entry);

			    		for (Iterator<?> itT = entry.getPickTextValues().iterator(); itT.hasNext(); ) {
			    			PicklistText text = (PicklistText)itT.next();
			    			cache.putPicklistText(text);
			    		}
			    	}
			    	cache.putEntryList(p.getKey(), entrylist);
		    	}
		    	*/

		    	/*list = api.getEntities(PicklistText.class, null, new PicklistTextQuery());
		    	for (Iterator it = list.iterator(); it.hasNext(); ) {
		    		PicklistText text = (PicklistText)it.next();
		    		allTexts.put(new LangKey(text.getPicklistEntry().getId(), text.getLanguageID()), text);
		    	}*/

		    	return null;
    		}
		});

	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.model.daos.IPicklisteDAO#createPicklistEntry()
	 */
	@Override
	public IPicklistEntry createPicklistEntry() {
		return new PicklistEntry();
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.model.daos.IPicklisteDAO#getPickListEntryByKey(java.lang.String, java.lang.String)
	 */
	@Override
	public IPicklistEntry getPickListEntryByKey(String listKey, String key) {

		if (key == null) {
			throw new NullPointerException("key must not be null.");
		}

		EntityList list = getAllEntriesToList(listKey);
        //look in cache first...
		for (Object name : list) {
			IPicklistEntry entry = (IPicklistEntry)name;
			if (key.equals(entry.getKey())) {
				return entry;
			}
		}
		return null;
	}
	
	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.dao.event.AbstractDAOWithEvent#softDelete(de.xwic.appkit.core.dao.IEntity)
	 */
	@Override
	public void softDelete(IEntity entity) throws DataAccessException {
		super.softDelete(entity);
		if (entity instanceof IPicklistEntry) {
			IPicklistEntry entry = (IPicklistEntry)entity;
			cache.removePicklistEntry(entry);
		}
		fireEntityChangeEvent(new DaoEntityEvent<IEntity>(DaoEntityEvent.DELETE, entity));
	}
}
