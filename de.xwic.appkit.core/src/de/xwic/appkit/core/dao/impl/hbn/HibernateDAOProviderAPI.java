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
 * de.xwic.appkit.core.model.impl.AbstractHibernateDAOProvider
 * Created on 05.04.2005
 *
 */
package de.xwic.appkit.core.dao.impl.hbn;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.exception.GenericJDBCException;

import de.xwic.appkit.core.config.ConfigurationManager;
import de.xwic.appkit.core.config.model.EntityDescriptor;
import de.xwic.appkit.core.config.model.Model;
import de.xwic.appkit.core.config.model.Property;
import de.xwic.appkit.core.dao.DAO;
import de.xwic.appkit.core.dao.DAOProviderAPI;
import de.xwic.appkit.core.dao.DAOSystem;
import de.xwic.appkit.core.dao.DataAccessException;
import de.xwic.appkit.core.dao.Entity;
import de.xwic.appkit.core.dao.EntityKey;
import de.xwic.appkit.core.dao.EntityList;
import de.xwic.appkit.core.dao.EntityQuery;
import de.xwic.appkit.core.dao.IEntity;
import de.xwic.appkit.core.dao.IEntityQueryResolver;
import de.xwic.appkit.core.dao.IHistory;
import de.xwic.appkit.core.dao.Limit;
import de.xwic.appkit.core.model.entities.IPicklistText;
import de.xwic.appkit.core.model.queries.PropertyQuery;
import de.xwic.appkit.core.security.IUser;
import de.xwic.appkit.core.trace.ITraceOperation;
import de.xwic.appkit.core.trace.Trace;

/**
 * Base implementation of a DAOProvider that is using hibernate.
 * 
 * @author Florian Lippisch
 */
public class HibernateDAOProviderAPI implements DAOProviderAPI {
	
	/** active hibernate session */
	protected Session session;
	/** parent provider */
	protected HibernateDAOProvider provider;
	
	/**
	 * Constructor.
	 * @param provider
	 * @param session
	 */
	public HibernateDAOProviderAPI(HibernateDAOProvider provider, Session session) {
		this.provider = provider;
		this.session = session;
	}
	
    /* (non-Javadoc)
     * @see de.xwic.appkit.core.dao.DAOProvider#getEntity(int)
     */
    public IEntity getEntity(Class<? extends Object> clazz, int id) throws DataAccessException {
        
        return (IEntity) session.get(clazz, new Integer(id));
        
    }
    

    /* (non-Javadoc)
     * @see de.xwic.appkit.core.dao.DAOProvider#remove(de.xwic.appkit.core.dao.IEntity)
     */
    public void delete(IEntity entity) throws DataAccessException {
    	//System.out.println("DELETE " + entity + " #" + entity.getId() + " - ver: " + entity.getVersion());
        session.delete(entity);
    }

    /* (non-Javadoc)
     * @see de.xwic.appkit.core.dao.DAOProvider#update(de.xwic.appkit.core.dao.IEntity)
     */
    public void update(IEntity entity) throws DataAccessException {
        
    	IUser user = DAOSystem.getSecurityManager().getCurrentUser(); 
    	String userName = "<no-user>";
    	if (user != null) {
    		// FLI: always use logon name now, as its the unique identifier.
    		userName = user.getLogonName(); // user.getName() == null ? user.getLogonName() : user.getName();
    	}
    	
    	
        //set the common properties before saving the object again or at first time
        Entity e = (Entity)entity;
        Date date = new Date();
        
        //history is always new, so ignore these objects
        if (e.getId() == 0 && (!(entity instanceof IHistory))) {
            //if it is new, set these properties...
            e.setCreatedAt(date);
            e.setCreatedFrom(userName);
        }
        
        //more common properties... 
        e.setLastModifiedAt(date);
        e.setLastModifiedFrom(userName);
        
//        try {
//        	throw new RuntimeException("saveOrUpdate: " + e.getClass().getName());
//        } catch (Exception ex) {
//        	ex.printStackTrace();
//        }
       
        session.saveOrUpdate(e);
    }

    /* (non-Javadoc)
     * @see de.xwic.appkit.core.dao.DAOProvider#getEntities(de.xwic.appkit.core.dao.Limit)
     */
    public EntityList getEntities(Class<? extends Object> clazz, Limit limit) {
        return getEntities(clazz, limit, null);
    }

    /*// FLI: Deactivated as this references to the wrong hibernate version!
    public String getSql(Query query) {
    	String hql = query.getQueryString();
    	SessionFactory sf = session.getSessionFactory();
		SessionFactoryImpl sfimpl = (SessionFactoryImpl) sf; // hack - to get to the actual queries..
		StringBuffer str = new StringBuffer(256);
		String s = null;
		HQLQueryPlan plan = new HQLQueryPlan(hql, false, Collections.EMPTY_MAP, sfimpl);

		QueryTranslator[] translators = plan.getTranslators();
		for (int i = 0; i < translators.length; i++) {
			QueryTranslator translator = translators[i];
			if(translator.isManipulationStatement()) {
				Iterator<?> iterator = translator.getQuerySpaces().iterator();
				while ( iterator.hasNext() ) {
					Object qspace = iterator.next();
					str.append(qspace);
					if(iterator.hasNext()) { str.append(", "); } //$NON-NLS-1$
				}

			} else {
				Type[] returnTypes = translator.getReturnTypes();
				str.append(i +": "); //$NON-NLS-1$
				for (int j = 0; j < returnTypes.length; j++) {
					Type returnType = returnTypes[j];
					str.append(returnType.getName());
					if(j<returnTypes.length-1) { str.append(", "); }							 //$NON-NLS-1$
				}
			}
			str.append("\n-----------------\n"); //$NON-NLS-1$
			Iterator<?> sqls = translator.collectSqlStrings().iterator();
			while ( sqls.hasNext() ) {
				String sql = (String) sqls.next();
				if (s == null) {
					s = sql;
				}
				str.append(sql);
				str.append("\n\n");	 //$NON-NLS-1$
			}
		}
		return s;

    }
    */
    
	public Query getQuery(Class<? extends Object> clazz, EntityQuery query) {
    	Query q;
    	IEntityQueryResolver resolver = null;
    	if (query != null) {
        	resolver = provider.getResolver(query.getClass());
    		Object result = resolver.resolve(clazz, query, false);
    		if (result instanceof Query) {
    			q = (Query)result;
    		} else if (result instanceof String){
    			q = session.createQuery((String)result);
    		} else {
    			throw new IllegalArgumentException("Resolver class " + resolver.getClass() + " not supported");
    		}
    	} else {
    		q = session.createQuery("from " + clazz.getName() );
    	}
        
    	return q;
	}

	/* (non-Javadoc)
     * @see de.xwic.appkit.core.dao.DAOProvider#getEntities(de.xwic.appkit.core.dao.Limit, de.xwic.appkit.core.dao.EntityFilter)
     */
    @SuppressWarnings("unchecked")
	public EntityList getEntities(Class<? extends Object> clazz, Limit limit, EntityQuery query) {

    	ITraceOperation traceOp = null;
    	if (Trace.isEnabled()) { 
    		traceOp = Trace.startOperation(DAO.TRACE_CAT);
    	}
    	try {
	    	Query q = getQuery(clazz, query);
	    	
	    	IEntityQueryResolver resolver = null;
	    	if (query != null) {
	        	resolver = provider.getResolver(query.getClass());
	    	}
	    	
	    	if (limit != null) {
	            q.setFirstResult(limit.startNo);
	            if (limit.maxResults != 0) {
	            	q.setMaxResults(limit.maxResults);
	            }
	        }
	        
	    	if (traceOp != null) {
	    		traceOp.setInfo("getEntities ('" + clazz.getName() + "', " + limit + ", '" + q.getQueryString() + "')");
	    	}
	    	
	        List<Object> result = null;
	        try {
	            result = q.list();
	        } catch (GenericJDBCException e) {
	        	//figure out the message from SQL, that is better for the user
	        	SQLException se = e.getSQLException();
	        	throw new DataAccessException(se.getMessage(), se);
	        }
	        
	        List<String> columns = query != null ? query.getColumns() : null;
	        if (columns != null) {
	        	//long l = System.currentTimeMillis();
	        	result = resolveColumns(result, query);
	        	//System.out.println("RESOLVE TIME: " + (System.currentTimeMillis() - l));
	        }
	        
	        int count = result.size();
	        if (limit != null && result.size() != 0) {
	        	if (resolver != null) {
		    		Object rq = resolver.resolve(clazz, query, true);
		    		if (rq instanceof Query) {
		    			q = (Query)rq;
		    		} else {
		    			q = session.createQuery((String)rq);
		    		}
	        	} else {
	        		q = session.createQuery("select count(*) from " + clazz.getName());
	        	}
	        	Object obj = q.list().get(0);
	        	
	        	if (obj instanceof Integer) {
	        		count = ((Integer) obj).intValue();
	        	} 
	        	else if (obj instanceof Long) {
	        		count = ((Long) obj).intValue();
	        	} 
	        	else {
	        		throw new RuntimeException("The requested object type for count request is NOT long or int! Please check " + getClass().getName());
	        	}
	        }
	
	        // check that entity is returned and not arrays: use first object in array
	        // if query null assume entity check, FIXME might break existing logic
	        if ((query == null || query.isReturnEntity()) && result.size() > 0) {
	        	Object obj = result.get(0);
	        	if (obj != null && obj.getClass().isArray()) {
	        		for (int i = 0; i < result.size(); i++) {
		        		Object[] obj_array= (Object[])result.get(i);
		        		obj = null;
		        		if (obj_array != null && obj_array.length > 0) {
		        			obj = obj_array[0];
		        		}
		        		result.set(i, obj);
	        		}        		
	        	}
	        }
	        
	        return new EntityList(result, limit, count);
	        
    	} finally {
    		if (traceOp != null) {
    			traceOp.finished();
    		}
    	}
        
    }
    
    /* (non-Javadoc)
     * @see de.xwic.appkit.core.dao.DAOProviderAPI#getCollectionProperty(java.lang.Class, int, java.lang.String)
     */
	@Override
    public Collection<?> getCollectionProperty(Class<? extends IEntity> entityImplClass, int entityId, String propertyId) {
    	
    	String hql = "select elements(obj." + propertyId + ") from " + entityImplClass.getName() + " obj where obj.id = ?";
    	Query query = session.createQuery(hql);
    	query.setParameter(0, entityId);
    	
    	List<?> result = query.list();
     	return result;
    }

	/**
	 * 
	 * @param result
	 * @param query
	 * @return
	 */
	private List<Object> resolveColumns(List<Object> result, EntityQuery query) {
		
		if (result.size() == 0) {
			return result;
		}
		
		Object obj = result.get(0);
		if (!(obj instanceof IEntity)) {
			return result;
		}
		
		List<String> cols = query.getColumns();
		List<Object> rows = new ArrayList<Object>(result.size());
		PropertyResolver[] resolver = new PropertyResolver[cols.size()];
		for (int i = 0; i < cols.size(); i++) {
			resolver[i] = new PropertyResolver((String)cols.get(i));
		}
		int colSize = cols.size();
		for (Iterator<Object> it = result.iterator(); it.hasNext(); ) {
			IEntity entity = (IEntity)it.next();
			Object[] row = new Object[cols.size() + 1];
			row[0] = new Integer(entity.getId());
			for (int i = 0; i < colSize; i++) {
				Object data = resolver[i].getData(entity);
				row[i + 1] = data;
			}
			rows.add(row);
		}
		
		return rows;
	}

	/**
	 * Returns the session.
	 * @return
	 */
	public Session getSession() {
		return session;
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.dao.DAOProviderAPI#softDelete(de.xwic.appkit.core.dao.IEntity)
	 */
	@SuppressWarnings("unchecked")
	public void softDelete(IEntity entity) throws DataAccessException {

		// build a list of possible references
		List<Property> refList = new ArrayList<Property>();
		Set<Object> toDelete = new HashSet<Object>();
		toDelete.add(new EntityKey(entity));
		
		Class<? extends IEntity> type = entity.type();
		List<Model> models = ConfigurationManager.getSetup().getModels();
		for (Iterator<Model> itM = models.iterator(); itM.hasNext(); ) {
			Model model = itM.next();
			for (Iterator<String> itE = model.getManagedEntities().iterator(); itE.hasNext(); ) {
				String entityType = itE.next();
				if (!entityType.equals(IPicklistText.class.getName())) {
					EntityDescriptor descr = model.getEntityDescriptor(entityType);
					if (!descr.isHidden()) {
						for (Iterator<Property> itP = descr.getProperties().values().iterator(); itP.hasNext(); ) {
							Property prop = itP.next();
							if (prop.getDescriptor().getPropertyType().equals(type) &&
									prop.getOnRefDeletion() != Property.IGNORE) {
								refList.add(prop);
							}
						}
					}
				}
			}
		}
		
		List<String> badRefList = new ArrayList<String>();
		
		Integer intEntityId = new Integer(entity.getId());
		// iterate through references (and check them)
		for (Iterator<Property> it = refList.iterator(); it.hasNext(); ) {
			Property prop = it.next();
			// must get the right DAO to query
			DAO dao = DAOSystem.findDAOforEntity(prop.getEntityDescriptor().getClassname());
			PropertyQuery query = new PropertyQuery();
			query.addEquals(prop.getName(), intEntityId);
			
			List<IEntity> result = dao.getEntities(null, query, false);
			
			// handle results
			for (Iterator<IEntity> itR = result.iterator(); itR.hasNext(); ) {
				IEntity refE = (IEntity)itR.next();
				
				String info = prop.getEntityDescriptor().getClassname() + ";" + prop.getName() + ";" + refE.getId() + ";" + dao.buildTitle(refE);
				switch (prop.getOnRefDeletion()) {
					case Property.DENY : {
						if (!toDelete.contains(new EntityKey(refE))) {
							// add if the entity is not already in the list of entities to be deleted.
							badRefList.add(info);
						}
						break;
					}
					case Property.DELETE : {
						if (badRefList.size() == 0) {				
							try {
								dao.softDelete(refE);
								toDelete.add(refE);
							} catch (DataAccessException dae) {
								if (dae.getMessage().equals("softdelete.hasref")) {
									badRefList.addAll((Collection<? extends String>)dae.getInfo());
									badRefList.add(info);
								} else {
									throw dae;
								}
							}
						}
						break;
					}
					case Property.CLEAR_REFERENCE : {
						if (badRefList.size() == 0 && !toDelete.contains(new EntityKey(refE))) {				
							try {
								PropertyDescriptor propDesc = new PropertyDescriptor(prop.getName(), refE.getClass());
								Method mWrite = propDesc.getWriteMethod();
								mWrite.invoke(refE, new Object[] { null });
								dao.update(refE);
							} catch (Exception e) {
								badRefList.add(info);
							}
						}
						break;
					}
				}
				
			}
			
		}
		
		if (badRefList.size() > 0) {
			DataAccessException dae = new DataAccessException("softdelete.hasref");
			dae.setInfo(badRefList);
			throw dae;
		}
		
		// now modify the entity using its DAO to make sure that
		// history informations are logged when changing the entity.
		
		DAO myDao = DAOSystem.findDAOforEntity(type);
		Entity e = (Entity)entity;
		e.setDeleted(true);
		e.setLastModifiedAt(new Date());
		myDao.update(e);	
		
		
	}

	
}
