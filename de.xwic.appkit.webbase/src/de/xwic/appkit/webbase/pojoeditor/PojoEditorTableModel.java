/*
 * Copyright (c) NetApp Inc. - All Rights Reserved
 * 
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 * Proprietary and confidential.
 * 
 * de.xwic.appkit.webbase.pojoeditor.PojoEditorTableModel 
 */

package de.xwic.appkit.webbase.pojoeditor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Andrei Pat
 *
 */
public class PojoEditorTableModel {

	private Class<?> rowClass;
	private List data = new ArrayList();

	private Object newRow;

	/**
	 * @param rowClass
	 * @param data
	 */
	public PojoEditorTableModel(Class<?> rowClass) {
		super();
		this.rowClass = rowClass;
	}

	/**
	 * @param row
	 */
	public void remove(Object row) {
		data.remove(row);
	}

	/**
	 * @return
	 */
	public Object makeNewRow() {

		try {
			newRow = rowClass.newInstance();

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return newRow;
	}

	/**
	 * @param row
	 */
	public void save() {
		if (newRow != null) {
			data.add(newRow);
		}
	}

	/**
	 * 
	 */
	public void cancel() {
		newRow = null;
	}

	/**
	 * @return the rowClass
	 */
	public Class<?> getRowClass() {
		return rowClass;
	}

	/**
	 * @return the data
	 */
	public List<?> getData() {
		return data;
	}

	/**
	 * @param data
	 *            the data to set
	 */
	public void setData(List<?> data) {
		this.data = data;
	}
}
