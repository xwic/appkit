/**
 *
 */
package de.xwic.appkit.core.util;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

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
	 * @param allowDupes if we allow duplicate keys in the map, the latest one will always override the previous value, if we don't support it, throw {@link com.notbed.util.map.DuplicateKeyException}
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
	 * @param allowDupes if we allow duplicate keys in the map, the latest one will always override the previous value, if we don't support it, throw {@link com.notbed.util.map.DuplicateKeyException}
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
	 * @param allowDupes if we allow duplicate keys in the map, the latest one will always override the previous value, if we don't support it, throw {@link com.notbed.util.map.DuplicateKeyException}
	 * @param skipNullObjects if we want to skip null objects, if we don't skip these objects, we will get a {@link java.lang.NullPointerException}
	 * @param skipNullValues if we want to skip the null values from being added to the map
	 * @return a map created from the items using the generator
	 * @throws DuplicateKeyException if we have a duplicate key and we don't allow that
	 * @throws NullPointerException if there is a null value in the collection and we don't skip it
	 */
	public static <Key, Obj> Map<Key, Obj> generateMap(Collection<Obj> items, IEvaluator<Obj, Key> generator, boolean allowDupes,
			boolean skipNullObjects, boolean skipNullValues) throws DuplicateKeyException {
		Map<Key, Obj> map = new HashMap<Key, Obj>();
		for (Obj t : items) {
			if (t == null) {
				if (skipNullObjects) {
					continue;
				} else {
					throw new NullPointerException("Null Value in Collection");
				}
			}
			Key evaluate = generator.evaluate(t);
			if (evaluate == null && skipNullValues) {
				continue;
			}
			if (!allowDupes && map.containsKey(evaluate)) {
				throw new DuplicateKeyException(evaluate);
			}
			map.put(evaluate, t);
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
}
