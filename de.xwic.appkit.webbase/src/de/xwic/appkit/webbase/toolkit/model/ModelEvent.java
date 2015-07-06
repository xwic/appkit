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
package de.xwic.appkit.webbase.toolkit.model;

/**
 * Event for model / UI information.
 * 
 * Created on 19.02.2008
 * @author Ronny Pfretzschner
 */
public class ModelEvent {

	public final static int CLOSE_REQUEST = 0;
	public final static int AFTER_SAVE = 1;
	public final static int VALIDATION_REQUEST = 2;
	public final static int VALIDATION_REFRESH_REQUEST = 3;
	public final static int MODEL_CONTENT_CHANGED = 4;
	public final static int ABORT_REQUEST = 5;
	
	public final static int RELOAD_REQUEST = 6;
	public final static int SAVE_REQUEST=7;
	
	private int eventType = -1;

	private Object source = null;
	
	/**
	 * Creates the event for model changes.
	 * 
	 * @param eventType
	 * @param source
	 */
	public ModelEvent(int eventType, Object source) {
		this.eventType = eventType;
		this.source = source;
	}

	/**
	 * @return Returns the eventType.
	 */
	public int getEventType() {
		return eventType;
	}

	/**
	 * @return Returns the source.
	 */
	public Object getSource() {
		return source;
	}
}
