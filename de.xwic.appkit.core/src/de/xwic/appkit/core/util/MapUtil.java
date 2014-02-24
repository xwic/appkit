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
public class MapUtil {

	/**
	 * Used to generate a map from the collection if <b>items</b> by using a <b>evaluator</b>. <br>
	 * If there are two objects with the same key, the latest entry will override the previous entry<br>
	 * If there are <b>null</b> values in the collection and objects that evaluate to <b>null</b> they are skipped<br>
	 * @param items the collection from which we create the map
	 * @param generator the key generator
	 * @return a map created from the items using the generator, if the key evaluates to null, it is not added to the map, if two items evaluate to the same key, the latest one will override any previous values
	 */
	public static <Key, Obj> Map<Key, Obj> generateMap(final Collection<? extends Obj> items, final ILazyEval<Obj, Key> generator) {
		final Map<Key, Obj> map = new HashMap<Key, Obj>();

		if (items == null) {
			return map;
		}

		for (final Obj obj : items) {
			if (obj == null) {
				continue;
			}
			final Key key = generator.evaluate(obj);
			if (key != null) {
				map.put(key, obj);
			}
		}

		return map;
	}

	/**
	 * @param initializer the map initializer
	 * @return a unmodifiable {@link java.util.HashMap} containting the values that were set using the <b>initializer</b>
	 */
	public static <Key, Obj> Map<Key, Obj> unmodifiableMap(final IMapInitializer<Key, Obj> initializer) {
		final Map<Key, Obj> map = new HashMap<Key, Obj>();
		initializer.initMap(map);
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
	public abstract static class LazyInit<V> implements ILazyEval<Object, V>, Callable<V> {

		/* (non-Javadoc)
		 * @see de.xwic.appkit.core.util.IEvaluator#evaluate(java.lang.Object)
		 */
		@Override
		public final V evaluate(final Object ignoreThis) {
			try {
				return call();
			} catch (Exception e) {
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
	public final static <K, X, I, V extends X> Map<K, V> wrapAI(final Map<K, V> map, final ILazyEval<I, X> initializer) {
		return new AIMap<K, X, I, V>(map, initializer);
	}

	/**
	 * Wraps a {@link LinkedHashMap} into a Auto-Initializing Map. If the map does not contain the requested key
	 * the evaluator is called to compute the value.  <br>
	 * @param initializer
	 * @return
	 */
	public final static <K, X, I, V extends X> Map<K, V> aiMap(final ILazyEval<I, X> initializer) {
		return wrapAI(new LinkedHashMap<K, V>(), initializer);
	}
}
