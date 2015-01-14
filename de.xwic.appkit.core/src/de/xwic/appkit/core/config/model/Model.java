/*
 * Copyright 2005, 2006 pol GmbH
 *
 * de.xwic.appkit.core.config.Model
 * Created on 02.11.2006
 *
 */
package de.xwic.appkit.core.config.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import de.xwic.appkit.core.config.ParseException;

/**
 * The model contains a list of entities that are connected to each other.
 * 
 * @author Florian Lippisch
 */
public class Model {

	private Map<String, EntityDescriptor> entities = new HashMap<String, EntityDescriptor>();
	private Model parentModel = null;
	
	/**
	 * Default Constructor.
	 */
	public Model() {
		
	}
	
	/**
	 * Construct a dependent model.
	 * @param parentModel
	 */
	public Model(Model parentModel) {
		this.parentModel = parentModel;
	}
	
	/**
	 * Add an EntityDescriptor to the model.
	 * @param entityDescriptor
	 * @throws ParseException 
	 */
	public void addEntityDescriptor(final EntityDescriptor entityDescriptor) throws ParseException {
		final String classname = entityDescriptor.getClassname();
		if (entities.containsKey(classname)) {
			throw new ParseException(classname  + " already defined.");
		}
		entities.put(classname, entityDescriptor);
	}
	
	/**
	 * Returns the list of managed entities.
	 * @return Set of classnames as string 
	 */
	public Set<String> getManagedEntities() {
		return entities.keySet();
	}

	/**
	 * Returns the entityDescriptor with the specified classname. 
	 * Returns <code>null</code> if the entitiyDescriptor does not exist.
	 * @param classname
	 * @return
	 */
	public EntityDescriptor getEntityDescriptor(String classname) {
		EntityDescriptor ed = entities.get(classname);
		if (ed == null && parentModel != null) {
			ed = parentModel.getEntityDescriptor(classname);
		}
		return ed;
	}
	
}
