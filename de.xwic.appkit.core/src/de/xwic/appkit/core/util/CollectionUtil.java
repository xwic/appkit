/**
 *
 */
package de.xwic.appkit.core.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.xwic.appkit.core.util.InternalEvaluator.EvaluationResult;
import de.xwic.appkit.core.util.InternalEvaluator.IDupeChecker;


/**
 * @author Alexandru Bledea
 * @since Jul 30, 2013
 */
public class CollectionUtil {

	/**
	 * @param objects the collection from which we create the collection
	 * @param evaluator the evaluator
	 * @param collection the collection where the evaluation result will be added
	 * @return the collection passed as argument filled with the evaluated values, if an object evaluates to null, it is not added to the collection
	 */
	public static <C extends Collection<V>, V, O> C createCollection(Collection<O> objects, IEvaluator<O, V> evaluator, C collection) {
		try {
			return createCollection(objects, evaluator, collection, true);
		} catch (DuplicateKeyException e) {
			return null; //not going to happen
		}
	}

	/**
	 * @param objects the collection from which we create the collection
	 * @param evaluator the evaluator
	 * @param collection the collection where the evaluation result will be added
	 * @param allowDupes if we allow dupes in the collection
	 * @return the collection passed as argument filled with the evaluated values, if an object evaluates to null, it is not added to the collection
	 * @throws DuplicateKeyException if we have a duplicate key and we don't allow that
	 */
	public static <C extends Collection<V>, V, O> C createCollection(Collection<O> objects, IEvaluator<O, V> evaluator, C collection,
			boolean allowDupes) throws DuplicateKeyException {
		return createCollection(objects, evaluator, collection, allowDupes, true);
	}

	/**
	 * @param objects the collection from which we create the collection
	 * @param evaluator the evaluator
	 * @param collection the collection where the evaluation result will be added
	 * @param allowDupes if we allow dupes in the collection
	 * @param skipNullObjects if we skip null objects, if we don't we will get a we will get a {@link java.lang.NullPointerException}
	 * @return the collection passed as argument filled with the evaluated values, if an object evaluates to null, it is not added to the collection
	 * @throws DuplicateKeyException if we have a duplicate key and we don't allow that
	 * @throws NullPointerException if there is a null value in the collection and we don't skip it
	 */
	public static <C extends Collection<V>, V, O> C createCollection(Collection<O> objects, IEvaluator<O, V> evaluator, C collection,
			boolean allowDupes, boolean skipNullObjects) throws DuplicateKeyException {
		return createCollection(objects, evaluator, collection, allowDupes, skipNullObjects, true);
	}

	/**
	 * @param objects the collection from which we create the collection
	 * @param evaluator the evaluator
	 * @param collection the collection where the evaluation result will be added
	 * @param allowDupes if we allow dupes in the collection
	 * @param skipNullObjects if we skip null objects, if we don't we will get a we will get a {@link java.lang.NullPointerException}
	 * @param skipNullValues if we want to skip the null values from being added to the collection
	 * @return the collection passed as argument filled with the evaluated values
	 * @throws DuplicateKeyException if we have a duplicate key and we don't allow that
	 * @throws NullPointerException if there is a null value in the collection and we don't skip it
	 */
	public static <C extends Collection<V>, V, O> C createCollection(Collection<O> objects, IEvaluator<O, V> evaluator, final C collection,
			boolean allowDupes, boolean skipNullObjects, boolean skipNullValues) throws DuplicateKeyException {
		EvaluationResult<V> result = new EvaluationResult<V>();
		IDupeChecker<V> dupeChecker = new IDupeChecker<V>() {

			@Override
			public boolean checkIfDupe(V what) {
				return collection.contains(what);
			}
		};

		for (O t : objects) {
			result = InternalEvaluator.evaluate(t, skipNullObjects, evaluator, skipNullValues, allowDupes, dupeChecker, result);
			if (!result.skip()) {
				collection.add(result.getResult());
			}
		}
		return collection;
	}

	/**
	 * @param objects if you plan on calling it multiple times, it would be best if you would send a list
	 * @param evaluator
	 * @param collection
	 * @return
	 */
	public static <O> Collection<O> createCollection(Collection<O> objects, int start, int limit) {
		Collection<O> result = new ArrayList<O>();
		int step = start + limit;
		List<O> collection = getList(objects);
		int size = collection.size();
		for (int i = start; i < step && i < size; i++) {
			result.add(collection.get(i));
		}
		return result;
	}

	/**
	 * @param collection
	 * @return
	 */
	private static <O> List<O> getList(Collection<O> collection) {
		return collection instanceof List ? (List<O>) collection : new ArrayList<O>(collection);
	}
}
