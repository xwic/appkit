/*
 * de.xwic.appkit.core.dao.EntityList
 * Created on 04.04.2005
 *
 */
package de.xwic.appkit.core.dao;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Contains the immutable result of a query. The EntityList is a 
 * standard collection that is extended by a <code>totalSize</code>
 * property wich contains the real size in the database.
 * @author Florian Lippisch
 */
public class EntityList implements List {

	private List<Object> list = null;
	private Limit limit = null;
	private int count = 0;

	/**
	 * Constructor.
	 * @param list
	 * @param count
	 */
	public EntityList(List<Object> list, Limit limit, int count) {
		this.list = list;
		this.count = count;
		this.limit = limit;
	}

	/**
	 * @return Returns the count.
	 */
	public int getTotalSize() {
		return count;
	}
	/**
	 * @return Returns the list.
	 */
	public List<Object> getList() {
		return list;
	}
	/**
	 * Returns the limit used to create this list.
	 * @return
	 */
	public Limit getLimit() {
	    return limit;
	}

    /* (non-Javadoc)
     * @see java.util.Collection#size()
     */
    public int size() {
        
        return list.size();
    }

    /* (non-Javadoc)
     * @see java.util.Collection#isEmpty()
     */
    public boolean isEmpty() {
        return list.isEmpty();
    }

    /* (non-Javadoc)
     * @see java.util.Collection#contains(java.lang.Object)
     */
    public boolean contains(Object o) {
        return list.contains(o);
    }

    /* (non-Javadoc)
     * @see java.util.Collection#iterator()
     */
    public Iterator<Object> iterator() {
        return list.iterator();
    }

    /* (non-Javadoc)
     * @see java.util.Collection#toArray()
     */
    public Object[] toArray() {
        return list.toArray();
    }

    /* (non-Javadoc)
     * @see java.util.Collection#toArray(java.lang.Object[])
     */
    public Object[] toArray(Object[] a) {
        return list.toArray(a);
    }

    /* (non-Javadoc)
     * @see java.util.Collection#add(java.lang.Object)
     */
    public boolean add(Object o) {
        return list.add(o);
    }

    /* (non-Javadoc)
     * @see java.util.Collection#remove(java.lang.Object)
     */
    public boolean remove(Object o) {
        return list.remove(o);
    }

    /* (non-Javadoc)
     * @see java.util.Collection#containsAll(java.util.Collection)
     */
    public boolean containsAll(Collection c) {
        return list.containsAll(c);
    }

    /* (non-Javadoc)
     * @see java.util.Collection#addAll(java.util.Collection)
     */
    public boolean addAll(Collection c) {
        return list.addAll(c);
    }

    /* (non-Javadoc)
     * @see java.util.Collection#removeAll(java.util.Collection)
     */
    public boolean removeAll(Collection c) {
        return list.removeAll(c);
    }

    /* (non-Javadoc)
     * @see java.util.Collection#retainAll(java.util.Collection)
     */
    public boolean retainAll(Collection c) {
        return list.retainAll(c);
    }

    /* (non-Javadoc)
     * @see java.util.Collection#clear()
     */
    public void clear() {
        list.clear();
    }

    /* (non-Javadoc)
     * @see java.util.List#addAll(int, java.util.Collection)
     */
    public boolean addAll(int index, Collection c) {
        return list.addAll(index, c);
    }

    /* (non-Javadoc)
     * @see java.util.List#get(int)
     */
    public Object get(int index) {
        return list.get(index);
    }

    /* (non-Javadoc)
     * @see java.util.List#set(int, java.lang.Object)
     */
    public Object set(int index, Object element) {
        return list.set(index, element);
    }

    /* (non-Javadoc)
     * @see java.util.List#add(int, java.lang.Object)
     */
    public void add(int index, Object element) {
        list.add(index, element);
    }

    /* (non-Javadoc)
     * @see java.util.List#remove(int)
     */
    public Object remove(int index) {
        return list.remove(index);
    }

    /* (non-Javadoc)
     * @see java.util.List#indexOf(java.lang.Object)
     */
    public int indexOf(Object o) {
        return list.indexOf(o);
    }

    /* (non-Javadoc)
     * @see java.util.List#lastIndexOf(java.lang.Object)
     */
    public int lastIndexOf(Object o) {
        return list.lastIndexOf(o);
    }

    /* (non-Javadoc)
     * @see java.util.List#listIterator()
     */
    public ListIterator<Object> listIterator() {
        return list.listIterator();
    }

    /* (non-Javadoc)
     * @see java.util.List#listIterator(int)
     */
    public ListIterator<Object> listIterator(int index) {
        return list.listIterator(index);
    }

    /* (non-Javadoc)
     * @see java.util.List#subList(int, int)
     */
    public List<Object> subList(int fromIndex, int toIndex) {
        return list.subList(fromIndex, toIndex);
    }

}
