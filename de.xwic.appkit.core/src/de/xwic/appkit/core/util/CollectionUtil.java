/**
 *
 */
package de.xwic.appkit.core.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;


/**
 * @author Alexandru Bledea
 * @since Jul 30, 2013
 */
public final class CollectionUtil {

	/**
	 *
	 */
	private CollectionUtil() {
	}

	/**
	 * @param collection
	 * @param resultClass
	 * @return
	 */
	public static boolean isOf(final Collection<?> collection, final Class<?> resultClass) {
		Validate.notNull(resultClass, "No result class provided.");
		Validate.notNull(collection, "No collection provided.");
		if (collection.isEmpty()) {
			return true;
		}
		for (final Object cal : collection) {
			if (cal != null && !resultClass.isInstance(cal)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * @param c
	 * @return
	 */
	public static boolean isEmpty(final Collection<?> c) {
		return c == null || c.isEmpty();
	}

	/**
	 * for defensive copying
	 * @param set
	 * @return
	 */
	public static <E> Set<E> cloneToSet(final Collection<E> set) {
		if (set == null) {
			return null;
		}
		return new LinkedHashSet<E>(set);
	}

	/**
	 * for defensive copying
	 * @param list
	 * @return
	 */
	public static <E> List<E> cloneToList(final Collection<E> list) {
		if (list == null) {
			return null;
		}
		return new ArrayList<E>(list);
	}

	/**
	 * @param where collection to clear and add to
	 * @param fromWhere what to add
	 * @throws NullPointerException if <code>where</code> is empty
	 */
	public static <E> void clearAndAddAll(final Collection<? super E> where, final Collection<? extends E> fromWhere) throws NullPointerException {
		where.clear();
		if (!isEmpty(fromWhere)) {
			where.addAll(fromWhere);
		}
	}

	/**
	 * adds all not null elements to the specified collection
	 * @param c
	 * @param elements
	 */
	public static <E> void addAllNotNull(final Collection<? super E> c, final E... elements) {
		if (elements == null) {
			return;
		}
		for (E e : elements) {
			addIfNotNull(e, c);
		}
	}

	/**
	 * Used to transform a collection of <b>objects</b> into a list<br>
	 * The values for the second collection are generated by using a <b>evaluator</b>. <br>
	 * Objects are null or that evaluate to <b>null</b> will be skipped
	 * @param objects the collection from which we create the collection
	 * @param evaluator the evaluator
	 * @return a list filled with the evaluated values, if an object evaluates to null, it is not added to the collection
	 */
	public static <E, O> List<E> createList(final Collection<? extends O> objects, final ILazyEval<O, E> evaluator) {
		return createCollection(objects, evaluator, new ArrayList<E>());
	}

	/**
	 * Used to transform a collection of <b>objects</b> into a set<br>
	 * The values for the second collection are generated by using a <b>evaluator</b>. <br>
	 * Objects are null or that evaluate to <b>null</b> will be skipped
	 * @param objects the collection from which we create the collection
	 * @param evaluator the evaluator
	 * @return a list filled with the evaluated values, if an object evaluates to null, it is not added to the collection
	 */
	public static <E, O> Set<E> createSet(final Collection<? extends O> objects, final ILazyEval<O, E> evaluator) {
		return createCollection(objects, evaluator, new LinkedHashSet<E>());
	}

	/**
	 * Used to transform an array of <b>objects</b> into a <b>collection</b><br>
	 * The values for the second collection are generated by using a <b>evaluator</b>. <br>
	 * Objects are null or that evaluate to <b>null</b> will be skipped
	 * @param objects the collection from which we create the collection
	 * @param evaluator the evaluator
	 * @param collection the collection where the evaluation result will be added
	 * @return the collection passed as argument filled with the evaluated values, if an object evaluates to null, it is not added to the collection
	 */
	public static <C extends Collection<V>, V, O> C createCollection(O[] objects, ILazyEval<O, V> evaluator, C collection) {
		if (objects == null) {
			return collection;
		}
		return createCollection(Arrays.asList(objects), evaluator, collection);
	}

	/**
	 * @deprecated use {@link #createSet(Collection, ILazyEval)} or {@link #createList(Collection, ILazyEval)} instead
	 * Used to transform a collection of <b>objects</b> into another <b>collection</b><br>
	 * The values for the second collection are generated by using a <b>evaluator</b>. <br>
	 * Objects are null or that evaluate to <b>null</b> will be skipped
	 * @param objects the collection from which we create the collection
	 * @param evaluator the evaluator
	 * @param collection the collection where the evaluation result will be added
	 * @return the collection passed as argument filled with the evaluated values, if an object evaluates to null, it is not added to the collection
	 */
	@Deprecated
	public static <C extends Collection<V>, V, O> C createCollection(final Collection<? extends O> objects, ILazyEval<O, V> evaluator, final C collection) {
		if (objects != null) {

			for (final O obj : objects) {
				if (obj == null) {
					continue;
				}

				final V value = evaluator.evaluate(obj);
				addIfNotNull(value, collection);
			}

		}
		return collection;
	}

	/**
	 * Used to filter a collection of <b>objects</b><br>
	 * Only the values that return true are kept<br>
	 * <b>It is your responsibility to make sure that the element is not null, consider {@link NotNullFilter}</b><br>
	 * @param objects the collection from which we create the collection
	 * @param filter the filter
	 * @return the filtered collection passed as the parameter. <b>the original collection is also altered.</b>
	 */
	public static <C extends Collection<O>, O> C filter(C objects, IFilter<O> filter) {
		if (objects != null) {
			Iterator<O> iterator = objects.iterator();
			while (iterator.hasNext()) {
				if (!filter.keep(iterator.next())) {
					iterator.remove();
				}
			}
		}
		return objects;
	}

	/**
	 * used to break a large collection into hashsets
	 * @param collection
	 * @param maxElements
	 * @return
	 */
	public static <O> List<Collection<O>> breakInSets(final Collection<O> collection, final int maxElements) {
		return breakCollection(collection, maxElements, Type.HASHSET);
	}

	/**
	 * used to break a large collection into arraylists
	 * @param collection
	 * @param maxElements
	 * @return
	 */
	public static <O> List<Collection<O>> breakInLists(final Collection<O> collection, final int maxElements) {
		return breakCollection(collection, maxElements, Type.ARRAYLIST);
	}

	/**
	 * used to break a large collection into smaller collection
	 * consider using {@link CollectionUtil#breakInLists(Collection, int)} or {@link CollectionUtil#breakInSets(Collection, int)} instead
	 * @param collection
	 * @param maxElements
	 * @param type
	 * @return
	 */
	private static <O> List<Collection<O>> breakCollection(final Collection<O> collection, final int maxElements, final Type type) {
		final List<Collection<O>> result = new ArrayList<Collection<O>>();
		if (collection == null){
			return result;
		}
		Collection<O> step = type.create();
		final Iterator<O> iterator = collection.iterator();
		while (iterator.hasNext()) {
			if (step.size() == maxElements) {
				result.add(step);
				step = type.create();
			}
			step.add(iterator.next());
		}
		if (!step.isEmpty()) {
			result.add(step);
		}
		return result;
	}

	/**
	 * @param collection
	 * @param evaluator
	 * @param emptyMessage
	 * @return
	 */
	public static <O> String join(final Collection<O> collection, final ILazyEval<O, String> evaluator, final String separator, final String emptyMessage) {
		final List<String> strings = new ArrayList<String>();
		createCollection(collection, evaluator, strings);

		final Iterator<String> iterator = strings.iterator();
		if (iterator.hasNext()) {
			return StringUtils.join(iterator, separator);
		}
		return emptyMessage;
	}

	/**
	 * @param collection
	 * @param evaluator
	 * @return an empty string if no values
	 */
	public static <O> String join(final Collection<O> collection, final ILazyEval<O, String> evaluator, final String separator) {
		return join(collection, evaluator, separator, "");
	}

	/**
	 * @param element
	 * @param collection
	 */
	public static <O> void addIfNotNull(O element, Collection<O> collection) {
		if (element != null) {
			collection.add(element);
		}
	}

	/**
	 * Converts the elements to a list of elements. <br> Null elements are not included. Returns an unmodifiable view of the specified array.
	 * This method allows modules to provide users with "read-only" access to the list.
	 * Query operations on the returned list "read through" to the specified list, and attempts to modify the returned list, whether direct or via its iterator,
	 * result in an {@link UnsupportedOperationException}.
	 *
	 * @param the array for which an unmodifiable view is to be returned.
	 * @return an unmodifiable view of the generated list
	 */
	public static <E> List<E> convertToList(final E... elements) {
		if (elements == null || elements.length == 0) {
			return Collections.emptyList();
		}
		final List<E> list = new ArrayList<E>();
		addAllNotNull(list, elements);
		return Collections.unmodifiableList(list);
	}

	/**
	 * Used to transform a collection of <b>objects</b> into another <b>collection</b><br>
	 * The values for the second collection are generated by using a <b>evaluator</b>. <br>
	 * Objects are null or that evaluate to <b>null</b> will be skipped
	 * @param where to search
	 * @param what to search for
	 * @return the first element that does not evaluate to <code>null</code> or <code>null</code>
	 */
	public static <V, O> V findFirst(final Collection<? extends O> objects, final ILazyEval<O, V> evaluator) {
		return findFirst(objects, evaluator, null);
	}

	/**
	 * Used to transform a collection of <b>objects</b> into another <b>collection</b><br>
	 * The values for the second collection are generated by using a <b>evaluator</b>. <br>
	 * Objects are null or that evaluate to <b>null</b> will be skipped
	 * @param objects where to search
	 * @param evaluator what to search for
	 * @param whatIfNull what to return if element is null
	 * @return the first element that does not evaluate to <code>null</code> or <code>whatIfNull</code>
	 */
	public static <V, O> V findFirst(final Collection<? extends O> objects, final ILazyEval<O, V> evaluator, final V whatIfNull) {
		if (null == objects) {
			return whatIfNull;
		}
		for (final O obj : objects) {
			if (null == obj) {
				continue;
			}
			final V value = evaluator.evaluate(obj);
			if (null != value ) {
				return value;
			}
		}
		return whatIfNull;
	}

	/**
	 * @author Alexandru Bledea
	 * @since Feb 4, 2014
	 */
	private static enum Type {
		HASHSET {
			@Override
			<E> Collection<E> create() {
				return new HashSet<E>();
			}
		},
		ARRAYLIST {
			@Override
			<E> Collection<E> create() {
				return new ArrayList<E>();
			}
		};

		abstract <E> Collection<E> create();
	}

}
