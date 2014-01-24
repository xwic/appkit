/**
 *
 */
package de.xwic.appkit.core.model.util;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.xwic.appkit.core.ApplicationData;
import de.xwic.appkit.core.dao.DAO;
import de.xwic.appkit.core.dao.DAOSystem;
import de.xwic.appkit.core.dao.IEntity;
import de.xwic.appkit.core.dao.IHistory;
import de.xwic.appkit.core.dao.Limit;
import de.xwic.appkit.core.dao.impl.hbn.HibernateUtil;
import de.xwic.appkit.core.model.queries.PropertyQuery;
import de.xwic.appkit.core.util.CollectionUtil;
import de.xwic.appkit.core.util.ILazyEval;

/**
 * @author Alexandru Bledea
 * @since Jul 1, 2013
 */
public final class EntityUtil {

	public static final int NEW_ENTITY_ID = 0;
	public static final int LOWEST_POSSIBLE_ID = 1;

	private static final Collection<Class<? extends IEntity>> INVALID_TYPES =
			Collections.unmodifiableCollection(Arrays.asList(IEntity.class, IHistory.class));

	public final static ILazyEval<IEntity, Integer> ENTITY_ID_EVALUATOR = new ILazyEval<IEntity, Integer>() {

		@Override
		public Integer evaluate(final IEntity obj) {
			return obj.getId();
		}

	};

	/**
	 * @param collection
	 * @return
	 */
	public static <E extends IEntity> Set<Integer> getIds(final Collection<E> collection) {
		return CollectionUtil.createCollection(collection, ENTITY_ID_EVALUATOR, new HashSet<Integer>());
	}

	/**
	 * @param entityClass
	 * @param id
	 * @return
	 */
	public static <E extends IEntity> E getEntity(final Class<E> entityClass, final Integer id) {
		return id == null ? null : (E) findDAO(entityClass).getEntity(id);
	}

	/**
	 * @param entityClass
	 * @return
	 */
	public static <E extends IEntity> E createEntity(final Class<E> entityClass) {
		return (E) findDAO(entityClass).createEntity();
	}

	/**
	 * @param entityClass
	 * @return
	 */
	public static <E extends IEntity> String buildTitle(final E entity) {
		if (entity == null) {
			return "";
		}
		return findDAO(entity.getClass()).buildTitle(entity);
	}

	/**
	 * @param entityClass
	 * @return
	 */
	public static <E extends IEntity> DAO findDAO(final Class<E> entityClass) {
		return DAOSystem.findDAOforEntity(entityClass);
	}

	/**
	 * @param entity
	 * @return
	 */
	public static <E extends IEntity> E refreshEntity(final E entity) {
		if (entity != null) {
			HibernateUtil.currentSession().refresh(entity);
		}
		return entity;
	}

	/**
	 * @param entityClass
	 * @param query
	 * @return
	 */
	public static <E extends IEntity> List<E> getEntities(final Class<E> entityClass, final PropertyQuery query) {
		return getEntities(entityClass, null, query);
	}

	/**
	 * @param entityClass
	 * @param limit
	 * @param query
	 * @return
	 */
	public static <E extends IEntity> List<E> getEntities(final Class<E> entityClass, final Limit limit, final PropertyQuery query) {
		return findDAO(entityClass).getEntities(limit, query);
	}

	/**
	 * @param entity
	 * @return
	 */
	public static <E extends IEntity> E update(final E entity) {
		if (entity != null) {
			findDAO(entity.getClass()).update(entity);
		}
		return entity;
	}

	/**
	 * @param entityClass
	 * @param action
	 * @return
	 */
	public static <E extends IEntity> boolean hasRight(final Class<E> entityClass, final String action) {
		return DAOSystem.getSecurityManager().hasRight(entityClass.getName(), action);
	}

	/**
	 * @param entityClass
	 * @return
	 */
	public static <E extends IEntity> boolean canRead(final Class<E> entityClass) {
		return hasRight(entityClass, ApplicationData.SECURITY_ACTION_READ);
	}

	/**
	 * @param entityClass
	 * @return
	 */
	public static <E extends IEntity> boolean canUpdate(final Class<E> entityClass) {
		return hasRight(entityClass, ApplicationData.SECURITY_ACTION_UPDATE);
	}

	/**
	 * @param entityClass
	 * @return
	 */
	public static <E extends IEntity> boolean canDelete(final Class<E> entityClass) {
		return hasRight(entityClass, ApplicationData.SECURITY_ACTION_DELETE);
	}

	/**
	 * @param myClass
	 * @return
	 * @throws IllegalStateException if no type can be found
	 */
	public static Class<? extends IEntity> type(final Class<?> myClass) throws IllegalStateException {
		if (myClass != null && myClass.isInterface()) {
			if (isSatisfactoryEntityType(myClass)) {
				return (Class<? extends IEntity>) myClass;
			}
		}
		return oldType(myClass);
	}

	/**
	 * @param entity
	 * @return
	 * @throws IllegalStateException if no type can be found
	 */
	public static Class<? extends IEntity> type(final IEntity entity) throws IllegalStateException {
		Class<?> c = null;
		if (entity != null) {
			c = entity.getClass();
		}
		return oldType(c);
	}

	/**
	 * @param myClass
	 * @return
	 * @throws IllegalStateException if no type can be found
	 */
	public static Class<? extends IEntity> oldType(final Class<?> myClass) throws IllegalStateException {

		// this method makes a 'guess' what type of entity it is by
		// iterating through all interfaces the instance implements
		// and returns the first interface that is not IEntity or IHistory,
		// but extends IEntity
		Class<?> clasz = myClass;
		while (clasz != null) {
			Class<?>[] interfaces = clasz.getInterfaces();
			for (Class<?> current : interfaces) {
				if (isSatisfactoryEntityType(current)) {
					return (Class<? extends IEntity>) current;
				}
			}
			clasz = clasz.getSuperclass();
		}

		throw new IllegalStateException("Can't determine entity type.");
	}

	/**
	 * @param clasz
	 * @return
	 */
	private static boolean isSatisfactoryEntityType(final Class clasz) {
		if (!INVALID_TYPES.contains(clasz)) {
			return IEntity.class.isAssignableFrom(clasz);
		}
		return false;
	}
}
