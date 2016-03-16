/**
 * 
 */
package de.xwic.appkit.webbase.editors;

import de.jwic.controls.ToolBar;

/**
 * Generic Editors can be extended via the extension-point mechanism to enrich
 * the editor with additional features such as actions, components, lists and
 * others.
 * 
 * A new instance of this class is instantiated for each editor session that is created. This
 * enables the extension to store information about the entity being edited, the state or
 * other data. 
 * 
 * @author lippisch
 */
public interface IEntityEditorExtension {

	/**
	 * Initialize the extension and provide the EditorContext for this session.
	 * @param context
	 * @param editor 
	 */
	public void initialize(EditorContext context, EntityEditor editor);
	
	/**
	 * Add actions to the toolbar in the editor. This method will be invoked after the
	 * standard actions have been added and the editor was already created.
	 * 
	 * @param toolbar
	 */
	public void addActions(ToolBar toolbar);
	
	
	
}
