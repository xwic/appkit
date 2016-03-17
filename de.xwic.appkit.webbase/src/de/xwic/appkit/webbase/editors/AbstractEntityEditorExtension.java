/**
 * 
 */
package de.xwic.appkit.webbase.editors;

import de.jwic.controls.ToolBar;

/**
 * Abstract base class for EntityEditorExtensions implementing the IEntityEditorExtension interface.
 * 
 * @author lippisch
 */
public abstract class AbstractEntityEditorExtension implements IEntityEditorExtension {

	protected EditorContext context = null;
	protected EntityEditor editor;

	/* (non-Javadoc)
	 * @see de.xwic.appkit.webbase.editors.IEntityEditorExtension#initialize(de.xwic.appkit.webbase.editors.EditorContext)
	 */
	@Override
	public void initialize(EditorContext context, EntityEditor editor) {
		this.context  = context;
		this.editor = editor;
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.webbase.editors.IEntityEditorExtension#addActions(de.jwic.controls.ToolBar)
	 */
	@Override
	public void addActions(ToolBar toolbar) {

	}

}
