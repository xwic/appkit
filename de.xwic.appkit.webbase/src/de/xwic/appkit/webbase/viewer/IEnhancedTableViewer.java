/*
 * Copyright (c) NetApp Inc. - All Rights Reserved
 * 
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 * Proprietary and confidential.
 * 
 * de.xwic.appkit.webbase.viewer.IEnhancedTableViewer 
 */

package de.xwic.appkit.webbase.viewer;

import de.jwic.controls.tableviewer.TableColumn;
import de.xwic.appkit.webbase.utils.UserProfileWrapper;

/**
 * Interface for abstraction of some commmon used functions to allow 
 * Entity Table viewer enhancements, like a SQL table viewer implementation.
 * @author dotto
 * @date May 29, 2015
 * 
 */
public interface IEnhancedTableViewer {

	/**
	 * @return the userLS
	 */
	public abstract UserProfileWrapper getUserListProfile();

	/**
	 * Change the sort icon.
	 * 
	 * @param tableColumn
	 */
	public abstract void handleSorting(TableColumn tableColumn);

}
