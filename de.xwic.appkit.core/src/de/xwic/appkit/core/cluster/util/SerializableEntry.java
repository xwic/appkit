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
public class SerializableEntry<K extends Serializable, V extends Serializable> implements Entry<K, V>, Serializable {

	private K key;
	private V val;
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

}
