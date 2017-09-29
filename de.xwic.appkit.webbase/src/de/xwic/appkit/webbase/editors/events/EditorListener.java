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
package de.xwic.appkit.webbase.editors.events;

/**
 * @author Florian Lippisch
 */
public interface EditorListener {

	/**
	 * Fired after the editor has saved the entity.
	 * @param event
	 */
	public void afterSave(EditorEvent event);
	
	/**
	 * Fired after the entity has been loaded or refreshed.
	 * @param event
	 */
	public void entityLoaded(EditorEvent event);

	/**
	 * Fired before the entity is saved.
	 * @param event
	 */
	public void beforeSave(EditorEvent event);
	
	/**
	 * Fired, after all pages, widgets and so on are created. <p>
	 * 
	 * Could also be considered of some kind of "initialize" event.
	 * This event is called just once, after an editor is created.
	 * 
	 * @param event
	 */
	public void pagesCreated(EditorEvent event);
	
	/**
	 * Fired when a static message was added or removed from the context.
	 * @param event
	 */
	default void messagesUpdated(EditorEvent event) {
		
	}

}
