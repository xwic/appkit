package de.xwic.appkit.webbase.entityview;

import de.xwic.appkit.core.ApplicationData;
import de.xwic.appkit.core.dao.DAO;
import de.xwic.appkit.core.dao.DAOSystem;
import de.xwic.appkit.core.dao.EntityQuery;
import de.xwic.appkit.core.dao.IEntity;
import de.xwic.appkit.webbase.toolkit.components.ListModel;

/**
 * 
 * <a href="mailto:vzhovtiuk@gmail.com">Vitaliy Zhovtyuk</a>
 */
public class EntityDisplayListModel extends ListModel {

	private Class<? extends IEntity> entityClass;
	private IEntity baseEntity;

	private EntityQuery originalQuery;

	/**
	 * @param query
	 */
	public EntityDisplayListModel(EntityQuery query, Class<? extends IEntity> entityClass) {
		super(query);
		originalQuery = query;
		if (null == entityClass) {
			throw new RuntimeException("EntityClass attribute is not allowed to be null.");
		}

		this.entityClass = entityClass;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.xwic.appkit.webbase.toolkit.components.ListModel#canDelete()
	 */
	public boolean canDelete() {
		boolean hasRight = DAOSystem.getSecurityManager().hasRight(entityClass.getName(),
				ApplicationData.SECURITY_ACTION_DELETE);
		return hasRight;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.xwic.appkit.webbase.toolkit.components.ListModel#canEdit()
	 */
	public boolean canEdit() {
		boolean hasRight = DAOSystem.getSecurityManager().hasRight(entityClass.getName(),
				ApplicationData.SECURITY_ACTION_UPDATE);
		return hasRight;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.xwic.appkit.webbase.toolkit.components.ListModel#canNew()
	 */
	public boolean canNew() {
		boolean hasRight = DAOSystem.getSecurityManager().hasRight(entityClass.getName(),
				ApplicationData.SECURITY_ACTION_CREATE);
		return hasRight;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.xwic.appkit.webbase.toolkit.components.ListModel#getDAO()
	 */
	@Override
	public DAO getDAO() {
		return DAOSystem.findDAOforEntity(entityClass);
	}

	public void delete(int entityId) {
		IEntity entity = getDAO().getEntity(entityId);
		getDAO().softDelete(entity);
	}

	/**
	 * @return the entityClass
	 */
	public Class<? extends IEntity> getEntityClass() {
		return entityClass;
	}

	/**
	 * @param entityClass
	 *            the entityClass to set
	 */
	public void setEntityClass(Class<? extends IEntity> entityClass) {
		this.entityClass = entityClass;
	}

	/**
	 * @return the baseEntity
	 */
	public IEntity getBaseEntity() {
		return baseEntity;
	}

	/**
	 * @param baseEntity
	 *            the baseEntity to set
	 */
	public void setBaseEntity(IEntity baseEntity) {
		this.baseEntity = baseEntity;
	}

	/**
	 * @return the originalQuery
	 */
	public EntityQuery getOriginalQuery() {
		return originalQuery;
	}

	/**
	 * @param originalQuery
	 *            the originalQuery to set
	 */
	public void setOriginalQuery(EntityQuery originalQuery) {
		this.originalQuery = originalQuery;
	}

}
