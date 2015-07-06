/*******************************************************************************
 * Copyright 2015 xWic group (http://www.xwic.de)
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 		http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 *  
 *******************************************************************************/
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
