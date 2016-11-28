/*
 * Copyright (c) NetApp Inc. - All Rights Reserved
 * 
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 * Proprietary and confidential.
 * 
 * de.xwic.appkit.webbase.pojoeditor.IPojoRenderer 
 */

package de.xwic.appkit.webbase.pojoeditor.renderers;

import java.util.List;

import de.jwic.base.RenderContext;
import de.xwic.appkit.webbase.pojoeditor.IPojoEditorFieldRenderLogic;
import de.xwic.appkit.webbase.pojoeditor.PojoEditorControl;
import de.xwic.appkit.webbase.pojoeditor.PojoEditorField;

/**
 * Interface for PojoRenderer
 * 
 * @author Andrei Pat
 *
 */
public interface IPojoRenderer {

	public static final String DEFAULT_CONTROL_RENDERER = "jwic.renderer.Default";

	/**
	 * The render method
	 * 
	 * @param context
	 */
	public void render(RenderContext context);

	/**
	 * The control container for the editor fields
	 * 
	 * @param editor
	 */
	void setEditorControl(PojoEditorControl editor);

	/**
	 * The list of fields to be rendered
	 * 
	 * @param fields
	 */
	void setFields(List<PojoEditorField> fields);

	/**
	 * Class of the pojo being edited
	 * 
	 * @param pojoClass
	 */
	void setPojoClass(Class pojoClass);

	/**
	 * Custom logic for rendering the field
	 * 
	 * @param logic
	 */
	void setFieldRenderLogic(IPojoEditorFieldRenderLogic logic);
}
