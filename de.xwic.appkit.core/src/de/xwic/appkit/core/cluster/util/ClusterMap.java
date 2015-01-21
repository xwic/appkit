package de.xwic.appkit.core.cluster.util;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import de.xwic.appkit.core.cluster.ICluster;
import de.xwic.appkit.core.cluster.util.ClusterCollectionUpdateEventData.EventType;

/**
 * A map implementation that synchronizes over the cluster.
 * A unique identifier must be given to each instance.
 *
 * @author Razvan Pat on 1/15/2015.
 */
public class ClusterMap<K extends Serializable, V extends Serializable> extends AbstractClusterCollection implements Map<K, V> {

	private Map<K, V> map;

	/**
	 * Package private, use ClusterCollections to instantiate
	 *
	 * @param identifier
	 * @param cluster
	 * @param map
	 */
	ClusterMap(String identifier, ICluster cluster, Map<K, V> map) {
		super(identifier, cluster);
		this.map = map;
	}

	/**
	 * @param key
	 * @param value
	 * @return
	 */
	@Override
	public V put(K key, V value) {
		V result = map.put(key, value);
		sendClusterUpdate(new SerializableEntry<Serializable, Serializable>(key, value), EventType.ADD_ELEMENT);
		return result;
	}

	/**
	 * @param m
	 */
	@Override
	public void putAll(Map<? extends K, ? extends V> m) {
		map.putAll(m);
		sendClusterUpdate((Serializable)map, EventType.REPLACE_ALL);
	}

	/**
	 * @param key
	 * @return
	 */
	@Override
	public V remove(Object key) {
		V result = map.remove(key);
		if(result != null){
			sendClusterUpdate((K)key, EventType.REMOVE_ELEMENT);
		}
		return result;
	}

	/**
	 */
	@Override
	public void clear() {
		map.clear();
		sendClusterUpdate(null, EventType.CLEAR);
	}
	
	
	@Override public int size() {
		return map.size();
	}

	@Override public boolean isEmpty() {
		return map.isEmpty();
	}

	@Override public boolean containsKey(Object key) {
		return map.containsKey(key);
	}

	@Override public boolean containsValue(Object value) {
		return map.containsValue(value);
	}

	@Override public V get(Object key) {
		return map.get(key);
	}

	@Override public Set<K> keySet() {
		return map.keySet();
	}

	@Override public Collection<V> values() {
		return map.values();
	}

	@Override public Set<Entry<K, V>> entrySet() {
		return map.entrySet();
	}

	@Override public boolean equals(Object o) {
		return map.equals(o);
	}

	@Override public int hashCode() {
		return map.hashCode();
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.util.AbstractClusterCollection#eventReceived(java.io.Serializable, de.xwic.appkit.core.cluster.util.ClusterCollectionUpdateEventData.EventType)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eventReceived(Serializable obj, EventType eventType) {
		if(eventType == EventType.ADD_ELEMENT){
			SerializableEntry<K, V> newElement = (SerializableEntry<K, V>) obj;
			map.put(newElement.getKey(), newElement.getValue());
		}else if (eventType == EventType.REPLACE_ALL){
			map = (Map<K, V>) obj;
		}else if (eventType == EventType.REMOVE_ELEMENT){
			map.remove(obj);
		}else if(eventType == EventType.CLEAR){
			map.clear();
		}
	}
}
