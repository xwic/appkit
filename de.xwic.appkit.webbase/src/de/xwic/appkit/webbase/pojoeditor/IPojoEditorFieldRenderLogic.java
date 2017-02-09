/*
 * Copyright (c) NetApp Inc. - All Rights Reserved
 * 
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 * Proprietary and confidential.
 * 
 * de.xwic.appkit.webbase.pojoeditor.IPojoEditorFieldRenderer 
 */

package de.xwic.appkit.webbase.pojoeditor;

/**
 * @author Andrei Pat
 *
 */
public interface IPojoEditorFieldRenderLogic {

	/**
	 * Should the field be rendered at all
	 * 
	 * @param fieldName
	 * @return
	 */
	boolean isRenderField(String fieldName);

	/**
	 * Should the field be disabled
	 * 
	 * @param fieldName
	 * @return
	 */
	boolean isEnabled(String fieldName);

}
