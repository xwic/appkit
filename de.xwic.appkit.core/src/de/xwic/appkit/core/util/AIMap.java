package de.xwic.appkit.core.util;

import java.util.Map;

/**
 * Auto Initializing Map
 * @author Alexandru Bledea
 * @since Oct 19, 2013
 */
final class AIMap<K, X, I, V extends X> extends AbstractMapDecorator<K, V> implements Map<K, V>{

	private final Function<I, X> initializer;

	/**
	 * @param map
	 * @param initializer
	 */
	public AIMap(Map<K, V> map, Function<I, X> initializer) {
		super(map);
		this.initializer = initializer;
	}

	/* (non-Javadoc)
	 * @see java.util.Map#get(java.lang.Object)
	 */
	@SuppressWarnings ("deprecation")
	@Override
	public V get(Object key) {
		if (this.containsKey(key)) {
			return super.get(key);
		}
		final V call = (V) initializer.evaluate((I) key);
		this.put((K) key, call);
		return call;
	}

	/**
	 * Wraps a Map into a {@link AIMap} <br>
	 * @param map
	 * @param initializer
	 * @return
	 */
	public final static <K, X, I, V extends X> Map<K, V> wrap(Map<K, V> map, Function<I, X> initializer) {
		return new AIMap<K, X, I, V>(map, initializer);
	}
}
