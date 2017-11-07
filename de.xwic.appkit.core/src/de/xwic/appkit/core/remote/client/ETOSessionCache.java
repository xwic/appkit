/**
 * 
 */
package de.xwic.appkit.core.remote.client;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import de.xwic.appkit.core.dao.DAO;
import de.xwic.appkit.core.dao.DAOSystem;
import de.xwic.appkit.core.dao.EntityKey;
import de.xwic.appkit.core.dao.IEntity;
import de.xwic.appkit.core.transfer.EntityTransferObject;
import de.xwic.appkit.core.transfer.PropertyValue;

/**
 * Simple Thread Local cache implementation for the ETOs on client side to be cached.
 * 
 * @author dotto
 */
public class ETOSessionCache {

	private static ETOSessionCache instance = new ETOSessionCache();
	private boolean isClientSystem;

	private static ThreadLocal<Map<EntityKey, EntityTransferObject>> tlCaches = new ThreadLocal<>();

	/*
	 * 
	 */
	private ETOSessionCache() {

	}

	/**
	 * @return
	 */
	public static ETOSessionCache getInstance() {
		return instance;
	}

	/**
	 * 
	 */
	public void initCache() {
		Map<EntityKey, EntityTransferObject> cache = new HashMap<>();
		tlCaches.set(cache);
	}

	/**
	 * 
	 */
	public void closeCache() {
		tlCaches.set(null);
	}

	
	/**
	 * Does refresh given entity and also makes an update on cache and replaces the entity on all references
	 * @param entity
	 * @return refreshed entity
	 */
	public <T extends IEntity> T refreshEntity(T entity) {
		DAO<?> dao = DAOSystem.findDAOforEntity(entity.type());
		EntityKey key = new EntityKey(entity);
		Map<EntityKey, EntityTransferObject> sessionCache = getSessionCache();
		boolean existedInCache = sessionCache.remove(key) != null;
		T refreshed = (T) dao.getEntity(entity.getId());

		if (existedInCache) {
			// the ETO got added back in the cache by the dao.getEntity method
			EntityTransferObject refreshedEto = sessionCache.get(key); 
			refreshReferencesInCache(refreshed, refreshedEto, key);
		}
		return refreshed;
	}
	
	/**
	 * 
	 * @param entity
	 * @param key
	 */
	public void refreshReferencesInCache(IEntity refreshedEntity, EntityTransferObject refreshedEto, EntityKey key){
		Class<? extends IEntity> entityType = refreshedEntity.type();
		for (EntityTransferObject eto : getSessionCache().values()) {
			for (PropertyValue pv : eto.getPropertyValues().values()) {
				if (pv.isEntityType() && entityType.equals(pv.getType()) && pv.getEntityId() == refreshedEntity.getId()) {
					if (pv.isLoaded()) {
						if (pv.getValue() instanceof EntityTransferObject) {
							pv.setValue(refreshedEto);
						} else if (pv.getValue() instanceof IEntity) {
							pv.setValue(refreshedEntity);
						}
					} else {
						pv.setLoaded(true);
						pv.setValue(refreshedEntity);
					}
				}
			}
		}
	}

	/**
	 * Get cache for current thread
	 * 
	 * @return
	 */
	public Map<EntityKey, EntityTransferObject> getSessionCache() {
		if (isClientSystem()) {
			Map<EntityKey, EntityTransferObject> cache = tlCaches.get();
			if (cache == null) {
				throw new IllegalStateException("The cache has not been initialized!");
			}
			return cache;
		} else {
			return new EmptyMap<>();
		}
	}

	/**
	 * For server systems a Dummy Map implementation is used and nothing gets cached.
	 * 
	 * @return the isClientSystem
	 */
	public boolean isClientSystem() {
		return isClientSystem;
	}

	/**
	 * @param isClientSystem the isClientSystem to set
	 */
	public void setClientSystem(boolean isClientSystem) {
		this.isClientSystem = isClientSystem;
	}

	/**
	 * Dummy map implementation.
	 * 
	 * @author dotto
	 * @param <K>
	 * @param <V>
	 */
	public class EmptyMap<K, V> implements Map<K, V> {

		/*
		 * (non-Javadoc)
		 * @see java.util.Map#size()
		 */
		@Override
		public int size() {
			return 0;
		}

		/*
		 * (non-Javadoc)
		 * @see java.util.Map#isEmpty()
		 */
		@Override
		public boolean isEmpty() {
			return true;
		}

		/*
		 * (non-Javadoc)
		 * @see java.util.Map#containsKey(java.lang.Object)
		 */
		@Override
		public boolean containsKey(Object key) {
			return false;
		}

		/*
		 * (non-Javadoc)
		 * @see java.util.Map#containsValue(java.lang.Object)
		 */
		@Override
		public boolean containsValue(Object value) {
			return false;
		}

		/*
		 * (non-Javadoc)
		 * @see java.util.Map#get(java.lang.Object)
		 */
		@Override
		public V get(Object key) {
			return null;
		}

		/*
		 * (non-Javadoc)
		 * @see java.util.Map#put(java.lang.Object, java.lang.Object)
		 */
		@Override
		public V put(K key, V value) {
			return value;
		}

		/*
		 * (non-Javadoc)
		 * @see java.util.Map#remove(java.lang.Object)
		 */
		@Override
		public V remove(Object key) {
			return null;
		}

		/*
		 * (non-Javadoc)
		 * @see java.util.Map#putAll(java.util.Map)
		 */
		@Override
		public void putAll(Map<? extends K, ? extends V> m) {

		}

		/*
		 * (non-Javadoc)
		 * @see java.util.Map#clear()
		 */
		@Override
		public void clear() {

		}

		/*
		 * (non-Javadoc)
		 * @see java.util.Map#keySet()
		 */
		@Override
		public Set<K> keySet() {
			return new HashSet<>();
		}

		/*
		 * (non-Javadoc)
		 * @see java.util.Map#values()
		 */
		@Override
		public Collection<V> values() {
			return new ArrayList<>();
		}

		/*
		 * (non-Javadoc)
		 * @see java.util.Map#entrySet()
		 */
		@Override
		public Set<java.util.Map.Entry<K, V>> entrySet() {
			return new HashSet<>();
		}

	}

}
