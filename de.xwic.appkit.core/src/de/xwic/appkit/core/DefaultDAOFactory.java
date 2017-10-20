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
 * de.xwic.appkit.core.model.daos.CRMDefaultDAOFactory
 * Created on 07.07.2005
 *
 */
package de.xwic.appkit.core;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import de.xwic.appkit.core.dao.DAO;
import de.xwic.appkit.core.dao.DAOFactory;
import de.xwic.appkit.core.dao.DAOProvider;
import de.xwic.appkit.core.dao.DataAccessException;
import de.xwic.appkit.core.dao.EntityQuery;
import de.xwic.appkit.core.dao.IEntityQueryResolver;
import de.xwic.appkit.core.model.daos.IAnhangDAO;
import de.xwic.appkit.core.model.daos.IEntityChangeLogDAO;
import de.xwic.appkit.core.model.daos.IEntityCommentDAO;
import de.xwic.appkit.core.model.daos.IMitarbeiterDAO;
import de.xwic.appkit.core.model.daos.IMitarbeiterRelationDAO;
import de.xwic.appkit.core.model.daos.IMonitoringElementDAO;
import de.xwic.appkit.core.model.daos.INewsDAO;
import de.xwic.appkit.core.model.daos.IPicklisteDAO;
import de.xwic.appkit.core.model.daos.IQuickLaunchDAO;
import de.xwic.appkit.core.model.daos.IReportFolderDAO;
import de.xwic.appkit.core.model.daos.IReportTemplateDAO;
import de.xwic.appkit.core.model.daos.ISalesTeamDAO;
import de.xwic.appkit.core.model.daos.IServerConfigPropertyDAO;
import de.xwic.appkit.core.model.daos.ISyncStateDAO;
import de.xwic.appkit.core.model.daos.ISystemTraceStatisticDAO;
import de.xwic.appkit.core.model.daos.IUserListProfileDAO;
import de.xwic.appkit.core.model.daos.IUserViewConfigurationDAO;
import de.xwic.appkit.core.model.daos.impl.AnhangDAO;
import de.xwic.appkit.core.model.daos.impl.EntityChangeLogDAO;
import de.xwic.appkit.core.model.daos.impl.EntityCommentDAO;
import de.xwic.appkit.core.model.daos.impl.MitarbeiterDAO;
import de.xwic.appkit.core.model.daos.impl.MitarbeiterRelationDAO;
import de.xwic.appkit.core.model.daos.impl.MonitoringElementDAO;
import de.xwic.appkit.core.model.daos.impl.NewsDAO;
import de.xwic.appkit.core.model.daos.impl.PicklisteDAO;
import de.xwic.appkit.core.model.daos.impl.QuickLaunchDAO;
import de.xwic.appkit.core.model.daos.impl.ReportFolderDAO;
import de.xwic.appkit.core.model.daos.impl.ReportTemplateDAO;
import de.xwic.appkit.core.model.daos.impl.SalesTeamDAO;
import de.xwic.appkit.core.model.daos.impl.ServerConfigPropertyDAO;
import de.xwic.appkit.core.model.daos.impl.SyncStateDAO;
import de.xwic.appkit.core.model.daos.impl.SystemTraceStatisticDAO;
import de.xwic.appkit.core.model.daos.impl.UserListProfileDAO;
import de.xwic.appkit.core.model.daos.impl.UserViewConfigurationDAO;
import de.xwic.appkit.core.model.daos.impl.local.LocalAnhangDAO;
import de.xwic.appkit.core.model.daos.impl.local.LocalMitarbeiterDAO;
import de.xwic.appkit.core.model.daos.impl.local.LocalPicklisteDAO;
import de.xwic.appkit.core.model.daos.impl.local.LocalSalesTeamDAO;
import de.xwic.appkit.core.model.queries.AllUNBetreuerQuery;
import de.xwic.appkit.core.model.queries.CMFastSearchQuery;
import de.xwic.appkit.core.model.queries.HsqlQuery;
import de.xwic.appkit.core.model.queries.PicklistEntryQuery;
import de.xwic.appkit.core.model.queries.PicklistQuery;
import de.xwic.appkit.core.model.queries.PicklistTextQuery;
import de.xwic.appkit.core.model.queries.PropertyQuery;
import de.xwic.appkit.core.model.queries.SetRelatedQuery;
import de.xwic.appkit.core.model.queries.UniqueSTQuery;
import de.xwic.appkit.core.model.queries.resolver.hbn.AllUNBetreuerQueryResolver;
import de.xwic.appkit.core.model.queries.resolver.hbn.CMQueryShortFilterResolver;
import de.xwic.appkit.core.model.queries.resolver.hbn.HsqlQueryResolver;
import de.xwic.appkit.core.model.queries.resolver.hbn.PickListQueryResolver;
import de.xwic.appkit.core.model.queries.resolver.hbn.PicklistEntryQueryResolver;
import de.xwic.appkit.core.model.queries.resolver.hbn.PicklistTextQueryResolver;
import de.xwic.appkit.core.model.queries.resolver.hbn.PropertyQueryResolver;
import de.xwic.appkit.core.model.queries.resolver.hbn.SetRelatedQueryResolver;
import de.xwic.appkit.core.model.queries.resolver.hbn.UniqueSTQueryResolver;
import de.xwic.appkit.core.security.daos.IActionDAO;
import de.xwic.appkit.core.security.daos.IActionSetDAO;
import de.xwic.appkit.core.security.daos.IRightDAO;
import de.xwic.appkit.core.security.daos.IRoleDAO;
import de.xwic.appkit.core.security.daos.IScopeDAO;
import de.xwic.appkit.core.security.daos.IUserDAO;
import de.xwic.appkit.core.security.daos.IUserSessionDAO;
import de.xwic.appkit.core.security.daos.impl.ActionDAO;
import de.xwic.appkit.core.security.daos.impl.ActionSetDAO;
import de.xwic.appkit.core.security.daos.impl.RightDAO;
import de.xwic.appkit.core.security.daos.impl.RoleDAO;
import de.xwic.appkit.core.security.daos.impl.ScopeDAO;
import de.xwic.appkit.core.security.daos.impl.UserDAO;
import de.xwic.appkit.core.security.daos.impl.UserSessionDAO;
import de.xwic.appkit.core.security.daos.impl.local.LocalActionDAO;
import de.xwic.appkit.core.security.daos.impl.local.LocalActionSetDAO;
import de.xwic.appkit.core.security.daos.impl.local.LocalRightDAO;
import de.xwic.appkit.core.security.daos.impl.local.LocalRoleDAO;
import de.xwic.appkit.core.security.daos.impl.local.LocalScopeDAO;
import de.xwic.appkit.core.security.daos.impl.local.LocalUserDAO;
import de.xwic.appkit.core.security.queries.RightQuery;
import de.xwic.appkit.core.security.queries.UniqueActionQuery;
import de.xwic.appkit.core.security.queries.UniqueActionSetQuery;
import de.xwic.appkit.core.security.queries.UniqueRightQuery;
import de.xwic.appkit.core.security.queries.UniqueRoleQuery;
import de.xwic.appkit.core.security.queries.UniqueScopeQuery;
import de.xwic.appkit.core.security.queries.UniqueUserQuery;
import de.xwic.appkit.core.security.queries.UserQuery;
import de.xwic.appkit.core.security.queries.resolver.hbn.RightQueryResolver;
import de.xwic.appkit.core.security.queries.resolver.hbn.UniqueActionResolver;
import de.xwic.appkit.core.security.queries.resolver.hbn.UniqueActionSetResolver;
import de.xwic.appkit.core.security.queries.resolver.hbn.UniqueRightResolver;
import de.xwic.appkit.core.security.queries.resolver.hbn.UniqueRoleResolver;
import de.xwic.appkit.core.security.queries.resolver.hbn.UniqueScopeResolver;
import de.xwic.appkit.core.security.queries.resolver.hbn.UniqueUserResolver;
import de.xwic.appkit.core.security.queries.resolver.hbn.UserQueryResolver;

/**
 * DAO Factory for CRM default DAOs. This factory implements a simple map with
 * all default DAOs. This factory might be replaced by a component container oriented
 * implementation.
 *  
 * @author Florian Lippisch
 */
public class DefaultDAOFactory implements DAOFactory {

	private Map<Class<? extends DAO>, DAO> daos = new HashMap<Class<? extends DAO>, DAO>();
	private Map<String, DAO> entityDAOMap = new HashMap<String, DAO>();
	
	private DAOProvider provider;
	
	/**
	 * Default contstructor. Setup the list of available DAOs.
	 *
	 */
	public DefaultDAOFactory(DAOProvider provider) {
		this(provider, true);
	}		
	
	/**
	 * Construct a new DAO Factory.
	 * @param provider
	 * @param useLocalDAO
	 */
	public DefaultDAOFactory(DAOProvider provider, boolean useLocalDAO) {
		if (provider == null) {
			throw new IllegalArgumentException("Provider in " + getClass().getName() + " is null!");
		}
		this.provider = provider;

		ServerConfigPropertyDAO propDao = new ServerConfigPropertyDAO();
		propDao.setProvider(provider);
		daos.put(IServerConfigPropertyDAO.class, propDao);
		
		PicklisteDAO plDao = useLocalDAO ? new LocalPicklisteDAO() : new PicklisteDAO();
		plDao.setProvider(provider);
		daos.put(IPicklisteDAO.class, plDao);

		AnhangDAO ahDao = useLocalDAO ? new LocalAnhangDAO() : new AnhangDAO();
		ahDao.setProvider(provider);
		daos.put(IAnhangDAO.class, ahDao);
		
		SyncStateDAO ssDao = new SyncStateDAO();
		ssDao.setProvider(provider);
		daos.put(ISyncStateDAO.class, ssDao);
		
		ReportTemplateDAO rtDao = new ReportTemplateDAO();
		rtDao.setProvider(provider);
		daos.put(IReportTemplateDAO.class, rtDao);

		ReportFolderDAO rfDao = new ReportFolderDAO();
		rfDao.setProvider(provider);
		daos.put(IReportFolderDAO.class, rfDao);
		
		MonitoringElementDAO meDao = new MonitoringElementDAO();
		meDao.setProvider(provider);
		daos.put(IMonitoringElementDAO.class, meDao);
		
		SystemTraceStatisticDAO stsDAO = new SystemTraceStatisticDAO();
		stsDAO.setProvider(provider);
		daos.put(ISystemTraceStatisticDAO.class, stsDAO);
		
        provider.registerQuery(PicklistTextQuery.class, new PicklistTextQueryResolver());
        provider.registerQuery(PicklistEntryQuery.class, new PicklistEntryQueryResolver());
        provider.registerQuery(PicklistQuery.class, new PickListQueryResolver());
        provider.registerQuery(HsqlQuery.class, new HsqlQueryResolver());
        provider.registerQuery(SetRelatedQuery.class, new SetRelatedQueryResolver());

		// security DAOs
		
		ActionDAO actionDAO = useLocalDAO ? new LocalActionDAO() : new ActionDAO();
		actionDAO.setProvider(provider);
		daos.put(IActionDAO.class, actionDAO);
		
		ActionSetDAO actionSetDAO = useLocalDAO ? new LocalActionSetDAO() : new ActionSetDAO();
		actionSetDAO.setProvider(provider);
		daos.put(IActionSetDAO.class, actionSetDAO);
		
		RightDAO rightDAO = useLocalDAO ? new LocalRightDAO() : new RightDAO();
		rightDAO.setProvider(provider);
		daos.put(IRightDAO.class, rightDAO);
		
		RoleDAO roleDAO = useLocalDAO ? new LocalRoleDAO() : new RoleDAO();
		roleDAO.setProvider(provider);
		daos.put(IRoleDAO.class, roleDAO);
		
		ScopeDAO scopeDAO = useLocalDAO ? new LocalScopeDAO() : new ScopeDAO();
		scopeDAO.setProvider(provider);
		daos.put(IScopeDAO.class, scopeDAO);
		
		UserDAO userDAO = useLocalDAO ? new LocalUserDAO() : new UserDAO();
		userDAO.setProvider(provider);
		daos.put(IUserDAO.class, userDAO);
		
		UserSessionDAO userSessionDAO = new UserSessionDAO();
		userSessionDAO.setProvider(provider);
		daos.put(IUserSessionDAO.class, userSessionDAO);
		
		EntityCommentDAO ecDAO = new EntityCommentDAO();
		ecDAO.setProvider(provider);
		daos.put(IEntityCommentDAO.class, ecDAO);
		
		MitarbeiterDAO cmDao = new MitarbeiterDAO();
		registerDao(IMitarbeiterDAO.class, useLocalDAO ? new LocalMitarbeiterDAO() : cmDao);
		
		SalesTeamDAO stDao = useLocalDAO ? new LocalSalesTeamDAO() : new SalesTeamDAO();
		registerDao(ISalesTeamDAO.class, stDao);
		
		registerDao(IMitarbeiterRelationDAO.class, new MitarbeiterRelationDAO());
		
		registerDao(IUserListProfileDAO.class, new UserListProfileDAO());
		registerDao(IUserViewConfigurationDAO.class, new UserViewConfigurationDAO());
		registerDao(IEntityChangeLogDAO.class, new EntityChangeLogDAO());
		registerDao(INewsDAO.class, new NewsDAO());
		registerDao(IQuickLaunchDAO.class, new QuickLaunchDAO());
		
        provider.registerQuery(UserQuery.class, new UserQueryResolver());
        provider.registerQuery(RightQuery.class, new RightQueryResolver());
        provider.registerQuery(UniqueActionQuery.class, new UniqueActionResolver());
        provider.registerQuery(UniqueActionSetQuery.class, new UniqueActionSetResolver());
        provider.registerQuery(UniqueRightQuery.class, new UniqueRightResolver());
        provider.registerQuery(UniqueRoleQuery.class, new UniqueRoleResolver());
        provider.registerQuery(UniqueScopeQuery.class, new UniqueScopeResolver());
        provider.registerQuery(UniqueUserQuery.class, new UniqueUserResolver());
        
        provider.registerQuery(PropertyQuery.class, new PropertyQueryResolver());
        
        provider.registerQuery(AllUNBetreuerQuery.class, new AllUNBetreuerQueryResolver());
        provider.registerQuery(UniqueSTQuery.class, new UniqueSTQueryResolver());
        provider.registerQuery(CMFastSearchQuery.class, new CMQueryShortFilterResolver());

	}
	
	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.dao.DAOFactory#getDAO(java.lang.Class)
	 */
	public DAO getDAO(Class<? extends DAO> daoClass) {
		return daos.get(daoClass);
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.dao.DAOFactory#findDAOforEntity(java.lang.Class)
	 */
	public DAO findDAOforEntity(String entityClass) {
		
		DAO dao = entityDAOMap.get(entityClass);
		if (dao == null) {
			for (Iterator<DAO> it = daos.values().iterator(); it.hasNext();) {
				DAO daoTmp = it.next();
				if (daoTmp.handlesEntity(entityClass)) {
					dao = daoTmp;
					break;
				}
			}
			if (dao == null) {
				throw new DataAccessException("Can't find DAO for entity class " + entityClass);
			}
			entityDAOMap.put(entityClass, dao);
		}
		return dao;
	}
	
	/**
	 * Returns the map of daos.
	 * @return
	 */
	public Map<Class<? extends DAO>, DAO> getDAOMap() {
		return daos;
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.IDAOFactoryAdmin#registerDao(de.xwic.appkit.core.dao.DAO)
	 */
	public <D extends DAO<?>> void registerDao(Class<D> clazz, D dao) {
		dao.setProvider(provider);
		daos.put(clazz, dao);
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.IDAOFactoryAdmin#registerQuery(java.lang.Class, de.xwic.appkit.core.model.queries.resolver.hbn.QueryResolver)
	 */
	public void registerQuery(Class<? extends EntityQuery> queryClass, IEntityQueryResolver resolver) {
		provider.registerQuery(queryClass, resolver);
	}

}
