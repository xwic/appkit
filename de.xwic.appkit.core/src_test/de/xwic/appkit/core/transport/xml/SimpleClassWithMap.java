package de.xwic.appkit.core.transport.xml;

import java.util.Collections;
import java.util.Map;

/**
 * @author Alexandru Bledea
 * @since Oct 8, 2014
 */
class SimpleClassWithMap {

	private Map<String, Integer> map;

	/**
	 * @return the map
	 */
	public Map<String, Integer> getMap() {
		return map;
	}

	/**
	 * @param map
	 *            the map to set
	 */
	public void setMap(final Map<String, Integer> map) {
		this.map = map;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((map == null) ? 0 : map.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final SimpleClassWithMap other = (SimpleClassWithMap) obj;
		if (map == null) {
			if (other.map != null) {
				return false;
			}
		} else if (!map.equals(other.map)) {
			return false;
		}
		return true;
	}

	/**
	 * @param map
	 * @return
	 */
	static SimpleClassWithMap of(final Map<String, Integer> map) {
		final SimpleClassWithMap enumObject = new SimpleClassWithMap();
		enumObject.setMap(map);
		return enumObject;
	}

	/**
	 * @param key
	 * @param value
	 * @return
	 */
	static SimpleClassWithMap of(final String key, final Integer value) {
		return of(Collections.singletonMap(key, value));
	}

}
