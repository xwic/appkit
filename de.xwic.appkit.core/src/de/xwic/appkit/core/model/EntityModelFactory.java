/*
 * (c) Copyright 2005, 2006 by pol GmbH 
 * 
 * de.xwic.appkit.core.model.EntityModelFactory
 * Created on 20.11.2006 by Florian Lippisch
 *
 */
package de.xwic.appkit.core.model;

import java.lang.reflect.Proxy;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.xwic.appkit.core.dao.IEntity;

/**
 * Creates IEntityModel objects. The model is a dynamicaly generated proxy that implements
 * both the IEntityModel interface as the interface(s) of the given entity. All modifications
 * must be made against the proxy class. 
 * 
 * The entityModel keeps a map of modified properties that are written to the base
 * entity on commit.
 * 
 * @author Florian Lippisch
 */
public class EntityModelFactory {

	/** logger */
	protected static final Log log = LogFactory.getLog(EntityModelFactory.class);
	
	private static ClassLoader classLoader = null;
	
	static {
		classLoader = new EntityModelFactory().getClass().getClassLoader();
	}

	/**
	 * Returns true if the specified entity is a EntityModel implementation.
	 * @param entity
	 * @return
	 */
	public static boolean isModel(IEntity entity) {
		return (
				Proxy.isProxyClass(entity.getClass()) && 
				Proxy.getInvocationHandler(entity) instanceof EntityModelInvocationHandler
				);
	}
	
	/**
	 * Returns the original entity.
	 * @param entityModel
	 * @return
	 */
	public static IEntity getOriginalEntity(IEntity entityModel) {
		if (!Proxy.isProxyClass(entityModel.getClass())) {
			throw new IllegalArgumentException("entity is not an entityModel");
		}
		Object oIh = Proxy.getInvocationHandler(entityModel);
		if (!(oIh instanceof EntityModelInvocationHandler)) {
			throw new IllegalArgumentException("entity is a proxy but not an entityModel");
		}
		EntityModelInvocationHandler emIh = (EntityModelInvocationHandler)oIh;
		return emIh.getEntityModelImpl().getOriginalEntity();
	}
	
	/**
	 * Creates a new entity model based upon the specified entity.
	 * @param entity
	 * @return
	 * @throws EntityModelException 
	 */
	@SuppressWarnings("unchecked")
	public static IEntityModel createModel(IEntity entity) {
		
		// create model and InvocationHandler
		Set<Class<?>> interfacesSet = new HashSet<Class<?>>();
		Class<? extends IEntity> clazz = entity.getClass();
		interfacesSet.add(IEntityModel.class);
		
		// recursivly add all interfaces
		Class<?> c = clazz;
		while (c != null) {
			Class<?>[] orgInterfaces = c.getInterfaces();
			for (int i = 0; i < orgInterfaces.length; i++) {
				interfacesSet.add(orgInterfaces[i]);
			}
			c = c.getSuperclass();
		}
		Class[] interfaces = new Class[interfacesSet.size()];
		int idx = 0;
		for (Iterator<Class<?>> it = interfacesSet.iterator(); it.hasNext(); ) {
			interfaces[idx++] = it.next();
		}

		EntityModelInvocationHandler ih = new EntityModelInvocationHandler(entity);
		return (IEntityModel)Proxy.newProxyInstance(classLoader, interfaces, ih);

	}
	
	/**
	 * Set the classLoader to be used.
	 * @param classLoader
	 */
	public static void setClassLoader(ClassLoader classLoader) {
		EntityModelFactory.classLoader = classLoader;
	}
	
}
