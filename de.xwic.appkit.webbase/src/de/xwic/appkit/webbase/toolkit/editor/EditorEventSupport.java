/**
 * 
 */
package de.xwic.appkit.webbase.toolkit.editor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ronny Pfretzschner
 *
 */
public class EditorEventSupport {

	
    protected List<IEditorModelListener> listeners = new ArrayList<IEditorModelListener>();
	
    /**
     * Fire close request event.
     */
    public void onCloseRequest(Object source) {
    	EditorModelEvent event = new EditorModelEvent(EditorModelEvent.CLOSE_REQUEST, source);
    	fireModelChangedEvent(event);
    }

    /**
     * Fire after save event.
     */
    public void onAfterSave(Object source) {
    	EditorModelEvent event = new EditorModelEvent(EditorModelEvent.AFTER_SAVE, source);
    	fireModelChangedEvent(event);
    }

    /**
     * Fire validation request event.
     */
    public void onValidationRequest(Object source) {
    	EditorModelEvent event = new EditorModelEvent(EditorModelEvent.VALIDATION_REQUEST, source);
    	fireModelChangedEvent(event);
    }

    /**
     * Fire request after validation for refresh event.
     */
    public void onAfterValidationRefreshRequest(Object source) {
    	EditorModelEvent event = new EditorModelEvent(EditorModelEvent.VALIDATION_REFRESH_REQUEST, source);
    	fireModelChangedEvent(event);
    }

    
    
    /**
     * Add model listener to the model changes.
     * @param listener
     */
    public void addModelListener(IEditorModelListener listener) {
    	listeners.add(listener);
    }
    
    /**
     * Removes the given listener.
     * 
     * @param listener
     */
    public void removeModelListener(IEditorModelListener listener) {
    	listeners.remove(listener);
    }
    
    /**
     * Fire event.
     * 
     * @param event
     */
    public void fireModelChangedEvent(EditorModelEvent event) {
    	Object[] lst = listeners.toArray();
    	for (int i = 0; i < lst.length; i++) {
			IEditorModelListener type = (IEditorModelListener)lst[i];
			type.modelContentChanged(event);
		}
    }

}
