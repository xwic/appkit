/**
 * 
 */
package de.xwic.appkit.webbase.editors;

/**
 * Interface for controls that host the editor in a container and need to receive 
 * callback events from the EditorContext.
 * @author lippisch
 */
public interface IEditorHost {


	/**
	 * Invoked by the context when the editor should try to save the entity.
	 * @return
	 */
	public boolean requestSave();
	
	/**
	 * Invoked by the context when the editor should be closed.
	 */
	public void requestClosure();
	
}
