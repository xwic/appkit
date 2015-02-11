package de.xwic.appkit.core.cluster.util;

import java.io.Serializable;
import java.util.*;

import de.xwic.appkit.core.cluster.ICluster;

/**
 * @author Razvan Pat on 2/10/2015.
 */
@SuppressWarnings({ "EqualsWhichDoesntCheckParameterClass", "unchecked" })
public class ClusterList<T extends Serializable> extends AbstractClusterCollection implements List<T> {

	final private List<T> list;

	/**
	 * @param identifier
	 * @param cluster
	 * @param list
	 */
	public ClusterList(String identifier, ICluster cluster, List<T> list) {
		super(identifier, cluster);
		this.list = new ArrayList<T>(list);
	}

	/**
	 * @param obj
	 * @param eventType
	 */
	@Override
	public void eventReceived(Object obj, ClusterCollectionUpdateEventData.EventType eventType) {
		if(eventType == ClusterCollectionUpdateEventData.EventType.ADD_ELEMENT){
			ListOperationWithIndex operation = (ListOperationWithIndex) obj;
			T newElement = (T) (operation.getObject());
			if(operation.getIndex() == null) {
				list.add(newElement);
			} else {
				list.add(operation.getIndex(), newElement);
			}
		} else if (eventType == ClusterCollectionUpdateEventData.EventType.ADD_ALL) {
			ListOperationWithIndex operation = (ListOperationWithIndex) obj;
			if(operation.getIndex() == null) {
				list.addAll((Collection<? extends T>) operation.getObject());
			} else {
				list.addAll(operation.getIndex(), (Collection<? extends T>) operation.getObject());
			}
		} else if (eventType == ClusterCollectionUpdateEventData.EventType.REMOVE_ALL) {
			list.removeAll((Collection<T>) obj);
		} else if (eventType == ClusterCollectionUpdateEventData.EventType.REMOVE_ELEMENT) {
			ListOperationWithIndex operation = (ListOperationWithIndex) obj;
			if(operation.getIndex() != null) {
				list.remove(operation.getIndex().intValue());
			} else {
				//noinspection SuspiciousMethodCalls
				list.remove(operation.getObject());
			}
		} else if(eventType == ClusterCollectionUpdateEventData.EventType.CLEAR) {
			list.clear();
		} else if(eventType == ClusterCollectionUpdateEventData.EventType.RETAIN_ALL) {
			list.retainAll((Collection<?>) obj);
		} else if(eventType == ClusterCollectionUpdateEventData.EventType.SET_ELEMENT) {
			ListOperationWithIndex operation = (ListOperationWithIndex) obj;
			list.set(operation.getIndex(), (T) operation.getObject());
		}
	}

	@Override
	public int size() {
		return list.size();
	}

	@Override
	public boolean isEmpty() {
		return list.isEmpty();
	}

	@Override
	public boolean contains(Object o) {
		return list.contains(o);
	}

	@Override
	public Iterator<T> iterator() {
		return list.iterator();
	}

	@Override
	public Object[] toArray() {
		return list.toArray();
	}

	@SuppressWarnings("SuspiciousToArrayCall")
	@Override
	public <T1> T1[] toArray(T1[] a) {
		return list.toArray(a);
	}

	@Override
	public boolean add(T t) {
		sendClusterUpdate(
				new ListOperationWithIndex(null, t),
				ClusterCollectionUpdateEventData.EventType.ADD_ELEMENT);
		return list.add(t);
	}

	@Override
	public boolean remove(Object o) {
		sendClusterUpdate(
				new ListOperationWithIndex(null, (Serializable) o),
				ClusterCollectionUpdateEventData.EventType.REMOVE_ELEMENT);
		return list.remove(o);
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return list.containsAll(c);
	}

	@Override
	public boolean addAll(Collection<? extends T> c) {
		sendClusterUpdate(
				new ListOperationWithIndex(null, (Serializable) c),
				ClusterCollectionUpdateEventData.EventType.ADD_ALL);
		return list.addAll(c);
	}

	@Override
	public boolean addAll(int index, Collection<? extends T> c) {
		sendClusterUpdate(
				new ListOperationWithIndex(index, (Serializable) c),
				ClusterCollectionUpdateEventData.EventType.ADD_ALL);
		return list.addAll(index, c);
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		sendClusterUpdate(
				c,
				ClusterCollectionUpdateEventData.EventType.REMOVE_ALL);
		return list.removeAll(c);
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		sendClusterUpdate(
				c,
				ClusterCollectionUpdateEventData.EventType.RETAIN_ALL);
		return list.retainAll(c);
	}

	@Override
	public void clear() {
		sendClusterUpdate(null, ClusterCollectionUpdateEventData.EventType.CLEAR);
		list.clear();
	}

	@Override
	public boolean equals(Object o) {
		return list.equals(o);
	}

	@Override
	public int hashCode() {
		return list.hashCode();
	}

	@Override
	public T get(int index) {
		return list.get(index);
	}

	@Override
	public T set(int index, T element) {
		sendClusterUpdate(
				new ListOperationWithIndex(index, element),
				ClusterCollectionUpdateEventData.EventType.SET_ELEMENT);
		return list.set(index, element);
	}

	@Override
	public void add(int index, T element) {
		sendClusterUpdate(
				new ListOperationWithIndex(index, element),
				ClusterCollectionUpdateEventData.EventType.ADD_ELEMENT);
		list.add(index, element);
	}

	@Override
	public T remove(int index) {
		sendClusterUpdate(
				new ListOperationWithIndex(index, null),
				ClusterCollectionUpdateEventData.EventType.REMOVE_ELEMENT);
		return list.remove(index);
	}

	@Override
	public int indexOf(Object o) {
		return list.indexOf(o);
	}

	@Override
	public int lastIndexOf(Object o) {
		return list.lastIndexOf(o);
	}

	@Override
	public ListIterator<T> listIterator() {
		return list.listIterator();
	}

	@Override
	public ListIterator<T> listIterator(int index) {
		return list.listIterator(index);
	}

	@Override
	public List<T> subList(int fromIndex, int toIndex) {
		return list.subList(fromIndex, toIndex);
	}
}
