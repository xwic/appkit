/*
 * (c) Copyright 2005, 2006 by pol GmbH 
 * 
 * de.xwic.appkit.webbase.editors.mappers.PropertyMapper
 * Created on May 14, 2007 by Aron Cotrau
 *
 */
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
import de.xwic.appkit.core.model.queries.IPropertyQuery;
import de.xwic.appkit.webbase.editors.ValidationException;

/**
 * Maps widgets to entity properties. The PropertyMapper manages a list of
 * controls that are linked to an property of an entity. Each PropertyManager
 * handles properties/widgets of a special kind.
 * 
 * @author Florian Lippisch
 */
public abstract class PropertyMapper {

	/** base entity */
	protected EntityDescriptor baseEntity;
	/** list of widgetProperties */
	protected List widgets = new ArrayList();
	
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
	public void registerProperty(IControl widget, Property[] property) {
		widgets.add(new ControlProperty(widget, property));
	}
	
	/**
	 * @param widget
	 * @param property
	 * @param infoMode
	 */
	public void registerProperty(IControl widget, Property[] property, boolean infoMode) {
		widgets.add(new ControlProperty(widget, property, infoMode));
	}
	
	/**
	 * Load the content from the entity into the widgets.
	 * @param entity
	 */
	public void loadContent(IEntity entity) throws MappingException {
		
		for (Iterator it = widgets.iterator(); it.hasNext(); ) {
			ControlProperty wp = (ControlProperty)it.next();
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
		for (Iterator it = widgets.iterator(); it.hasNext(); ) {
			ControlProperty wp = (ControlProperty)it.next();
			String msg = validateWidget(wp.getWidget(), wp.getProperty());
			if (msg != null) {
				result.addError(getPropertyKey(wp.getProperty()), msg);
			}
		}
		return result;
	}
	
	/**
	 * Changes the readonly flag for all controlled widgets.
	 * @param readonly
	 */
	public void setEditable(boolean editable) {
		for (Iterator it = widgets.iterator(); it.hasNext(); ) {
			ControlProperty wp = (ControlProperty)it.next();
			setEditable(wp.getWidget(), wp.getProperty(), editable);
		}
	}
	
	/**
	 * Change the readonly flag for the specified widget/property.
	 * @param widget
	 * @param property
	 * @param readonly
	 */
	public abstract void setEditable(IControl widget, Property[] property, boolean editable);
	
	/**
	 * Load the content from the entity into the widgets.
	 * @param entity
	 */
	public void storeContent(IEntity entity) throws MappingException, ValidationException {
		
		for (Iterator it = widgets.iterator(); it.hasNext(); ) {
			ControlProperty wp = (ControlProperty)it.next();
			if (! wp.isInfoMode()) {
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
	public String validateWidget(IControl widget, Property[] property) {
		return null;
	}

	/**
	 * @param entity
	 * @param widget
	 * @param property
	 * @throws MappingException 
	 */
	public abstract void loadContent(IEntity entity, IControl widget, Property[] property) throws MappingException;

	/**
	 * @param entity
	 * @param widget
	 * @param property
	 */
	public abstract void storeContent(IEntity entity, IControl widget, Property[] property) throws MappingException, ValidationException;
	
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
		for (Iterator it = widgets.iterator(); it.hasNext(); ) {
			ControlProperty wp = (ControlProperty)it.next();
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

		for (Iterator it = widgets.iterator(); it.hasNext(); ) {
			ControlProperty wp = (ControlProperty)it.next();
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
	protected void addPropertyToQuery(IControl widget, Property[] property, IPropertyQuery query) {
		throw new RuntimeException("This mapper does not support queries");
		
	}
}