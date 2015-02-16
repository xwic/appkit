package de.xwic.appkit.core.cluster.util;

import java.io.Serializable;

/**
 * @author Razvan Pat on 2/10/2015.
 */
public class ListOperationWithIndex implements Serializable {

	private Integer index;
	private Object object;

	/**
	 *
	 */
	public ListOperationWithIndex() {
	}

	/**
	 * @param index
	 * @param object
	 */
	public ListOperationWithIndex(Integer index, Object object) {
		this.index = index;
		this.object = object;
	}

	/**
	 * @return
	 */
	public Integer getIndex() {
		return index;
	}

	/**
	 * @param index
	 */
	public void setIndex(Integer index) {
		this.index = index;
	}

	/**
	 * @return
	 */
	public Object getObject() {
		return object;
	}

	/**
	 * @param object
	 */
	public void setObject(Serializable object) {
		this.object = object;
	}
}
