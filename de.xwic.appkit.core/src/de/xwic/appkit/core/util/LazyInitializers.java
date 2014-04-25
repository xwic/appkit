package de.xwic.appkit.core.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import de.xwic.appkit.core.util.MapUtil.LazyInit;

/**
 * @author Alexandru Bledea
 * @since Oct 19, 2013
 */
public class LazyInitializers {

	/**
	 * Lazy initializer that returns a Collection (ArrayList)
	 */
	public static final LazyInit<Collection> COLLECTION_ARRAYLIST = new LazyInit<Collection>() {

		/* (non-Javadoc)
		 * @see java.util.concurrent.Callable#call()
		 */
		@Override
		public Collection call() throws Exception {
			return new ArrayList();
		}

	};

	/**
	 * Lazy initializer that returns a Collection (HashSet)
	 */
	public static final LazyInit<Collection> COLLECTION_HASHSET = new LazyInit<Collection>() {

		/* (non-Javadoc)
		 * @see java.util.concurrent.Callable#call()
		 */
		@Override
		public Collection call() throws Exception {
			return new HashSet();
		}

	};

	/**
	 * Lazy initializer that returns a Set (HashSet)
	 */
	public static final LazyInit<Set> SET_HASHSET = new LazyInit<Set>() {

		/* (non-Javadoc)
		 * @see java.util.concurrent.Callable#call()
		 */
		@Override
		public Set call() throws Exception {
			return new HashSet();
		}

	};

	/**
	 * Lazy initializer that returns a Set (TreeSet)
	 */
	public static final LazyInit<Set> SET_TREESET = new LazyInit<Set>() {

		/* (non-Javadoc)
		 * @see java.util.concurrent.Callable#call()
		 */
		@Override
		public Set call() throws Exception {
			return new TreeSet();
		}
	};

	/**
	 * Lazy initializer that returns a List (ArrayList)
	 */
	public static final LazyInit<List> LIST_ARRAYLIST = new LazyInit<List>() {

		/* (non-Javadoc)
		 * @see java.util.concurrent.Callable#call()
		 */
		@Override
		public List call() throws Exception {
			return new ArrayList();
		}
	};

	/**
	 * Lazy initializer that returns a List (LinkedList)
	 */
	public static final LazyInit<List> LIST_LINKEDLIST = new LazyInit<List>() {

		/* (non-Javadoc)
		 * @see java.util.concurrent.Callable#call()
		 */
		@Override
		public List call() throws Exception {
			return new LinkedList();
		}
	};

	/**
	 * Lazy initializer that returns a Map (HashMap)
	 */
	public static final LazyInit<Map> MAP_HASHMAP = new LazyInit<Map>() {

		/* (non-Javadoc)
		 * @see java.util.concurrent.Callable#call()
		 */
		@Override
		public Map call() throws Exception {
			return new HashMap();
		}
	};

	/**
	 * Lazy initializer that returns a Map (LinkedHashMap)
	 */
	public static final LazyInit<Map> MAP_LINKEDHASHMAP = new LazyInit<Map>() {

		/* (non-Javadoc)
		 * @see java.util.concurrent.Callable#call()
		 */
		@Override
		public Map call() throws Exception {
			return new LinkedHashMap();
		}
	};

	/**
	 * Lazy initializer that returns a Map (TreeMap)
	 */
	public static final LazyInit<Map> MAP_TREEMAP = new LazyInit<Map>() {

		/* (non-Javadoc)
		 * @see java.util.concurrent.Callable#call()
		 */
		@Override
		public Map call() throws Exception {
			return new TreeMap();
		}
	};

}