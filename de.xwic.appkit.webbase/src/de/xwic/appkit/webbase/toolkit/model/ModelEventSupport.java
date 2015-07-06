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
package de.xwic.appkit.webbase.toolkit.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Eventsupport for UI models.
 * 
 * @author Ronny Pfretzschner
 * 
 */
public class ModelEventSupport implements IModelEventSupport {

	protected List<IModelListener> listeners = new ArrayList<IModelListener>();

	/**
	 * Fire close request event.
	 */
	protected void onCloseRequest(Object source) {
		ModelEvent event = new ModelEvent(ModelEvent.CLOSE_REQUEST, source);
		fireModelChangedEvent(event);
	}

	/**
	 * Fire content changed event.
	 */
	protected void onContentChanged(Object source) {
		ModelEvent event = new ModelEvent(ModelEvent.MODEL_CONTENT_CHANGED, source);
		fireModelChangedEvent(event);
	}

	/**
	 * Fire after save event.
	 */
	protected void onAfterSave(Object source) {
		ModelEvent event = new ModelEvent(ModelEvent.AFTER_SAVE, source);
		fireModelChangedEvent(event);
	}

	/**
	 * Fire validation request event.
	 */
	protected void onValidationRequest(Object source) {
		ModelEvent event = new ModelEvent(ModelEvent.VALIDATION_REQUEST, source);
		fireModelChangedEvent(event);
	}

	/**
	 * Fire abort event.
	 */
	protected void onAbortRequest(Object source) {
		ModelEvent event = new ModelEvent(ModelEvent.ABORT_REQUEST, source);
		fireModelChangedEvent(event);
	}

	/**
	 * Fire request after validation for refresh event.
	 */
	protected void onAfterValidationRefreshRequest(Object source) {
		ModelEvent event = new ModelEvent(ModelEvent.VALIDATION_REFRESH_REQUEST, source);
		fireModelChangedEvent(event);
	}

	/**
	 * Fire request after validation for refresh event.
	 */
	protected void onSaveRequest(Object source) {
		ModelEvent event = new ModelEvent(ModelEvent.SAVE_REQUEST, source);
		fireModelChangedEvent(event);
	}

	/**
	 * Fire request reload for refresh event.
	 */
	protected void onReloadRequest(Object source) {
		ModelEvent event = new ModelEvent(ModelEvent.RELOAD_REQUEST, source);
		fireModelChangedEvent(event);
	}

	/* (non-Javadoc)
	 * @see de.pol.isis.web.toolkit.model.IModelEventSupport#addModelListener(de.pol.isis.web.toolkit.model.IModelListener)
	 */
	public void addModelListener(IModelListener listener) {
		listeners.add(listener);
	}

	/* (non-Javadoc)
	 * @see de.pol.isis.web.toolkit.model.IModelEventSupport#removeModelListener(de.pol.isis.web.toolkit.model.IModelListener)
	 */
	public void removeModelListener(IModelListener listener) {
		listeners.remove(listener);
	}

	/**
	 * Fire event.
	 * 
	 * @param event
	 */
	protected void fireModelChangedEvent(ModelEvent event) {
		Object[] lst = listeners.toArray();
		for (int i = 0; i < lst.length; i++) {
			IModelListener type = (IModelListener) lst[i];
			type.modelContentChanged(event);
		}
	}

}
