/*
 * de.xwic.appkit.core.dao.EntityList
 * Created on 04.04.2005
 *
 */
package de.xwic.appkit.core.dao;

import java.io.Serializable;
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
public class EntityList<E> implements List<E>, Serializable {

	private static final long serialVersionUID = 2641640564678329627L;
	
	private List<E> list = null;
	private Limit limit = null;
	private int count = 0;

	/**
	 * Constructor.
	 * @param list
	 * @param count
	 */
	public EntityList(List<E> list, Limit limit, int count) {
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
	public List<E> getList() {
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
    @Override
	public int size() {

        return list.size();
    }

    /* (non-Javadoc)
     * @see java.util.Collection#isEmpty()
     */
    @Override
	public boolean isEmpty() {
        return list.isEmpty();
    }

    /* (non-Javadoc)
     * @see java.util.Collection#contains(java.lang.Object)
     */
    @Override
	public boolean contains(Object o) {
        return list.contains(o);
    }

    /* (non-Javadoc)
     * @see java.util.Collection#iterator()
     */
    @Override
	public Iterator<E> iterator() {
        return list.iterator();
    }

    /* (non-Javadoc)
     * @see java.util.Collection#toArray()
     */
    @Override
	public Object[] toArray() {
        return list.toArray();
    }

    /* (non-Javadoc)
	 * @see java.util.List#toArray(T[])
	 */
    @Override
	public <T> T[] toArray(T[] a) {
		return list.toArray(a);
    }

    /* (non-Javadoc)
     * @see java.util.Collection#add(java.lang.Object)
     */
    @Override
	public boolean add(E o) {
        return list.add(o);
    }

    /* (non-Javadoc)
     * @see java.util.Collection#remove(java.lang.Object)
     */
    @Override
	public boolean remove(Object o) {
        return list.remove(o);
    }

    /* (non-Javadoc)
     * @see java.util.Collection#containsAll(java.util.Collection)
     */
    @Override
	public boolean containsAll(Collection c) {
        return list.containsAll(c);
    }

    /* (non-Javadoc)
     * @see java.util.Collection#addAll(java.util.Collection)
     */
    @Override
	public boolean addAll(Collection c) {
        return list.addAll(c);
    }

    /* (non-Javadoc)
     * @see java.util.Collection#removeAll(java.util.Collection)
     */
    @Override
	public boolean removeAll(Collection c) {
        return list.removeAll(c);
    }

    /* (non-Javadoc)
     * @see java.util.Collection#retainAll(java.util.Collection)
     */
    @Override
	public boolean retainAll(Collection c) {
        return list.retainAll(c);
    }

    /* (non-Javadoc)
     * @see java.util.Collection#clear()
     */
    @Override
	public void clear() {
        list.clear();
    }

    /* (non-Javadoc)
     * @see java.util.List#addAll(int, java.util.Collection)
     */
    @Override
	public boolean addAll(int index, Collection c) {
        return list.addAll(index, c);
    }

    /* (non-Javadoc)
     * @see java.util.List#get(int)
     */
    @Override
	public E get(int index) {
        return list.get(index);
    }

    /* (non-Javadoc)
     * @see java.util.List#set(int, java.lang.Object)
     */
    @Override
	public E set(int index, E element) {
        return list.set(index, element);
    }

    /* (non-Javadoc)
     * @see java.util.List#add(int, java.lang.Object)
     */
    @Override
	public void add(int index, E element) {
        list.add(index, element);
    }

    /* (non-Javadoc)
     * @see java.util.List#remove(int)
     */
    @Override
	public E remove(int index) {
        return list.remove(index);
    }

    /* (non-Javadoc)
     * @see java.util.List#indexOf(java.lang.Object)
     */
    @Override
	public int indexOf(Object o) {
        return list.indexOf(o);
    }

    /* (non-Javadoc)
     * @see java.util.List#lastIndexOf(java.lang.Object)
     */
    @Override
	public int lastIndexOf(Object o) {
        return list.lastIndexOf(o);
    }

    /* (non-Javadoc)
     * @see java.util.List#listIterator()
     */
    @Override
	public ListIterator<E> listIterator() {
        return list.listIterator();
    }

    /* (non-Javadoc)
     * @see java.util.List#listIterator(int)
     */
    @Override
	public ListIterator<E> listIterator(int index) {
        return list.listIterator(index);
    }

    /* (non-Javadoc)
     * @see java.util.List#subList(int, int)
     */
    @Override
	public List<E> subList(int fromIndex, int toIndex) {
        return list.subList(fromIndex, toIndex);
    }

}
