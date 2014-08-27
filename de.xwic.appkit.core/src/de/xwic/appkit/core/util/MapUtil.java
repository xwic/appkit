/**
 *
 */
package de.xwic.appkit.core.util;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * @author Alexandru Bledea
 * @since Jul 9, 2013
 */
public final class MapUtil {

	/**
	 *
	 */
	private MapUtil() {
	}

	/**
	 * Used to generate a map from the collection if <b>items</b> by using a <b>evaluator</b>. <br>
	 * If there are two objects with the same key, the latest entry will override the previous entry<br>
	 * If there are <b>null</b> values in the collection and objects that evaluate to <b>null</b> they are skipped<br>
	 * @param items the collection from which we create the map
	 * @param generator the key generator
	 * @return a map created from the items using the generator, if the key evaluates to null, it is not added to the map, if two items evaluate to the same key, the latest one will override any previous values
	 */
	public static <Key, Obj> Map<Key, Obj> generateMap(final Collection<? extends Obj> items, final Function<Obj, Key> generator) {
		return generateMap(items, generator, Evaluators.<Obj> identity());
	}

	/**
	 * Used to generate a map from the collection if <b>items</b> by using <b>evaluators</b>. <br>
	 * If there are two objects with the same key, the latest entry will override the previous entry<br>
	 * If there are <b>null</b> values in the collection and objects that evaluate to <b>null</b> they are skipped<br>
	 * @param items the collection from which we create the map
	 * @param keyGenerator the key generator
	 * @param valueGenerator the value generator
	 * @return a map created from the items using the generator, if the key or value evaluate to null, it is not added to the map, if two items evaluate to the same key, the latest one will override any previous values
	 */
	public static <K, V, X> Map<K, V> generateMap(final Collection<? extends X> items,
			final Function<X, K> keyGenerator, final Function<X, V> valueGenerator) {
		final Map<K, V> map = new HashMap<K, V>();

		if (items == null) {
			return map;
		}

		for (final X obj : items) {
			if (obj == null) {
				continue;
			}
			final K key = keyGenerator.evaluate(obj);
			if (null == key) {
				continue;
			}
			final V value = valueGenerator.evaluate(obj);
			if (null != value){
				map.put(key, value);
			}
		}

		return map;
	}

	/**
	 * @param initializer the map initializer
	 * @return a unmodifiable {@link java.util.HashMap} containting the values that were set using the <b>initializer</b>
	 */
	public static <K, V> Map<K, V> unmodifiableMap(final IMapInitializer<K, V> initializer) {
		Map<K, V> map = new HashMap<K, V>();
		initializer.initMap(map);

//		the user may have saved the map from initMap, let's make a defensive copy
		map = new HashMap<K, V>(map);

		return Collections.unmodifiableMap(map);
	}

	/**
	 * used to create objects in a lazy manner, used when you <b>may</b> need something<br><br>
	 * for instance<br>
	 * if (condition1 & condition2 & condition3 & condition4){<br>
	 * &nbsp;&nbsp;return <b>initializer.call();</b><br>
	 * }
	 * @author Alexandru Bledea
	 * @since Aug 12, 2013
	 * @param <V>
	 *
	 */
	public abstract static class LazyInit<V> implements Function<Object, V>, Callable<V> {

		/* (non-Javadoc)
		 * @see de.xwic.appkit.core.util.IEvaluator#evaluate(java.lang.Object)
		 */
		@Override
		public final V evaluate(final Object ignoreThis) {
			try {
				return call();
			} catch (final Exception e) {
				throw new IllegalStateException("Error creating new object", e);
			}
		}
	}

	/**
	 * Wraps a map into a Auto-Initializing Map. If the map does not contain the requested key
	 * the evaluator is called to compute the value. <br>
	 * @param map
	 * @param initializer
	 * @return
	 */
	public final static <K, X, I, V extends X> Map<K, V> wrapAI(final Map<K, V> map, final Function<I, X> initializer) {
		return new AIMap<K, X, I, V>(map, initializer);
	}

	/**
	 * Wraps a {@link LinkedHashMap} into a Auto-Initializing Map. If the map does not contain the requested key
	 * the evaluator is called to compute the value.  <br>
	 * @param initializer
	 * @return
	 */
	public final static <K, X, I, V extends X> Map<K, V> aiMap(final Function<I, X> initializer) {
		return wrapAI(new LinkedHashMap<K, V>(), initializer);
	}
}
