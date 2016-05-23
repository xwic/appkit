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
package de.xwic.appkit.core.dao.event;

import de.xwic.appkit.core.dao.IEntity;

/**
 * DAO event.
 * 
 * Created on 20.05.2016
 * @author dotto
 */
public class DaoEntityEvent<T extends IEntity> {

	public final static int UPDATE = 0;
	public final static int DELETE = 1;
	public final static int CACHE_CHANGE = 2;
		
	private int eventType = -1;

	private T source = null;
	
	/**
	 * Creates the event for model changes.
	 * 
	 * @param eventType
	 * @param source
	 */
	public DaoEntityEvent(int eventType, T source) {
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
	public T getSource() {
		return source;
	}
}
