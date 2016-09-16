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
package de.xwic.appkit.webbase.editors;

import de.jwic.base.IControl;
import de.xwic.appkit.core.config.Bundle;
import de.xwic.appkit.core.config.editor.UIElement;
import de.xwic.appkit.core.config.model.EntityDescriptor;
import de.xwic.appkit.core.config.model.Property;

/**
 * Interface for Context objects. Could be used for editors or the quick search
 * panel.
 * <p>
 * 
 * @author Ronny Pfretzschner
 */
public interface IBuilderContext {

	/**
	 * Register a field to the specified mapper.
	 * 
	 * @param property
	 * @param widget
	 * @param id
	 * @param customMapper
	 */
	public abstract void registerField(Property[] property, IControl widget, UIElement uiDef, String mapperId);

	/**
	 * Register a field that uses a custom mapper.
	 * 
	 * @param property
	 * @param widget
	 * @param id
	 * @param customMapper
	 * @param infoMode
	 */
	public abstract void registerField(Property[] property, IControl widget, UIElement uiDef, String mapperId, boolean infoMode);
	
	/**
	 * Register a widget with the given id. If the id is <code>null</code>, the widget
	 * will not be registered but no exception is thrown.
	 * <p>This is useful for widgets that should be accessible from script but are not a field by
	 * itself, like a container.
	 * 
	 * @param id
	 * @param widget
	 */
	public void registerWidget(IControl widget, UIElement uiDef);

	/**
	 * Returns the widget with the specified id or null if no such widget
	 * exists.
	 * 
	 * @param id
	 * @return
	 */
	public abstract IControl getControlById(String id);

	/**
	 * Change the editable flag of a specified property.
	 * 
	 * @param editable
	 * @param propertyKey
	 */
	public void setFieldEditable(boolean editable, String propertyKey);

	/**
	 * @return the EntityDescriptor of the entity behind
	 */
	public EntityDescriptor getEntityDescriptor();

	/**
	 * @return the bundle
	 */
	public Bundle getBundle();

	/**
	 * @param key
	 * @return the resource string by given key
	 */
	public String getResString(String key);

	/**
	 * @param property
	 * @return the resource string by given property
	 */
	public String getResString(Property property);

	/**
	 * @param property
	 */
	public abstract void fieldChanged(Property[] property);
}