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
