/**
 *
 */
package de.xwic.appkit.core.util;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

import de.xwic.appkit.core.util.InternalEvaluator.EvaluationResult;
import de.xwic.appkit.core.util.InternalEvaluator.IDupeChecker;

/**
 * @author Alexandru Bledea
 * @since Jul 9, 2013
 */
public class MapUtil {

	/**
	 * @param items the collection from which we create the map
	 * @param generator the key generator
	 * @return a map created from the items using the generator, if the key evaluates to null, it is not added to the map, if two items evaluate to the same key, the latest one will override any previous values
	 */
	public static <Key, Obj> Map<Key, Obj> generateMap(Collection<Obj> items, IEvaluator<Obj, Key> generator) {
		try {
			return generateMap(items, generator, true);
		} catch (DuplicateKeyException e) {
			return null; // not going to happen
		}
	}

	/**
	 * @param items the collection from which we create the map
	 * @param generator the key generator
	 * @param allowDupes if we allow duplicate keys in the map, the latest one will always override the previous value, if we don't support it, throw {@link de.xwic.appkit.core.util.DuplicateKeyException}
	 * @return a map created from the items using the generator, if the key evaluates to null, it is not added to the map
	 * @throws DuplicateKeyException if we have a duplicate key and we don't allow that
	 */
	public static <Key, Obj> Map<Key, Obj> generateMap(Collection<Obj> items, IEvaluator<Obj, Key> generator, boolean allowDupes)
			throws DuplicateKeyException {
		return generateMap(items, generator, allowDupes, true);
	}

	/**
	 * @param items the collection from which we create the map
	 * @param generator the key generator
	 * @param allowDupes if we allow duplicate keys in the map, the latest one will always override the previous value, if we don't support it, throw {@link de.xwic.appkit.core.util.DuplicateKeyException}
	 * @param skipNullObjects if we want to skip null objects, if we don't skip these objects, we will get a {@link java.lang.NullPointerException}
	 * @return a map created from the items using the generator, if the key evaluates to null, it is not added to the map
	 * @throws DuplicateKeyException if we have a duplicate key and we don't allow that
	 * @throws NullPointerException if there is a null value in the collection and we don't skip it
	 */
	public static <Key, Obj> Map<Key, Obj> generateMap(Collection<Obj> items, IEvaluator<Obj, Key> generator, boolean allowDupes,
			boolean skipNullObjects) throws DuplicateKeyException {
		return generateMap(items, generator, allowDupes, skipNullObjects, true);
	}

	/**
	 * @param items the collection from which we create the map
	 * @param generator the key generator
	 * @param allowDupes if we allow duplicate keys in the map, the latest one will always override the previous value, if we don't support it, throw {@link de.xwic.appkit.core.util.DuplicateKeyException}
	 * @param skipNullObjects if we want to skip null objects, if we don't skip these objects, we will get a {@link java.lang.NullPointerException}
	 * @param skipNullValues if we want to skip the null values from being added to the map
	 * @return a map created from the items using the generator
	 * @throws DuplicateKeyException if we have a duplicate key and we don't allow that
	 * @throws NullPointerException if there is a null value in the collection and we don't skip it
	 */
	public static <Key, Obj> Map<Key, Obj> generateMap(Collection<Obj> items, IEvaluator<Obj, Key> generator, boolean allowDupes,
			boolean skipNullObjects, boolean skipNullValues) throws DuplicateKeyException {
		EvaluationResult<Key> result = new EvaluationResult<Key>();

		InternalHashMap<Key, Obj> map = new InternalHashMap<Key, Obj>();

		for (Obj t : items) {
			result = InternalEvaluator.evaluate(t, skipNullObjects, generator, skipNullValues, allowDupes, map, result);
			if (!result.skip()) {
				map.put(result.getResult(), t);
			}
		}
		return map;
	}

	/**
	 * @param initializer the map initializer
	 * @return an unmodifiable {@link java.util.HashMap} containting the values that were set using the initializer
	 */
	public static <Key, Obj> Map<Key, Obj> unmodifiableMap(IMapInitializer<Key, Obj> initializer) {
		Map<Key, Obj> map = new HashMap<Key, Obj>();
		initializer.initMap(map);
		return Collections.unmodifiableMap(map);
	}

	/**
	 * @param get or create and add
	 * @return
	 */
	public static <Key, Obj> Obj get(Map<Key, Obj> map, Key key, Callable<Obj> newObjectCallable) {
		if (map.containsKey(key)) {
			return map.get(key);
		}
		try {
			Obj call = newObjectCallable.call();
			map.put(key, call);
			return call;
		} catch (Exception e) {
			throw new RuntimeException("Error creating new object", e);
		}
	}

	/**
	 * @author Alexandru Bledea
	 * @since Jul 31, 2013
	 * @param <K>
	 * @param <V>
	 */
	private static class InternalHashMap<K, V> extends HashMap<K, V> implements IDupeChecker<K> {

		/* (non-Javadoc)
		 * @see de.xwic.appkit.core.util.InternalEvaluator.DupeChecker#checkDupe(java.lang.Object)
		 */
		@Override
		public boolean checkIfDupe(K what) {
			return containsKey(what);
		}
	}
}
