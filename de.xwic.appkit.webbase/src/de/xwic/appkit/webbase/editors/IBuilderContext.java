/*
 * (c) Copyright 2005, 2006 by pol GmbH 
 * 
 * de.xwic.appkit.webbase.editors.IBuilderContext
 * Created on Jun 11, 2007 by Aron Cotrau
 *
 */
package de.xwic.appkit.webbase.editors;

import de.jwic.base.IControl;
import de.jwic.events.SelectionListener;
import de.xwic.appkit.core.config.Bundle;
import de.xwic.appkit.core.config.model.EntityDescriptor;
import de.xwic.appkit.core.config.model.Property;
import de.xwic.appkit.webbase.editors.mappers.PropertyMapper;

/**
 * Interface for Context objects. Could be used for editors or the quick search
 * panel.
 * <p>
 * 
 * @author Ronny Pfretzschner
 */
public interface IBuilderContext {

	/**
	 * Register a created field.
	 * 
	 * @param property
	 * @param widget
	 * @param id
	 */
	public abstract void registerField(Property[] property, IControl widget, String id);

	/**
	 * Register a field that uses a custom mapper.
	 * 
	 * @param property
	 * @param widget
	 * @param id
	 * @param customMapper
	 */
	public abstract void registerField(Property[] property, IControl widget, String id, PropertyMapper customMapper);

	/**
	 * Register a field that uses a custom mapper.
	 * 
	 * @param property
	 * @param widget
	 * @param id
	 * @param customMapper
	 * @param infoMode
	 */
	public abstract void registerField(Property[] property, IControl widget, String id, PropertyMapper customMapper,
			boolean infoMode);

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
	 * The default SelectionListener
	 * 
	 * @return the defaultSelectionListener
	 */
	public SelectionListener getDefaultSelectionListener();

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
}