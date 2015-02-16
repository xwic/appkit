/**
 * 
 */
package de.xwic.appkit.core.cluster.util;

import java.io.Serializable;
import java.util.Map.Entry;

/**
 * @author dotto
 *
 */
public class SerializableEntry<K, V> implements Entry<K, V>, Serializable {

	private K key;
	private V val;
	
	public SerializableEntry(){
		
	}
	/**
	 * 
	 */
	public SerializableEntry(K key, V val) {
		this.key = key;
		this.val = val;
	}
	/* (non-Javadoc)
	 * @see java.util.Map.Entry#getKey()
	 */
	@Override
	public K getKey() {
		return key;
	}

	/* (non-Javadoc)
	 * @see java.util.Map.Entry#getValue()
	 */
	@Override
	public V getValue() {
		return val;
	}

	/* (non-Javadoc)
	 * @see java.util.Map.Entry#setValue(java.lang.Object)
	 */
	@Override
	public V setValue(V value) {
		this.val = val;
		return val;
	}
	/**
	 * @return the val
	 */
	public V getVal() {
		return val;
	}
	/**
	 * @param val the val to set
	 */
	public void setVal(V val) {
		this.val = val;
	}
	/**
	 * @param key the key to set
	 */
	public void setKey(K key) {
		this.key = key;
	}

}
