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
/**
 *
 */
package de.xwic.appkit.core.model.util;

import static de.xwic.appkit.core.dao.DAOSystem.findDAOforEntity;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import de.xwic.appkit.core.ApplicationData;
import de.xwic.appkit.core.dao.DAOSystem;
import de.xwic.appkit.core.dao.EntityList;
import de.xwic.appkit.core.dao.IEntity;
import de.xwic.appkit.core.dao.IHistory;
import de.xwic.appkit.core.dao.Limit;
import de.xwic.appkit.core.dao.impl.hbn.HibernateUtil;
import de.xwic.appkit.core.dao.util.Entities;
import de.xwic.appkit.core.model.queries.PropertyQuery;
import de.xwic.appkit.core.util.CollectionUtil;
import de.xwic.appkit.core.util.Function;

/**
 * @author Alexandru Bledea
 * @since Jul 1, 2013
 */
public final class EntityUtil {

	@SuppressWarnings("unchecked")
	private static final Collection<Class<? extends IEntity>> INVALID_TYPES = Arrays.asList(IEntity.class, IHistory.class);

	public final static Function<IEntity, Long> ENTITY_ID_EVALUATOR = new Function<IEntity, Long>() {

		@Override
		public Long evaluate(final IEntity obj) {
			return obj.getId();
		}

	};

	/**
	 * @param collection
	 * @return
	 */
	public static <E extends IEntity> Set<Long> getIds(final Collection<E> collection) {
		return CollectionUtil.createSet(collection, ENTITY_ID_EVALUATOR);
	}

	/**
	 * @param entityClass
	 * @param id
	 * @return
	 */
	public static <E extends IEntity> E getEntity(final Class<E> entityClass, final Long id) {
		if (id == null || id.longValue() == Entities.NEW_ENTITY_ID){
			return null;
		}
		return findDAOforEntity(entityClass).getEntity(id.longValue());
	}

	/**
	 * @param entityClass
	 * @param id
	 * @return
	 */
	public static <E extends IEntity> E getOrCreateEntity(final Class<E> entityClass, final Long id) {
		if (id == null || id.longValue() < Entities.LOWEST_POSSIBLE_ID) {
			return createEntity(entityClass);
		}
		return getEntity(entityClass, id);
	}

	/**
	 * @param entityClass
	 * @return
	 */
	public static <E extends IEntity> E createEntity(final Class<E> entityClass) {
		return findDAOforEntity(entityClass).createEntity();
	}

	/**
	 * @param entityClass
	 * @return
	 */
	public static <E extends IEntity> String buildTitle(final E entity) {
		if (entity == null) {
			return "";
		}
		return findDAOforEntity(entity.getClass()).buildTitle(entity);
	}

	/**
	 * @deprecated only works if we use hibernate, maybe not such a good idea
	 * @param entity
	 * @return
	 */
	@Deprecated
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
	public static <E extends IEntity> EntityList<E> getEntities(final Class<E> entityClass, final PropertyQuery query) {
		return getEntities(entityClass, query, null);
	}

	/**
	 * @param entityClass
	 * @param limit
	 * @param query
	 * @return
	 */
	public static <E extends IEntity> EntityList<E> getEntities(final Class<E> entityClass, final PropertyQuery query, final Limit limit) {
		return findDAOforEntity(entityClass).getEntities(limit, query);
	}

	/**
	 * @param entityClass
	 * @param ids
	 * @return
	 */
	public static <E extends IEntity> List<E> getEntities(final Class<E> entityClass, final Collection<Long> ids) {
		if (CollectionUtil.isEmpty(ids)) {
			return Collections.emptyList();
		}
		final PropertyQuery pq = new PropertyQuery();
		pq.addIn("id", ids);
		return getEntities(entityClass, pq);
	}

	/**
	 * @param entity
	 * @return
	 */
	public static <E extends IEntity> E update(final E entity) {
		if (entity != null) {
			findDAOforEntity(entity.getClass()).update(entity);
		}
		return entity;
	}

	/**
	 * @param entities
	 * @return
	 */
	public static <E extends IEntity, C extends Collection<E>> C update(final C entities) {
		if (entities != null) {
			for (E entity : entities) {
				update(entity);
			}
		}
		return entities;
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
			for (Class<?> current : clasz.getInterfaces()) {
				if (isSatisfactoryEntityType(current)) {
					return (Class<? extends IEntity>) current;
				}
			}
			clasz = clasz.getSuperclass();
		}

		throw new IllegalStateException("Can't determine entity type for " + myClass);
	}

	/**
	 * @param clasz
	 * @return
	 */
	private static boolean isSatisfactoryEntityType(final Class<?> clasz) {
		if (!INVALID_TYPES.contains(clasz)) {
			return IEntity.class.isAssignableFrom(clasz);
		}
		return false;
	}

	/**
	 * Checks if the specified entity exists and is saved
	 * @param entity the entity to be checked
	 * @throws NullPointerException if the entity is null
	 * @throws IllegalStateException if the entity was not saved
	 */
	public static void checkEntityExists(final IEntity entity) throws NullPointerException, IllegalStateException {
		if (entity == null) {
			throw new NullPointerException("Entity argument is null!");
		}
		if (entity.getId() < Entities.LOWEST_POSSIBLE_ID) {
			throw new IllegalStateException("Entity argument is not saved!");
		}
	}

}
