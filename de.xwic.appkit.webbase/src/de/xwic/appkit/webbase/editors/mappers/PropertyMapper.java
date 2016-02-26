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
package de.xwic.appkit.webbase.editors.mappers;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import de.jwic.base.IControl;
import de.xwic.appkit.core.config.model.EntityDescriptor;
import de.xwic.appkit.core.config.model.Property;
import de.xwic.appkit.core.dao.IEntity;
import de.xwic.appkit.core.dao.ValidationResult;
import de.xwic.appkit.core.dao.ValidationResult.Severity;
import de.xwic.appkit.core.model.queries.IPropertyQuery;
import de.xwic.appkit.webbase.editors.ValidationException;

/**
 * Maps widgets to entity properties. The PropertyMapper manages a list of
 * controls that are linked to an property of an entity. Each PropertyManager
 * handles properties/widgets of a special kind.
 * 
 * @author Florian Lippisch
 */
public abstract class PropertyMapper<T extends IControl> {

	/** base entity */
	protected EntityDescriptor baseEntity;
	/** list of widgetProperties */
	protected List<ControlProperty<T>> widgets = new ArrayList<ControlProperty<T>>();
	
	/**
	 * Constructor.
	 * @param baseEntity
	 */
	public PropertyMapper(EntityDescriptor baseEntity) {
		this.baseEntity = baseEntity;
	}
	

	/**
	 * @param property
	 * @return
	 */
	protected String getPropertyKey(Property[] property) {
		
		StringBuffer sb = new StringBuffer();
		if (null != property) {
			for (int i = 0; i < property.length; i++) {
				if (i > 0) {
					sb.append(".");
				}
				sb.append(property[i].getName());
			}
		}
		return sb.toString();
	}

	/**
	 * 
	 * @param widget
	 * @param property
	 */
	public void registerProperty(T widget, Property[] property) {
		widgets.add(new ControlProperty<T>(widget, property));
	}
	
	/**
	 * @param widget
	 * @param property
	 * @param infoMode
	 */
	public void registerProperty(T widget, Property[] property, boolean infoMode) {
		widgets.add(new ControlProperty<T>(widget, property, infoMode));
	}
	
	/**
	 * Load the content from the entity into the widgets.
	 * @param entity
	 */
	public void loadContent(IEntity entity) throws MappingException {

		for (ControlProperty<T> wp : widgets) {
			Property[] prop = wp.getProperty();
			if (prop == null || prop[prop.length - 1].hasReadAccess()) {
				loadContent(entity, wp.getWidget(), wp.getProperty());
			}
		}
		
	}

	/**
	 * Validates the widgets.
	 * @throws ValidationException
	 */
	public ValidationResult validateWidgets() {
		ValidationResult result = new ValidationResult();
		for (ControlProperty<T> wp : widgets) {
			String msg = validateWidget(wp.getWidget(), wp.getProperty());
			if (msg != null) {
				result.addError(getPropertyKey(wp.getProperty()), msg);
			}
		}
		return result;
	}
	
	/**
	 * Changes the readonly flag for all controlled widgets.
	 * @param editable
	 */
	public void setEditable(boolean editable) {
		for (ControlProperty<T> wp : widgets) {
			setEditable(wp.getWidget(), wp.getProperty(), editable);
		}
	}
	
	/**
	 * Change the readonly flag for the specified widget/property.
	 * @param widget
	 * @param property
	 * @param editable
	 */
	public abstract void setEditable(T widget, Property[] property, boolean editable);
	
	/**
	 * Load the content from the entity into the widgets.
	 * @param entity
	 */
	public void storeContent(IEntity entity) throws MappingException, ValidationException {

		for (ControlProperty<T> wp : widgets) {
			if (!wp.isInfoMode()) {
				Property[] prop = wp.getProperty();
				if (prop == null || prop[prop.length - 1].hasReadAccess()) {
					storeContent(entity, wp.getWidget(), wp.getProperty());
				}
			}
		}
		
	}
	
	/**
	 * Must return <code>null</code> if the widget content is valid or 
	 * returns an string that identifies the error.
	 * @param widget
	 * @param property
	 * @return
	 */
	public String validateWidget(T widget, Property[] property) {
		return null;
	}

	/**
	 * @param entity
	 * @param widget
	 * @param property
	 * @throws MappingException 
	 */
	public abstract void loadContent(IEntity entity, T widget, Property[] property) throws MappingException;

	/**
	 * @param entity
	 * @param widget
	 * @param property
	 */
	public abstract void storeContent(IEntity entity, T widget, Property[] property) throws MappingException, ValidationException;
	
	/**
	 * @param entity
	 * @param property
	 * @return
	 */
	protected Object readValue(IEntity entity, Property[] property) throws MappingException {
		
		Object root = entity;
		for (int i = 0; i < property.length && root != null; i++) {
			Method mRead = property[i].getDescriptor().getReadMethod();
			try {
				root = mRead.invoke(root, null);
			} catch (Exception e) {
				throw new MappingException("Error reading property '" + property[i].getName() + "': " + e, e);
			}
		}
		return root;
	}

	/**
	 * Write the value into the object tree. Automatically converts
	 * basic types.
	 * @param entity
	 * @param property
	 * @return
	 * @throws MappingException
	 */
	protected void writeValue(IEntity entity, Property[] property, Object value) throws MappingException {
		
		Object root = entity;
		// read until the "final" object
		int max = property.length - 1;
		for (int i = 0; i < max && root != null; i++) {
			Method mRead = property[i].getDescriptor().getReadMethod();
			try {
				root = mRead.invoke(root, null);
			} catch (IllegalArgumentException e) {
				throw new MappingException("Error reading property", e);
			} catch (IllegalAccessException e) {
				throw new MappingException("Error reading property", e);
			} catch (InvocationTargetException e) {
				throw new MappingException("Error reading property", e);
			}
		}
		
		if (root == null) {
			// nothing to write to
			return;
		}
		
		Method mWrite = property[max].getDescriptor().getWriteMethod();
		Class targetType = property[max].getDescriptor().getPropertyType();
		
		// We convert the values on our own as we will treat numbers 
		// in a special way.
		if (value != null && !value.getClass().equals(targetType)) {
			if (value instanceof String) { // String to
				String sValue = (String)value;
				if (targetType.equals(Integer.class)) { // .. Integer
					value = new Integer(sValue); 
				} else if (targetType.equals(Double.class)) { // .. Double
					value = new Double(sValue);
				}
			} else if (value instanceof Integer) { // Integer to..
				Integer iValue = (Integer)value;
				if (targetType.equals(String.class)) {
					value = iValue.toString();
				} else if (targetType.equals(Double.class)) {
					value = new Double(iValue.doubleValue());
				}
			}
		}
		
		try {
			mWrite.invoke(root, new Object[] { value });
		} catch (Exception e) {
			throw new MappingException("Error writing property '" + property[max].getName() + "':" + e, e);
		}
		
	}


	/**
	 * @param editable
	 * @param propertyKey
	 */
	public void setFieldEditable(boolean editable, String propertyKey) {
		String pkStartsWith = propertyKey + ".";
		for (ControlProperty<T> wp : widgets) {
			String propKey = getPropertyKey(wp.getProperty());
			if (propKey.equals(propertyKey) || propKey.startsWith(pkStartsWith)) {
				setEditable(wp.getWidget(), wp.getProperty(), editable);
			}
		}
	}

	/**
	 * Loads the value of the property into a propertyquery object. <p>
	 * 
	 * @param query
	 */
	public void addToQuery(IPropertyQuery query) {

		for (ControlProperty<T> wp : widgets) {
			Property[] prop = wp.getProperty();
			if (prop == null || prop[prop.length - 1].hasReadAccess()) {
				addPropertyToQuery(wp.getWidget(), wp.getProperty(), query);
			}
		}
		
	}

	/**
	 * To be implemented by propertyMapper to support the field in QuickSearch 
	 * elements in list views.
	 * @param widget
	 * @param property
	 * @param query
	 */
	protected void addPropertyToQuery(T widget, Property[] property, IPropertyQuery query) {
		throw new RuntimeException("This mapper does not support queries");
		
	}


	/**
	 * @param result
	 */
	public void highlightValidationResults(ValidationResult result) {
		for (ControlProperty<T> wp : widgets) {
			StringBuilder sbPropId = new StringBuilder();
			sbPropId.append(baseEntity.getClassname());
			for (Property p : wp.getProperty()) {
				sbPropId.append(".");
				sbPropId.append(p.getName());
			}
			String fullPropertyId = sbPropId.toString();

			if (result.getErrorMap().containsKey(fullPropertyId)) {
				highlightWidget(wp.getWidget(), Severity.ERROR);
			} else if (result.getWarningMap().containsKey(fullPropertyId)) {
				highlightWidget(wp.getWidget(), Severity.WARN);
			} else {
				clearHighlightWidget(wp.getWidget());
			}
		}		
	}


	/**
	 * Remove any highlight indicator from the widget.
	 * @param widget
	 */
	protected void clearHighlightWidget(T widget) {
		
	}


	/**
	 * Add a highlight indicator to the widget.
	 * @param widget
	 * @param error
	 */
	protected void highlightWidget(T widget, Severity error) {
		
	}
}
