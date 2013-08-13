/**
 *
 */
package de.xwic.appkit.core.model.util;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.xwic.appkit.core.dao.DAO;
import de.xwic.appkit.core.dao.DAOSystem;
import de.xwic.appkit.core.dao.IEntity;
import de.xwic.appkit.core.dao.Limit;
import de.xwic.appkit.core.dao.impl.hbn.HibernateUtil;
import de.xwic.appkit.core.model.queries.PropertyQuery;
import de.xwic.appkit.core.util.CollectionUtil;
import de.xwic.appkit.core.util.IEvaluator;

/**
 * @author Alexandru Bledea
 * @since Jul 1, 2013
 */
public class EntityUtil {

	public final static IEvaluator<IEntity, Integer> ENTITY_ID_EVALUATOR = new IEvaluator<IEntity, Integer>() {

		@Override
		public Integer evaluate(IEntity obj) {
			return obj.getId();
		}

	};

	/**
	 * @param collection
	 * @return
	 */
	public static <E extends IEntity> Set<Integer> getIds(Collection<E> collection) {
		return CollectionUtil.createCollection(collection, ENTITY_ID_EVALUATOR, new HashSet<Integer>());
	}

	/**
	 * @param entityClass
	 * @param id
	 * @return
	 */
	public static <E extends IEntity> E getEntity(Class<E> entityClass, Integer id) {
		return id == null ? null : (E) findDAO(entityClass).getEntity(id);
	}

	/**
	 * @param entityClass
	 * @return
	 */
	public static <E extends IEntity> E createEntity(Class<E> entityClass) {
		return (E) findDAO(entityClass).createEntity();
	}

	/**
	 * @param entityClass
	 * @return
	 */
	public static <E extends IEntity> String buildTitle(E entity) {
		return findDAO(entity.getClass()).buildTitle(entity);
	}

	/**
	 * @param entityClass
	 * @return
	 */
	public static <E extends IEntity> DAO findDAO(Class<E> entityClass) {
		return DAOSystem.findDAOforEntity(entityClass);
	}

	/**
	 * @param entity
	 * @return
	 */
	public static <E extends IEntity> E refreshEntity(E entity) {
		HibernateUtil.currentSession().refresh(entity);
		return entity;
	}

	/**
	 * @param entityClass
	 * @param limit
	 * @param query
	 * @return
	 */
	public static <E extends IEntity> List<E> getEntities(Class<E> entityClass, PropertyQuery query) {
		return getEntities(entityClass, null, query);
	}

	/**
	 * @param entityClass
	 * @param limit
	 * @param query
	 * @return
	 */
	public static <E extends IEntity> List<E> getEntities(Class<E> entityClass, Limit limit, PropertyQuery query) {
		return findDAO(entityClass).getEntities(limit, query);
	}

}
