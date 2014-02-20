package de.xwic.appkit.core.util;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * Auto Initializing Map
 * @author Alexandru Bledea
 * @since Oct 19, 2013
 */
final class AIMap<K, X, I, V extends X> implements Map<K, V> {

	private final Map<K, V> map;
	private final ILazyEval<I, X> initializer;

	/**
	 * @param map
	 * @param initializer
	 */
	public AIMap(Map<K, V> map, ILazyEval<I, X> initializer) {
		this.map = map;
		this.initializer = initializer;
	}

	/* (non-Javadoc)
	 * @see java.util.Map#size()
	 */
	@Override
	public int size() {
		return map.size();
	}

	/* (non-Javadoc)
	 * @see java.util.Map#isEmpty()
	 */
	@Override
	public boolean isEmpty() {
		return map.isEmpty();
	}

	/* (non-Javadoc)
	 * @see java.util.Map#containsKey(java.lang.Object)
	 */
	@Override
	public boolean containsKey(Object key) {
		return map.containsKey(key);
	}

	/* (non-Javadoc)
	 * @see java.util.Map#containsValue(java.lang.Object)
	 */
	@Override
	public boolean containsValue(Object value) {
		return map.containsKey(value);
	}

	/* (non-Javadoc)
	 * @see java.util.Map#get(java.lang.Object)
	 */
	@SuppressWarnings ("deprecation")
	@Override
	public V get(Object key) {
		return MapUtil.get(map, (K) key, (I) key, initializer);
	}

	/* (non-Javadoc)
	 * @see java.util.Map#put(java.lang.Object, java.lang.Object)
	 */
	@Override
	public V put(K key, V value) {
		return map.put(key, value);
	}

	/* (non-Javadoc)
	 * @see java.util.Map#remove(java.lang.Object)
	 */
	@Override
	public V remove(Object key) {
		return map.remove(key);
	}

	/* (non-Javadoc)
	 * @see java.util.Map#putAll(java.util.Map)
	 */
	@Override
	public void putAll(Map<? extends K, ? extends V> m) {
		map.putAll(m);
	}

	/* (non-Javadoc)
	 * @see java.util.Map#clear()
	 */
	@Override
	public void clear() {
		map.clear();
	}

	/* (non-Javadoc)
	 * @see java.util.Map#keySet()
	 */
	@Override
	public Set<K> keySet() {
		return map.keySet();
	}

	/* (non-Javadoc)
	 * @see java.util.Map#values()
	 */
	@Override
	public Collection<V> values() {
		return map.values();
	}

	/* (non-Javadoc)
	 * @see java.util.Map#entrySet()
	 */
	@Override
	public Set<Entry<K, V>> entrySet() {
		return map.entrySet();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return map.toString();
	}

	/**
	 * Wraps a Map into a {@link AIMap} <br>
	 * @param map
	 * @param initializer
	 * @return
	 */
	public final static <K, X, I, V extends X> Map<K, V> wrap(Map<K, V> map, ILazyEval<I, X> initializer) {
		return new AIMap<K, X, I, V>(map, initializer);
	}
}