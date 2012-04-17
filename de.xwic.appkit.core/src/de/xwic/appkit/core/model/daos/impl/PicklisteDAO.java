/*
 * de.xwic.appkit.core.model.daos.impl.PicklisteDAO
 * Created on 07.07.2005
 *
 */
package de.xwic.appkit.core.model.daos.impl;

import java.util.ArrayList;
import java.util.Iterator;

import de.xwic.appkit.core.config.ConfigurationException;
import de.xwic.appkit.core.config.model.EntityDescriptor;
import de.xwic.appkit.core.dao.AbstractDAO;
import de.xwic.appkit.core.dao.DAOCallback;
import de.xwic.appkit.core.dao.DAOProviderAPI;
import de.xwic.appkit.core.dao.DAOSystem;
import de.xwic.appkit.core.dao.DataAccessException;
import de.xwic.appkit.core.dao.Entity;
import de.xwic.appkit.core.dao.EntityList;
import de.xwic.appkit.core.dao.IEntity;
import de.xwic.appkit.core.dao.ValidationResult;
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
import de.xwic.appkit.core.model.queries.PropertyQuery;

/**
 * DAO implementation for the Pickliste object.
 * @author Florian Lippisch
 */
public class PicklisteDAO extends AbstractDAO implements IPicklisteDAO {

	/** container for the picklist cache. */
	protected PicklistCache cache = new PicklistCache();
	
	
	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.model.daos.IPicklisteDAO#create()
	 */
	public IPickliste create(final String key, final String title, final String beschreibung) {
	    
	    IPickliste pl = getPicklisteByKey(key);
	    
	    if (pl != null)
	        throw new RuntimeException("Pickliste mit key: \"" + key + "\" existiert bereits!");
	    
	    return (IPickliste)provider.execute(new DAOCallback() {
			public Object run(DAOProviderAPI api) {
				IPickliste picklist = new Pickliste(key, title, beschreibung);
				api.update(picklist);
				return picklist;
			}
		});
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.dao.DAO#createEntity()
	 */
	public IEntity createEntity() throws DataAccessException {
		return new Pickliste();
	}
	
	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.dao.AbstractDAO#createEntity(java.lang.String)
	 */
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
	 * @see de.xwic.appkit.core.dao.DAO#getEntityClass()
	 */
	public Class<? extends IEntity> getEntityClass() {
		return IPickliste.class;
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.dao.AbstractDAO#handlesEntity(java.lang.String)
	 */
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
    public IPicklistEntry createPickListEntry(final IPickliste list) {
		return (IPicklistEntry)provider.execute(new DAOCallback() {
			public Object run(DAOProviderAPI api) {
		        IPicklistEntry newEntry = new PicklistEntry(list);
		        api.update(newEntry);
		        return newEntry;
			}
		});
    }

    /*
     *  (non-Javadoc)
     * @see de.xwic.appkit.core.model.daos.IPicklisteDAO#createBezeichnung(de.xwic.appkit.core.model.entities.IPicklistEntry, java.lang.String, java.lang.String)
     */
    public IPicklistText createBezeichnung(final IPicklistEntry entry, final String langID, final String bezeichnung) {
    	return (IPicklistText)provider.execute(new DAOCallback() {
			public Object run(DAOProviderAPI api) {
				PicklistText newText = new PicklistText(entry, langID, bezeichnung);
		        api.update(newText);
		        return newText;
			}
		});
    }
    
    /* (non-Javadoc)
     * @see de.xwic.appkit.core.model.daos.IPicklisteDAO#getPickListTextByID(int)
     */
    public IPicklistText getPickListTextByID(final int id) {
        //look in cache first...
    	// must scan all entries
    	IPicklistText pt = cache.getPicklistTextById(id);
    	if (pt != null) {
    		return pt;
    	}
        
		//not in cache, get from db...
    	return (IPicklistText)provider.execute(new DAOCallback() {
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
    public IPicklistText getPicklistText(final IPicklistEntry entry, final String langID) {

    	//look in cache first...		
    	IPicklistText text = cache.getPicklistText(entry.getId(), langID);
    	if (text != null) {
    		return text;
    	}
    	
		//not in cache, get from db...    	
        Object o = provider.execute(new DAOCallback() {
			public Object run(DAOProviderAPI api) {
		    	EntityList list = api.getEntities(PicklistText.class, null, new PicklistTextQuery(entry, langID));
		    	if (list.size() == 0) {
		    	    //TODO IF you had added a new language ID then you have to 
		    	    //create here a new IPicklistText with the new id coming and return it!!
		    	    //THIS would then fix the Project on its own!!!
		    	    IPicklistText newTextWithNewID = createBezeichnung(entry, langID, "<nicht gepflegt>");
		    	    cache.putPicklistText(newTextWithNewID);
		    	    return newTextWithNewID;
		    	    //return null;
		    	} else {
		    		IPicklistText t = (IPicklistText)list.get(0); 
		    		cache.putPicklistText(t);
		    		return t;
		    	}
			}
		}); 
        
        //System.out.println(o);
        
        
        return (IPicklistText)o; 
	}
    

    /* (non-Javadoc)
     * @see de.xwic.appkit.core.model.daos.IPicklisteDAO#getAllEntriesToList(de.xwic.appkit.core.model.entities.IPickliste)
     */
    public EntityList getAllEntriesToList(final IPickliste list) {
    	if (list == null) {
    		throw new IllegalArgumentException("Picklist is null!");
    	}
		EntityList erg = cache.getEntryList(list.getKey());

		if (erg == null) {
		
	    	erg = (EntityList) provider.execute(new DAOCallback() {
		           /* (non-Javadoc)
		         * @see de.xwic.appkit.core.dao.DAOCallback#run(de.xwic.appkit.core.dao.DAOProviderAPI)
		         */
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
    	
		EntityList copy = new EntityList(new ArrayList<Object>(), erg.getLimit(), erg.getTotalSize());
		copy.addAll(erg);
		return copy;
    }
    
    /*
     *  (non-Javadoc)
     * @see de.xwic.appkit.core.model.daos.IPicklisteDAO#getAllEntriesToList(java.lang.String)
     */
    public EntityList getAllEntriesToList(String pickListKey) {
    	if (pickListKey == null || pickListKey.length() < 1) {
    		throw new IllegalArgumentException("Picklist-Key is null or not set correctly!");
    	}
    	
    	IPickliste pl = getPicklisteByKey(pickListKey);
    	if (pl == null) {
    		
    		return new EntityList(new ArrayList<Object>(), null, 0);
    		
    		//throw new IllegalArgumentException("Picklist with given key: " + pickListKey + " not found in system!");
    	}
    	
    	return getAllEntriesToList(pl);
    }
    
    
    /*
     *  (non-Javadoc)
     * @see de.xwic.appkit.core.model.daos.IPicklisteDAO#getPickListEntryByID(int)
     */
    public IPicklistEntry getPickListEntryByID(final int id) {
        //look in cache first...
		IPicklistEntry entry = cache.getPicklistEntry(id);
		if (entry != null) {
			return entry;
		}
        
		//not in cache, get from db...
    	return (IPicklistEntry)provider.execute(new DAOCallback() {
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
	 * @see de.xwic.appkit.core.model.daos.IPicklisteDAO#getPicklisteByKey(java.lang.String)
	 */
	public IPickliste getPicklisteByKey(final String key) {
        //look in cache first...
		IPickliste pickList = cache.getPicklisteByKey(key);
		if (pickList != null) {
			return pickList;
		}
		//not in cache, get from db...
		return (IPickliste)provider.execute(new DAOCallback() {
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
	 * @see de.xwic.appkit.core.dao.AbstractDAO#getEntityImplClass()
	 */
	public Class<? extends Entity> getEntityImplClass() {
		return Pickliste.class;
	}
	
	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.dao.AbstractDAO#validateEntity(de.xwic.appkit.core.dao.IEntity)
	 */
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
	public void dropCache() {
		cache.clear();
	}
	
	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.dao.AbstractDAO#update(de.xwic.appkit.core.dao.IEntity)
	 */
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
	public void delete(IEntity entity) throws DataAccessException {
		super.delete(entity);
		if (entity instanceof IPicklistEntry) {
			IPicklistEntry entry = (IPicklistEntry)entity;
			cache.removePicklistEntry(entry);
			
		}
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
	public void cacheAll() {

		provider.execute(new DAOCallback() {
    		public Object run(DAOProviderAPI api) {
    			// clear cache
    			cache.clear();

    			PropertyQuery query = new PropertyQuery();
    			query.addLeftOuterJoinProperty("picklistEntry");
    			query.addLeftOuterJoinProperty("picklistEntry.pickliste");
    			
				EntityList list = api.getEntities(PicklistText.class, null, query);
		    	for (Iterator<?> it = list.iterator(); it.hasNext(); ) {
		    		IPicklistText text = (IPicklistText)it.next();
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
	public IPicklistEntry createPicklistEntry() {
		return new PicklistEntry();
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.model.daos.IPicklisteDAO#getPickListEntryByKey(java.lang.String, java.lang.String)
	 */
	public IPicklistEntry getPickListEntryByKey(String listKey, String key) {

		if (key == null) {
			throw new NullPointerException("key must not be null.");
		}
		
		EntityList list = getAllEntriesToList(listKey);
        //look in cache first...
		for (Iterator<?> it = list.iterator(); it.hasNext(); ) {
			IPicklistEntry entry = (IPicklistEntry)it.next();
			if (key.equals(entry.getKey())) {
				return entry;
			}
		}
		return null;
	}
}
