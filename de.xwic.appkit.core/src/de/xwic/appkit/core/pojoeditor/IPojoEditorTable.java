/*
 * Copyright (c) NetApp Inc. - All Rights Reserved
 * 
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 * Proprietary and confidential.
 * 
 * de.xwic.appkit.webbase.pojoeditor.IPojoEditorTable 
 */

package de.xwic.appkit.core.pojoeditor;

import java.util.List;

import de.jwic.base.IControl;

/**
 * @author Andrei Pat
 *
 */
public interface IPojoEditorTable extends IControl {

	/**
	 * @param contents
	 */
	void setData(List<?> data);

}
